package seventhwheel.pos.control;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class NumberTextField extends TextField {

    public NumberTextField() {
        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (!event.getCharacter().matches("[0-9]")) {
                    event.consume();
                }
            }
        });
    }
}
