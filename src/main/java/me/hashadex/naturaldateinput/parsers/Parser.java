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

/**
 * Base abstract class for all parsers. A parser is a tool that handles the
 * parsing of all occurences of a certain format of time or date within a string.
 * <p>
 * <b>In most cases users of this library should not use the Parsers directly,
 * instead they should use
 * {@link me.hashadex.naturaldateinput.ParsingConfiguration ParsingConfigurations}.</b>
 * <p>
 * Parsers work by utilizing regular expressions for locating time/date within
 * a string and processing the time/date with the
 * {@link #parseMatch(MatchResult, LocalDateTime, String)} method.
 * Each concrete parser has it's own regex and <code>parseMatch</code> method.
 * A parser handles only one date/time format.
 * <p>
 * Some examples of parsers included in this library are:
 * <ul>
 * <li>{@link me.hashadex.naturaldateinput.parsers.common.ISODateParser ISODateParser},
 * which handles the ISO 8601 (YYYY-MM-DD) date format
 * <li>{@link me.hashadex.naturaldateinput.parsers.common.TwentyFourHourTimeParser TwentyFourHourTimeParser},
 * which handles the 24 hour HH:MM:SS time format
 * <li>{@link me.hashadex.naturaldateinput.parsers.en.ENRelativeWordParser ENRelativeWordParser}
 * which handles English words like "today" and "tomorrow"
 * </ul>
 * 
 * @author hashadex
 * @since 1.0.0
 */
public abstract class Parser {
    /**
     * Compiled regex, used by {@link #parse(String, LocalDateTime) parse}.
     * The regex is compiled and the field is set in the
     * {@link #Parser(String, int) constructor}.
     * 
     * @since 1.0.0
     */
    protected final Pattern pattern;

    /**
     * Constructs the class and compiles the provided <code>regex</code> using
     * {@link java.util.regex.Pattern#compile(String, int)} with the provided
     * <code>flags</code>.
     * <p>
     * The {@link #Parser(String)} constructor should be used if the concrete
     * parser wishes to use the default match flags.
     * 
     * @param regex Regular expression which will be used by the parser to locate
     *              date/time expressions and process them
     * @param flags A bit mask representing match flags that will be passed to
     *              <code>Pattern.compile</code>
     * @see java.util.regex.Pattern
     * @since 1.0.0
     */
    protected Parser(String regex, int flags) {
        pattern = Pattern.compile(regex, flags);
    }

