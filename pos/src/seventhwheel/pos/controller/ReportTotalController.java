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

public class ReportTotalController implements Initializable {

    public Label lblDate;

    private String currentMonth;
    private UserAction userAction;

    public String getCurrentMonth() {
        return currentMonth;
    }

    public UserAction getUserAction() {
        return userAction;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        DateFormat df = new SimpleDateFormat("yyyy/MM");
        Date d = new Date();
        currentMonth = df.format(d);
        lblDate.setText(currentMonth);
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
            DateFormat df = new SimpleDateFormat("yyyy/MM");
            Date d = df.parse(currentMonth);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.add(Calendar.MONTH, delta);

            DateFormat df2 = new SimpleDateFormat("yyyy/MM");
            currentMonth = df2.format(calendar.getTime());
            lblDate.setText(currentMonth);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
