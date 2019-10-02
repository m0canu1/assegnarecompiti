import classfiles.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.DataManager;

import java.sql.SQLException;

public class Model {
    private static final Model model = new Model();
    private DataManager dataManager = new DataManager();
    //private Summary currentSummary;
    private ObservableList<Event> eventObservableList = FXCollections.observableArrayList();
    private ObservableList<Task> taskObservableList = FXCollections.observableArrayList();
    private ObservableList<Cook> cookObservableList = FXCollections.observableArrayList();
    private ObservableList<Recipe> recipeObservableList = FXCollections.observableArrayList();
    private Event currentEvent;
    private Task currentTask;
    private Cook currentCook;
    private Recipe currentRecipe;


    private Model() {
 /*       try {
            dataManager.initialize();
        } catch (SQLException exc) {
//         Rimando l'eccezione a terminale
            exc.printStackTrace();
        }

        //caricamento lista eventi
        for (Event e: dataManager.loadEvents()){
            eventObservableList.add(e);
            dataManager.loadTasks(e);
        }
*/
        //caricamento lista task
//        for (Task t : dataManager.loadTasks()) {
//            taskObservableList.add(t);
//        }

        //solo un test
        for(int i = 0; i < 10; i++){
            eventObservableList.add(new Event("gigi" + i));
            taskObservableList.add(new Task(new Recipe("banana" + i)));
            cookObservableList.add(new Cook("Paulino Dybala" + i));
            recipeObservableList.add(new Recipe("picca" + i));
        }

        currentEvent = getCurrentEventByIndex(0);
        currentTask = getCurrentTaskByIndex(0);
        currentRecipe = getCurrentRecipeByIndex(0);
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

    public ObservableList<String> getRecipeObservableList() {
        ObservableList<String> recipeListName=FXCollections.observableArrayList();
        for (Recipe r: recipeObservableList) {
            recipeListName.add(r.getName());
        }
        return recipeListName;
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

    Recipe getCurrentRecipe() { return currentRecipe; }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public void setCurrentCook(Cook currentCook) {
        this.currentCook = currentCook;
    }

    public void setCurrentRecipe(Recipe currentRecipe ) { this.currentRecipe = currentRecipe; }

    public Event getCurrentEventByIndex(int newIndex) {
        return eventObservableList.get(newIndex);
    }

    public Task getCurrentTaskByIndex(int newIndex) {
        return taskObservableList.get(newIndex);
    }

    public Cook getCurrentCookByIndex(int newIndex) {
        return cookObservableList.get(newIndex);
    }

    public Recipe getCurrentRecipeByIndex(int newIndex) { return recipeObservableList.get(newIndex); }

    static Model getModel() {
        return model;
    }

    public Summary getCurrentSummary() {
        return currentEvent.getSummarySheet();
    }

    public void removeTaskFromView(Task t){
        taskObservableList.remove(t);
        setCurrentTask(null);

    }

    public void addNewTask(Task t){
        taskObservableList.add(t);
        setCurrentTask(t);
    }

}
