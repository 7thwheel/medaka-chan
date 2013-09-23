package seventhwheel.pos.controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import seventhwheel.pos.constants.UserAction;

public class ReportDetailController implements Initializable {

    public Label lblDate;

    private String currentDate;
    private UserAction userAction;

    public String getCurrentDate() {
        return currentDate;
    }

    public UserAction getUserAction() {
        return userAction;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd（E）");
        Date d = new Date();
        currentDate = df.format(d);
        lblDate.setText(currentDate);
    }

    @FXML
    private void handleBtnPrevAction(ActionEvent event) {
        move(-1);
    }

    @FXML
    private void handleBtnNextAction(ActionEvent event) {
        move(1);
    }

    @FXML
    private void handleBtnCreateAction(ActionEvent event) {
        userAction = UserAction.SUBMIT;
        hide();
    }

    @FXML
    private void handleBtnCancelAction(ActionEvent event) {
        userAction = UserAction.CANCEL;
        hide();
    }

    void hide() {
        lblDate.getScene().getWindow().hide();
    }

    void move(int delta) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            Date d = df.parse(currentDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.add(Calendar.DATE, delta);

            DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd（E）");
            currentDate = df2.format(calendar.getTime());
            lblDate.setText(currentDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
