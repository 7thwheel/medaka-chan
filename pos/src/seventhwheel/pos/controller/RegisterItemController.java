package seventhwheel.pos.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import seventhwheel.pos.application.PosApplication;
import seventhwheel.pos.constants.UserAction;
import seventhwheel.pos.control.MessageBox;
import seventhwheel.pos.control.NumberTextField;
import seventhwheel.pos.db.ConnectionPool;
import seventhwheel.pos.model.Bumon;
import seventhwheel.pos.model.Item;
import seventhwheel.pos.model.Suppliers;

public class RegisterItemController implements Initializable {

  public TextField txtBarCode;
  public TextField txtItemName;
  public NumberTextField txtPrice;
  public ComboBox<Bumon> cobBumon;
  public ComboBox<Suppliers> cobSuppliers;
  public Button btnRegister;
  public Button btnBack;
  public BorderPane borderPaneRegister;
  public Label indicator;
  public Label errorMessage;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    indicator.setVisible(false);
    errorMessage.setVisible(false);
  }

  @FXML
  private void handleTxtBarCodeAction(ActionEvent event) {
    if (txtBarCode.getText().isEmpty()) {
      return;
    }

    txtItemName.setDisable(false);
    txtItemName.requestFocus();

    Item item = select(txtBarCode.getText());
    if (item != null) {
      txtItemName.setText(item.getName());
      txtPrice.setText(item.getPrice());
      Suppliers suppliers = new Suppliers(item.getSupplierCode());
      cobSuppliers.setValue(suppliers);
    } else {
      txtItemName.clear();
      txtPrice.clear();
    }

    indicator.setVisible(item != null);
  }

  @FXML
  private void handleTxtItemNameAction(ActionEvent event) {
    if (txtItemName.getText().isEmpty()) {
      return;
    }
    txtPrice.setDisable(false);
    txtPrice.requestFocus();
  }

  @FXML
  private void handleTxtPriceAction(ActionEvent event) {
    if (txtPrice.getText().isEmpty()) {
      return;
    }
    btnRegister.setDisable(false);
    btnRegister.requestFocus();
  }

  /**
   * 登録ボタンのアクションハンドラ
   * @param event
   */
  @FXML
  private void handleBtnRegisterAction(ActionEvent event) {
    if (!validate()) {
      return;
    }

    if (MessageBox.show(btnRegister.getScene().getWindow()) == UserAction.CANCEL) {
      return;
    }

    // update
    String itemCode = txtBarCode.getText();
    Item item = select(itemCode);

    Connection con = ConnectionPool.getConnection();

    try {
        if (item == null) {
            try (Statement stmt2 = con.createStatement()) {
              String sql = "INSERT INTO Item (ItemCode, Name, Price, SupplierCode) VALUES ('%s', '%s', '%s', '%s');";
              stmt2.executeUpdate(String.format(
                      sql, itemCode, txtItemName.getText(), txtPrice.getText(), cobSuppliers.getValue().getSuppliercode()));
            }
          } else {
            try (Statement stmt2 = con.createStatement()) {
              String sql = "UPDATE Item SET Name='%s', Price='%s', SupplierCode=%s WHERE ItemCode='%s';";
              stmt2.executeUpdate(
                      String.format(
                              sql, txtItemName.getText(), txtPrice.getText(), cobSuppliers.getValue().getSuppliercode(), itemCode));
            }
          }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
//    try (Statement stmt = con.createStatement()) {
//      String sql = "SELECT * FROM Item where ItemCode = '%s';";
//      String itemCode = txtBarCode.getText();
//      ResultSet rs = stmt.executeQuery(String.format(sql, itemCode));
//
//      List<Item> items = new ArrayList<>();
//      while (rs.next()) {
//        Item item = new Item();
//        item.setItemcode(rs.getString("ItemCode"));
//        item.setName(rs.getString("Name"));
//        item.setPrice(rs.getString("Price"));
//        items.add(item);
//      }
//      if (items.isEmpty()) {
//        try (Statement stmt2 = con.createStatement()) {
//          sql = "INSERT INTO Item (ItemCode, Name, Price) VALUES ('%s', '%s', '%s');";
//          stmt2.executeUpdate(String.format(sql, itemCode, txtItemName.getText(), txtPrice.getText()));
//        }
//      } else {
//        try (Statement stmt2 = con.createStatement()) {
//          sql = "UPDATE Item SET Name='%s', Price='%s' WHERE ItemCode='%s';";
//          stmt2.executeUpdate(String.format(sql, txtItemName.getText(), txtPrice.getText(), itemCode));
//        }
//      }
//
//    } catch (SQLException e) {
//      throw new RuntimeException(e);
//    }

    txtBarCode.requestFocus();
  }

  /**
   * 入力値の妥当性を確認します。
   * @return
   */
  boolean validate() {
    errorMessage.setVisible(false);
    errorMessage.setText("");

    if (!validateRequirement(txtBarCode, "バーコード")) {
      return false;
    }

    if (!validateRequirement(txtItemName, "商品名")) {
      return false;
    }

    if (!validateRequirement(txtPrice, "単価")) {
      return false;
    }

    return true;
  }

  boolean validateRequirement(TextField field, String fieldName) {
    if (field.getText().isEmpty()) {
      showErrorMessage(String.format("%sを入力してください。", fieldName));
      return false;
    }

    return true;
  }

  void showErrorMessage(String message) {
    errorMessage.setVisible(true);
    errorMessage.setText(message);
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
        .node(borderPaneRegister)
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
        MainController.remove(borderPaneRegister);
      }
    });

    new ParallelTransition(translateOut, translateIn).play();
  }

  public Item select(String itemCode) {
    try (Statement stmt = ConnectionPool.getConnection().createStatement()) {
      String sql = "SELECT * FROM Item where ItemCode = '%s';";
      ResultSet rs = stmt.executeQuery(String.format(sql, itemCode));

      List<Item> items = new ArrayList<>();
      while (rs.next()) {
        Item item = new Item();
        item.setItemcode(rs.getString("ItemCode"));
        item.setName(rs.getString("Name"));
        item.setPrice(rs.getString("Price"));
        item.setSupplierCode(rs.getInt("SupplierCode"));
        items.add(item);
      }

      if (items.isEmpty()) {
        return null;
      } else {
        return items.get(0);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
