package seventhwheel.pos.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import seventhwheel.pos.db.ConnectionPool;
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
        try (Statement stmt = ConnectionPool.getConnection().createStatement()) {
            String sql = "SELECT * FROM Suppliers;";
            ResultSet rs = stmt.executeQuery(sql);

            List<SuppliersModel> suppliers = new ArrayList<>();
            while (rs.next()) {
                SuppliersModel supplier = new SuppliersModel();
                supplier.setSuppliercode(rs.getInt("SupplierCode"));
                supplier.setName(rs.getString("Name"));
                suppliers.add(supplier);
            }

            return suppliers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void updateTable() {
        table.getItems().setAll(select());
    }

    @FXML
    private void handleBtnRegisterAction(ActionEvent event) {
        String sql = "";
        if (mode == Mode.MODIFY) {
            sql = "update Suppliers set name = ? where suppliercode = ?;";
            try (PreparedStatement ps = ConnectionPool.getConnection().prepareStatement(sql)) {
                ps.setString(1, txtName.getText());
                ps.setInt(2, selected.getSuppliercode());
                ps.executeUpdate();
                mode = Mode.ADD;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            sql = "insert into Suppliers (name) values (?);";
            try (PreparedStatement ps = ConnectionPool.getConnection().prepareStatement(sql)) {
                ps.setString(1, txtName.getText());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
