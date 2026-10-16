package com.example.gerenciamentofarmacia.validation;

import java.util.List;

/** Lançada quando os dados informados violam alguma regra de negócio. */
public class ValidacaoException extends Exception {

    private final List<String> erros;

    public ValidacaoException(String mensagem) {
        super(mensagem);
        this.erros = List.of(mensagem);
    }

    public ValidacaoException(List<String> erros) {
        super(String.join("\n", erros));
        this.erros = List.copyOf(erros);
    }

    public List<String> getErros() { return erros; }
}
