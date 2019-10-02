package classfiles;

import java.util.ArrayList;
import java.util.List;

public class Event {
    
    private int id;
    private String name;
    private Menu menu_selected;
    private List<Task> taskList = new ArrayList<>();

    public Event(String name) {this.name = name;}

    public String getName() {return this.name;}

    public Menu getMenu() {return this.menu_selected;}

    public void setMenu(Menu m) {this.menu_selected = m;}

    public void addTask(Task task) {this.taskList.add(task);}

    public boolean hasTask(Task task) {
        return this.taskList.contains(task);
    }

    public void deleteTask(Task task) {this.taskList.remove(task);}

    public List<Task> getTaskList() {
        return taskList;
    }
}
