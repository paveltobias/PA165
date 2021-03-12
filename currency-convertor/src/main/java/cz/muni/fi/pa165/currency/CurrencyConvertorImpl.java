package cz.muni.fi.pa165.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private final ExchangeRateTable exchangeRateTable;
    private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount) {
        logger.trace("convert({}, {}, {})", sourceCurrency, targetCurrency, sourceAmount);
        if (sourceCurrency == null || targetCurrency == null || sourceAmount == null) {
            throw new IllegalArgumentException("Source/target currencies and source amount must not be null");
        }
        try {
            BigDecimal rate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);
            if (rate == null) {
                logger.warn("{}->{} exchange rate not known", sourceCurrency, targetCurrency);
                throw new UnknownExchangeRateException(
                        String.format(
                                "%s->%s exchange rate not known",
                                sourceCurrency.getCurrencyCode(),
                                targetCurrency.getCurrencyCode()
                        )
                );
            }
            return sourceAmount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
        } catch (ExternalServiceFailureException e) {
            logger.error("Error looking up {}->{} exchange rate", sourceCurrency, targetCurrency);
            throw new UnknownExchangeRateException(
                    String.format(
                            "Error looking up %s->%s exchange rate",
                            sourceCurrency.getCurrencyCode(),
                            targetCurrency.getCurrencyCode()
                    )
            );
        }
    }

}
