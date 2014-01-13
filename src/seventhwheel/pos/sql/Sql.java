package seventhwheel.pos.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Sql {

  public static String get(String sqlfilename) {
    File file = new File(sqlfilename);
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(file));

      String line = "";
      String sql = "";
      while (line != null) {
        line = br.readLine();
        sql += line + "\r\n";
      }

      return sql;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        br.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
