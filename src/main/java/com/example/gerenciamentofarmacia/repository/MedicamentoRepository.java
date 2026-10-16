package com.example.gerenciamentofarmacia.repository;

import com.example.gerenciamentofarmacia.model.Fornecedor;
import com.example.gerenciamentofarmacia.model.Medicamento;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistência em arquivo CSV com campos separados por ponto-e-vírgula.
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

    /** Lê todos os medicamentos salvos no arquivo CSV. */
    public List<Medicamento> carregar() throws IOException {
        List<Medicamento> lista = new ArrayList<>();
        if (!Files.exists(arquivo)) return lista;

        List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
        for (int i = 1; i < linhas.size(); i++) { // Pula o cabeçalho
            String linha = linhas.get(i);
            if (linha.isBlank()) continue;
            try {
                lista.add(deLinha(linha));
            } catch (Exception e) {
                System.err.println("Aviso: Linha " + (i + 1) + " ignorada por erro de formato: " + e.getMessage());
            }
        }
        return lista;
    }

    /** Regrava todo o arquivo CSV atualizado. */
    public synchronized void salvar(List<Medicamento> medicamentos) throws IOException {
        List<String> linhas = new ArrayList<>();
        linhas.add(CABECALHO);
        if (medicamentos != null) {
            medicamentos.forEach(m -> linhas.add(paraLinha(m)));
        }

        Path pai = arquivo.toAbsolutePath().getParent();
        if (pai != null && !Files.exists(pai)) {
            Files.createDirectories(pai);
        }

        Files.write(arquivo, linhas, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    private static String paraLinha(Medicamento m) {
        Fornecedor f = m.getFornecedor() != null ? m.getFornecedor() : new Fornecedor("", "", "", "", "", "");
        String validade = m.getDataValidade() != null ? m.getDataValidade().toString() : LocalDate.now().toString();
        String preco = m.getPreco() != null ? m.getPreco().toPlainString() : "0.00";

        return String.join(SEP,
                limpar(m.getCodigo()),
                limpar(m.getNome()),
                limpar(m.getDescricao()),
                limpar(m.getPrincipioAtivo()),
                validade,
                String.valueOf(m.getQuantidadeEstoque()),
                preco,
                String.valueOf(m.isControlado()),
                limpar(f.getCnpj()),
                limpar(f.getRazaoSocial()),
                limpar(f.getTelefone()),
                limpar(f.getEmail()),
                limpar(f.getCidade()),
                limpar(f.getEstado()));
    }

    private static Medicamento deLinha(String linha) {
        String[] c = linha.split(SEP, -1);
        if (c.length != COLUNAS) {
            throw new IllegalArgumentException("Número incorreto de colunas: " + c.length);
        }

        Fornecedor f = new Fornecedor(c[8], c[9], c[10], c[11], c[12], c[13]);
        return new Medicamento(c[0], c[1], c[2], c[3], LocalDate.parse(c[4]),
                Integer.parseInt(c[5]), new BigDecimal(c[6]), Boolean.parseBoolean(c[7]), f);
    }

    private static String limpar(String s) {
        return s == null ? "" : s.replace(";", ",").replaceAll("[\\r\\n]+", " ").trim();
    }
}