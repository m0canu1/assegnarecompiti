import java.util.ArrayList;

public class Menu {

    private String title;
    private boolean published, inUse, fingerFood, requiresCook, hotDishes, requiresKitchen;

    private User owner;

    private ArrayList<Section> sections;
    private ArrayList<MenuItem> itemsWithoutSection;

    public Menu(User owner, String title) {
        if (title != null) {
            //TODO check if title exists maybe
            this.title = title;
        }
        this.owner = owner;

        sections = new ArrayList<Section>();
        itemsWithoutSection = new ArrayList<>();
    }

    public Section addSection(String name) {
        Section newSection = new Section(name);
        sections.add(newSection);
        return newSection;
    }

    public boolean hasSection(Section sect) {
        return false;
    }

    public boolean addItem(Recipe recipe, Section section, String description) {

        MenuItem menuItem = new MenuItem(recipe, description);

        if (section == null) {
            itemsWithoutSection.add(menuItem);
        } else {
            section.addItem(recipe, description);
        }

        return true;
    }

    public boolean setPublished(boolean p) {
        published = p;
        return true;
    }

    public ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();

        for (int i = 0; i < itemsWithoutSection.size(); i++) {
            recipes.add(itemsWithoutSection.get(i).getRecipe());
        }

        for (int i = 0; i < sections.size(); i++) {
            ArrayList<MenuItem> items = sections.get(i).getItems();
            for (int j = 0; j < items.size(); j++) {
                recipes.add(items.get(j).getRecipe());
            }
        }

        return recipes;
    }

}
