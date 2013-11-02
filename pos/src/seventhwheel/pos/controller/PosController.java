package seventhwheel.pos.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seventhwheel.pos.db.ConnectionPool;
import seventhwheel.pos.model.Item;
import seventhwheel.pos.model.ItemSaleModel;
import seventhwheel.pos.model.Saleitems;
import seventhwheel.pos.model.Sales;

public class PosController implements Initializable {
    public TextField txtBarCode;
    public TextField txtItemName;
    public TextField txtPrice;
    public BorderPane borderPane;
    public Label lblTotalAmount;
    public Button btnRegister;
    public Button btnCancel;
    public VBox bodyPos;
    public HBox hboxChange;
    public Label lblChange;
    public Label lblCustomers;
    public Label lblItems;

    public TableView<ItemSaleModel> table;
    public TableColumn<ItemSaleModel, String> colItemName;
    public TableColumn<ItemSaleModel, String> colPrice;
    public TableColumn<ItemSaleModel, String> colQuantity;
    public TableColumn<ItemSaleModel, String> colAmount;

    private int quantity;
    private SimpleStringProperty totalAmountProperty = new SimpleStringProperty("0");
    private int totalAmount = 0;
    private int itemCounter = 0;

    public PosController() {
    }

    void initQuantity() {
        quantity = 1;
    }

    void initItemCounter() {
        itemCounter = 0;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ConnectionPool.getConnection();

        colItemName.setCellValueFactory(
                new PropertyValueFactory<ItemSaleModel, String>("itemName"));
        colPrice.setCellValueFactory(
                new PropertyValueFactory<ItemSaleModel, String>("price"));
        colQuantity.setCellValueFactory(
                new PropertyValueFactory<ItemSaleModel, String>("quantity"));
        colAmount.setCellValueFactory(
                new PropertyValueFactory<ItemSaleModel, String>("amount"));

        initQuantity();
        txtBarCode.requestFocus();
        lblTotalAmount.textProperty().bind(totalAmountProperty);
        table.getItems().addListener(new ListChangeListener<ItemSaleModel>() {

            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends ItemSaleModel> arg0) {
                btnRegister.setDisable(table.getItems().isEmpty());
                btnCancel.setDisable(table.getItems().isEmpty());
            }
        });

        table.getItems().clear();
        hideChange();
        countCustomers();
    }

    private void hideChange() {
        bodyPos.getChildren().remove(hboxChange);
    }

    private void showChange() {
        if (bodyPos.getChildren().contains(hboxChange)) {
            return;
        }

        bodyPos.getChildren().add(1, hboxChange);
    }

    @FXML
    private void handleBtnRegisterAction(ActionEvent event) {
        String id = String.valueOf(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = format.format(new Date(Long.parseLong(id)));

        Connection con = ConnectionPool.getConnection();
        try {
            con.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Sales sales = new Sales(id, datetime, String.valueOf(totalAmount));
        try {
            sales.insert(con);
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException r) {
                throw new RuntimeException(r);
            }
            throw new RuntimeException(e);
        }

        for (ItemSaleModel model : table.getItems()) {
            Saleitems saleItems = new Saleitems();
            saleItems.setSaleid(id);
            saleItems.setItemcode(model.getItem().getItemcode());
            saleItems.setName(model.getItemName());
            saleItems.setPrice(model.getPrice());
            saleItems.setQuantity(Integer.parseInt(model.getQuantity()));
            saleItems.setSuppliercode(model.getItem().getSupplierCode());
            saleItems.setBumoncode(model.getItem().getBumonCode());
            try {
                saleItems.insert(con);
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException r) {
                    throw new RuntimeException(r);
                }
                throw new RuntimeException(e);
            }
        }

        try {
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        clearFields();
    }

    private void clearFields() {
        initQuantity();
        initTotalAmount();
        initItemCounter();
        txtBarCode.clear();
        txtBarCode.requestFocus();
        table.getItems().clear();
        hideChange();
        countCustomers();
        updateItemCounter(0);
    }

    @FXML
    private void handleBtnClearAction(ActionEvent event) {
        clearBarCode();
    }

    private void clearBarCode() {
        initQuantity();
        txtBarCode.clear();
        txtBarCode.requestFocus();
    }

    @FXML
    private void handleBtnCancelAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void handleBtnDeleteAction(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex > -1) {
            ItemSaleModel model = table.getItems().remove(selectedIndex);
            sumTotalAmount(Integer.parseInt(model.getAmount()) * -1);
            updateItemCounter(Integer.parseInt(model.getQuantity()) * -1);
        }
    }

    @FXML
    private void handleTxtBarCodeAction(ActionEvent event) {
        String itemCode = txtBarCode.getText();

        // Subtraction
        if (itemCode.matches("[0-9]+-$")) {
            int input = Integer.parseInt(itemCode.replaceAll("-", ""));
            if (input > 0 && totalAmount > 0 && input - totalAmount > 0) {
                showChange();
                lblChange.setText(String.format("%,d", input - totalAmount));
            }

            clearBarCode();
            return;
        }

        hideChange();

        // Quantity
        if (itemCode.matches("^[0-9]+\\*[0-9]+")) {
            String[] split = itemCode.split("\\*");
            quantity = Integer.valueOf(split[0]);
            itemCode = split[1];
        }

        Connection con = ConnectionPool.getConnection();
        ItemSaleModel model = null;

        try (Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM Item where ItemCode = '%s'";
            ResultSet rs = stmt.executeQuery(String.format(sql, itemCode));

            while (rs.next()) {
                itemCode = rs.getString("ItemCode");
                String name = rs.getString("Name");
                String price = rs.getString("Price");
                int supplierCode = rs.getInt("SupplierCode");
                int bumonCode = rs.getInt("BumonCode");
                BigDecimal amount = BigDecimal.valueOf(Integer.parseInt(price)).multiply(
                        BigDecimal.valueOf(quantity));
                Item item = new Item();
                item.setItemcode(itemCode);
                item.setName(name);
                item.setPrice(price);
                item.setSupplierCode(supplierCode);
                item.setBumonCode(bumonCode);
                model = new ItemSaleModel(name, price, String.valueOf(quantity), amount.toString(), item);
                table.getItems().add(model);
                sumTotalAmount(amount.intValue());
                updateItemCounter(quantity);
            }
            initQuantity();
            txtBarCode.clear();
            txtBarCode.home();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void updateItemCounter(int delta) {
        itemCounter += delta;
        lblItems.setText(String.format("お買い上げ点数 %s 点", itemCounter));
    }

    void sumTotalAmount(int amount) {
        totalAmount = BigDecimal.valueOf(totalAmount).add(BigDecimal.valueOf(amount)).intValue();
        totalAmountProperty.set(String.format("%,d", totalAmount));
    }

    void initTotalAmount() {
        totalAmount = 0;
        totalAmountProperty.set("0");
    }

    void countCustomers() {
        String sql = "SELECT COUNT(*) FROM Sales WHERE SUBSTR(DateTime, 1, 10) = '%s';";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        String currentDate = df.format(d);

        Connection con = ConnectionPool.getConnection();
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(String.format(sql, currentDate));

            while (rs.next()) {
                String count = rs.getString(1);
                lblCustomers.setText(String.format("来客数 %s 人", count));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
