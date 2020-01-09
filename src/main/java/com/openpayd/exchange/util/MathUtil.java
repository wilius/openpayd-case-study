package com.openpayd.exchange.util;

import com.openpayd.exchange.exception.InvalidRequestException;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public class MathUtil {
    private MathUtil() {
    }

    public static BigDecimal scale(BigDecimal decimal) {
        Assert.notNull(decimal, "decimal cannot be null");
        return decimal.setScale(Constants.Math.SCALE, Constants.Math.ROUNDING_MODE);
    }

    public static BigDecimal divide(BigDecimal divider, BigDecimal dividend) {
        Assert.notNull(divider, "divider cannot be null");
        Assert.notNull(dividend, "dividend cannot be null");
        Assert.isTrue(dividend.signum() != 0, "dividend cannot be zero");
        return divider.divide(dividend, Constants.Math.SCALE, Constants.Math.ROUNDING_MODE);
    }

    public static void validatePositive(int number, String message) {
        if (number < 1) {
            throw new InvalidRequestException(message);
        }
    }
}
