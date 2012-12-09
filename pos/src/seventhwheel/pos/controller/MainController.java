package seventhwheel.pos.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import seventhwheel.pos.application.PosApplication;

public class MainController implements Initializable {

    public BorderPane borderPane;
    public StackPane rootPane;

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

}
