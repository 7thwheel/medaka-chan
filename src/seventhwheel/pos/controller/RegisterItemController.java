package seventhwheel.pos.controller;

import static seventhwheel.pos.db.ConnectionPool.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import seventhwheel.pos.application.PosApplication;
import seventhwheel.pos.control.NumberTextField;
import seventhwheel.pos.control.combobox.BumonComboBox;
import seventhwheel.pos.control.combobox.SuppliersComboBox;
import seventhwheel.pos.model.Bumon;
import seventhwheel.pos.model.Item;
import seventhwheel.pos.model.Suppliers;

public class RegisterItemController implements Initializable {

    public TextField txtBarCode;
    public TextField txtItemName;
    public NumberTextField txtPrice;
    public SuppliersComboBox cobSuppliers;
    public BumonComboBox cobBumon;
    public Button btnRegister;
    public Button btnBack;
    public BorderPane borderPaneRegister;
    public HBox hboxIndicator;
    public Label indicator;
    public Label errorMessage;

    void clearIndicator() {
        hboxIndicator.getChildren().remove(indicator);
        errorMessage.setVisible(false);
    }

    void imeOff() {
      try {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_HIRAGANA);
        robot.keyPress(KeyEvent.VK_HIRAGANA);
        robot.keyPress(KeyEvent.VK_INPUT_METHOD_ON_OFF);
      } catch (AWTException e) {
        /* ignore exception */
      }
    }

    void imeOn() {
      try {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_HIRAGANA);
        robot.keyPress(KeyEvent.VK_HIRAGANA);
      } catch (AWTException e) {
        /* ignore exception */
      }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        imeOff();
        clearIndicator();
        errorMessage.setVisible(false);

        txtBarCode.focusedProperty().addListener(new ChangeListener<Boolean>() {

          @Override
          public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
            if (newValue) {
              imeOff();
            }
          }
        });

        txtPrice.focusedProperty().addListener(new ChangeListener<Boolean>() {

          @Override
          public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
            if (newValue) {
              imeOff();
            } else {
              String price = txtPrice.getText();
              price = price.replaceAll("０", "0");
              price = price.replaceAll("１", "1");
              price = price.replaceAll("２", "2");
              price = price.replaceAll("３", "3");
              price = price.replaceAll("４", "4");
              price = price.replaceAll("５", "5");
              price = price.replaceAll("６", "6");
              price = price.replaceAll("７", "7");
              price = price.replaceAll("８", "8");
              price = price.replaceAll("９", "9");
              txtPrice.setText(price);
            }
          }
        });

        txtItemName.focusedProperty().addListener(new ChangeListener<Boolean>() {

          @Override
          public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
            if (newValue) {
              imeOn();
            }
          }
        });
    }

    @FXML
    private void handleTxtBarCodeAction(ActionEvent event) {
        clearIndicator();

        if (txtBarCode.getText().isEmpty()) {
            return;
        }

        txtItemName.setDisable(false);
        txtItemName.requestFocus();

        Item item = select(txtBarCode.getText());
        if (item != null) {
            txtItemName.setText(item.getName());
            txtPrice.setText(item.getPrice());

            if (item.getSupplierCode() == 0) {
                cobSuppliers.setValue(null);
            } else {
                Suppliers suppliers = new Suppliers(item.getSupplierCode());
                cobSuppliers.setValue(suppliers);
            }

            if (item.getBumonCode() == 0) {
                cobBumon.setValue(null);
            } else {
                Bumon bumon = new Bumon(item.getBumonCode());
                cobBumon.setValue(bumon);
            }
        } else {
            txtItemName.clear();
            txtPrice.clear();
            cobSuppliers.setValue(null);
            cobBumon.setValue(null);
        }

        if (item != null) {
            showIndicator(indicator);
        }
    }

    void showIndicator(Label indicator) {
        clearIndicator();
        hboxIndicator.getChildren().add(indicator);
    }

    void showSuccessMessage() {
        MainController.showMessageBar("登録しました");
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
        cobSuppliers.requestFocus();
    }

    /**
     * 登録ボタンのアクションハンドラ
     *
     * @param event
     */
    @FXML
    private void handleBtnRegisterAction(ActionEvent event) {
        if (!validate()) {
            return;
        }

        String itemCode = txtBarCode.getText();
        Item item = select(itemCode);

        if (item == null) {
            String insert = "INSERT INTO Item (ItemCode, Name, Price, SupplierCode, BumonCode) VALUES (?, ?, ?, ?, ?);";
            getPersist().executeUpdate(insert, itemCode, txtItemName.getText(), txtPrice.getText(),
                    cobSuppliers.getSupplierCode(), cobBumon.getBumonCode());
        } else {
            item.setName(txtItemName.getText());
            item.setPrice(txtPrice.getText());
            item.setSupplierCode(cobSuppliers.getSupplierCode());
            item.setBumonCode(cobBumon.getBumonCode());
            getPersist().update(item);
        }

        txtBarCode.requestFocus();
        txtBarCode.clear();
        txtItemName.clear();
        txtPrice.clear();
        cobSuppliers.setValue(null);
        cobBumon.setValue(null);
        showSuccessMessage();
        clearIndicator();
    }

    /**
     * 入力値の妥当性を確認します。
     *
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
        String sql = "SELECT * FROM Item where ItemCode = ?;";
        return getPersist().read(Item.class, sql, itemCode);
    }

}
