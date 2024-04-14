 

// Parent class
class Animal {
    public void sound() {
        System.out.println("Animal makes a sound");
    }
}

// Child class inheriting from the parent class
class Dog extends Animal {
    public void sound() {
        System.out.println("Dog barks");
    }
}

// Main class
public class InheritanceExample {
    public static void main(String[] args) {
        // Create an object of the child class
        Dog dog = new Dog();

        // Call the overridden method
        dog.sound();
    }
}
