package com.myorg.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for resolving date tokens and formatting dates
 */
public class DateTimeUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateTimeUtils.class);
    
    // Date token patterns
    private static final Pattern NEXT_WEEKDAY_PATTERN = Pattern.compile("<NEXT_(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)>");
    private static final Pattern PLUS_DAYS_PATTERN = Pattern.compile("<PLUS_(\\d+)_DAYS?>");
    private static final Pattern PLUS_WEEKS_PATTERN = Pattern.compile("<PLUS_(\\d+)_WEEKS?>");
    private static final Pattern PLUS_MONTHS_PATTERN = Pattern.compile("<PLUS_(\\d+)_MONTHS?>");
    private static final Pattern PLUS_YEARS_PATTERN = Pattern.compile("<PLUS_(\\d+)_YEARS?>");
    private static final Pattern MINUS_DAYS_PATTERN = Pattern.compile("<MINUS_(\\d+)_DAYS?>");
    private static final Pattern MINUS_WEEKS_PATTERN = Pattern.compile("<MINUS_(\\d+)_WEEKS?>");
    private static final Pattern MINUS_MONTHS_PATTERN = Pattern.compile("<MINUS_(\\d+)_MONTHS?>");
    
    // Common date formats
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String US_DATE_FORMAT = "MM/dd/yyyy";
    public static final String EU_DATE_FORMAT = "dd/MM/yyyy";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    // Private constructor to prevent instantiation
    private DateTimeUtils() {}

    /**
     * Resolves date tokens in text using default date format
     * 
     * @param text Text containing date tokens
     * @return Text with resolved dates
     */
    public static String resolve(String text) {
        return resolve(text, DEFAULT_DATE_FORMAT);
    }

    /**
     * Resolves date tokens in text using specified format
     * 
     * @param text Text containing date tokens
     * @param dateFormat Date format pattern
     * @return Text with resolved dates
     */
    public static String resolve(String text, String dateFormat) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        logger.debug("Resolving date tokens in text: {}", text);
        String resolvedText = text;

        // Resolve current date/time tokens
        resolvedText = resolvedText.replace("<TODAY>", formatDate(LocalDate.now(), dateFormat));
        resolvedText = resolvedText.replace("<NOW>", formatDateTime(LocalDateTime.now(), DEFAULT_DATETIME_FORMAT));
        resolvedText = resolvedText.replace("<YESTERDAY>", formatDate(LocalDate.now().minusDays(1), dateFormat));
        resolvedText = resolvedText.replace("<TOMORROW>", formatDate(LocalDate.now().plusDays(1), dateFormat));

        // Resolve next weekday tokens
        resolvedText = resolveNextWeekdayTokens(resolvedText, dateFormat);

        // Resolve plus/minus tokens
        resolvedText = resolvePlusMinusTokens(resolvedText, dateFormat);

        logger.debug("Resolved text: {}", resolvedText);
        return resolvedText;
    }

    /**
     * Resolves next weekday tokens (e.g., <NEXT_FRIDAY>)
     */
    private static String resolveNextWeekdayTokens(String text, String dateFormat) {
        Matcher matcher = NEXT_WEEKDAY_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String weekdayName = matcher.group(1);
            DayOfWeek targetDayOfWeek = DayOfWeek.valueOf(weekdayName);
            LocalDate nextWeekday = LocalDate.now().with(TemporalAdjusters.next(targetDayOfWeek));
            String formattedDate = formatDate(nextWeekday, dateFormat);
            matcher.appendReplacement(result, formattedDate);
            logger.debug("Resolved <NEXT_{}> to {}", weekdayName, formattedDate);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * Resolves plus/minus tokens (e.g., <PLUS_3_DAYS>, <MINUS_2_WEEKS>)
     */
    private static String resolvePlusMinusTokens(String text, String dateFormat) {
        String result = text;

        // Plus days
        result = resolvePattern(result, PLUS_DAYS_PATTERN, dateFormat, 
                (amount) -> LocalDate.now().plusDays(amount), "PLUS_DAYS");

        // Plus weeks
        result = resolvePattern(result, PLUS_WEEKS_PATTERN, dateFormat, 
                (amount) -> LocalDate.now().plusWeeks(amount), "PLUS_WEEKS");

        // Plus months
        result = resolvePattern(result, PLUS_MONTHS_PATTERN, dateFormat, 
                (amount) -> LocalDate.now().plusMonths(amount), "PLUS_MONTHS");

        // Plus years
        result = resolvePattern(result, PLUS_YEARS_PATTERN, dateFormat, 
                (amount) -> LocalDate.now().plusYears(amount), "PLUS_YEARS");

        // Minus days
        result = resolvePattern(result, MINUS_DAYS_PATTERN, dateFormat, 
                (amount) -> LocalDate.now().minusDays(amount), "MINUS_DAYS");

        // Minus weeks
        result = resolvePattern(result, MINUS_WEEKS_PATTERN, dateFormat, 
                (amount) -> LocalDate.now().minusWeeks(amount), "MINUS_WEEKS");

        // Minus months
        result = resolvePattern(result, MINUS_MONTHS_PATTERN, dateFormat, 
                (amount) -> LocalDate.now().minusMonths(amount), "MINUS_MONTHS");

        return result;
    }

    /**
     * Helper method to resolve patterns with date calculations
     */
    private static String resolvePattern(String text, Pattern pattern, String dateFormat, 
                                       DateCalculator calculator, String tokenType) {
        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            long amount = Long.parseLong(matcher.group(1));
            LocalDate calculatedDate = calculator.calculate(amount);
            String formattedDate = formatDate(calculatedDate, dateFormat);
            matcher.appendReplacement(result, formattedDate);
            logger.debug("Resolved <{}_{}> to {}", tokenType, amount, formattedDate);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * Formats a LocalDate using the specified format
     */
    public static String formatDate(LocalDate date, String format) {
        if (date == null) {
            return "";
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return date.format(formatter);
        } catch (Exception e) {
            logger.error("Error formatting date {} with format {}", date, format, e);
            return date.toString();
        }
    }

    /**
     * Formats a LocalDateTime using the specified format
     */
    public static String formatDateTime(LocalDateTime dateTime, String format) {
        if (dateTime == null) {
            return "";
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return dateTime.format(formatter);
        } catch (Exception e) {
            logger.error("Error formatting datetime {} with format {}", dateTime, format, e);
            return dateTime.toString();
        }
    }

    /**
     * Gets the current date in default format
     */
    public static String getCurrentDate() {
        return formatDate(LocalDate.now(), DEFAULT_DATE_FORMAT);
    }

    /**
     * Gets the current date in specified format
     */
    public static String getCurrentDate(String format) {
        return formatDate(LocalDate.now(), format);
    }

    /**
     * Gets the current datetime in default format
     */
    public static String getCurrentDateTime() {
        return formatDateTime(LocalDateTime.now(), DEFAULT_DATETIME_FORMAT);
    }

    /**
     * Gets the current datetime in specified format
     */
    public static String getCurrentDateTime(String format) {
        return formatDateTime(LocalDateTime.now(), format);
    }

    /**
     * Calculates days between two dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Checks if a date is in the future
     */
    public static boolean isFuture(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    /**
     * Checks if a date is in the past
     */
    public static boolean isPast(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    /**
     * Checks if a date is today
     */
    public static boolean isToday(LocalDate date) {
        return date.equals(LocalDate.now());
    }

    /**
     * Functional interface for date calculations
     */
    @FunctionalInterface
    private interface DateCalculator {
        LocalDate calculate(long amount);
    }
}