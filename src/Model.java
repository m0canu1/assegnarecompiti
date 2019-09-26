import classfiles.Event;
import classfiles.Summary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
    private static final Model model = new Model();
    private Event currentEvent;

    //    private Summary currentSummary;
    private final ObservableList<Event> eventObservableList = FXCollections.observableArrayList();

    public ObservableList<Event> getEventObservableList() {
        return eventObservableList;
    }

    private Model() {
        currentEvent = null;
//        currentSummary = null;
    }

    Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    static Model getModel() {
        return model;
    }

    public Summary getCurrentSummary() {
        return currentEvent.getSummary();
    }

    public Event getCurrentEventByIndex(int i) {
        return eventObservableList.get(i);
    }

//    public void setCurrentSummary(Summary currentSummary) {
//        this.currentSummary = currentSummary;
//    }


}
