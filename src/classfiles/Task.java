package classfiles;

import java.util.Objects;

public class Task {

    private Recipe recipe;
    private boolean assigned;
    private Cook cook; //TODO maybe more cooks?
    private String startTime;
    private String endTime;
    private int estimatedTime;
    private int doses;

    public Task(Recipe recipe) {
        this.recipe = recipe;
    }

    public Task(Recipe recipe, Cook cook) {
        this.recipe = recipe;
        this.cook = cook;
        this.assigned = true;
    }

    public Task(Recipe recipe, String startTime, String endTime) {
        this.recipe = recipe;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Task(Recipe recipe, Cook cook, String startTime, String endTime) {
        this.recipe = recipe;
        this.cook = cook;
        this.startTime = startTime;
        this.endTime = endTime;
        this.assigned = true;
    }

    public Recipe getRecipe() {
        return recipe;
    }


    public boolean isAssigned() {
        return this.assigned;
    }

    public Cook getCook() {
        return cook;
    }

    public void setCook(Cook cook) {
        this.cook = cook;

    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int time) {
        this.estimatedTime = time;
    }

    public int getDoses() {
        return doses;
    }

    public void setDoses(int doses) {
        this.doses = doses;
    }

    public String getName() {
        return this.recipe.getName();
    }

    @Override
    public String toString() {
        return "Task{" +
                "recipe=" + recipe.getName() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return isAssigned() == task.isAssigned() &&
                getRecipe().equals(task.getRecipe()) &&
                Objects.equals(getCook(), task.getCook());
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(getRecipe(), isAssigned());
//    }
}
