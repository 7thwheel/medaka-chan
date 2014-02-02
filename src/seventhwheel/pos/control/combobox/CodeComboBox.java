package seventhwheel.pos.control.combobox;

import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;

public class CodeComboBox<T extends IComboBoxItem> extends ComboBox<T> {

    private boolean isEntering = false;
    private StringBuffer buffer;

    public void setupHandler() {
        addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                String typed = event.getCharacter();
                if (typed.matches("[0-9]")) {
                    select(typed);
                } else {
                    return;
                }

                if (isEntering) {
                    buffer.append(typed);
                    select(buffer.toString());
                } else {
                    buffer = new StringBuffer(typed);
                    isEntering = true;

                    Task<Integer> task = new Task<Integer>() {

                        @Override
                        protected Integer call() throws Exception {
                            Thread.sleep(1000);
                            isEntering = false;
                            return null;
                        }
                    };

                    Thread th = new Thread(task);
                    th.setDaemon(true);
                    th.start();
                }
            }
        });

    }

    public void select(String code) {
        for (T item : getItems()) {
            if (item.getKey().equals(code)) {
                setValue(item);
                break;
            }
        }
    }

}
