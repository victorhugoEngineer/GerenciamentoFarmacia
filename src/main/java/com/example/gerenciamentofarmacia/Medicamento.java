package com.example.gerenciamentofarmacia;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** Medicamento do estoque. Compõe um {@link Fornecedor}. */
public class Medicamento {

    private final String codigo;
    private final String nome;
    private final String descricao;
    private final String principioAtivo;
    private final LocalDate dataValidade;
    private final int quantidadeEstoque;
    private final BigDecimal preco;
    private final boolean controlado;
    private final Fornecedor fornecedor;

    public Medicamento(String codigo, String nome, String descricao, String principioAtivo,
                       LocalDate dataValidade, int quantidadeEstoque, BigDecimal preco,
                       boolean controlado, Fornecedor fornecedor) {
        this.codigo = codigo != null ? codigo.trim() : "";
        this.nome = nome != null ? nome.trim() : "";
        this.descricao = descricao != null ? descricao.trim() : "";
        this.principioAtivo = principioAtivo != null ? principioAtivo.trim() : "";
        this.dataValidade = dataValidade;
        this.quantidadeEstoque = Math.max(0, quantidadeEstoque);
        this.preco = preco != null ? preco : BigDecimal.ZERO;
        this.controlado = controlado;
        this.fornecedor = fornecedor != null ? fornecedor : new Fornecedor("", "", "", "", "", "");
    }

    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getPrincipioAtivo() { return principioAtivo; }
    public LocalDate getDataValidade() { return dataValidade; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public BigDecimal getPreco() { return preco; }
    public boolean isControlado() { return controlado; }
    public Fornecedor getFornecedor() { return fornecedor; }

    /** Valor total em estoque deste item (preço x quantidade). */
    public BigDecimal getValorEstoque() {
        return preco.multiply(BigDecimal.valueOf(quantidadeEstoque));
    }

    // Atalhos usados pelas colunas da TableView
    public String getFornecedorNome() {
        return (fornecedor != null && fornecedor.getRazaoSocial() != null) ? fornecedor.getRazaoSocial() : "";
    }

    public String getControladoTexto() { return controlado ? "Sim" : "Não"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicamento m = (Medicamento) o;
        return Objects.equals(codigo.toUpperCase(), m.codigo.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo.toUpperCase());
    }

    @Override
    public String toString() { return codigo + " - " + nome; }
}