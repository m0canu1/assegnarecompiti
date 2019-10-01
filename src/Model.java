import classfiles.Event;
import classfiles.Summary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.DataManager;

import java.sql.SQLException;
import java.util.ArrayList;

public class Model {
    private static final Model model = new Model();
    private Event currentEvent;
    private DataManager dataManager = new DataManager();

    //    private Summary currentSummary;
    private final ObservableList<Event> eventObservableList = FXCollections.observableArrayList();

    public ObservableList<String> getEventObservableList() {
        ObservableList<String> eventListName=FXCollections.observableArrayList();
        for (Event e: eventObservableList) {
            eventListName.add(e.getName());
        }
        return eventListName;
    }

    private Model() {
        try {
            dataManager.initialize();
        } catch (SQLException exc) {
            // Rimando l'eccezione a terminale
            exc.printStackTrace();
        }
//        dataManager.loadEvents();

//        for (Event e: dataManager.loadEvents()){
//            eventObservableList.add(e);
//        }


        // solo un test
        for(int i = 0; i < 10; i++){
            eventObservableList.add(new Event("gigi" + i));
        }
        currentEvent = getCurrentEventByIndex(0);
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
        return currentEvent.getSummarySheet();
    }

    public Event getCurrentEventByIndex(int i) {
        return eventObservableList.get(i);
    }

//    public void setCurrentSummary(Summary currentSummary) {
//        this.currentSummary = currentSummary;
//    }


}
