package com.openpayd.exchange.util;

import java.math.RoundingMode;

public interface Constants {
    interface Math {
        int SCALE = 10;
        RoundingMode ROUNDING_MODE = RoundingMode.HALF_DOWN;
    }
}
