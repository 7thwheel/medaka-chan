package seventhwheel.pos.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import seventhwheel.pos.application.PosApplication;
import seventhwheel.pos.db.ConnectionPool;
import seventhwheel.pos.model.Item;
import seventhwheel.pos.model.ItemSaleModel;

public class PosController implements Initializable {

    public TextField txtBarCode;
    public TextField txtItemName;
    public TextField txtPrice;
    public Button btnRegisterItem;
    public BorderPane borderPane;

    public TableView<ItemSaleModel> table;
    public TableColumn<ItemSaleModel, String> colItemName;
    public TableColumn<ItemSaleModel, String> colPrice;
    public TableColumn<ItemSaleModel, String> colAmount;

    public PosController() {
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        txtBarCode.setOnKeyTyped(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                System.out.println(event);
            }
        });

        colItemName.setCellValueFactory(
            new PropertyValueFactory<ItemSaleModel, String>("itemName"));
        colPrice.setCellValueFactory(
            new PropertyValueFactory<ItemSaleModel, String>("price"));
        colAmount.setCellValueFactory(
            new PropertyValueFactory<ItemSaleModel, String>("amount"));
    }

    @FXML
    private void handleBtnClearAction(ActionEvent event) {
        txtItemName.clear();
        txtPrice.clear();
        txtBarCode.clear();
        txtBarCode.requestFocus();
    }

    @FXML
    private void handleTxtBarCodeAction(ActionEvent event) {
        Connection con = ConnectionPool.getConnection();
        ItemSaleModel model = null;

        try (Statement stmt = con.createStatement()) {
            String sql = "SELECT * FROM Item where ItemCode = '%s'";
            String itemCode = txtBarCode.getText();
            ResultSet rs = stmt.executeQuery(String.format(sql, itemCode));

            while (rs.next()) {
                String name = rs.getString("name");
                String price = rs.getString("price");
                model = new ItemSaleModel(name, price, "1");
            }
            txtBarCode.clear();
            txtBarCode.home();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        table.getItems().add(model);
    }

    @FXML
    private void handleBtnRegisterItem(ActionEvent event) {
        btnRegisterItem.setDisable(true);
        final Region root;
        try {
            root = FXMLLoader.load(PosApplication.class.getResource("RegisterItem.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        root.prefHeightProperty().bind(borderPane.heightProperty());
        root.prefWidthProperty().bind(borderPane.widthProperty());

        MainController.add(root);

        double windowWidth = root.getScene().getWindow().getWidth();

        TranslateTransition translateOut = TranslateTransitionBuilder.create()
                .node(borderPane)
                .duration(Duration.millis(600))
                .fromX(0)
                .toX(-windowWidth)
                .build();
        TranslateTransition translateIn = TranslateTransitionBuilder.create()
                .node(root)
                .duration(Duration.millis(600))
                .fromX(windowWidth)
                .toX(0)
                .build();

        translateOut.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                MainController.remove(borderPane);
            }
        });

        new ParallelTransition(translateOut, translateIn).play();
    }

}
