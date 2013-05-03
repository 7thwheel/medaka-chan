package seventhwheel.pos.model;

/* Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri May 03 10:47:09 JST 2013
 */
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * BumonVo.
 * @author akifumi
 * @version 1.0
 * history
 * Symbol	Date		Person		Note
 * [1]		2013/05/03	akifumi		Generated.
 */
public class Bumon implements Serializable{

	public static final String TABLE = "BUMON";

	/**
	 * BumonCode:integer(2000000000,10) <Primary Key>
	 */
	private int bumoncode;

	/**
	 * Name:text(2000000000,10)
	 */
	private String name;

	/**
	* Constractor
	*/
	public Bumon(){}

	/**
	* Constractor
	* @param <code>bumoncode</code>
	*/
	public Bumon(int bumoncode){
		this.bumoncode = bumoncode;
	}

	public int getBumoncode(){ return this.bumoncode; }

	public void setBumoncode(int bumoncode){ this.bumoncode = bumoncode; }

	public String getName(){ return this.name; }

	public void setName(String name){ this.name = name; }

	public String toString(){
		return String.format("%2d : %s", getBumoncode(), getName());
	}

  public static List<Bumon> toObjects(ResultSet resultSet) {
    List<Bumon> list = new ArrayList<>();
    try {
      while (resultSet.next()) {
        int code = resultSet.getInt("BumonCode");
        String name = resultSet.getString("Name");
        Bumon data = new Bumon();
        data.setBumoncode(code);
        data.setName(name);

        list.add(data);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return list;
  }

}
