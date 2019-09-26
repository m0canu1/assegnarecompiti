import java.util.ArrayList;

public class Section {

    private String name;

    private ArrayList<MenuItem> items;

    public Section(String name) {
        this.name = name;

        items = new ArrayList<>();
    }

    public boolean addItem(Recipe recipe, String description) {

        MenuItem menuItem = new MenuItem(recipe, description);

        items.add(menuItem);

        return true;
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }
}
