package cz.muni.fi.pa165;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import static cz.muni.fi.pa165.DependencyInjectionTest.testDi;

public class MainXml {
    private static final String XML_PATH = "application-context.xml";

    public static void main(String[] argv) {
        testDi(new ClassPathXmlApplicationContext(XML_PATH));
    }
}
