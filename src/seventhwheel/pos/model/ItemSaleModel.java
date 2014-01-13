package seventhwheel.pos.model;

public class ItemSaleModel {

    private String itemName;

    private String price;

    private String quantity;

    private String amount;

    private Item item;

    public ItemSaleModel(String itemName, String price, String quantity, String amount, Item item) {
        setItemName(itemName);
        setPrice(price);
        setQuantity(quantity);
        setAmount(amount);
        setItem(item);
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
