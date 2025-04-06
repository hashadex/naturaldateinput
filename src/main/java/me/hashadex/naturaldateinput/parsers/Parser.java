package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class Parser {
    private final Pattern pattern;

    protected Parser(String regex, int flags) {
        pattern = Pattern.compile(regex, flags);
    }

    protected Parser(String regex) {
        this(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
    }

    public static final class ParsedComponent {
        private final LocalDateTime reference;
    
        private final String source;
        private final int startIndex;
        private final int endIndex;
    
        private final LocalDate startDate;
        private final LocalTime startTime;
    
        private final LocalDate endDate;
        private final LocalTime endTime;

        private ParsedComponent(ParsedComponentBuilder builder) {
            this.reference = builder.reference;

            this.source = builder.source;
            this.startIndex = builder.startIndex;
            this.endIndex = builder.endIndex;

            this.startDate = builder.startDate;
            this.startTime = builder.startTime;

            this.endDate = builder.endDate;
            this.endTime = builder.endTime;
        }
    
        public LocalDateTime getReference() {
            return reference;
        }
    
        public String getSource() {
            return source;
        }
    
        public int getStartIndex() {
            return startIndex;
        }
    
        public int getEndIndex() {
            return endIndex;
        }
    
        public String getText() {
            return source.substring(startIndex, endIndex);
        }
    
        public Optional<LocalDate> getStartDate() {
            return Optional.ofNullable(startDate);
        }
    
        public Optional<LocalTime> getStartTime() {
            return Optional.ofNullable(startTime);
        }
    
        public Optional<LocalDate> getEndDate() {
            return Optional.ofNullable(endDate);
        }
    
        public Optional<LocalTime> getEndTime() {
            return Optional.ofNullable(endTime);
        }
    }

    protected static final class ParsedComponentBuilder {
        private LocalDateTime reference;
    
        private String source;
        private int startIndex;
        private int endIndex;
    
        private LocalDate startDate;
        private LocalTime startTime;
    
        private LocalDate endDate;
        private LocalTime endTime;

        public ParsedComponentBuilder(LocalDateTime reference, String source, int startIndex, int endIndex) {
            this.reference = reference;
            
            this.source = source;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public ParsedComponentBuilder(LocalDateTime reference, String source, MatchResult matchResult) {
            this(reference, source, matchResult.start(), matchResult.end());
        }

        public ParsedComponentBuilder start(LocalDate date) {
            this.startDate = date;
            return this;
        }

        public ParsedComponentBuilder start(LocalTime time) {
            this.startTime = time;
            return this;
        }

        public ParsedComponentBuilder start(LocalDateTime dateTime) {
            this.start(dateTime.toLocalDate());
            this.start(dateTime.toLocalTime());
            return this;
        }

        public ParsedComponentBuilder end(LocalDate date) {
            this.endDate = date;
            return this;
        }

        public ParsedComponentBuilder end(LocalTime time) {
            this.endTime = time;
            return this;
        }

        public ParsedComponentBuilder end(LocalDateTime dateTime) {
            this.end(dateTime.toLocalDate());
            this.end(dateTime.toLocalTime());
            return this;
        }

        public ParsedComponent build() {
            if (
                this.startDate == null &&
                this.startTime == null &&
                this.endDate == null &&
                this.endTime == null
            ) {
                throw new IllegalArgumentException("At least one date/time field must be non-null");
            }

            return new ParsedComponent(this);
        }
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
