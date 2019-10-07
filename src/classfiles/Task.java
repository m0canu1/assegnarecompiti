package classfiles;

import java.util.Objects;

public class Task {

    private Recipe recipe;
    private Cook cook;
    private String startTime;
    private String startTimeForInt;
    private int startTimeInt;
    private String endTime;
    private String estimatedTime;
    private int doses;
    private int preparedDoses;

    public Task(Recipe recipe) {
        this.recipe = recipe;
    }

    public Task(Recipe recipe, Cook cook, String startTime, String endTime, String estimatedTime, int doses, int alreadyPreparedDoses) {
        this.recipe = recipe;
        if (cook != null) this.cook = cook;
        if (startTime != null) this.startTime = startTime;
        if (endTime != null) this.endTime = endTime;
        if (estimatedTime != null) this.estimatedTime = estimatedTime;
        this.preparedDoses = alreadyPreparedDoses;
        this.doses = doses;
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

    public int getPreparedDoses() {
        return preparedDoses;
    }

    public void setPreparedDoses(int preparedDoses) {
        this.preparedDoses = preparedDoses;
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
        return "Task {" +
                "recipe= " + recipe.getName() +
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
