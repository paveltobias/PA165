package cz.muni.fi.pa165;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static cz.muni.fi.pa165.DependencyInjectionTest.testDi;

public class MainJavaConfig {
    private static final String BASE_PACKAGE = "cz.muni.fi.pa165.currency";

    public static void main(String[] argv) {
        testDi(new AnnotationConfigApplicationContext(BASE_PACKAGE));
    }
}
