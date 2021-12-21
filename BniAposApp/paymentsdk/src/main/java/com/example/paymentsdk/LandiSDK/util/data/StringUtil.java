package com.example.paymentsdk.LandiSDK.util.data;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * String utils.
 */

public class StringUtil {

    /**
     * Platform default encoding.
     */
    private static final String PLATFORM_DEFAULT_ENCODING = Charset.defaultCharset().name();

    /**
     * Assume shift JIS.
     */
    private static final boolean ASSUME_SHIFT_JIS = ("SJIS".equalsIgnoreCase(PLATFORM_DEFAULT_ENCODING)) ||
            ("EUC_JP".equalsIgnoreCase(PLATFORM_DEFAULT_ENCODING));

    /**
     * Whether is number.
     */
    public static boolean isNumber(String string) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    /**
     * Filter digits string from a raw string.
     */
    public static String getDigits(String data) {
        StringBuilder sb = new StringBuilder();
        if (isNumber(data)) {
            return data;
        }

        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c <= '9' && c >= '0') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Format string data.
     */
    public static String formatString(String value, String formatString, boolean isLeftDirection) {
        StringBuilder result = new StringBuilder();

        if (isLeftDirection) {
            for (int i=0, n=0; i<formatString.length() && n<value.length(); i++) {
                char cur = formatString.charAt(i);

                if (cur == 'x' || cur == 'X') {
                    result.append(value.charAt(n));
                    n++;
                } else {
                    result.append(cur);
                }
            }
        } else {
            for (int i=formatString.length()-1, n=value.length()-1; i>=0 && n>=0; i--) {
                char cur = formatString.charAt(i);
                if (cur == 'x' || cur == 'X') {
                    result.insert(0, value.charAt(n));
                    n--;
                } else {
                    result.insert(0, cur);
                }
            }
        }

        return result.toString();
    }

    /**
     * Get readable amount.
     */
    public static String getReadableAmount(String amount) {
        String formattedAmount;
        if (amount.length() > 2) {
            formattedAmount = amount.substring(0, amount.length() - 2);
            formattedAmount += ".";
            formattedAmount += amount.substring(amount.length() - 2);
        } else if (amount.length() == 2) {
            formattedAmount = "0.";
            formattedAmount += amount;
        } else if (amount.length() == 1) {
            formattedAmount = "0.0";
            formattedAmount += amount;
        } else {
            formattedAmount = "0.00";
        }

        return formattedAmount;
    }
}
