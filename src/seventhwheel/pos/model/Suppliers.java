package seventhwheel.pos.model;

/* Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Sun Aug 18 19:41:08 JST 2013
 */
import seventhwheel.pos.control.combobox.IComboBoxItem;

public class Suppliers implements IComboBoxItem {

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
    public Suppliers() {
    }

    /**
     * Constractor
     *
     * @param <code>suppliercode</code>
     */
    public Suppliers(int suppliercode) {
        this.suppliercode = suppliercode;
    }

    public int getSuppliercode() {
        return this.suppliercode;
    }

    public void setSuppliercode(int suppliercode) {
        this.suppliercode = suppliercode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        return getSuppliercode() == ((Suppliers) obj).getSuppliercode();
    }

    public String toString() {
        return String.format("%2d : %s", getSuppliercode(), getName());
    }

    @Override
    public String getKey() {
        return String.valueOf(getSuppliercode());
    }

}
