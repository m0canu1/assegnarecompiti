package classfiles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Event {
    
    private int id;
    private String name;
    private Menu menu_selected;
    private ObservableList<Task> taskObservableList = FXCollections.observableArrayList();
    private Task currentTask;

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public Event(String name) {this.name = name;}

    public String getName() {return this.name;}

    public Menu getMenu() {return this.menu_selected;}

    public void setMenu(Menu m) {this.menu_selected = m;}

    public void addTask(Task task) {this.taskObservableList.add(task);}

    public boolean hasTask(Task task) {
        return this.taskObservableList.contains(task);
    }

    public Task getCurrentTaskByIndex(int newIndex) {
        return taskObservableList.get(newIndex);
    }

    public void deleteTask(Task task) {
        System.out.println("Provo ad eliminare il task");
        this.taskObservableList.remove(task);
    }

    public ObservableList<String> getTaskListAsString() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        for (Task e : taskObservableList) {
            observableList.add(e.getName());
        }

        return observableList;
    }
}
