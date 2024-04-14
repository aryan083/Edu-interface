 

// Importing the required classes
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Main class
public class FileHandlingExample {
    public static void main(String[] args) {
        try {
            // Create a new file
            File file = new File("example.txt");

            // Write content to the file
            FileWriter writer = new FileWriter(file);
            writer.write("Hello, World!");
            writer.close();

            System.out.println("File created and written successfully.");
        } catch (IOException e) {
            // Handling the exception
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
