 
import java.lang.annotation.*;


// Using the custom annotation
public class AnnotationExample {
    @MyAnnotation("Custom Annotation")
    public void myMethod() {
        // Method implementation
    }

    public static void main(String[] args) throws NoSuchMethodException {
        // Get the annotation value using reflection
        Annotation[] annotations = AnnotationExample.class.getMethod("myMethod").getAnnotations();
        MyAnnotation myAnnotation = (MyAnnotation) annotations[0];
        System.out.println(myAnnotation.value());
    }
}
