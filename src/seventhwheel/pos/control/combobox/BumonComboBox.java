package seventhwheel.pos.control.combobox;

import static seventhwheel.pos.db.ConnectionPool.*;

import java.util.List;

import javafx.collections.FXCollections;
import seventhwheel.pos.model.Bumon;

public class BumonComboBox extends CodeComboBox<Bumon> {

    public BumonComboBox() {
        setupItems();
        setupHandler();
    }

    private void setupItems() {
        String sql = "SELECT * FROM Bumon ORDER BY BumonCode";
        List<Bumon> items = getPersist().readList(Bumon.class, sql);
        setItems(FXCollections.observableArrayList(items));
    }

    public int getBumonCode() {
        return getValue().getBumoncode();
    }

}
