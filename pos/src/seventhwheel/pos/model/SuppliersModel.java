package seventhwheel.pos.model;

/* Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Sun Aug 18 19:41:08 JST 2013
 */

public class SuppliersModel {

    private int suppliercode;

    private String name;

    /**
     * Constractor
     */
    public SuppliersModel() {
    }

    public SuppliersModel(int suppliercode, String name) {
        this.suppliercode = suppliercode;
        this.name = name;
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

        return getSuppliercode() == ((SuppliersModel) obj).getSuppliercode() &&
                getName() == ((SuppliersModel) obj).getName();
    }

    public String toString() {
        return String.format("%2d : %s", getSuppliercode(), getName());
    }

}
