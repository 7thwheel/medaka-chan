package seventhwheel.pos.controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import seventhwheel.pos.application.PosApplication;
import seventhwheel.pos.control.NumberTextField;

public class RegisterItemController implements Initializable {

    public TextField txtBarCode;
    public TextField txtItemName;
    public NumberTextField txtPrice;
    public Button btnRegister;
    public Button btnBack;
    public BorderPane borderPaneRegister;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }

    @FXML
    private void handleTxtBarCodeAction(ActionEvent event) {
        if (txtBarCode.getText().isEmpty()) {
            return;
        }
        txtItemName.setDisable(false);
        txtItemName.requestFocus();
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
        if (!txtPrice.getText().matches("[0-9]+")) {
            txtPrice.requestFocus();
            return;
        }


        txtBarCode.requestFocus();
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

}
