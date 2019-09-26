public class Event.java{
    
    private int id;
    private String name;
    private Menu menu_selected;
    private Summary summary;

    public Event(String name) {this.name = name;}

    public String getName() {return this.name;}

    public Menu getMenu() {return this.menu_selected;}

    public Summary getSummary() {return this.summary;}

    public void setMenu(Menu m) {this.menu_selected = m;}
    public void setSummary(Summary s) {this.summary = s;}

    public boolean hasSummary() {return this.summary != null;}

    public void addTask(Recipe r) {summary.addTask(r);}

    public boolean hasTask(Recipe r) {summary.hasTask(r)};

    public void deleteTask(Recipe r) {summary.deleteTask(r)};

}
