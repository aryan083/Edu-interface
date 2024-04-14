 

// Importing the annotation package
import java.lang.annotation.*;

// Declaring a custom annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyAnnotation {
    String value();
}
