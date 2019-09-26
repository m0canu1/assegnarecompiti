public class User {

    private String name, surname;
    private boolean isChef;

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public boolean isChef() {
        return isChef;
    }

    public void setChef(boolean b) {
        isChef = b;
    }

    public boolean hasEvent(Event event) {
        return true; //TODO
    }
}
