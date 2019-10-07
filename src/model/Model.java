package model;

import classfiles.Cook;
import classfiles.Event;
import classfiles.Recipe;
import classfiles.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.DataManager;

import java.sql.SQLException;
import java.util.Comparator;

public class Model {
    private static final Model model = new Model();
    private DataManager dataManager = new DataManager();
    private ObservableList<Event> eventObservableList = FXCollections.observableArrayList();
    private ObservableList<Cook> cookObservableList = FXCollections.observableArrayList();
    private ObservableList<Recipe> recipeObservableList = FXCollections.observableArrayList();
    private Comparator<Task> taskComparator = Comparator.comparingInt(Task::getStartTimeInt);
    private Event currentEvent;
    private Cook currentCook;
    private Recipe currentRecipe;

    private Model() {
        try {
            dataManager.initialize();
            loadLists();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        /*Carica tutte le liste*/
    }

    public ObservableList<String> getEventObservableList() {
        ObservableList<String> eventListName = FXCollections.observableArrayList();
        for (Event e : eventObservableList) {
            eventListName.add(e.getName());
        }
        return eventListName;
    }

    public ObservableList<String> getCookObservableList() {
        ObservableList<String> cookListName = FXCollections.observableArrayList();
        for (Cook c : cookObservableList) {
            cookListName.add(c.getName());
        }
        return cookListName;
    }

    public ObservableList<String> getRecipeObservableList() {
        ObservableList<String> recipeListName = FXCollections.observableArrayList();
        for (Recipe r : recipeObservableList) {
            recipeListName.add(r.getName());
        }
        return recipeListName;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }


    public Cook getCurrentCook() {
        return currentCook;
    }

    public Recipe getCurrentRecipe() {
        return currentRecipe;
    }


    public void setCurrentEvent(Event event) {
        currentEvent = event;
    }


    public void setCurrentCook(Cook currentCook) {
        this.currentCook = currentCook;
    }

    public void setCurrentRecipe(Recipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    public Event getCurrentEventByIndex(int newIndex) {
        return eventObservableList.get(newIndex);
    }

    public Cook getCurrentCookByIndex(int newIndex) {
        return cookObservableList.get(newIndex);
    }

    public Recipe getCurrentRecipeByIndex(int newIndex) {
        return recipeObservableList.get(newIndex);
    }

    public static Model getModel() {
        return model;
    }


    private void loadLists() {
        recipeObservableList.addAll(dataManager.loadRecipes());

        /* CARICAMENTO DELLA LISTA EVENTI E DEI PROPRI TASK */
        for (Event e : dataManager.loadEvents()) {
            eventObservableList.add(e);
            for (Task t : dataManager.loadTasks(e)) {
                e.addTask(t);
            }
        }

        cookObservableList.addAll(dataManager.loadCooks());
    }

    public void bindCookToTask(Cook tempCook) {
        currentEvent.getCurrentTask().setCook(tempCook);
        dataManager.bindCookToTask(tempCook, currentEvent.getCurrentTask());
    }

    public void removeTask(Task task) {
        dataManager.removeTask(task);
    }

    public void addTaskToEvent(Task t){
        currentEvent.addTask(t);
        dataManager.addTask(t, currentEvent);
    }

    public void putTasksInOrder() {
        System.out.println("Sorting the tasks based on start time...");
        FXCollections.sort(currentEvent.getTaskObservableList(), taskComparator);

    }

    public void bindTimeToTask(String sTime, String eTime, String estTime, String doses, String preparedDoses) {
        getCurrentEvent().getCurrentTask().setStartTime(sTime);
        getCurrentEvent().getCurrentTask().setEndTime(eTime);
        getCurrentEvent().getCurrentTask().setEstimatedTime(estTime);
        getCurrentEvent().getCurrentTask().setDoses(doses);
        getCurrentEvent().getCurrentTask().setPreparedDoses(preparedDoses);
        dataManager.bindTimeToTask(getCurrentEvent().getCurrentTask());
    }
}
