package io.github.hashadex.naturaldateinput.parsers.ru;

import static java.time.temporal.ChronoUnit.DECADES;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

import static java.util.Map.entry;

import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Class containing various Russian language constants.
 * 
 * @author hashadex
 * @since 2.1.0
 */
final class RUConstants {
    /**
     * Map of Russian names of timeunits and their respective
     * {@link java.time.temporal.ChronoUnit ChronoUnits}.
     * 
     * @since 2.1.0
     */
    public static final Map<String, ChronoUnit> chronoUnitMap = Map.ofEntries(
        entry("десятилетий", DECADES),
        entry("десятилетие", DECADES),
        entry("десятилетия", DECADES),

        entry("лет", YEARS),
        entry("год", YEARS),
        entry("года", YEARS),

        entry("месяцев", MONTHS),
        entry("месяц", MONTHS),
        entry("месяца", MONTHS),

        entry("недель", WEEKS),
        entry("неделя", WEEKS),
        entry("недели", WEEKS),

        entry("дней", DAYS),
        entry("день", DAYS),
        entry("дня", DAYS),

        entry("часов", HOURS),
        entry("час", HOURS),
        entry("часа", HOURS),

        entry("минут", MINUTES),
        entry("минута", MINUTES),
        entry("минуты", MINUTES),

        entry("секунд", SECONDS),
        entry("секунда", SECONDS),
        entry("секунды", SECONDS)
    );

    /**
     * Map of Russian cardinal numbers and their respective meanings as an
     * integer.
     * 
     * @since 2.1.0
     */
    public static final Map<String, Integer> cardinalNumberMap = Map.ofEntries(
        entry("ноль", 0),

        entry("одно", 1),
        entry("один", 1),
        entry("одна", 1),

        entry("два", 2),
        entry("две", 2),

        entry("три", 3),
        entry("четыре", 4),
        entry("пять", 5),
        entry("шесть", 6),
        entry("семь", 7),
        entry("восемь", 8),
        entry("девять", 9),
        entry("десять", 10),
        entry("одиннадцать", 11),
        entry("двенадцать", 12),
        entry("тринадцать", 13),
        entry("четырнадцать", 14),
        entry("пятнадцать", 15),
        entry("шестнадцать", 16),
        entry("семнадцать", 17),
        entry("восемнадцать", 18),
        entry("девятнадцать", 19),
        entry("двадцать", 20)
    );
}
