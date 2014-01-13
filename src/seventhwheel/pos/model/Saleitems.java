package seventhwheel.pos.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Saleitems {

    public static final String TABLE = "SaleItems";

    /**
     * Id:integer(2000000000,10) <Primary Key>
     */
    private int id;

    /**
     * SaleId:(2000000000,10)
     */
    private String saleid;

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
     * Quantity:integer(2000000000,10)
     */
    private int quantity;

    /**
     * SupplierCode:integer(2000000000,10)
     */
    private int suppliercode;

    /**
     * BumonCode:integer(2000000000,10)
     */
    private int bumoncode;

    /**
     * Constractor
     */
    public Saleitems() {
    }

    /**
     * Constractor
     *
     * @param <code>id</code>
     */
    public Saleitems(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSaleid() {
        return this.saleid;
    }

    public void setSaleid(String saleid) {
        this.saleid = saleid;
    }

    public String getItemcode() {
        return this.itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSuppliercode() {
        return this.suppliercode;
    }

    public void setSuppliercode(int suppliercode) {
        this.suppliercode = suppliercode;
    }

    public int getBumoncode() {
        return this.bumoncode;
    }

    public void setBumoncode(int bumoncode) {
        this.bumoncode = bumoncode;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SaleitemsVo:");
        buffer.append(" id: ");
        buffer.append(id);
        buffer.append(" saleid: ");
        buffer.append(saleid);
        buffer.append(" itemcode: ");
        buffer.append(itemcode);
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append(" price: ");
        buffer.append(price);
        buffer.append(" quantity: ");
        buffer.append(quantity);
        buffer.append(" suppliercode: ");
        buffer.append(suppliercode);
        buffer.append(" bumoncode: ");
        buffer.append(bumoncode);
        buffer.append("]");
        return buffer.toString();
    }

    public void insert(Connection con) throws SQLException {
        PreparedStatement ps =
                con.prepareStatement(
                        "insert into SaleItems (SaleId, ItemCode, Name, Price, Quantity, SupplierCode, BumonCode) " +
                        "values (?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, getSaleid());
        ps.setString(2, getItemcode());
        ps.setString(3, getName());
        ps.setString(4, getPrice());
        ps.setInt(5, getQuantity());
        ps.setInt(6, getSuppliercode());
        ps.setInt(7, getBumoncode());
        ps.execute();
    }

}
