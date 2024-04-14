 

public class EnumExample {
    public static void main(String[] args) {
        // Access enum values
        Day today = Day.MONDAY;
        System.out.println("Today is: " + today);

        // Iterate over enum values
        System.out.println("All days of the week:");
        for (Day day : Day.values()) {
            System.out.println(day);
        }
    }
}
