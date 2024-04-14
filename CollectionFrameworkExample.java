 

// Importing the required collection classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Example usage of collection framework
public class CollectionFrameworkExample {
    public static void main(String[] args) {
        // List example
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        System.out.println("Names: " + names);

        // Map example
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 80);
        scores.put("Bob", 90);
        scores.put("Charlie", 75);
        System.out.println("Scores: " + scores);
    }
}
