package seventhwheel.pos.model;

/* Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri May 03 10:47:09 JST 2013
 */
import seventhwheel.pos.control.combobox.IComboBoxItem;

/**
 * BumonVo.
 *
 * @author akifumi
 * @version 1.0 history Symbol Date Person Note [1] 2013/05/03 akifumi
 *          Generated.
 */
public class Bumon implements IComboBoxItem {

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
    public Bumon() {
    }

    /**
     * Constractor
     *
     * @param <code>bumoncode</code>
     */
    public Bumon(int bumoncode) {
        this.bumoncode = bumoncode;
    }

    public int getBumoncode() {
        return this.bumoncode;
    }

    public void setBumoncode(int bumoncode) {
        this.bumoncode = bumoncode;
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

        return getBumoncode() == ((Bumon) obj).getBumoncode();
    }

    public String toString() {
        return String.format("%2d : %s", getBumoncode(), getName());
    }

    @Override
    public String getKey() {
        return String.valueOf(getBumoncode());
    }

}
