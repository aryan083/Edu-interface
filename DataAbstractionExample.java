 

// Abstract class
abstract class Shape {
    // Abstract method
    public abstract void draw();
}

// Concrete class extending the abstract class
class Circle extends Shape {
    public void draw() {
        System.out.println("Drawing a circle");
    }
}

// Main class
public class DataAbstractionExample {
    public static void main(String[] args) {
        // Create an object of the concrete class
        Circle circle = new Circle();

        // Call the abstract method
        circle.draw();
    }
}
