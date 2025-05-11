package me.hashadex.naturaldateinput.constants;

import static java.time.Month.JANUARY;
import static java.time.Month.FEBRUARY;
import static java.time.Month.MARCH;
import static java.time.Month.APRIL;
import static java.time.Month.MAY;
import static java.time.Month.JUNE;
import static java.time.Month.JULY;
import static java.time.Month.AUGUST;
import static java.time.Month.SEPTEMBER;
import static java.time.Month.OCTOBER;
import static java.time.Month.NOVEMBER;
import static java.time.Month.DECEMBER;

import static java.time.temporal.ChronoUnit.DECADES;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HALF_DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

import static java.util.Map.entry;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

public class ENConstants {
    public static final Map<String, Month> monthMap;
    public static final Map<String, Integer> relativeWordOffsetMap;
    public static final Map<String, ChronoUnit> chronoUnitMap;
    public static final Map<String, DayOfWeek> weekdayMap;
    public static final Set<String> ordinalIndicators;

    static {
        monthMap = Map.ofEntries(
            // January
            entry("january", JANUARY),
            entry("jan.", JANUARY),
            entry("jan", JANUARY),
            // February
            entry("february", FEBRUARY),
            entry("feb.", FEBRUARY),
            entry("feb", FEBRUARY),
            // March
            entry("march", MARCH),
            entry("mar.", MARCH),
            entry("mar", MARCH),
            // April
            entry("april", APRIL),
            entry("apr.", APRIL),
            entry("apr", APRIL),
            // May
            entry("may", MAY),
            entry("may.", MAY),
            // June
            entry("june", JUNE),
            entry("jun.", JUNE),
            entry("jun", JUNE),
            // July
            entry("july", JULY),
            entry("jul.", JULY),
            entry("jul", JULY),
            // August
            entry("august", AUGUST),
            entry("aug.", AUGUST),
            entry("aug", AUGUST),
            // September
            entry("september", SEPTEMBER),
            entry("sep.", SEPTEMBER),
            entry("sep", SEPTEMBER),
            // October
            entry("october", OCTOBER),
            entry("oct.", OCTOBER),
            entry("oct", OCTOBER),
            // November
            entry("november", NOVEMBER),
            entry("nov.", NOVEMBER),
            entry("nov", NOVEMBER),
            // December
            entry("december", DECEMBER),
            entry("dec.", DECEMBER),
            entry("dec", DECEMBER)
        );

        relativeWordOffsetMap = Map.of(
            // yesterday
            "yesterday", -1,
            "ytd", -1,
            // today
            "today", 0,
            "tod", 0,
            // tomorrow
            "tomorrow", 1,
            "tmrw", 1,
            "tmr", 1
        );

        chronoUnitMap = Map.of(
            "decade", DECADES,
            "year", YEARS,
            "month", MONTHS,
            "week", WEEKS,
            "day", DAYS,
            "half-day", HALF_DAYS,
            "halfday", HALF_DAYS,
            "hour", HOURS,
            "minute", MINUTES,
            "seconds", SECONDS
        );

        weekdayMap = Map.ofEntries(
            // Monday
            entry("monday", MONDAY),
            entry("mon", MONDAY),
            // Tuesday
            entry("tuesday", TUESDAY),
            entry("tue", TUESDAY),
            // Wednesday
            entry("wednesday", WEDNESDAY),
            entry("wed", WEDNESDAY),
            // Thursday
            entry("thursday", THURSDAY),
            entry("thu", THURSDAY),
            // Friday
            entry("friday", FRIDAY),
            entry("fri", FRIDAY),
            // Saturday
            entry("saturday", SATURDAY),
            entry("sat", SATURDAY),
            // Sunday
            entry("sunday", SUNDAY)
            // "sun" is not in the map to avoid confusion with the Sun (the star)
        );

        ordinalIndicators = Set.of("th", "st", "nd", "rd");
    }
}
