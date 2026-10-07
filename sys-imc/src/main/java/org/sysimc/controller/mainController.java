package org.sysimc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.sysimc.model.Pessoa;

import java.text.DecimalFormat;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
public class mainController {

    @FXML
    protected TextField txtNome;

    @FXML
    protected TextField txtAltura;

    @FXML
    protected TextField txtPeso;

    @FXML
    protected Label lbIMC;
    @FXML
    protected Label lbClassificacao;


    Pessoa pessoa = new Pessoa();

    @FXML
    protected void onCalularIMCClick() {
        DecimalFormat df = new DecimalFormat("#0.00");
        this.pessoa = new Pessoa();   // <-- nova pessoa a cada cálculo
        this.pessoa.setNome( this.txtNome.getText() );
        this.pessoa.setAltura( Float.parseFloat(this.txtAltura.getText().replace(",", ".")) );
        this.pessoa.setPeso( Float.parseFloat(this.txtPeso.getText().replace(",", ".")) );

        this.lbIMC.setText(df.format(this.pessoa.calcularIMC()) );
        this.lbClassificacao.setText( this.pessoa.classificacaoIMC() );

        lista.add(this.pessoa);       // <-- aparece na tabela
    }


    @FXML
    protected void onSalvarClick() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Salvar dados");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivo de texto", "*.txt"));
        File arquivo = fc.showSaveDialog(txtNome.getScene().getWindow());

        if (arquivo != null) {
            try {
                Files.write(arquivo.toPath(), List.of(
                        txtNome.getText(),
                        txtAltura.getText(),
                        txtPeso.getText()));
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Erro ao salvar: " + ex.getMessage()).show();
            }
        }
    }

    @FXML
    protected void onCarregarClick() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Carregar dados");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivo de texto", "*.txt"));
        File arquivo = fc.showOpenDialog(txtNome.getScene().getWindow());

        if (arquivo != null) {
            try {
                List<String> linhas = Files.readAllLines(arquivo.toPath());
                txtNome.setText(linhas.get(0));
                txtAltura.setText(linhas.get(1));
                txtPeso.setText(linhas.get(2));
                onCalularIMCClick(); // recalcula e atualiza IMC e classificação
            } catch (IOException | IndexOutOfBoundsException | NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR,
                        "Arquivo inválido: " + ex.getMessage()).show();
            }
        }
    }
    @FXML private TableView<Pessoa> tabela;
    @FXML private TableColumn<Pessoa, String> colId;
    @FXML private TableColumn<Pessoa, String> colNome;
    @FXML private TableColumn<Pessoa, String> colAltura;
    @FXML private TableColumn<Pessoa, String> colPeso;
    @FXML private TableColumn<Pessoa, String> colImc;

    private final ObservableList<Pessoa> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        DecimalFormat df = new DecimalFormat("#0.00");
        tabela.setItems(lista);

        colId.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(lista.indexOf(c.getValue()) + 1)));
        colNome.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNome()));
        colAltura.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getAltura())));
        colPeso.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getPeso())));
        colImc.setCellValueFactory(c ->
                new SimpleStringProperty(df.format(c.getValue().calcularIMC())));
    }
}