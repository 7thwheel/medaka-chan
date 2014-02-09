package seventhwheel.pos.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import net.sf.persist.Persist;

public class ConnectionPool {

  private static Connection connection;

  /**
   * returns pooled connection.
   * @return
   */
  public static Connection getConnection() {
    loadJdbcDriver();

    String datasource = getDataSource();

    if (connection == null) {
      try {
        connection = DriverManager.getConnection(datasource);
        System.out.println(datasource);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }

    return connection;
  }

  public static Persist getPersist() {
      return new Persist(getConnection());
  }

  /**
   * closes connection.
   */
  public static void close() {
    if (connection == null) {
      return;
    }

    try {
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * loads JDBC driver for sqlite.
   */
  static void loadJdbcDriver() {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * gets Data Source from properties file.
   * @return
   */
  static String getDataSource() {
    Properties props = new Properties();
    try {
      props.load(new InputStreamReader(new FileInputStream("pos.properties"), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("jarファイルと同じディレクトリに pos.properties を配置してください。");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return props.getProperty("datasource");
  }

}
