 

// Interface
interface Shape {
    void draw();
}

// Class implementing the interface
class Circle implements Shape {
    public void draw() {
        System.out.println("Drawing a circle");
    }
}

// Main class
public class InterfaceExample {
    public static void main(String[] args) {
        // Create an object of the class implementing the interface
        Circle circle = new Circle();

        // Call the interface method
        circle.draw();
    }
}
