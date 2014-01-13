package seventhwheel.pos.control.combobox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import seventhwheel.pos.db.ConnectionPool;
import seventhwheel.pos.model.Bumon;

public class BumonComboBox extends ComboBox<Bumon> {

  public BumonComboBox() {
    setupItems();
  }

  private void setupItems() {
    Connection con = ConnectionPool.getConnection();

    try (Statement stmt = con.createStatement()) {
      String sql = "SELECT * FROM Bumon ORDER BY BumonCode";
      ResultSet rs = stmt.executeQuery(sql);

      List<Bumon> items = Bumon.toObjects(rs);

      setItems(FXCollections.observableArrayList(items));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

}
