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
    private String preparedDoses;

    public Task(Recipe recipe) {
        this.recipe = recipe;
    }

    public Task(Recipe recipe, Cook cook, String startTime, String endTime, String estimatedTime, int doses, String alreadyPreparedDoses) {
        this.recipe = recipe;
        if (cook != null) this.cook = cook;
        if (startTime != null) this.startTime = startTime;
        if (endTime != null) this.endTime = endTime;
        if (estimatedTime != null) this.estimatedTime = estimatedTime;
        if (alreadyPreparedDoses != null) this.preparedDoses = alreadyPreparedDoses;
                else this.preparedDoses = "0";
        this.doses = String.valueOf(doses);
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

    public String getPreparedDoses() {
        return preparedDoses;
    }

    public void setPreparedDoses(String preparedDoses) {
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