    /**
     * Constructs the class and compiles the provided <code>regex</code> using
     * {@link java.util.regex.Pattern#compile(String, int)} with the default
     * match flags, which are
     * {@link java.util.regex.Pattern#CASE_INSENSITIVE CASE_INSENSITIVE},
     * {@link java.util.regex.Pattern#UNICODE_CASE UNICODE_CASE} and
     * {@link java.util.regex.Pattern#COMMENTS COMMENTS}.
     * <p>
     * Use the {@link #Parser(String, int)} constructor to override the default
     * flags.
     * 
     * @param regex Regular expression which will be used by the parser to locate
     *              date/time expressions and process them
     * @since 1.0.0
     */
    protected Parser(String regex) {
        this(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
    }

    /**
     * Joins a <code>Collection</code> of strings into a single string, where
     * all elements of the collection are separated by a regex alternate symbol
     * (<code>|</code>). This string may then be used inside a regular expression.
     * <p>
     * All strings inside the collection are escaped using
     * {@link java.util.regex.Pattern#quote(String)}, so any metacharacters or
     * escape sequences in the strings will be given no special meaning.
     * 
     * @param collection <code>Collection</code>, elements of which will be
     *                   used to construct the string
     * @return           String containing all elements of
     *                   <code>collection</code>, joined by <code>|</code>
     * @since 1.0.0
     */
    protected static String toRegexAlternate(Collection<String> collection) {
        return collection.stream()
            .map(Pattern::quote)
            .collect(Collectors.joining("|"));
    }

    /**
     * Immutable data class that represents one parsed regex match. Contains
     * date and/or time, reference datetime, source string, as well as the
     * start and end indexes of the regex match from which the date/time were
     * parsed.
     * <p>
     * <b>Note:</b> A <code>ParsedComponent</code> will always contain a date
     * or a time. A case when both {@link #date()} and {@link #time()} methods
     * return empty <code>Optionals</code> is not possible.
     * <p>
     * Objects of this class can only be created by {@link Parser Parsers}
     * by using the protected {@link ParsedComponentBuilder}.
     * 
     * @author hashadex
     * @since 1.0.0
     */
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
    
        /**
         * Returns the reference datetime used by the parser, which is usually
         * the datetime when the string was parsed.
         * <p>
         * For example,
         * {@link me.hashadex.naturaldateinput.parsers.en.ENRelativeWordParser ENRelativeWordParser}
         * uses the reference datetime to parse words like "today" and
         * "tomorrow".
         * 
         * @return Reference datetime
         * @since 1.0.0
         */
        public LocalDateTime reference() {
            return reference;
        }
    
        /**
         * Returns the source string in which the parser had found and
         * successfully parsed a certain date/time format.
         * 
         * @return Source string
         * @since 1.0.0
         */
        public String source() {
            return source;
        }
    
        /**
         * Returns the start index of the regex match from which the date/time
         * were parsed.
         * <p>
         * Use the {@link #text()} method to get the substring of the source
         * string containing the regex match text.
         * 
         * @return Start index
         * @see #endIndex()
         * @see #source()
         * @since 1.0.0
         */
        public int startIndex() {
            return startIndex;
        }
    
        /**
         * Returns the end index of the regex match from which the date/time
         * were parsed.
         * <p>
         * Use the {@link #text()} method to get the substring of the source
         * string containing the regex match text.
         * 
         * @return End index
         * @see #startIndex()
         * @see #source()
         * @since 1.0.0
         */
        public int endIndex() {
            return endIndex;
        }

        /**
         * Returns the length of the regex match from which the date/time
         * were parsed.
         * 
         * @return Length, calculated with <code>endIndex - startIndex</code>
         * @see #source()
         * @since 1.0.0
         */
        public int length() {
            return endIndex - startIndex;
        }
    
        /**
         * Returns the text of the regex match from which the date/time were
         * parsed.
         * <p>
         * Example:
         * <pre>
         * {@code
         * ENRelativeWordParser parser = new ENRelativeWordParser();
         * 
         * ParsedComponent result = parser.parse("Meeting tomorrow").findAny().get();
         * System.out.println(result.text()); // -> "tomorrow"
         * }
         * </pre>
         * 
         * @return Substring of the source string beginning at
         *         {@link #startIndex()} and ending at {@link #endIndex()}
         * @see #source()
         * @since 1.0.0
         */
        public String text() {
            return source.substring(startIndex, endIndex);
        }
    
        /**
         * Returns the parsed date if the parser was able to parse a date from
         * the regex match.
         * <p>
         * <b>Note:</b> a <code>ParsedComponent</code> will always contain a
         * date and/or a time. Therefore, a case when both {@link #date()}
         * and {@link #time()} return empty <code>Optionals</code> is not
         * possible.
         * 
         * @return <code>Optional</code> that may contain the parsed
         *         <code>LocalDate</code>
         * @see #source()
         * @since 1.0.0
         */
        public Optional<LocalDate> date() {
            return Optional.ofNullable(date);
        }
    
        /**
         * Returns the parsed time if the parser was able to parse a time from
         * the regex match.
         * <p>
         * <b>Note:</b> a <code>ParsedComponent</code> will always contain a
         * date and/or a time. Therefore, a case when both {@link #date()}
         * and {@link #time()} return empty <code>Optionals</code> is not
         * possible.
         * 
         * @return <code>Optional</code> that may contain the parsed
         *         <code>LocalTime</code>
         * @see #source()
         * @since 1.0.0
         */
        public Optional<LocalTime> time() {
            return Optional.ofNullable(time);
        }

        /**
         * Compares this <code>ParsedComponent</code> to the specified object.
         * <p>
         * Returns <code>true</code> if the specified object is a
         * <code>ParsedComponent</code> and all fields (reference, source
         * string, start index, end index, date, time) are equal.
         * 
         * @param obj Object to compare, <code>null</code> returns
         *            <code>false</code>
         * @return <code>true</code> if this <code>ParsedComponent</code>
         *         is equal to the specified object
         * @since 1.0.0
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ParsedComponent)) {
                return false;
            }

            ParsedComponent component = (ParsedComponent) obj;

            return (
                reference.equals(component.reference()) &&

                source.equals(component.source()) &&
                startIndex == component.startIndex() &&
                endIndex == component.endIndex() &&

                date().equals(component.date()) &&
                time().equals(component.time())
            );
        }

        /**
         * Returns the hash code for this <code>ParsedComponent</code>.
         * 
         * @return Hash code integer
         * @since 1.0.0
         */
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

        /**
         * Returns a string representation of this <code>ParsedComponent</code>.
         * 
         * @return String in the format {@code "ParsedComponent["<text>" -> <date>T<time>]"}
         * @since 1.0.0
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            
            sb.append('"' + text() + '"');

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

    /**
     * Protected builder for {@link ParsedComponent ParsedComponents} which can
     * only be accessed by {@link Parser} and its subclasses.
     * 
     * @author hashadex
     * @since 1.0.0
     */
    protected static final class ParsedComponentBuilder {
        private LocalDateTime reference;
    
        private String source;
        private int startIndex;
        private int endIndex;
    
        private LocalDate date;
        private LocalTime time;

        /**
         * Constructs the builder.
         * <p>
         * Use {@link #ParsedComponentBuilder(LocalDateTime, String, MatchResult)}
         * to extract the start and end indexes from the
         * {@link java.util.regex.MatchResult MatchResult} if they were not
         * shifted during the parsing process.
         * 
         * @param reference  Reference datetime, not null
         * @param source     Source string, not null
         * @param startIndex Start index of the regex match that was processed
         * @param endIndex   End index of the regex match that was processed
         * @throws NullPointerException If <code>reference</code> or
         *                              <code>source</code> parameters are null
         * @since 1.0.0
         */
        public ParsedComponentBuilder(LocalDateTime reference, String source, int startIndex, int endIndex) {
            this.reference = Objects.requireNonNull(reference, "reference must not be null");
            
            this.source = Objects.requireNonNull(source, "source must not be null");
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        /**
         * Constructs the builder, extracting the start and end indexes from
         * the provided {@link java.util.regex.MatchResult MatchResult}.
         * <p>
         * Use the {@link #ParsedComponentBuilder(LocalDateTime, String, int, int)}
         * constructor if you want to provide start and end indexes that are
         * different from the MatchResult's indexes.
         * 
         * @param reference   Reference datetime, not null
         * @param source      Source string, not null
         * @param matchResult MatchResult to extract indexes from, not null
         * @throws NullPointerException If any of the parameters are null
         * @since 1.0.0
         */
        public ParsedComponentBuilder(LocalDateTime reference, String source, MatchResult matchResult) {
            this(
                reference,
                source,
                Objects.requireNonNull(matchResult, "matchResult must not be null").start(),
                matchResult.end() // No need to call requireNonNull twice
            );
        }

        /**
         * Sets the date field that the <code>ParsedComponent</code> is going
         * to be created with.
         * 
         * @param date Date field
         * @return This instance of <code>ParsedComponentBuilder</code>
         * @since 1.0.0
         */
        public ParsedComponentBuilder date(LocalDate date) {
            this.date = date;

            return this;
        }

        /**
         * Sets the time field that the <code>ParsedComponent</code> is going
         * to be created with.
         * 
         * @param time Time field
         * @return This instance of <code>ParsedComponentBuilder</code>
         * @since 1.0.0
         */
        public ParsedComponentBuilder time(LocalTime time) {
            this.time = time;
            
            return this;
        }

        /**
         * Sets both date and time fields that the <code>ParsedComponent</code>
         * is going to be created with.
         * 
         * @param dateTime <code>LocalDateTime</code> from which date and time
         *                 fields are going to be extracted
         * @return This instance of <code>ParsedComponentBuilder</code>
         * @since 1.0.0
         */
        public ParsedComponentBuilder dateTime(LocalDateTime dateTime) {
            date(dateTime.toLocalDate());
            time(dateTime.toLocalTime());

            return this;
        }

        /**
         * Builds a new instance of <code>ParsedComponent</code> using fields
         * that can be set with {@link #date(LocalDate)},
         * {@link #time(LocalTime)} and {@link #dateTime(LocalDateTime)}.
         * 
         * @return New instance of <code>ParsedComponent</code>
         * @throws NullPointerException if both time and date fields are null/
         *                              were not set
         * @since 1.0.0
         */
        public ParsedComponent build() {
            if (this.date == null && this.time == null) {
                throw new NullPointerException("Both time and date fields can not be null");
            }

            return new ParsedComponent(this);
        }
    }

    /**
     * Extracts match information from the provided
     * {@link java.util.regex.MatchResult MatchResult} and parses it into a
     * date and/or time. The resulting date/time is returned inside a
     * {@link ParsedComponent} alongside with the reference datetime and the
     * source string.
     * <p>
     * This method should only be called from {@link #parse(String, LocalDateTime)}.
     * 
     * @param match     Match info
     * @param reference Reference datetime
     * @param source    Source string
     * @return <code>Optional</code> containing a <code>ParsedComponent</code>
     *         if the parsing was successful, or an empty <code>Optional</code>
     *         if the parsing was unsuccessful.
     * @since 1.0.0
     */
    protected abstract Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source);

    /**
     * Parses the provided string.
     * <p>
     * This method parses the string by matching it against the parser's
     * {@link #pattern} and calling
     * {@link #parseMatch(MatchResult, LocalDateTime, String)} for every match.
     * The <code>parseMatch</code> method tries to process the match and turn
     * it into a {@link ParsedComponent}.
     * <p>
     * Use {@link #parse(String)} to automatically use
     * {@link java.time.LocalDateTime#now()} as reference.
     * 
     * @param input     String to be parsed
     * @param reference <code>LocalDateTime</code> that serves as a reference 
     *                  point for parsers that deal with relative date/time
     *                  expressions, such as
     *                  {@link me.hashadex.naturaldateinput.parsers.en.ENRelativeWordParser ENRelativeWordParser}
     * @return <code>Stream</code> of <code>ParsedComponents</code> that represent
     *         parsing results. The stream can be empty.
     * @since 1.0.0
     */
    public Stream<ParsedComponent> parse(String input, LocalDateTime reference) {
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(reference, "reference must not be null");

        Matcher matcher = pattern.matcher(input);

        return matcher.results()
            .flatMap(match -> parseMatch(matcher, reference, input).stream());
    }

    /**
     * Parses the provided string using
     * {@link java.time.LocalDateTime#now()} as reference.
     * <p>
     * See {@link #parse(String, LocalDateTime)} for explanation on how the
     * method works internally.
     * 
     * @param input String to be parsed
     * @return <code>Stream</code> of {@link ParsedComponent ParsedComponents}
     *         that represent parsing results. The stream can be empty.
     * @since 1.0.0
     */
    public Stream<ParsedComponent> parse(String input) {
        return parse(input, LocalDateTime.now());
    }
}
