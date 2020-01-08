package com.openpayd.exchange.util;

import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;

public interface Constants {
    interface Math {
        int SCALE = 10;
        RoundingMode ROUNDING_MODE = RoundingMode.HALF_DOWN;
    }

    interface JWT {
        byte[] SECRET = "aedfe5b6eca8852f15b70a749c90jkhb569f1dsaf33820ab57502bca77a36fffd7".getBytes(StandardCharsets.UTF_8);
    }
}
