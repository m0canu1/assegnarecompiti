package classfiles;

import java.util.ArrayList;
import java.util.List;

public class Cook {

    private String name;

    private List<CookAvailability> availability = new ArrayList<>();

    public void addAvailability(List<CookAvailability> cs) {
        availability.addAll(cs);
    }

    public String showAvailability() {
        String ret = "\nAvailability:";
//        System.out.println("\n\n" + this.name + "\nAvailability:");
        for (CookAvailability cs : availability) {
            ret = ret + "\n" + cs.toString();
//            System.out.println(cs.toString());
        }
        return ret;
    }

    public boolean isCookAvailable(String start, String end) {
        boolean available = false;
        for (CookAvailability ca : availability) {
//            System.out.println(start + "e" + ca.getStartTime());
//            System.out.println(start.compareTo(ca.getStartTime()));
//            System.out.println(end + "e" + ca.getEndTime());
//            System.out.println(end.compareTo(ca.getEndTime()));
            if (!available && start.compareTo(ca.getStartTime()) >= 0 && end.compareTo(ca.getEndTime()) <= 0) {
                available = true;
            }
        }
        return available;
    }

    public Cook(String n) {
        this.name = n;
//        availability.add(new CookAvailability("Mercoledì", "09:00", "12:00"));
    }

    public String getName() {
        return this.name;
    }

}
