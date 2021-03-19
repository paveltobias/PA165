package cz.muni.fi.pa165.currency;

import javax.inject.Named;
import java.math.BigDecimal;
import java.util.Currency;

// @Named("exchangeRateTable")
public class ExchangeRateTableImpl implements ExchangeRateTable {
    @Override
    public BigDecimal getExchangeRate(Currency sourceCurrency, Currency targetCurrency) {
        if (sourceCurrency.getCurrencyCode().equals("EUR") &&
                targetCurrency.getCurrencyCode().equals("CZK")) {
            return new BigDecimal(27);
        }

        return null;
    }
}
