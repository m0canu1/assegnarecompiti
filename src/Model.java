import classfiles.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.DataManager;

import java.sql.SQLException;

public class Model {
    private static final Model model = new Model();
    private DataManager dataManager = new DataManager();
    //private Summary currentSummary;
    private final ObservableList<Event> eventObservableList = FXCollections.observableArrayList();
    private final ObservableList<Task> taskObservableList = FXCollections.observableArrayList();
    private final ObservableList<Cook> cookObservableList = FXCollections.observableArrayList();
    private Event currentEvent;
    private Task currentTask;
    private Cook currentCook;


    private Model() {
        try {
            dataManager.initialize();
        } catch (SQLException exc) {
//         Rimando l'eccezione a terminale
            exc.printStackTrace();
        }

        //caricamento lista eventi
        for (Event e: dataManager.loadEvents()){
            eventObservableList.add(e);
//            dataManager.loadTasks(e);
        }

        //caricamento lista task
//        for (Task t : dataManager.loadTasks()) {
//            taskObservableList.add(t);
//        }

        //solo un test
        for(int i = 0; i < 10; i++){
//            eventObservableList.add(new Event("gigi" + i));
            taskObservableList.add(new Task(new Recipe("banana")));
            cookObservableList.add(new Cook("Paulino Dybala"));
        }

//        currentEvent = getCurrentEventByIndex(0);
//        currentTask = getCurrentTaskByIndex(0);
//        currentSummary = null;
    }

    public ObservableList<String> getEventObservableList() {
        ObservableList<String> eventListName=FXCollections.observableArrayList();
        for (Event e: eventObservableList) {
            eventListName.add(e.getName());
        }
        return eventListName;
    }

    public ObservableList<String> getTaskObservableList() {
        ObservableList<String> taskListName=FXCollections.observableArrayList();
        for (Task t: taskObservableList) {
            taskListName.add(t.getName());
        }
        return taskListName;
    }

    public ObservableList<String> getCookObservableList() {
        ObservableList<String> cookListName=FXCollections.observableArrayList();
        for (Cook c: cookObservableList) {
            cookListName.add(c.getName());
        }
        return cookListName;
    }

    Event getCurrentEvent() {
        return currentEvent;
    }

    Task getCurrentTask() {
        return currentTask;
    }

    Cook getCurrentCook() {
        return currentCook;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public void setCurrentCook(Cook currentCook) {
        this.currentCook = currentCook;
    }

    public Event getCurrentEventByIndex(int newIndex) {
        return eventObservableList.get(newIndex);
    }

    public Task getCurrentTaskByIndex(int newIndex) {
        return taskObservableList.get(newIndex);
    }

    public Cook getCurrentCookByIndex(int newIndex) {
        return cookObservableList.get(newIndex);
    }

    static Model getModel() {
        return model;
    }

    public Summary getCurrentSummary() {
        return currentEvent.getSummarySheet();
    }

}
