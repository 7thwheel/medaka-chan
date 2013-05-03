package seventhwheel.pos.model;

/* Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri May 03 08:25:02 JST 2013
 */
import java.io.Serializable;

/**
 * ItemVo.
 * @author akifumi
 * @version 1.0 
 * history 
 * Symbol	Date		Person		Note
 * [1]		2013/05/03	akifumi		Generated.
 */
public class Item implements Serializable{

	public static final String TABLE = "ITEM";

	/**
	 * id:integer(2000000000,10) <Primary Key>
	 */
	private int id;

	/**
	 * ItemCode:(2000000000,10)
	 */
	private String itemcode;

	/**
	 * Name:(2000000000,10)
	 */
	private String name;

	/**
	 * Price:(2000000000,10)
	 */
	private String price;

	/**
	* Constractor
	*/
	public Item(){}

	/**
	* Constractor
	* @param <code>id</code>
	*/
	public Item(int id){
		this.id = id;
	}

	public int getId(){ return this.id; }

	public void setId(int id){ this.id = id; }

	public String getItemcode(){ return this.itemcode; }

	public void setItemcode(String itemcode){ this.itemcode = itemcode; }

	public String getName(){ return this.name; }

	public void setName(String name){ this.name = name; }

	public String getPrice(){ return this.price; }

	public void setPrice(String price){ this.price = price; }

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ItemVo:");
		buffer.append(" id: ");
		buffer.append(id);
		buffer.append(" itemcode: ");
		buffer.append(itemcode);
		buffer.append(" name: ");
		buffer.append(name);
		buffer.append(" price: ");
		buffer.append(price);
		buffer.append("]");
		return buffer.toString();
	}

}
