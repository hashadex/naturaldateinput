package me.hashadex.naturaldateinput.parsers.en;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.ParseResult;
import me.hashadex.naturaldateinput.parsers.DateParser;

public class ENMonthDateParser extends DateParser {
    private static HashMap<String, Month> months = new HashMap<>();

    static {
        // January
        months.put("january", Month.JANUARY);
        months.put("jan", Month.JANUARY);
        // February
        months.put("february", Month.FEBRUARY);
        months.put("feb", Month.FEBRUARY);
        // March
        months.put("march", Month.MARCH);
        months.put("mar", Month.MARCH);
        // April
        months.put("april", Month.APRIL);
        months.put("apr", Month.APRIL);
        // May
        months.put("may", Month.MAY);
        // June
        months.put("june", Month.JUNE);
        months.put("jun", Month.JUNE);
        // July
        months.put("july", Month.JULY);
        months.put("jul", Month.JULY);
        // August
        months.put("august", Month.AUGUST);
        months.put("aug", Month.AUGUST);
        // September
        months.put("september", Month.SEPTEMBER);
        months.put("sep", Month.SEPTEMBER);
        // October
        months.put("october", Month.OCTOBER);
        months.put("oct", Month.OCTOBER);
        // November
        months.put("november", Month.NOVEMBER);
        months.put("nov", Month.NOVEMBER);
        // December
        months.put("december", Month.DECEMBER);
        months.put("dec", Month.DECEMBER);
    }

    public ENMonthDateParser() {
        super(
            "(?:\\b(\\d{1,2}|\\d{4}) )?" +                                // optional ambiguous group 1
            "\\b" +                                                       // word boundary
            "(?<month>" + keySetToRegexAlternate(months.keySet()) + ")" + // month block
            "\\b" +                                                       // word boundary
            "(?:" +
            " (\\d{4}|\\d{1,2})(?:th|st|nd|rd)?" +                        // optional ambiguous group 3 + optional ordinal indicator
            "\\b" +                                                       // word boundary
            "(?:,? (?<year>\\d{4})\\b)?" +                                // optional year group
            ")?"
        );
    }

    private LocalDate resolveDate(Month month, int block1, int block2) throws IllegalArgumentException {
        int day;
        int year;

        if (isWithinDayRange(block1) && isWithinYearRange(block2)) {
            day = block1;
            year = block2;
        } else if (isWithinDayRange(block2) && isWithinYearRange(block1)) {
            day = block2;
            year = block1;
        } else {
            throw new IllegalArgumentException();
        }

        return safeGetLocalDate(year, month, day);
    }

    private LocalDate resolveDate(Month month, int block, LocalDateTime reference) throws IllegalArgumentException {
        LocalDate result;

        if (isWithinDayRange(block)) {
            // unknown year
            result = resolveDate(month, block, reference.getYear());

            if (result.isBefore(reference.toLocalDate())) {
                result = result.plusYears(1);
            }
        } else if (isWithinYearRange(block)) {
            // unknown day
            result = resolveDate(month, block, 1);
        } else {
            throw new IllegalArgumentException();
        }

        return result;
    }

    @Override
    public ArrayList<ParseResult<LocalDate>> parse(String input, LocalDateTime reference) {
        ArrayList<ParseResult<LocalDate>> results = new ArrayList<>();
        ArrayList<MatchResult> matches = findAllMatches(input);

        for (MatchResult match : matches) {
            Month month = months.get(match.group("month").toLowerCase());

            LocalDate result;

            if (match.group("year") != null) {
                // If both group 1 and group "year" are not null, ignore group 1.

                // If group "year" is not null, (and the regex pattern is correct!)
                // then group 3 is also not null.

                try {
                    result = resolveDate(
                        month,
                        Integer.parseInt(match.group("year")),
                        Integer.parseInt(match.group(3))
                    );
                } catch (IllegalArgumentException e) {
                    continue;
                }
            } else if (match.group(1) != null) {
                if (match.group(3) != null) {
                    try {
                        result = resolveDate(
                            month,
                            Integer.parseInt(match.group(1)),
                            Integer.parseInt(match.group(3))
                        );
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                } else {
                    try {
                        result = resolveDate(
                            month,
                            Integer.parseInt(match.group(1)),
                            reference
                        );
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                }
            } else if (match.group(3) != null) {
                try {
                    result = resolveDate(
                        month,
                        Integer.parseInt(match.group(3)),
                        reference
                    );
                } catch (IllegalArgumentException e) {
                    continue;
                }
            } else {
                result = resolveDate(month, 1, reference);
            }

            results.add(
                new ParseResult<LocalDate>(reference, input, match, result)
            );
        }

        return results;
    }
}
