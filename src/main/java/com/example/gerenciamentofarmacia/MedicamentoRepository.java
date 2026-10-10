package com.example.gerenciamentofarmacia;


import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistência em arquivo CSV com campos separados por ponto-e-vírgula.
 * Ordem das colunas: codigo;nome;descricao;principioAtivo;dataValidade;quantidadeEstoque;
 * preco;controlado;cnpj;razaoSocial;telefone;email;cidade;estado
 */
public class MedicamentoRepository {

    private static final String SEP = ";";
    private static final String CABECALHO = String.join(SEP,
            "codigo", "nome", "descricao", "principioAtivo", "dataValidade", "quantidadeEstoque",
            "preco", "controlado", "cnpj", "razaoSocial", "telefone", "email", "cidade", "estado");
    private static final int COLUNAS = 14;

    private final Path arquivo;

    public MedicamentoRepository(Path arquivo) {
        this.arquivo = arquivo;
    }

    public Path getArquivo() { return arquivo; }

    /** Lê todos os medicamentos. Se o arquivo não existir, devolve lista vazia. */
    public List<Medicamento> carregar() throws IOException {
        List<Medicamento> lista = new ArrayList<>();
        if (!Files.exists(arquivo)) return lista;

        List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
        for (int i = 1; i < linhas.size(); i++) { // pula o cabeçalho
            String linha = linhas.get(i);
            if (linha.isBlank()) continue;
            try {
                lista.add(deLinha(linha));
            } catch (RuntimeException e) {
                throw new IOException("Linha " + (i + 1) + " inválida em " + arquivo.getFileName()
                        + ": " + e.getMessage(), e);
            }
        }
        return lista;
    }

    /** Regrava o arquivo inteiro (chamado após cada operação). */
    public void salvar(List<Medicamento> medicamentos) throws IOException {
        List<String> linhas = new ArrayList<>();
        linhas.add(CABECALHO);
        medicamentos.forEach(m -> linhas.add(paraLinha(m)));

        Path pai = arquivo.toAbsolutePath().getParent();
        if (pai != null) Files.createDirectories(pai);

        // Escreve num temporário e move, para não corromper o CSV se algo falhar no meio.
        Path temp = arquivo.resolveSibling(arquivo.getFileName() + ".tmp");
        Files.write(temp, linhas, StandardCharsets.UTF_8);
        Files.move(temp, arquivo, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    private static String paraLinha(Medicamento m) {
        Fornecedor f = m.getFornecedor();
        return String.join(SEP,
                limpar(m.getCodigo()), limpar(m.getNome()), limpar(m.getDescricao()),
                limpar(m.getPrincipioAtivo()), m.getDataValidade().toString(),
                String.valueOf(m.getQuantidadeEstoque()), m.getPreco().toPlainString(),
                String.valueOf(m.isControlado()), f.getCnpj(), limpar(f.getRazaoSocial()),
                limpar(f.getTelefone()), limpar(f.getEmail()), limpar(f.getCidade()),
                limpar(f.getEstado()));
    }

    private static Medicamento deLinha(String linha) {
        String[] c = linha.split(SEP, -1);
        if (c.length != COLUNAS)
            throw new IllegalArgumentException("esperadas " + COLUNAS + " colunas, encontradas " + c.length);
        Fornecedor f = new Fornecedor(c[8], c[9], c[10], c[11], c[12], c[13]);
        return new Medicamento(c[0], c[1], c[2], c[3], LocalDate.parse(c[4]),
                Integer.parseInt(c[5]), new BigDecimal(c[6]), Boolean.parseBoolean(c[7]), f);
    }

    /** Evita que ';' ou quebras de linha digitados pelo usuário quebrem o formato do CSV. */
    private static String limpar(String s) {
        return s == null ? "" : s.replace(";", ",").replaceAll("[\\r\\n]+", " ").trim();
    }
}
