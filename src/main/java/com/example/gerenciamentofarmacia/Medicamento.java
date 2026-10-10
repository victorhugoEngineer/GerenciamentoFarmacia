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
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.principioAtivo = principioAtivo;
        this.dataValidade = dataValidade;
        this.quantidadeEstoque = quantidadeEstoque;
        this.preco = preco;
        this.controlado = controlado;
        this.fornecedor = fornecedor;
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
    public String getFornecedorNome() { return fornecedor.getRazaoSocial(); }
    public String getControladoTexto() { return controlado ? "Sim" : "Não"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicamento m = (Medicamento) o;
        return Objects.equals(codigo, m.codigo);
    }

    @Override
    public int hashCode() { return Objects.hash(codigo); }

    @Override
    public String toString() { return codigo + " - " + nome; }
}
