import classfiles.Cook;
import classfiles.Event;
import classfiles.Recipe;
import classfiles.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.DataManager;

import java.sql.SQLException;
import java.util.Comparator;

class Model {
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

    ObservableList<String> getEventObservableList() {
        ObservableList<String> eventListName = FXCollections.observableArrayList();
        for (Event e : eventObservableList) {
            eventListName.add(e.getName());
        }
        return eventListName;
    }

//    public ObservableList<String> getTaskObservableList() {
////        if (!taskObservableList.isEmpty()){
////        taskObservableList.removeAll();
////        updateTaskObservableList();
//            ObservableList<String> taskListName = FXCollections.observableArrayList();
//            for (Task t : taskObservableList) {
//                taskListName.add(t.getName());
//                currentEvent.addTask(t);
//            }
//            return taskListName;
////        }
////        else return null;
//    }

    ObservableList<String> getCookObservableList() {
        ObservableList<String> cookListName = FXCollections.observableArrayList();
        for (Cook c : cookObservableList) {
            cookListName.add(c.getName());
        }
        return cookListName;
    }

    ObservableList<String> getRecipeObservableList() {
        ObservableList<String> recipeListName = FXCollections.observableArrayList();
        for (Recipe r : recipeObservableList) {
            recipeListName.add(r.getName());
        }
        return recipeListName;
    }

//    public void updateTaskObservableList() {
//        if (taskObservableList.isEmpty())
//            System.out.println("\n1. taskObservableList VUOTA");
//        else
//            System.out.println("1. taskObservableList PIENA");
//
//        taskObservableList.removeAll();
//
//        if (taskObservableList.isEmpty())
//            System.out.println("2. taskObservableList VUOTA");
//        else
//            System.out.println("2. taskObservableList PIENA");
////        taskObservableList.notify();
//        if (!currentEvent.getTaskListAsString().isEmpty())
//            taskObservableList.addAll((Task) currentEvent.getTaskListAsString());
//
//        if (taskObservableList.isEmpty())
//            System.out.println("3. taskObservableList VUOTA");
//        else
//            System.out.println("3. taskObservableList PIENA");
//
//
//    }

    Event getCurrentEvent() {
        return currentEvent;
    }

//    Task getCurrentTask() {
//        return currentTask;
//    }

    Cook getCurrentCook() {
        return currentCook;
    }

    Recipe getCurrentRecipe() {
        return currentRecipe;
    }


    void setCurrentEvent(Event event) {
        currentEvent = event;
    }


    void setCurrentCook(Cook currentCook) {
        this.currentCook = currentCook;
    }

    void setCurrentRecipe(Recipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    Event getCurrentEventByIndex(int newIndex) {
        return eventObservableList.get(newIndex);
    }

    Cook getCurrentCookByIndex(int newIndex) {
        return cookObservableList.get(newIndex);
    }

    Recipe getCurrentRecipeByIndex(int newIndex) {
        return recipeObservableList.get(newIndex);
    }

    static Model getModel() {
        return model;
    }


    private void loadLists() {
        recipeObservableList.addAll(dataManager.loadRecipes());

        /* CARICAMENTO DELLA LISTA EVENTI E DEI PROPRI TASK */
//        System.out.println("LISTA EVENTI:\n");
        for (Event e : dataManager.loadEvents()) {
            eventObservableList.add(e);
            for (Task t : dataManager.loadTasks(e)) {
                e.addTask(t);
            }
//            System.out.println(e.getName());
        }

        cookObservableList.addAll(dataManager.loadCooks());
    }

    void bindCookToTask(Cook tempCook) {
        currentEvent.getCurrentTask().setCook(tempCook);
        dataManager.bindCookToTask(tempCook, currentEvent.getCurrentTask());
    }

    void removeTask(Task task) {
        dataManager.removeTask(task);
    }

    void addTaskToEvent(Task t){
        currentEvent.addTask(t);
        dataManager.addTask(t, currentEvent);
    }

    public void putTasksInOrder() {
        System.out.println("Sorting the tasks based on start time...");
        FXCollections.sort(currentEvent.getTaskObservableList(), taskComparator);

    }

    public void bindTimeToTask(String sTime, String eTime, String estTime, String doses) {
        getCurrentEvent().getCurrentTask().setStartTime(sTime);
        getCurrentEvent().getCurrentTask().setEndTime(eTime);
        getCurrentEvent().getCurrentTask().setEstimatedTime(estTime);
        getCurrentEvent().getCurrentTask().setDoses(doses);
        dataManager.bindTimeToTask(getCurrentEvent().getCurrentTask());
    }
}
