package seventhwheel.pos.model;

public class ItemSaleModel {

    private String itemName;

    private String price;

    private String quantity;

    private String amount;

    public ItemSaleModel(String itemName, String price, String quantity, String amount) {
      setItemName(itemName);
      setPrice(price);
      setQuantity(quantity);
      setAmount(amount);
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
