package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Parser {
    private final Pattern pattern;

    protected Parser(String regex, int flags) {
        pattern = Pattern.compile(regex, flags);
    }

    protected Parser(String regex) {
        this(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    protected ArrayList<MatchResult> findAllMatches(String input) {
        Matcher matcher = pattern.matcher(input);
        ArrayList<MatchResult> matchList = new ArrayList<MatchResult>();

        while (matcher.find()) {
            matchList.add(matcher.toMatchResult());
        }

        return matchList;
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
}
