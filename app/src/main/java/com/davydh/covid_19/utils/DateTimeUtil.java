package com.davydh.covid_19.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Util class to convert the date and time associated with an article in different formats.
 */
public class DateTimeUtil {

    private static final String ARTICLE_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String DATA_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private DateTimeUtil() {}

    /**
     * It converts the date and time based on the user settings.
     *
     * @param dateTime The date and time to be converted.
     * @return The date and time converted based the user settings.
     */
    public static String getArticleDate(String dateTime) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ARTICLE_DATE_TIME_PATTERN, Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm", Locale.getDefault());
        
        Date parsedDate = null;

        try {
            parsedDate = simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (parsedDate != null) {
            return outputDateFormat.format(parsedDate);
        }

        return null;
    }

    public static String getDataDate(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATA_DATE_TIME_PATTERN, Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm", Locale.getDefault());

        Date parsedDate = null;

        try {
            parsedDate = simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (parsedDate != null) {
            return outputDateFormat.format(parsedDate);
        }

        return null;
    }
}
