package seventhwheel.pos.control.combobox;

import static seventhwheel.pos.db.ConnectionPool.*;

import java.util.List;

import javafx.collections.FXCollections;
import seventhwheel.pos.model.Suppliers;

public class SuppliersComboBox extends CodeComboBox<Suppliers> {

    public SuppliersComboBox() {
        setupItems();
        setupHandler();
    }

    private void setupItems() {
        String sql = "SELECT * FROM Suppliers ORDER BY SupplierCode";
        List<Suppliers> items = getPersist().readList(Suppliers.class, sql);
        setItems(FXCollections.observableArrayList(items));
    }

    public int getSupplierCode() {
        return getValue().getSuppliercode();
    }

}
