package cz.muni.fi.pa165;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import cz.muni.fi.pa165.currency.CurrencyConvertor;

import java.math.BigDecimal;
import java.util.Currency;

public class MainXml {
    private static final String XML_PATH = "application-context.xml";
    private static final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(XML_PATH);

    public static void main(String[] argv) {
        testDi();
    }

    private static void testDi() {
        CurrencyConvertor convertor = (CurrencyConvertor)context.getBean("currencyConvertor");
        Currency eur = Currency.getInstance("EUR");
        Currency czk = Currency.getInstance("CZK");

        // valid conversion
        BigDecimal amount = convertor.convert(eur, czk, new BigDecimal("1"));
        System.out.println("27.00 == " + amount);

        // CZK->EUR rate not defined
        try {
            convertor.convert(czk, eur, new BigDecimal("1"));
        } catch (Exception e) {
            System.out.println("Cannot convert CZK->EUR");
        }
    }
}
