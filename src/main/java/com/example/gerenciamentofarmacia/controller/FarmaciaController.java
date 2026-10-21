package com.example.gerenciamentofarmacia.controller;

import com.example.gerenciamentofarmacia.util.Dialogos;
import com.example.gerenciamentofarmacia.model.Fornecedor;
import com.example.gerenciamentofarmacia.model.Medicamento;
import com.example.gerenciamentofarmacia.repository.MedicamentoRepository;
import com.example.gerenciamentofarmacia.service.MedicamentoService;
import com.example.gerenciamentofarmacia.service.RelatorioService;
import com.example.gerenciamentofarmacia.validation.ValidacaoException;
import com.example.gerenciamentofarmacia.validation.Validador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/** Controlador da tela principal. */
public class FarmaciaController {

    private static final String CSV = System.getProperty("farmacia.csv", "dados/medicamentos.csv");

    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final MedicamentoService service = new MedicamentoService(new MedicamentoRepository(Path.of(CSV)));
    private final RelatorioService relatorios = new RelatorioService();
    private final ObservableList<Medicamento> itens = FXCollections.observableArrayList();

    // Formulário
    @FXML private TextField txtCodigo, txtNome, txtDescricao, txtPrincipioAtivo, txtPreco;
    @FXML private DatePicker dpValidade;
    @FXML private Spinner<Integer> spQuantidade;
    @FXML private CheckBox chkControlado;
    @FXML private TextField txtCnpj, txtRazaoSocial, txtTelefone, txtEmail, txtCidade, txtEstado;

    // Consulta / tabela
    @FXML private TextField txtBusca;
    @FXML private TableView<Medicamento> tabela;
    @FXML private TableColumn<Medicamento, String> colCodigo, colNome, colPrincipioAtivo, colValidade,
            colQuantidade, colPreco, colControlado, colFornecedor;
    @FXML private Label lblStatus;

    // Relatórios
    @FXML private TextArea txtRelatorio;

