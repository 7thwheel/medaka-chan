/**
 *
 */
package seventhwheel.pos.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seventhwheel.pos.db.ConnectionPool;

/**
 * @author akifumi
 *
 */
public class PosApplication extends Application {

    /* (非 Javadoc)
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane main = FXMLLoader.load(PosApplication.class.getResource("main.fxml"));

        stage.setTitle("みせっこくちない専用POSシステム");
        stage.setScene(new Scene(main, 800, 600));
        stage.show();


    }

    @Override
    public void stop() throws Exception {
        super.stop();
        ConnectionPool.close();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
