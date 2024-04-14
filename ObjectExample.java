 

// Main class
public class ObjectExample {
    public static void main(String[] args) {
        // Create objects
        Object obj1 = new Object();
        Object obj2 = new Object();

        // Check object equality
        boolean areEqual = obj1.equals(obj2);
        System.out.println("Objects are equal: " + areEqual);

        // Get object's string representation
        String objString = obj1.toString();
        System.out.println("Object string: " + objString);
    }
}
