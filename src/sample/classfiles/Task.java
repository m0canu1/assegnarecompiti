public class Task {

    private Recipe recipe;
    private boolean assigned;
    private Cook cook; //TODO maybe more cooks?
    private Turn turn;
    private int estimatedTime;
    private int doses;

    public Task(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public boolean isAssigned() {
        return turn != null;
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
}
