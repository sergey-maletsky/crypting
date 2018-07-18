package com.snm.security.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

public class ConversionUtil {

    @Nullable
    public static Long parseLong(@Nullable String value) {
        return parseLong(value, null);
    }

    @Nullable
    public static Long parseLong(@Nullable String value, @Nullable Long defaultVal) {
        try {
            if (value != null && !value.isEmpty()) {
                return new Long(value);
            }
        } catch (NumberFormatException e) {
            return defaultVal;
        }
        return defaultVal;
    }

    @NotNull
    public static String hexStringToDecimalString(@NotNull String hexStr) {
        return new BigInteger(hexStr, 16).toString();
    }
}
