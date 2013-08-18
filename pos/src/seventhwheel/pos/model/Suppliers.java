package seventhwheel.pos.model;

/* Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Sun Aug 18 19:41:08 JST 2013
 */
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * SuppliersVo.
 * @author akifumi
 * @version 1.0
 * history
 * Symbol	Date		Person		Note
 * [1]		2013/08/18	akifumi		Generated.
 */
public class Suppliers implements Serializable{

	public static final String TABLE = "SUPPLIERS";

	/**
	 * SupplierCode:integer(2000000000,10) <Primary Key>
	 */
	private int suppliercode;

	/**
	 * Name:(2000000000,10)
	 */
	private String name;

	/**
	* Constractor
	*/
	public Suppliers(){}

	/**
	* Constractor
	* @param <code>suppliercode</code>
	*/
	public Suppliers(int suppliercode){
		this.suppliercode = suppliercode;
	}

	public int getSuppliercode(){ return this.suppliercode; }

	public void setSuppliercode(int suppliercode){ this.suppliercode = suppliercode; }

	public String getName(){ return this.name; }

	public void setName(String name){ this.name = name; }

	@Override
    public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }

        return getSuppliercode() == ((Suppliers) obj).getSuppliercode();
    }

    public String toString(){
        return String.format("%2d : %s", getSuppliercode(), getName());
	}


    public static List<Suppliers> toObjects(ResultSet resultSet) {
        List<Suppliers> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int code = resultSet.getInt("SupplierCode");
                String name = resultSet.getString("Name");
                Suppliers data = new Suppliers();
                data.setSuppliercode(code);
                data.setName(name);

                list.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

}
