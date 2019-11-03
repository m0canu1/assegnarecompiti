package classfiles;

import java.util.ArrayList;
import java.util.List;

public class Cook {

    private String name;

    private List<CookAvailability> availability = new ArrayList<>();

    public void addAvailability(List<CookAvailability> cs) {
        availability.addAll(cs);
    }

    public void showAvailability() {
        System.out.println("\n\n" + this.name + "\nDisponibile:");
        for (CookAvailability cs : availability) {
            System.out.println(cs.toString());
        }
    }

    public Cook(String n) {
        this.name = n;
//        availability.add(new CookAvailability("Mercoledì", "09:00", "12:00"));
    }

    public String getName() {
        return this.name;
    }

}
