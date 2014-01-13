package seventhwheel.pos.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import seventhwheel.pos.constants.UserAction;
import seventhwheel.pos.control.MessageBox;

public class MessageBoxController implements Initializable {

  public AnchorPane root;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
  }

  @FXML
  private void handleTorokuButton(ActionEvent event) {
    getMessageBox().userAction = UserAction.SUBMIT;
    hide();
  }

  @FXML
  private void handleCancelButton(ActionEvent event) {
    getMessageBox().userAction = UserAction.CANCEL;
    hide();
  }

  private MessageBox getMessageBox() {
    return (MessageBox) root.getScene().getWindow();
  }

  private void hide() {
    getMessageBox().hide();
  }

}