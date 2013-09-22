package seventhwheel.pos.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import seventhwheel.pos.application.PosApplication;
import seventhwheel.pos.db.ConnectionPool;
import seventhwheel.pos.sql.Sql;

public class MainController implements Initializable {

    public BorderPane borderPane;
    public StackPane rootPane;
    public MenuItem menuItems;
    public MenuItem menuSuppliers;

    static VBox messageBar;
    static HBox messageContainer;
    static Label lblMessage;
    static MainController mainController;

    static {
        messageBar = new VBox();
        messageContainer = new HBox();
        lblMessage = new Label();

        StackPane.setAlignment(messageBar, Pos.TOP_CENTER);
        messageContainer.setAlignment(Pos.CENTER);
        messageContainer.setPadding(new Insets(5));
        messageContainer.getStyleClass().add("message-bar");

        messageBar.getChildren().add(messageContainer);
        messageContainer.getChildren().add(lblMessage);
    }

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
    private void handleMenuItems(ActionEvent event) {
        final Node pos = rootPane.getChildren().get(0);
        if (pos.getId().equals("borderPaneRegister")) {
          return;
        }

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

    @FXML
    private void handleMenuSuppliers(ActionEvent event) {
        final Node pos = rootPane.getChildren().get(0);
        if (pos.getId().equals("borderPaneRegister")) {
          return;
        }

        final Region registerItem;
        try {
            registerItem = FXMLLoader.load(PosApplication.class.getResource("Suppliers.fxml"));
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

  @FXML
  private void handleMenuReportTotal(ActionEvent event) {
    String yyyymm = new SimpleDateFormat("yyyy-MM").format(new Date());
    File file = new File(String.format("report_%s.txt", yyyymm));
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

      Connection con = ConnectionPool.getConnection();
      try (PreparedStatement ps = con.prepareStatement(Sql.get("report-total.sql"))) {
        ps.setString(1, yyyymm);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
          String date = rs.getString(1);
          String total = rs.getString(2);

          bw.write(String.format("%s\t%s\r\n", date, total));

          reportDetail(date, bw);
        }

        showMessageBar("レポートを作成しました");
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  void reportDetail(String date, BufferedWriter bw) {
    Connection con = ConnectionPool.getConnection();
    try (PreparedStatement ps = con.prepareStatement(Sql.get("report-detail.sql"))) {
      ps.setString(1, date);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        String supplierCode = rs.getString(2);
        String supplierName = rs.getString(3);
        String total = rs.getString(4);

        bw.write(String.format("%s\t%s\t%s\t%s\r\n", date, supplierCode, supplierName, total));
      }

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void showMessageBar(String message) {
      lblMessage.setText(message);
      add(messageBar);
      FadeTransition fadeIn = FadeTransitionBuilder.create()
              .node(messageBar)
              .duration(Duration.seconds(0.2))
              .fromValue(0.0)
              .toValue(1.0)
              .build();
      FadeTransition fadeOut = FadeTransitionBuilder.create()
              .node(messageBar)
              .delay(Duration.seconds(1.2))
              .duration(Duration.seconds(1.0))
              .fromValue(1.0)
              .toValue(0.0)
              .build();
      SequentialTransition fade = new SequentialTransition(fadeIn, fadeOut);
      fade.setOnFinished(new EventHandler<ActionEvent>() {

          @Override
          public void handle(ActionEvent arg0) {
              remove(messageBar);
          }
      });

      fade.play();
  }

}
