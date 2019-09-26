public class MenuManager {

    private User currentUser;
    private Menu currentMenu;

    public MenuManager() {

    }

    public boolean createMenu(String title) throws Exception {

        if (currentUser == null || !currentUser.isChef()) {
            throw new Exception("not chef");//or return null?
        }

        currentMenu = new Menu(currentUser, title);

        return true;
    }

    public boolean defineSection(String name) {
        if (currentMenu == null) {
            return false; //or exception?
        }

        currentMenu.addSection(name);

        return true;
    }

    public boolean insertItem(Recipe recipe, Section section, String description) {
        return currentMenu.addItem(recipe, section, description);
    }

    public boolean publish() {

        if (currentMenu == null) {
            return false;
        }

        currentMenu.setPublished(true);

        return true;
    }
}
