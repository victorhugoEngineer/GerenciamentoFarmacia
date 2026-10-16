package com.example.gerenciamentofarmacia;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/** Regras de validação do domínio. Coleta todos os erros de uma vez. */
public final class Validador {

    /** Código: de 3 a 10 caracteres alfanuméricos (ex.: MED001 ou MED0001). */
    private static final Pattern CODIGO = Pattern.compile("^[A-Za-z0-9]{3,10}$");
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern UF = Pattern.compile("^[A-Za-z]{2}$");
    public static final int NOME_MINIMO = 3;

    private Validador() {}

    public static void validar(Medicamento m) throws ValidacaoException {
        List<String> erros = new ArrayList<>();

        if (m.getCodigo() == null || !CODIGO.matcher(m.getCodigo()).matches())
            erros.add("Código deve conter entre 3 e 10 caracteres alfanuméricos (ex.: MED0001).");

        if (m.getNome() == null || m.getNome().trim().length() < NOME_MINIMO)
            erros.add("Nome é obrigatório e deve ter ao menos " + NOME_MINIMO + " caracteres.");

        if (m.getDescricao() == null || m.getDescricao().isBlank())
            erros.add("Descrição é obrigatória.");

        if (m.getPrincipioAtivo() == null || m.getPrincipioAtivo().isBlank())
            erros.add("Princípio ativo é obrigatório.");

        if (m.getDataValidade() == null)
            erros.add("Data de validade é obrigatória.");
        else if (m.getDataValidade().isBefore(LocalDate.now()))
            erros.add("Data de validade não pode ser uma data passada.");

        if (m.getQuantidadeEstoque() < 0)
            erros.add("Quantidade em estoque não pode ser negativa.");

        if (m.getPreco() == null || m.getPreco().compareTo(BigDecimal.ZERO) <= 0)
            erros.add("Preço deve ser um valor positivo.");

        if (m.getFornecedor() == null)
            erros.add("Fornecedor é obrigatório.");
        else
            erros.addAll(errosFornecedor(m.getFornecedor()));

        if (!erros.isEmpty()) throw new ValidacaoException(erros);
    }

    public static List<String> errosFornecedor(Fornecedor f) {
        List<String> erros = new ArrayList<>();
        if (!cnpjValido(f.getCnpj()))
            erros.add("CNPJ inválido (formato ou dígitos verificadores).");
        if (f.getRazaoSocial() == null || f.getRazaoSocial().isBlank())
            erros.add("Razão social é obrigatória.");
        if (f.getTelefone() == null || f.getTelefone().isBlank())
            erros.add("Telefone é obrigatório.");
        if (f.getEmail() == null || !EMAIL.matcher(f.getEmail()).matches())
            erros.add("E-mail do fornecedor inválido.");
        if (f.getCidade() == null || f.getCidade().isBlank())
            erros.add("Cidade é obrigatória.");
        if (f.getEstado() == null || !UF.matcher(f.getEstado()).matches())
            erros.add("Estado deve ser a sigla da UF com 2 letras (ex.: GO).");
        return erros;
    }

    /** Remove tudo que não for dígito (aceita 11.222.333/0001-81 ou 11222333000181). */
    public static String somenteDigitos(String s) {
        return s == null ? "" : s.replaceAll("\\D", "");
    }

    /** Valida CNPJ: 14 dígitos, não repetidos, e os dois dígitos verificadores. */
    public static boolean cnpjValido(String cnpj) {
        String c = somenteDigitos(cnpj);
        if (c.length() != 14 || c.chars().distinct().count() == 1) return false;
        int[] p1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] p2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int d1 = digito(c, p1);
        int d2 = digito(c, p2);
        return d1 == c.charAt(12) - '0' && d2 == c.charAt(13) - '0';
    }

    private static int digito(String c, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) soma += (c.charAt(i) - '0') * pesos[i];
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}