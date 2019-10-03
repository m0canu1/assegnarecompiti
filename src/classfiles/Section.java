package classfiles;

import java.util.ArrayList;

public class Section {

    private ArrayList<MenuItem> items;

    Section(String name) {

        items = new ArrayList<>();
    }

    boolean addItem(Recipe recipe, String description) {

        MenuItem menuItem = new MenuItem(recipe, description);

        items.add(menuItem);

        return true;
    }

    ArrayList<MenuItem> getItems() {
        return items;
    }
}
