package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Set;

import me.hashadex.naturaldateinput.ParseResult;

public abstract class DateParser extends Parser {
    public DateParser(String regex, int flags) {
        super(regex, flags);
    }

    public DateParser(String regex) {
        super(regex);
    }

    protected static boolean isWithinYearRange(int year) {
        return year >= 1900 && year <= 2200; // This is the range supported by Todoist
    }

    protected static boolean isWithinMonthRange(int month) {
        return month >= 1 && month <= 12;
    }

    protected static boolean isWithinDayRange(int day) {
        return day >= 1 && day <= 31;
    }

    protected static int normalizeYear(int year) {
        year = Math.abs(year);

        if (year < 100) {
            return 2000 + year;
        } else {
            return year;
        }
    }

    protected static LocalDate safeGetLocalDate(int year, int month, int day) {
        return LocalDate.of(normalizeYear(year), month, 1).plusDays(day - 1);
    }

    protected static LocalDate safeGetLocalDate(int year, Month month, int day) {
        return safeGetLocalDate(year, month.getValue(), day);
    }

    protected static String keySetToRegexAlternate(Set<String> set) {
        return String.join("|", set);
    }

    public abstract ArrayList<ParseResult<LocalDate>> parse(String input, LocalDateTime reference);
}
