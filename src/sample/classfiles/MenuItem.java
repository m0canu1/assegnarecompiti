public class MenuItem {

    private String description;

    private Recipe recipe;

    public MenuItem(Recipe recipe, String description) {

        this.recipe = recipe;

        if (description == null) {
            this.description = recipe.getName();
        } else {
            this.description = description;
        }
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
