package seventhwheel.pos.control;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.SceneBuilder;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import seventhwheel.pos.application.PosApplication;
import seventhwheel.pos.constants.UserAction;

public class MessageBox extends Stage {

  public UserAction userAction;

  public static UserAction show(Window owner) {
    final Region root;
    try {
      root = FXMLLoader.load(PosApplication.class.getResource("MessageBox.fxml"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    MessageBox messageBox = new MessageBox();
    messageBox.initModality(Modality.APPLICATION_MODAL);
    messageBox.initOwner(owner);
    messageBox.initStyle(StageStyle.TRANSPARENT);
    StageBuilder.create()
        .scene(SceneBuilder.create()
            .root(root).build())
        .applyTo(messageBox);
    messageBox.showAndWait();

    UserAction userAction = messageBox.userAction;
    messageBox = null;

    return userAction;
  }

}
