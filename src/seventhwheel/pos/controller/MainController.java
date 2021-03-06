package seventhwheel.pos.controller;

import static seventhwheel.pos.db.ConnectionPool.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import seventhwheel.pos.application.PosApplication;
import seventhwheel.pos.constants.UserAction;
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
        lblMessage.getStyleClass().add("message");

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
        FXMLLoader fxmlLoader = new FXMLLoader(PosApplication.class.getResource("ReportTotal.fxml"));
        final Region reportDetail;
        try {
            reportDetail = (Region) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(reportDetail);
        Stage stage = StageBuilder.create()
                .scene(scene)
                .build();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(rootPane.getScene().getWindow());
        stage.initStyle(StageStyle.UNDECORATED);

        stage.showAndWait();

        ReportTotalController controller = fxmlLoader.getController();
        if (controller.getUserAction() == UserAction.SUBMIT) {
            String yyyymm = controller.getCurrentMonth().replaceAll("/", "-");
            File file = new File(String.format("report_%s.txt", yyyymm));
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                List<Map<String, Object>> maps = getPersist().readMapList(Sql.get("report-total.sql"), yyyymm);
                for (Map<String, Object> map : maps) {
                    String date = map.get("datetime").toString();
                    String total = map.get("total").toString();
                    bw.write(String.format("%s\t%s\r\n", date, total));
                    reportDetail(date, bw);
                }
                showMessageBar("レポートを作成しました");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void reportDetail(String date, BufferedWriter bw) {
        List<Map<String, Object>> maps = getPersist().readMapList(Sql.get("report-detail.sql"), date);
        for (Map<String, Object> map : maps) {
            String supplierCode = map.get("suppliercode").toString();
            String supplierName = map.get("name").toString();
            String amount = map.get("amount").toString();

            try {
                bw.write(String.format("%s\t%s\t%s\t%s\r\n", date, supplierCode, supplierName, amount));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void handleMenuReportDetail(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(PosApplication.class.getResource("ReportDetail.fxml"));
        final Region reportDetail;
        try {
            reportDetail = (Region) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(reportDetail);
        Stage stage = StageBuilder.create()
                .scene(scene)
                .build();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(rootPane.getScene().getWindow());
        stage.initStyle(StageStyle.UNDECORATED);

        stage.showAndWait();

        ReportDetailController controller = fxmlLoader.getController();
        if (controller.getUserAction() == UserAction.SUBMIT) {
            String date = controller.getCurrentDate().substring(0, 10).replaceAll("/", "-");
            File file = new File(String.format("report_%s.txt", date));
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\r\n",
                        "日時", "連番", "連番コード", "バーコード", "商品名", "単価", "数量", "仕入先", "部門", "金額"));

                try (PreparedStatement ps = getConnection().prepareStatement(Sql.get("report-items.sql"))) {
                    ps.setString(1, date);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        bw.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\r\n",
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getString(6),
                                rs.getString(7),
                                rs.getString(8),
                                rs.getString(9),
                                rs.getString(10)));
                    }

                    showMessageBar("レポートを作成しました");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

    @FXML
    private void handleMenuReportItems(ActionEvent event) {
        File file = new File("商品情報一覧.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(String.format("%s\t%s\t%s\t%s\t%s\r\n",
                    "バーコード", "商品名", "単価", "仕入先", "部門"));

            try (PreparedStatement ps = getConnection().prepareStatement(Sql.get("report-item-list.sql"))) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    bw.write(String.format("%s\t%s\t%s\t%s\t%s\r\n",
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5)));
                }

                showMessageBar("レポートを作成しました");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
