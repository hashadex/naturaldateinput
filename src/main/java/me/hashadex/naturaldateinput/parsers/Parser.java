package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ParsedComponent)) {
                return false;
            }

            ParsedComponent component = (ParsedComponent) obj;

            return (
                reference.equals(component.getReference()) &&

                source.equals(component.getSource()) &&
                startIndex == component.getStartIndex() &&
                endIndex == component.getEndIndex() &&

                getStartDate().equals(component.getStartDate()) &&
                getStartTime().equals(component.getStartTime()) &&

                getEndDate().equals(component.getEndDate()) &&
                getEndTime().equals(component.getEndTime())
            );
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;

            result = prime * result + ((reference == null) ? 0 : reference.hashCode());
            result = prime * result + ((source == null) ? 0 : source.hashCode());
            result = prime * result + startIndex;
            result = prime * result + endIndex;
            result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
            result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
            result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
            result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
            
            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(
                "'%s' -> ".formatted(source.substring(startIndex, endIndex))
            );

            getStartDate().ifPresentOrElse(
                startDate -> sb.append(startDate),
                () -> sb.append("???")
            );
            getStartTime().ifPresent(
                startTime -> {
                    sb.append("T");
                    sb.append(startTime);
                }
            );

            sb.append("-");

            getEndDate().ifPresentOrElse(
                endDate -> sb.append(endDate),
                () -> sb.append("???")
            );
            getEndTime().ifPresent(
                endTime -> {
                    sb.append("T");
                    sb.append(endTime);
                }
            );

            return sb.toString();
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
            this.reference = Objects.requireNonNull(reference, "reference must not be null");
            
            this.source = Objects.requireNonNull(source, "source must not be null");
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public ParsedComponentBuilder(LocalDateTime reference, String source, MatchResult matchResult) {
            this(
                reference,
                source,
                Objects.requireNonNull(matchResult, "matchResult must not be null").start(),
                matchResult.end() // No need to call requireNonNull twice
            );
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

    protected abstract Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source);

    public Stream<ParsedComponent> parse(String input, LocalDateTime reference) {
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(reference, "reference must not be null");

        Matcher matcher = pattern.matcher(input);

        return matcher.results()
            .flatMap(match -> parseMatch(matcher, reference, input).stream());
    }

    public Stream<ParsedComponent> parse(String input) {
        return parse(input, LocalDateTime.now());
    }
}
