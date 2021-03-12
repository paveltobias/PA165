package cz.muni.fi.pa165.currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Currency;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConvertorImplTest {
    private static final Currency CZK = Currency.getInstance("CZK");
    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency AUD = Currency.getInstance("AUD");

    private CurrencyConvertorImpl convertor;

    @Mock
    private ExchangeRateTable table;

    @Before
    public void initConvertor() {
        convertor = new CurrencyConvertorImpl(table);
    }

    @Test
    public void testConvert() throws ExternalServiceFailureException {
        when(table.getExchangeRate(EUR, CZK)).thenReturn(new BigDecimal("26.18"));
        when(table.getExchangeRate(AUD, EUR)).thenReturn(new BigDecimal("0.65"));

        // round up
        assertThat(convertor.convert(EUR, CZK, new BigDecimal("3.14")))
                .isEqualTo(new BigDecimal("82.21"));
        // round down
        assertThat(convertor.convert(EUR, CZK, new BigDecimal("1.95")))
                .isEqualTo(new BigDecimal("51.05"));
        // zero
        assertThat(convertor.convert(EUR, CZK, new BigDecimal("0")))
                .isEqualTo(new BigDecimal("0.00"));
        // negative amount
        assertThat(convertor.convert(EUR, CZK, new BigDecimal("-11.1")))
                .isEqualTo(new BigDecimal("-290.60"));
        // different currency
        assertThat(convertor.convert(AUD, EUR, new BigDecimal("2.46")))
                .isEqualTo(new BigDecimal("1.60"));
    }

    @Test
    public void testConvertWithNullSourceCurrency() {
        assertThatThrownBy(() -> convertor.convert(null, CZK, new BigDecimal("3.14")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testConvertWithNullTargetCurrency() {
        assertThatThrownBy(() -> convertor.convert(EUR, null, new BigDecimal("3.14")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testConvertWithNullSourceAmount() {
        assertThatThrownBy(() -> convertor.convert(EUR, CZK, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testConvertWithUnknownCurrency() throws ExternalServiceFailureException {
        when(table.getExchangeRate(EUR, CZK)).thenReturn(null);

        assertThatThrownBy(() -> convertor.convert(EUR, CZK, new BigDecimal("3.14")))
                .isInstanceOf(UnknownExchangeRateException.class);
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {
        when(table.getExchangeRate(EUR, CZK)).thenThrow(ExternalServiceFailureException.class);

        assertThatThrownBy(() -> convertor.convert(EUR, CZK, new BigDecimal("3.14")))
                .isInstanceOf(UnknownExchangeRateException.class);
    }
}
