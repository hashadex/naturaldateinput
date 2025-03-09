package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import me.hashadex.naturaldateinput.ParsedComponent;

public abstract class Parser {
    protected final Pattern pattern;

    protected Parser(String regex, int flags) {
        pattern = Pattern.compile(regex, flags);
    }

    protected Parser(String regex) {
        this(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
    }

    protected static boolean is4DigitNumber(int number) {
        return number >= 1000 && number <= 9999;
    }

    protected static boolean isWithinMonthRange(int month) {
        return month >= 1 && month <= 12;
    }

    protected static boolean isWithinDayRange(int day) {
        return day >= 1 && day <= 31;
    }

    protected static int normalizeYear(int year) {
        if (year < 100) {
            return 2000 + year;
        } else {
            return year;
        }
    }

    protected static String setToRegexAlternate(Set<String> set) {
        return String.join("|", set);
    }

    protected abstract Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source);

    public Stream<ParsedComponent> parse(String input, LocalDateTime reference) {
        ArrayList<ParsedComponent> results = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            parseMatch(matcher.toMatchResult(), reference, input).ifPresent(result -> results.add(result));
        }

        return results.stream();
    }
}
