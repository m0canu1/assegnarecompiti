package classfiles;

import java.util.Objects;

public class Task {

    private Recipe recipe;
    private Cook cook; //TODO maybe more cooks?
    private String startTime;
    private String startTimeForInt;
    private int startTimeInt;
    private String endTime;
    private String estimatedTime;
    private String doses;

    public Task(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getStartTimeInt() {
        if (startTime != null) {
            startTimeForInt = startTime.replace(":", "");
            startTimeInt = Integer.parseInt(startTimeForInt);
            return startTimeInt;
        } else {
            return 0;
        }
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Task(Recipe recipe, Cook cook) {
        this.recipe = recipe;
        this.cook = cook;
        this.estimatedTime = "0";

    }

    public Task(Recipe recipe, String startTime, String endTime) {
        this.recipe = recipe;
        this.startTime = startTime;
        this.endTime = endTime;
        this.estimatedTime = "0";

    }

    public Task(Recipe recipe, Cook cook, String startTime, String endTime) {
        this.recipe = recipe;
        this.cook = cook;
        this.startTime = startTime;
        this.endTime = endTime;
        this.estimatedTime = "0";
    }

    public Recipe getRecipe() {
        return recipe;
    }


    /**
     * verifica se un evento ha cuochi assegnati
     * @return vero se ha cuochi assegnati
     */
    private boolean isAssigned() {
        return this.cook != null;
    }

    public Cook getCook() {
        return cook;
    }

    public void setCook(Cook cook) {
        this.cook = cook;

    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String time) {
        this.estimatedTime = time;
    }

    public String getDoses() {
        return doses;
    }

    public void setDoses(String doses) {
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

}