    @FXML
    private void initialize() {
        spQuantidade.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1_000_000, 0));

        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colPrincipioAtivo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPrincipioAtivo()));
        colValidade.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDataValidade() != null ? c.getValue().getDataValidade().format(DATA) : ""
        ));
        colQuantidade.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getQuantidadeEstoque())));
        colPreco.setCellValueFactory(c -> new SimpleStringProperty(MOEDA.format(c.getValue().getPreco())));
        colControlado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getControladoTexto()));
        colFornecedor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFornecedorNome()));

        tabela.setItems(itens);
        tabela.setPlaceholder(new Label("Nenhum medicamento para exibir."));

        try {
            service.carregar();
        } catch (IOException e) {
            Dialogos.erro("Erro ao carregar", "Não foi possível ler o arquivo de dados:\n" + e.getMessage());
        }
        aoListarTodos();
        aoGerarRelatorio();
    }

    // ------------------------------------------------------------------ CRUD

    @FXML
    private void aoCadastrar() {
        try {
            Medicamento novo = lerFormulario();

            // Aplica as validações de regras de negócio acumulando todos os erros
            Validador.validar(novo);

            service.cadastrar(novo);
            aoLimpar();
            aoListarTodos();
            aoGerarRelatorio();
            status("Medicamento " + novo.getCodigo() + " cadastrado com sucesso.");
        } catch (ValidacaoException e) {
            Dialogos.erro("Dados inválidos", String.join("\n", e.getErros()));
        } catch (IOException e) {
            Dialogos.erro("Erro ao gravar o arquivo", e.getMessage());
        }
    }

    @FXML
    public void aoExcluir() {
        Medicamento selecionado = tabela.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            lblStatus.setText("Selecione um medicamento na tabela para excluir.");
            return;
        }

        try {
            service.excluir(selecionado.getCodigo());
            aoListarTodos();
            aoGerarRelatorio();
            lblStatus.setText("Medicamento " + selecionado.getCodigo() + " excluído com sucesso.");
        } catch (Exception e) {
            lblStatus.setText("Erro ao excluir medicamento: " + e.getMessage());
        }
    }

    @FXML
    private void aoConsultar() {
        String codigo = txtBusca.getText() == null ? "" : txtBusca.getText().trim();
        if (codigo.isEmpty()) {
            Dialogos.erro("Consulta", "Informe o código a consultar.");
            return;
        }
        service.buscarPorCodigo(codigo).ifPresentOrElse(m -> {
            itens.setAll(m);
            tabela.getSelectionModel().select(m);
            status("Medicamento encontrado: " + m.getNome());
        }, () -> {
            itens.clear();
            status("Nenhum medicamento com o código " + codigo + ".");
        });
    }

    @FXML
    private void aoListarTodos() {
        itens.setAll(service.listar());
        txtBusca.clear();
        status(itens.size() + " medicamento(s) cadastrado(s).");
    }

    @FXML
    private void aoLimpar() {
        List.of(txtCodigo, txtNome, txtDescricao, txtPrincipioAtivo, txtPreco,
                        txtCnpj, txtRazaoSocial, txtTelefone, txtEmail, txtCidade, txtEstado)
                .forEach(TextField::clear);
        dpValidade.setValue(null);
        if (dpValidade.getEditor() != null) {
            dpValidade.getEditor().clear();
        }
        spQuantidade.getValueFactory().setValue(0);
        chkControlado.setSelected(false);
    }

    // ------------------------------------------------------------ Relatórios

    @FXML private void aoGerarRelatorio() { txtRelatorio.setText(relatorios.gerarTexto(service.listar())); }
    @FXML private void aoRelatorioVencimento() { txtRelatorio.setText(relatorios.textoVencimento(service.listar())); }
    @FXML private void aoRelatorioEstoqueBaixo() { txtRelatorio.setText(relatorios.textoEstoqueBaixo(service.listar())); }
    @FXML private void aoRelatorioFornecedor() { txtRelatorio.setText(relatorios.textoPorFornecedor(service.listar())); }
    @FXML private void aoRelatorioControlados() { txtRelatorio.setText(relatorios.textoControlados(service.listar())); }

    // --------------------------------------------------------------- Apoio

    /** Converte os campos da tela em objetos e trata a data digitada no teclado. */
    private Medicamento lerFormulario() throws ValidacaoException {
        // Tenta pegar a data da seleção do DatePicker
        LocalDate dataValidade = dpValidade.getValue();

        // Se estiver nulo, converte o texto digitado manualmente no campo de entrada
        if (dataValidade == null && dpValidade.getEditor() != null) {
            String textoData = dpValidade.getEditor().getText();
            if (textoData != null && !textoData.trim().isEmpty()) {
                try {
                    dataValidade = LocalDate.parse(textoData.trim(), DATA);
                    dpValidade.setValue(dataValidade);
                } catch (Exception e) {
                    throw new ValidacaoException("Data de validade inválida. Utilize o formato dd/mm/aaaa.");
                }
            }
        }

        // Validação da Quantidade (Spinner)
        try {
            spQuantidade.increment(0);
        } catch (NumberFormatException | NullPointerException e) {
            throw new ValidacaoException("Quantidade deve ser um número inteiro.");
        }
        Integer quantidade = spQuantidade.getValue();
        if (quantidade == null) {
            quantidade = 0;
        }

        // Validação do Preço
        BigDecimal preco = null;
        String textoPreco = txtPreco.getText() == null ? "" : txtPreco.getText().trim();
        if (!textoPreco.isEmpty()) {
            try {
                preco = new BigDecimal(textoPreco.contains(",")
                        ? textoPreco.replace(".", "").replace(",", ".")
                        : textoPreco);
            } catch (NumberFormatException e) {
                throw new ValidacaoException("Preço deve ser um valor numérico válido (ex.: 12,50).");
            }
        }

        Fornecedor fornecedor = new Fornecedor(
                Validador.somenteDigitos(txtCnpj.getText()),
                texto(txtRazaoSocial),
                texto(txtTelefone),
                texto(txtEmail),
                texto(txtCidade),
                texto(txtEstado).toUpperCase());

        return new Medicamento(
                texto(txtCodigo).toUpperCase(),
                texto(txtNome),
                texto(txtDescricao),
                texto(txtPrincipioAtivo),
                dataValidade,
                quantidade,
                preco,
                chkControlado.isSelected(),
                fornecedor);
    }

    private static String texto(TextField campo) {
        return campo.getText() == null ? "" : campo.getText().trim();
    }

    private void status(String mensagem) {
        lblStatus.setText(mensagem);
    }
}