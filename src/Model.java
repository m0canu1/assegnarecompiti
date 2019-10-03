import classfiles.Cook;
import classfiles.Event;
import classfiles.Recipe;
import classfiles.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.DataManager;

import java.sql.SQLException;

public class Model {
    private static final Model model = new Model();
    private DataManager dataManager = new DataManager();
    private ObservableList<Event> eventObservableList = FXCollections.observableArrayList();
//    private ObservableList<Task> taskObservableList = FXCollections.observableArrayList();
    private ObservableList<Cook> cookObservableList = FXCollections.observableArrayList();
    private ObservableList<Recipe> recipeObservableList = FXCollections.observableArrayList();
    private Event currentEvent;
//    private Task currentTask;
    private Cook currentCook;
    private Recipe currentRecipe;

    private Model() {

        try {
            dataManager.initialize();
        } catch (SQLException exc) {
//         Rimando l'eccezione a terminale
            exc.printStackTrace();
        }

        /*Carica tutte le liste*/
        loadLists();

        //solo un test
//        for(int i = 0; i < 10; i++){
////            eventObservableList.add(new Event("gigi" + i));
////            taskObservableList.add(new Task(new Recipe("banana" + i)));
//            cookObservableList.add(new Cook("Paulino Dybala"));
//            recipeObservableList.add(new Recipe("picca"));
//        }

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

    public void setCurrentTask(Task currentTask) {
        currentTask = currentTask;
    }

    public void setCurrentEvent(Event event) {
        currentEvent = event;
//        updateTaskObservableList(); //TODO per evitare di contattare il database ad ogni cambio scelta
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

    static Model getModel() {
        return model;
    }


    private void loadLists() {
        recipeObservableList.addAll(dataManager.loadRecipes());

        /* CARICAMENTO DELLA LISTA EVENTI E DEI PROPRI TASK */
        System.out.println("LISTA EVENTI:\n");
        for (Event e : dataManager.loadEvents()) {
            eventObservableList.add(e);
            for (Task t : dataManager.loadTasks(e)) {
                e.addTask(t);
            }
            System.out.println(e.getName());
        }

        cookObservableList.addAll(dataManager.loadCooks());
    }
}
