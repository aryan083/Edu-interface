 

// Main class
public class ExceptionExample {
    public static void main(String[] args) {
        try {
            // Code that may throw an exception
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            // Handling the exception
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
