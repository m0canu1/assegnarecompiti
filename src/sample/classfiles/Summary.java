import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Summary {

    private ArrayList<Task> tasks;

    public Summary() {
        tasks = new ArrayList<>();
    }

    public Task addTask(Recipe recipe) {
        Task task = new Task(recipe);
        tasks.add(task);
        return task;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public boolean hasTaks(Recipe r) {
        for (Task t : tasks) {
            if (t.getRecipe() == r) {
                return true;
            }
        }
        return false;
    }

    public void deleteTask(Recipe r) {
        for (Iterator<Task> i = tasks.iterator(); i.hasNext(); ) {
            Task t = i.next();
            if (t.getRecipe() == r) {
                i.remove();
            }
        }
    }

    public void orderTask(Task t, boolean up) {
        int index = tasks.indexOf(t);
        if (up && index > 0) {
            Collections.swap(tasks, index, index - 1);
        }
        if (!up && index < tasks.size() - 1) {
            Collections.swap(tasks, index, index + 1);
        }
    }

    public void deleteAssignement(Task t) {
        t.setTurn(null);
    }
}
