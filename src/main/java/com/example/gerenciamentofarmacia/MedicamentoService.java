package com.example.gerenciamentofarmacia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Regras do CRUD: valida, mantém a lista em memória e persiste após cada operação. */
public class MedicamentoService {

    private final MedicamentoRepository repositorio;
    private final List<Medicamento> medicamentos = new ArrayList<>();

    public MedicamentoService(MedicamentoRepository repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio, "O repositório não pode ser nulo.");
    }

    /** Carrega os dados do CSV (chamado ao iniciar). */
    public void carregar() throws IOException {
        medicamentos.clear();
        List<Medicamento> carregados = repositorio.carregar();
        if (carregados != null) {
            medicamentos.addAll(carregados);
        }
    }

    public void cadastrar(Medicamento novo) throws ValidacaoException, IOException {
        if (novo == null) {
            throw new ValidacaoException("O medicamento não pode ser nulo.");
        }

        Validador.validar(novo);

        if (buscarPorCodigo(novo.getCodigo()).isPresent()) {
            throw new ValidacaoException("Já existe um medicamento com o código " + novo.getCodigo() + ".");
        }

        medicamentos.add(novo);
        try {
            repositorio.salvar(medicamentos);
        } catch (IOException e) {
            medicamentos.remove(novo); // Desfaz para manter memória e arquivo coerentes
            throw e;
        }
    }

    /** Atualiza um medicamento existente mantendo o mesmo código. */
    public void atualizar(Medicamento atualizado) throws ValidacaoException, IOException {
        if (atualizado == null) {
            throw new ValidacaoException("O medicamento não pode ser nulo.");
        }

        Validador.validar(atualizado);

        Medicamento existente = buscarPorCodigo(atualizado.getCodigo())
                .orElseThrow(() -> new ValidacaoException("Medicamento " + atualizado.getCodigo() + " não encontrado para atualização."));

        int posicao = medicamentos.indexOf(existente);
        medicamentos.set(posicao, atualizado);

        try {
            repositorio.salvar(medicamentos);
        } catch (IOException e) {
            medicamentos.set(posicao, existente); // Rollback da alteração em caso de erro no I/O
            throw e;
        }
    }

    public void excluir(String codigo) throws ValidacaoException, IOException {
        Medicamento existente = buscarPorCodigo(codigo)
                .orElseThrow(() -> new ValidacaoException("Medicamento " + codigo + " não encontrado."));

        int posicao = medicamentos.indexOf(existente);
        medicamentos.remove(existente);

        try {
            repositorio.salvar(medicamentos);
        } catch (IOException e) {
            medicamentos.add(posicao, existente); // Rollback
            throw e;
        }
    }

    public Optional<Medicamento> buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return Optional.empty();
        }

        String procurado = codigo.trim();
        return medicamentos.stream()
                .filter(m -> m.getCodigo() != null && m.getCodigo().equalsIgnoreCase(procurado))
                .findFirst();
    }

    /** Todos os medicamentos, ordenados por nome. */
    public List<Medicamento> listar() {
        return medicamentos.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(
                        m -> m.getNome() != null ? m.getNome() : "",
                        String.CASE_INSENSITIVE_ORDER
                ))
                .toList(); // Corrigido: .toList() em vez de .Stream()
    }
}