package com.example.gerenciamentofarmacia;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Relatórios gerados com a API Stream (filter, map, reduce, collect/groupingBy). */
public class RelatorioService {

    public static final int DIAS_VENCIMENTO = 30;
    public static final int ESTOQUE_BAIXO = 5;

    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final LocalDate hoje;

    public RelatorioService() {
        this(LocalDate.now());
    }

    /** Construtor com data fixa, útil para testes unitários. */
    public RelatorioService(LocalDate hoje) {
        this.hoje = Objects.requireNonNullElseGet(hoje, LocalDate::now);
    }

    /** filter: validade entre hoje e hoje + 30 dias, do mais próximo ao mais distante. */
    public List<Medicamento> proximosAoVencimento(List<Medicamento> lista) {
        if (lista == null) return Collections.emptyList();

        LocalDate limite = hoje.plusDays(DIAS_VENCIMENTO);
        return lista.stream()
                .filter(Objects::nonNull)
                .filter(m -> m.getDataValidade() != null)
                .filter(m -> !m.getDataValidade().isBefore(hoje) && !m.getDataValidade().isAfter(limite))
                .sorted(Comparator.comparing(Medicamento::getDataValidade))
                .toList();
    }

    /** filter: estoque abaixo de 5 unidades, do menor para o maior. */
    public List<Medicamento> estoqueBaixo(List<Medicamento> lista) {
        if (lista == null) return Collections.emptyList();

        return lista.stream()
                .filter(Objects::nonNull)
                .filter(m -> m.getQuantidadeEstoque() < ESTOQUE_BAIXO)
                .sorted(Comparator.comparingInt(Medicamento::getQuantidadeEstoque))
                .toList();
    }

    /** map + reduce: soma de preço x quantidade de cada fornecedor. */
    public Map<String, BigDecimal> valorTotalPorFornecedor(List<Medicamento> lista) {
        if (lista == null) return Collections.emptyMap();

        return lista.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        m -> (m.getFornecedor() != null && m.getFornecedor().getRazaoSocial() != null)
                                ? m.getFornecedor().getRazaoSocial()
                                : "Sem Fornecedor",
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                m -> m.getValorEstoque() != null ? m.getValorEstoque() : BigDecimal.ZERO,
                                BigDecimal::add
                        )
                ));
    }

    /** partitioningBy: true = controlados, false = não controlados. */
    public Map<Boolean, List<Medicamento>> controladosVsNaoControlados(List<Medicamento> lista) {
        if (lista == null) return Map.of(true, Collections.emptyList(), false, Collections.emptyList());

        return lista.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.partitioningBy(Medicamento::isControlado));
    }

    /** Valor total do estoque inteiro (map + reduce). */
    public BigDecimal valorTotalEstoque(List<Medicamento> lista) {
        if (lista == null) return BigDecimal.ZERO;

        return lista.stream()
                .filter(Objects::nonNull)
                .map(Medicamento::getValorEstoque)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Relatório completo em texto, exibido na aba "Relatórios". */
    public String gerarTexto(List<Medicamento> lista) {
        List<Medicamento> listaSegura = lista != null ? lista : Collections.emptyList();

        return "RELATÓRIO DE ESTOQUE - " + hoje.format(DATA) + "\n"
                + "Total de medicamentos: " + listaSegura.size()
                + " | Valor total em estoque: " + MOEDA.format(valorTotalEstoque(listaSegura)) + "\n\n"
                + textoVencimento(listaSegura) + "\n"
                + textoEstoqueBaixo(listaSegura) + "\n"
                + textoPorFornecedor(listaSegura) + "\n"
                + textoControlados(listaSegura);
    }

    public String textoVencimento(List<Medicamento> lista) {
        StringBuilder sb = new StringBuilder("== Próximos ao vencimento (" + DIAS_VENCIMENTO + " dias) ==\n");
        anexar(sb, proximosAoVencimento(lista),
                m -> m.getCodigo() + " - " + m.getNome() + " | vence em " + m.getDataValidade().format(DATA));
        return sb.toString();
    }

    public String textoEstoqueBaixo(List<Medicamento> lista) {
        StringBuilder sb = new StringBuilder("== Estoque baixo (menos de " + ESTOQUE_BAIXO + " unidades) ==\n");
        anexar(sb, estoqueBaixo(lista),
                m -> m.getCodigo() + " - " + m.getNome() + " | " + m.getQuantidadeEstoque() + " un.");
        return sb.toString();
    }

    public String textoPorFornecedor(List<Medicamento> lista) {
        StringBuilder sb = new StringBuilder("== Valor total do estoque por fornecedor ==\n");
        Map<String, BigDecimal> porFornecedor = valorTotalPorFornecedor(lista);
        if (porFornecedor.isEmpty()) {
            sb.append("  (nenhum)\n");
        } else {
            porFornecedor.entrySet().stream()
                    .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                    .forEach(e -> sb.append("  ").append(e.getKey()).append(": ")
                            .append(MOEDA.format(e.getValue())).append("\n"));
        }
        return sb.toString();
    }

    public String textoControlados(List<Medicamento> lista) {
        Map<Boolean, List<Medicamento>> particao = controladosVsNaoControlados(lista);
        List<Medicamento> controlados = particao.getOrDefault(true, Collections.emptyList());
        List<Medicamento> naoControlados = particao.getOrDefault(false, Collections.emptyList());

        StringBuilder sb = new StringBuilder("== Controlados (" + controlados.size() + ") ==\n");
        anexar(sb, controlados, m -> m.getCodigo() + " - " + m.getNome());
        sb.append("\n== Não controlados (").append(naoControlados.size()).append(") ==\n");
        anexar(sb, naoControlados, m -> m.getCodigo() + " - " + m.getNome());
        return sb.toString();
    }

    private static void anexar(StringBuilder sb, List<Medicamento> itens, Function<Medicamento, String> formato) {
        if (itens == null || itens.isEmpty()) {
            sb.append("  (nenhum)\n");
            return;
        }
        sb.append(itens.stream().map(formato).map(s -> "  " + s).collect(Collectors.joining("\n"))).append("\n");
    }
}