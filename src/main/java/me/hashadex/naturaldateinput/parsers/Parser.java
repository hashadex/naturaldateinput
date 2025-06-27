package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Parser {
    protected final Pattern pattern;

    protected Parser(String regex, int flags) {
        pattern = Pattern.compile(regex, flags);
    }

    protected Parser(String regex) {
        this(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
    }

    protected static String toRegexAlternate(Collection<String> collection) {
        return collection.stream()
            .map(Pattern::quote)
            .collect(Collectors.joining("|"));
    }

    public static final class ParsedComponent {
        private final LocalDateTime reference;
    
        private final String source;
        private final int startIndex;
        private final int endIndex;
    
        private final LocalDate date;
        private final LocalTime time;

        private ParsedComponent(ParsedComponentBuilder builder) {
            this.reference = builder.reference;

            this.source = builder.source;
            this.startIndex = builder.startIndex;
            this.endIndex = builder.endIndex;

            this.date = builder.date;
            this.time = builder.time;
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

        public int getLength() {
            return endIndex - startIndex;
        }
    
        public String getText() {
            return source.substring(startIndex, endIndex);
        }
    
        public Optional<LocalDate> getDate() {
            return Optional.ofNullable(date);
        }
    
        public Optional<LocalTime> getTime() {
            return Optional.ofNullable(time);
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

                getDate().equals(component.getDate()) &&
                getTime().equals(component.getTime())
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
            result = prime * result + ((date == null) ? 0 : date.hashCode());
            result = prime * result + ((time == null) ? 0 : time.hashCode());

            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            
            sb.append('"' + getText() + '"');

            sb.append(" -> ");

            // Append date and time
            if (date != null) {
                sb.append(date);
            }

            if (date != null && time != null) {
                sb.append('T');
            }

            if (time != null) {
                sb.append(time);
            }

            // Wrap string in "ParsedComponent[...]"
            sb.insert(0, this.getClass().getSimpleName() + '[');
            sb.append(']');

            return sb.toString();
        }
    }

    protected static final class ParsedComponentBuilder {
        private LocalDateTime reference;
    
        private String source;
        private int startIndex;
        private int endIndex;
    
        private LocalDate date;
        private LocalTime time;

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

        public ParsedComponentBuilder date(LocalDate date) {
            this.date = date;

            return this;
        }

        public ParsedComponentBuilder time(LocalTime time) {
            this.time = time;
            
            return this;
        }

        public ParsedComponentBuilder dateTime(LocalDateTime dateTime) {
            date(dateTime.toLocalDate());
            time(dateTime.toLocalTime());

            return this;
        }

        public ParsedComponent build() {
            if (this.date == null && this.time == null) {
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
