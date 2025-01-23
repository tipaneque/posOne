package com.posone;

import com.posone.dao.ProductDAO;
import com.posone.model.Product;
import com.posone.model.ProductTableRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonPrintBill;

    @FXML
    private TableColumn<ProductTableRow, Float> columnPrice;

    @FXML
    private TableColumn<ProductTableRow, String> columnProductID;

    @FXML
    private TableColumn<ProductTableRow, String> columnProductName;

    @FXML
    private TableColumn<ProductTableRow, Integer> columnQuantity;

    @FXML
    private TableColumn<ProductTableRow, Float> columnTotal;

    @FXML
    private TextField textFieldProductTotalPrice;

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private TableView<ProductTableRow> tableWiew;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField textFieldBalance;

    @FXML
    private TextField textFieldPay;

    @FXML
    private TextField textFieldProductName;

    @FXML
    private TextField textFieldProductPrice;

    @FXML
    private Label labelTotal;

    @FXML
    private TextField textFieldproductCpde;


    private ProductDAO productDAO;

    private SpinnerValueFactory<Integer> spinnerValueFactory;

    Product product;
    private float total;
    private boolean paid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paid = false;
        spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000);
        spinnerValueFactory.setValue(1);
        labelTotal.setText(textFieldProductPrice.getText());
        spinner.setValueFactory(spinnerValueFactory);
        spinner.setDisable(true);
        handleTotalPrice();
        textFieldproductCpde.setOnAction(e -> handleCodeField());
        buttonAdd.setOnAction( event -> handleButtonAdd());
        textFieldPay.setOnAction(e -> handleTextFieldPay());
        buttonPrintBill.setOnAction(e -> defBill());

    }

    void handleCodeField(){
        productDAO = new ProductDAO();
        product = productDAO.getProduct(textFieldproductCpde.getText());
        if(product != null){
            textFieldProductName.setText(product.getName());
            textFieldProductPrice.setText(product.getPrice() + "");
            textFieldProductTotalPrice.setText(textFieldProductPrice.getText());
            spinner.setDisable(false);
        }
    }

    void handleTotalPrice(){
        spinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                if(!textFieldProductPrice.getText().isEmpty())
                    textFieldProductTotalPrice.setText((spinner.getValue() * Float.parseFloat(textFieldProductPrice.getText())) + "");
            }
        });
    }

    void initTable(){
        columnProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));


    }

    void handleButtonAdd(){
        if(textFieldProductTotalPrice.getText().isEmpty()){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Erro");
           alert.setContentText("Preencha todos os dados");
           alert.show();
        }else {
            initTable();
            ObservableList<ProductTableRow> rows = FXCollections.observableArrayList();

            tableWiew.getItems().add(new ProductTableRow(
                    textFieldproductCpde.getText(),
                    textFieldProductName.getText(),
                    Float.parseFloat(textFieldProductPrice.getText()),
                    spinner.getValue(),
                    Float.parseFloat(textFieldProductTotalPrice.getText())
            ));
            textFieldproductCpde.requestFocus();
            setTotalValue();
            cleanFields();

        }
    }

    void cleanFields(){
        textFieldproductCpde.setText("");
        textFieldProductName.setText("");
        textFieldProductPrice.setText("");
        textFieldProductTotalPrice.setText("");
        spinnerValueFactory.setValue(1);
        spinner.setDisable(true);
    }

    void setTotalValue(){
        if(labelTotal.getText().isEmpty()) {
            total = Float.parseFloat(textFieldProductTotalPrice.getText());
            labelTotal.setText(String.valueOf(total));
        }
        else {
            float totalByProduct = Float.parseFloat(textFieldProductTotalPrice.getText());
            total = Float.parseFloat(labelTotal.getText());
            total += totalByProduct;
            labelTotal.setText(String.valueOf(total));
            System.out.println("total 1: " + total);
        }
        System.out.println("total 2: " + total);
    }

    void handleTextFieldPay(){
        if(textFieldPay.getText().trim().isEmpty() || labelTotal.getText().isEmpty()){
            System.out.println("Campo vazio");
        }else{
            float valor = Float.parseFloat(textFieldPay.getText());
            if(valor < total){
                System.out.println("Dinheiro insuficiente");
            }else {
                float balance = valor - total;
                textFieldBalance.setText(String.valueOf(balance));
                paid = true;
            }
        }
    }

    void defBill(){
        if(paid){
            textArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14;");

            StringBuilder receipt = new StringBuilder();

            receipt.append("**************************************\n");
            receipt.append(String.format("%22s\n", "RECIBO"));
            receipt.append("**************************************\n\n");

            receipt.append(String.format("%-22s %-10s %-4s\n", "Produto", "Preço", "Qtde"));

            for (int i = 0; i < tableWiew.getItems().size(); i++) {
                ProductTableRow prod = tableWiew.getItems().get(i);
                receipt.append(String.format("%-22s %-10.2f %-4d\n", prod.getName(), prod.getPrice(), prod.getQuantity()));
            }

            receipt.append("\n".repeat(4));
            receipt.append(String.format("%15s %-10s %7.2f\n", " ", "Total:", total));
            receipt.append(String.format("%15s %-6s %7.2f\n", " ", "Pagamento:", Float.parseFloat(textFieldPay.getText())));
            receipt.append(String.format("%15s %-10s %7.2f\n", " ", "Troco:", Float.parseFloat(textFieldBalance.getText())));

            receipt.append("\n\n");
            receipt.append("**************************************\n");

            receipt.append(String.format("%15s", "Obrigado pela preferência"));

            textArea.setText(receipt.toString());
            paid = false;
        }else {
            System.out.println("Pagamento nao efectuado");
        }

    }
}