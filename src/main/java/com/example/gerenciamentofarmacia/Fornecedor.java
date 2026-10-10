package com.example.gerenciamentofarmacia;

import java.util.Objects;

/**
 * Representa um fornecedor de medicamentos.
 * O CNPJ é guardado apenas com dígitos.
 */


        import java.util.Objects;

public class Fornecedor {

    private final String cnpj;
    private final String razaoSocial;
    private final String telefone;
    private final String email;
    private final String cidade;
    private final String estado;

    public Fornecedor(String cnpj, String razaoSocial, String telefone,
                      String email, String cidade, String estado) {
        // Opcional: remove caracteres não numéricos do CNPJ ao instanciar
        this.cnpj = cnpj != null ? cnpj.replaceAll("\\D", "") : null;
        this.razaoSocial = razaoSocial;
        this.telefone = telefone;
        this.email = email;
        this.cidade = cidade;
        this.estado = estado;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    /** CNPJ no formato 00.000.000/0000-00. */
    public String getCnpjFormatado() {
        if (cnpj == null || cnpj.length() != 14) {
            return cnpj;
        }

        return cnpj.substring(0, 2) + "."
                + cnpj.substring(2, 5) + "."
                + cnpj.substring(5, 8) + "/"
                + cnpj.substring(8, 12) + "-"
                + cnpj.substring(12);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fornecedor f = (Fornecedor) o;
        return Objects.equals(cnpj, f.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnpj);
    }

    @Override
    public String toString() {
        return razaoSocial + " (" + getCnpjFormatado() + ")";


    }
}