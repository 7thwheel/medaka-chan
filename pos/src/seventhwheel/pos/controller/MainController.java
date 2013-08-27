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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import seventhwheel.pos.application.PosApplication;

public class MainController implements Initializable {

    public BorderPane borderPane;
    public StackPane rootPane;
    public Button btnRegisterItem;

    private static MainController mainController;

    public static MainController getController() {
        return mainController;
    }

    public static void setController(MainController controller) {
        mainController = controller;
    }

    public static void add(Node node) {
        getController().rootPane.getChildren().add(node);
    }

    public static void remove(Object object) {
        getController().rootPane.getChildren().remove(object);
    }

    public MainController() {
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setController(this);

        BorderPane pos;
        try {
            pos = FXMLLoader.load(PosApplication.class.getResource("pos.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rootPane.getChildren().add(pos);
    }

    @FXML
    private void handleBtnRegisterItem(ActionEvent event) {
        final Node pos = rootPane.getChildren().get(0);

        final Region registerItem;
        try {
            registerItem = FXMLLoader.load(PosApplication.class.getResource("RegisterItem.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        registerItem.prefHeightProperty().bind(borderPane.heightProperty());
        registerItem.prefWidthProperty().bind(borderPane.widthProperty());

        MainController.add(registerItem);

        double windowWidth = registerItem.getScene().getWindow().getWidth();

        TranslateTransition translateOut = TranslateTransitionBuilder.create()
                .node(pos)
                .duration(Duration.millis(600))
                .fromX(0)
                .toX(-windowWidth)
                .build();
        TranslateTransition translateIn = TranslateTransitionBuilder.create()
                .node(registerItem)
                .duration(Duration.millis(600))
                .fromX(windowWidth)
                .toX(0)
                .build();

        translateOut.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                MainController.remove(pos);
            }
        });

        new ParallelTransition(translateOut, translateIn).play();
    }

}
