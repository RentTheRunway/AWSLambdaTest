package Utilities;


import com.google.common.base.Preconditions;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyUtil {
    private MoneyUtil() {
    }

    public static Money ZERO = valueAsUSD(0.00);

    public static Money valueAsUSD(double value) {
        return Money.of(CurrencyUnit.USD, value, RoundingMode.HALF_EVEN);
    }

    public static Money usd(double value) {
        return Money.of(CurrencyUnit.USD, value, RoundingMode.HALF_EVEN);
    }

    public static Money valueAsUSD(BigDecimal value) {
        return Money.of(CurrencyUnit.USD, value, RoundingMode.HALF_EVEN);
    }

    public static Money valueAsUSD(String value) {
        return Money.of(CurrencyUnit.USD, new BigDecimal(value), RoundingMode.HALF_EVEN);
    }

    public static Money multiplyUSD(Money money, double scalar) {
        return money.multipliedBy(scalar, RoundingMode.HALF_EVEN);
    }

    public static Money max(Money left, Money right) {
        Preconditions.checkArgument(left.isSameCurrency(right), "Currencies must be the same");
        return left.isGreaterThan(right) ? left : right;
    }

    public static BigDecimal getFraction(Money numerator, Money denominator) {
        if (denominator.isZero()) {
            throw new IllegalArgumentException(
                    "Attempted to divide " + numerator.getAmount() + " by " + denominator.getAmount());
        }
        if (!numerator.getCurrencyUnit().equals(denominator.getCurrencyUnit())) {
            throw new IllegalArgumentException("Attempted to divide different currency units");
        }
        return numerator.getAmount().divide(denominator.getAmount(), 10, RoundingMode.HALF_EVEN);
    }

    public static Money negateValue(Money money) {
        return multiplyUSD(money, -1.00);
    }

    public static Money zero() {
        return Money.zero(CurrencyUnit.USD);
    }
}
