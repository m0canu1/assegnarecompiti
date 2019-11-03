package classfiles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Event {
    
    private int id;
    private String name;
    private ObservableList<Task> taskObservableList = FXCollections.observableArrayList();
    private Task currentTask;

    public ObservableList<Task> getTaskObservableList() {
        return taskObservableList;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public Event(String name) {this.name = name;}

    public String getName() {return this.name;}

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

    public void deleteAllTasks() {
        this.taskObservableList.clear();
        System.out.println("Rimossi tutti i task!");
    }

    public ObservableList<String> getTaskListAsString() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        if(taskObservableList != null) {
            for (Task e : taskObservableList) {
                if (e.getCook() != null) {
                    observableList.add(e.getStartTime() + "-" + e.getEndTime() + "\t" +
                            e.getName() + "; Assigned to: " + e.getCook().getName());
                } else {
                    if (e.getStartTime() != null) {
                        observableList.add(e.getStartTime() + "-" + e.getEndTime() + "\t" +
                                e.getName() + "; Not assigned.");
                    } else {
                        observableList.add("-----NA-----" + "\t" +
                                e.getName() + "; Not assigned.");
                    }
                }
            }
        } else observableList.removeAll();
        return observableList;
    }
}
