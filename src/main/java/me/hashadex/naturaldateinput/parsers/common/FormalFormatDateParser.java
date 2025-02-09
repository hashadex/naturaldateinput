package me.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.ParseResult;
import me.hashadex.naturaldateinput.parsers.DateParser;

public class FormalFormatDateParser extends DateParser {
    public enum DateFormat {
        DayMonth,
        MonthDay
    }

    private final DateFormat preferredFormat;

    public FormalFormatDateParser(DateFormat preferredFormat) {
        // (?<![\/\.-])\b(\d{1,2}|\d{4})[\/\.-](\d{1,2})(?:[\/\.-](\d{4}|\d{2}))?\b
        super(
            "(?<![\\/\\.-])" +                // negative lookbehind
            "\\b" +                           // word boundary
            "(\\d{1,2}|\\d{4})" +             // first block
            "[\\/\\.-]" +                     // delimeter
            "(\\d{1,2})" +                    // second block
            "(?:[\\/\\.-](\\d{4}|\\d{2}))?" + // optional delimeter + third block
            "\\b"                             // word boundary
        );

        this.preferredFormat = preferredFormat;
    }

    private LocalDate getLocalDate(int day, int month, int year) {
        return LocalDate.of(normalizeYear(year), month, 1).plusDays(day - 1);
    }

    private LocalDate resolveDate(int block1, int block2, int year) {
        if (isWithinMonthRange(block2) && !isWithinMonthRange(block1)) {
            // DD/MM
            return getLocalDate(block1, block2, year);
        } else if (isWithinMonthRange(block1) && !isWithinMonthRange(block2)) {
            // MM/DD
            return getLocalDate(block2, block1, year);
        } else {
            // Use preferred format
            if (preferredFormat == DateFormat.DayMonth) {
                // DD/MM
                return getLocalDate(block1, block2, year);
            } else {
                // MM/DD
                return getLocalDate(block2, block1, year);
            }
        }
    }

    private LocalDate resolveDate(int block1, int block2, LocalDateTime reference) {
        LocalDate result = resolveDate(block1, block2, reference.getYear());

        if (result.isBefore(reference.toLocalDate())) {
            result = result.plusYears(1);
        }

        return result;
    }

    @Override
    public ArrayList<ParseResult<LocalDate>> parse(String input, LocalDateTime reference) {
        ArrayList<ParseResult<LocalDate>> results = new ArrayList<>();
        ArrayList<MatchResult> matches = findAllMatches(input);

        for (MatchResult match : matches) {
            int block1 = Integer.parseInt(match.group(1));
            int block2 = Integer.parseInt(match.group(2));

            if (match.group(3) == null) {
                // Two blocks
                // DD/MM
                // MM/DD
                if (!isWithinDayRange(block1) || !isWithinDayRange(block2)) {
                    continue;
                }

                results.add(
                    new ParseResult<LocalDate>(reference, input, match, resolveDate(block1, block2, reference))
                );
            } else {
                // Three blocks
                int block3 = Integer.parseInt(match.group(3));

                if (isWithinYearRange(block1)) {
                    // YYYY/MM/DD
                    if (!isWithinMonthRange(block2) || !isWithinDayRange(block3)) {
                        continue;
                    }

                    results.add(
                        new ParseResult<LocalDate>(reference, input, match, resolveDate(block3, block2, block1))
                    );
                } else {
                    // DD/MM/YYYY or
                    // MM/DD/YYYY
                    if (!isWithinDayRange(block1) || !isWithinDayRange(block2)) {
                        continue;
                    }

                    results.add(
                        new ParseResult<LocalDate>(reference, input, match, resolveDate(block1, block2, block3))
                    );
                }
            }
        }

        return results;
    }
}
