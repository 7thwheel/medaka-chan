package seventhwheel.pos.controller;
import static seventhwheel.pos.db.ConnectionPool.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import seventhwheel.pos.application.PosApplication;
import seventhwheel.pos.model.Suppliers;
import seventhwheel.pos.model.SuppliersModel;

public class SuppliersController implements Initializable {

    public Button btnRegister;
    public Button btnBack;
    public Node borderPane;
    public TextField txtName;

    public TableView<SuppliersModel> table;
    public TableColumn<SuppliersModel, String> colCode;
    public TableColumn<SuppliersModel, String> colName;

    enum Mode {
        ADD,
        MODIFY
    }

    Mode mode = Mode.ADD;
    SuppliersModel selected;

   @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       colCode.setCellValueFactory(
               new PropertyValueFactory<SuppliersModel, String>("suppliercode"));
       colName.setCellValueFactory(
               new PropertyValueFactory<SuppliersModel, String>("name"));

       updateTable();
    }

    @FXML
    private void handleBtnBackAction(ActionEvent event) {
        btnBack.setDisable(true);
        final Region root;
        try {
            root = FXMLLoader.load(PosApplication.class.getResource("pos.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MainController.add(root);

        double windowWidth = root.getScene().getWindow().getWidth();

        TranslateTransition translateOut = TranslateTransitionBuilder.create()
                .node(borderPane)
                .duration(Duration.millis(600))
                .fromX(0)
                .toX(windowWidth)
                .build();
        TranslateTransition translateIn = TranslateTransitionBuilder.create()
                .node(root)
                .duration(Duration.millis(600))
                .fromX(-windowWidth)
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

    List<SuppliersModel> select() {
        String sql = "SELECT * FROM Suppliers;";
        List<Suppliers> list = getPersist().readList(Suppliers.class, sql);
        List<SuppliersModel> models = new ArrayList<>();
        for (Suppliers suppliers : list) {
            SuppliersModel model = new SuppliersModel();
            model.setSuppliercode(suppliers.getSuppliercode());
            model.setName(suppliers.getName());
            models.add(model);
        }
        return models;
    }

    void updateTable() {
        table.getItems().setAll(select());
    }

    @FXML
    private void handleBtnRegisterAction(ActionEvent event) {
        if (mode == Mode.MODIFY) {
            String sql = "update Suppliers set name = ? where suppliercode = ?;";
            getPersist().executeUpdate(sql, txtName.getText(), selected.getSuppliercode());
            mode = Mode.ADD;
        } else {
            getPersist().executeUpdate("insert into Suppliers (name) values (?);", txtName.getText());
        }

        updateTable();
        txtName.clear();
        txtName.requestFocus();
        MainController.showMessageBar("登録しました");
    }

    @FXML
    private void handleBtnModifyAction(ActionEvent event) {
        if (table.getSelectionModel().isEmpty()) {
            return;
        }

        mode = Mode.MODIFY;
        selected = table.getSelectionModel().getSelectedItem();
        txtName.setText(selected.getName());
    }

}
