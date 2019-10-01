package classfiles;

public class Event {
    
    private int id;
    private String name;
    private Menu menu_selected;
    private Summary summarySheet;

    public Event(String name) {this.name = name;}

    public String getName() {return this.name;}

    public Menu getMenu() {return this.menu_selected;}

    public Summary getSummarySheet() {return this.summarySheet;}

    public void setMenu(Menu m) {this.menu_selected = m;}
    public void setSummarySheet(Summary s) {this.summarySheet = s;}

    public boolean hasSummarySheet() {return this.summarySheet != null;}

    public void addTask(Recipe r) {summarySheet.addTask(r);}

    public boolean hasTask(Recipe r) {
        return summarySheet.hasTask(r);
    }

    public void deleteTask(Recipe r) {summarySheet.deleteTask(r);}

}
