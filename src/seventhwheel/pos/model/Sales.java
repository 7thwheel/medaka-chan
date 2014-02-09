package seventhwheel.pos.model;


public class Sales {

    public static final String TABLE = "Sales";

    /**
     * Id:(2000000000,10) <Primary Key>
     */
    private String id;

    /**
     * DateTime:(2000000000,10)
     */
    private String datetime;

    /**
     * Total:(2000000000,10)
     */
    private String total;

    /**
     * Constractor
     */
    public Sales() {
    }

    /**
     * Constractor
     *
     * @param <code>id</code>
     */
    public Sales(String id, String datetime, String total) {
        this.id = id;
        this.datetime = datetime;
        this.total = total;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTotal() {
        return this.total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Sales:");
        buffer.append(" id: ");
        buffer.append(id);
        buffer.append(" datetime: ");
        buffer.append(datetime);
        buffer.append(" total: ");
        buffer.append(total);
        buffer.append("]");
        return buffer.toString();
    }

}
