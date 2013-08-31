package seventhwheel.pos.model;

public class ItemSaleModel {

    private String itemCode;

    private String itemName;

    private String price;

    private String quantity;

    private String amount;

    public ItemSaleModel(String itemCode, String itemName, String price, String quantity, String amount) {
        setItemCode(itemCode);
        setItemName(itemName);
        setPrice(price);
        setQuantity(quantity);
        setAmount(amount);
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
