package seventhwheel.pos.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import seventhwheel.pos.application.PosApplication;

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
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class SuppliersController implements Initializable {

    public Button btnRegister;
    public Button btnBack;
    public Node borderPane;

   @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

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

}
