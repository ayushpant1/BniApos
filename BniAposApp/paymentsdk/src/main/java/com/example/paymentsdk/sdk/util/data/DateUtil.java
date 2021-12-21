package com.example.paymentsdk.sdk.util.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date utils.
 */

public class DateUtil {

    /**
     * Get date.
     */
    public static String getDate(Date date, String expectedFormat) {
        SimpleDateFormat format = new SimpleDateFormat(expectedFormat);
        return format.format(date);
    }

    /**
     * Get current time.
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());
        return df.format(curDate);
    }
}
