package me.hashadex.naturaldateinput.constants;

import java.time.Month;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

public class ENConstants {
    public static final Map<String, Month> monthMap;
    public static final Map<String, Integer> relativeWordOffsetMap;
    public static final Set<String> ordinalIndicators;

    static {
        monthMap = Map.ofEntries(
            // January
            entry("january", Month.JANUARY),
            entry("jan\\.", Month.JANUARY),
            entry("jan", Month.JANUARY),
            // February
            entry("february", Month.FEBRUARY),
            entry("feb\\.", Month.FEBRUARY),
            entry("feb", Month.FEBRUARY),
            // March
            entry("march", Month.MARCH),
            entry("mar\\.", Month.MARCH),
            entry("mar", Month.MARCH),
            // April
            entry("april", Month.APRIL),
            entry("apr\\.", Month.APRIL),
            entry("apr", Month.APRIL),
            // May
            entry("may", Month.MAY),
            entry("may\\.", Month.MAY),
            entry("may", Month.MAY),
            // June
            entry("june", Month.JUNE),
            entry("jun\\.", Month.JUNE),
            entry("jun", Month.JUNE),
            // July
            entry("july", Month.JULY),
            entry("jul\\.", Month.JULY),
            entry("jul", Month.JULY),
            // August
            entry("august", Month.AUGUST),
            entry("aug\\.", Month.AUGUST),
            entry("aug", Month.AUGUST),
            // September
            entry("september", Month.SEPTEMBER),
            entry("sep\\.", Month.SEPTEMBER),
            entry("sep", Month.SEPTEMBER),
            // October
            entry("october", Month.OCTOBER),
            entry("oct\\.", Month.OCTOBER),
            entry("oct", Month.OCTOBER),
            // November
            entry("november", Month.NOVEMBER),
            entry("nov\\.", Month.NOVEMBER),
            entry("nov", Month.NOVEMBER),
            // December
            entry("december", Month.DECEMBER),
            entry("dec\\.", Month.DECEMBER),
            entry("dec", Month.DECEMBER)
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

        ordinalIndicators = Set.of("th", "st", "nd", "rd");
    }
}
