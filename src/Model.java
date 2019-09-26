import classfiles.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
    private static final Model model = new Model();
    private Event currentEvent;
    private final ObservableList<Event> eventObservableList = FXCollections.observableArrayList();

    private Model() {
        currentEvent = null;

    }

    public void setCurrentEvent(Event event) {
        currentEvent = event;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public static Model getModel() {
        return model;
    }
}
