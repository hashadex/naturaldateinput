package io.github.hashadex.naturaldateinput;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.hashadex.naturaldateinput.parsers.Parser;
import io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;

/**
 * Base abstract class for parsing configurations. A parsing configuration is a
 * set of multiple {@link io.github.hashadex.naturaldateinput.parsers.Parser Parsers},
 * that allows you to easily parse a string with multiple parsers at once.
 * {@link io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent ParsedComponents}
 * returned by parsers during the parsing operation are combined into a single
 * {@link io.github.hashadex.naturaldateinput.ParsingConfiguration.ParseResult ParseResult}.
 * 
 * @author hashadex
 * @since 1.0.0
 */
public abstract class ParsingConfiguration {
    private final Set<Parser> parsers;

    /**
     * Constructs a ParsingConfiguration containing the specified
     * <code>Set</code> of {@link io.github.hashadex.naturaldateinput.parsers.Parser Parsers}.
     * Parser set must not contain null elements.
     * 
     * @param parsers <code>Parser</code> set, not null
     * @throws NullPointerException if <code>parsers</code> is null or contains
     *                              null elements
     * @since 1.0.0
     */
    public ParsingConfiguration(Set<Parser> parsers) {
        Objects.requireNonNull(parsers, "parsers must not be null");

        for (Parser parser : parsers) {
            if (parser == null) {
                throw new NullPointerException("Parser set must not contain null elements");
            }
        }

        this.parsers = parsers;
    }

    /**
     * Immutable data class representing a final result of parsing using a
     * parsing configuration, combined from multiple
     * {@link io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent ParsedComponents}.
     * <p>
     * Unlike <code>ParsedComponent</code>, <code>ParseResult</code> may contain
     * neither time or date. Use {@link #isEmpty()} and {@link #isPresent()} to
     * check if a <code>ParseResult</code> is empty.
     * 
     * @author hashadex
     * @since 1.0.0
     */
    public final class ParseResult {
        private final List<ParsedComponent> components;

        private final LocalDate date;
        private final LocalTime time;

        private final LocalDateTime reference;
        private final String source;

        private ParseResult(
            List<ParsedComponent> components, LocalDate date, LocalTime time, LocalDateTime reference, String source
        ) {
            this.components = components;
            
            this.date = date;
            this.time = time;

            this.reference = reference;
            this.source = source;
        }

        /**
         * Returns a list of
         * {@link io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent ParsedComponents}
         * from which the date and time of this <code>ParseResult</code> were
         * extracted.
         * 
         * @return <code>List</code> of <code>ParsedComponents</code>
         * @see #date()
         * @see #time()
         * @since 1.0.0
         */
        public List<ParsedComponent> components() {
            return components;
        }

        /**
         * Returns the date extracted from the latest (by occurrence in source
         * string) {@link io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent ParsedComponent}
         * that contains a date.
         * 
         * @return <code>Optional</code> containing a <code>LocalDate</code> if
         *         a date was able to be parsed from the source string
         * @see io.github.hashadex.naturaldateinput.ParsingConfiguration#parse(String, LocalDateTime)
         * @since 1.0.0
         */ 
        public Optional<LocalDate> date() {
            return Optional.ofNullable(date);
        }

        /**
         * Returns the time extracted from the latest (by occurrence in source
         * string) {@link io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent ParsedComponent}
         * that contains a time.
         * 
         * @return <code>Optional</code> containing a <code>LocalTime</code> if
         *         a time was able to be parsed from the source string
         * @see io.github.hashadex.naturaldateinput.ParsingConfiguration#parse(String, LocalDateTime)
         * @since 1.0.0
         */ 
        public Optional<LocalTime> time() {
            return Optional.ofNullable(time);
        }

        /**
         * Returns the reference datetime used during the parsing operation.
         * 
         * The reference datetime serves as a reference point for some parsers
         * that deal with relative date/time expressions, like
         * {@link io.github.hashadex.naturaldateinput.parsers.en.ENRelativeWordParser ENRelativeWordParser}
         * The reference datetime is typically the timestamp that is made when
         * the parsing operation starts.
         * 
         * @return Reference datetime
         * @since 1.0.0
         */
        public LocalDateTime reference() {
            return reference;
        }

        /**
         * Returns the source string used during the parsing operation.
         * 
         * @return Source string
         * @since 1.0.0
         */
        public String source() {
            return source;
        }

        /**
         * Checks if this <code>ParseResult</code> contains neither time or date.
         * 
         * @return <code>true</code> if both date and time are <code>null</code>
         * @since 1.0.0
         */
        public boolean isEmpty() {
            return date == null && time == null;
        }

        /**
         * Checks if this <code>ParseResult</code> contains a time or a date.
         * 
         * @return <code>true</code> if time or date is not <code>null</code>
         * @since 1.0.0
         */
        public boolean isPresent() {
            return date != null || time != null;
        }

        /**
         * Returns the hash code for this <code>ParseResult</code>.
         * 
         * @return Hash code integer
         * @since 1.0.0
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            
            result = prime * result + ((components == null) ? 0 : components.hashCode());
            result = prime * result + ((date == null) ? 0 : date.hashCode());
            result = prime * result + ((time == null) ? 0 : time.hashCode());
            result = prime * result + ((reference == null) ? 0 : reference.hashCode());
            result = prime * result + ((source == null) ? 0 : source.hashCode());
            
            return result;
        }

        /**
         * Compares this <code>ParseResult</code> to the specified object.
         * <p>
         * Returns <code>true</code> if the specified object is a
         * <code>ParseResult</code> and all fields (components, date, time,
         * reference, source) are equal.
         * 
         * @param obj Object to compare, <code>null</code> returns
         *            <code>false</code>
         * @return <code>true</code> if this <code>ParseResult</code> is equal
         *         to the specified object
         * @since 1.0.0
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ParseResult)) {
                return false;
            }

            ParseResult result = (ParseResult) obj;

            return (
                components.equals(result.components()) &&

                date().equals(result.date()) &&
                time().equals(result.time()) &&

                reference.equals(result.reference()) &&
                source.equals(result.source())
            );
        }

        /**
         * Returns a string representation of this <code>ParseResult</code>
         * 
         * @return String in the format 
         *         {@code "ParseResult["<component text>", "<component text>" -> <date>T<time>]"}
         */
        @Override
        public String toString() {
            // Collect the texts of all components into a single string, e.g.
            // ""tomorrow", "at 5 am"""
            String componentTexts = components.stream()
                .map(c -> '"' + c.text() + '"')
                .collect(Collectors.joining(", "));
            
            StringBuilder sb = new StringBuilder();

            sb.append('{' + componentTexts + '}');
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

            // Wrap string in "ParseResult[...]"
            sb.insert(0, this.getClass().getSimpleName() + '[');
            sb.append(']');

            return sb.toString();
        }
    }

    /**
     * Parses the provided string using all
     * {@link io.github.hashadex.naturaldateinput.parsers.Parser Parsers} in the
     * configuration and combines returned
     * {@link io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent ParsedComponents}
     * into a single {@link ParseResult}.
     * <p>
     * Only up to last (by occurrence in text) two <code>ParsedComponents</code>
     * are used for the <code>ParseResult</code> {@link ParseResult#date() date}
     * and {@link ParseResult#time() time} fields. Meaning, if there are
     * multiple <code>ParsedComponents</code> containing a date returned by the
     * parsers, then only the last component is used for the date field in the
     * <code>ParseResult</code>.
     * <p>
     * Use the {@link #parse(String)} method to automatically use
     * {@link java.time.LocalDateTime#now()} as reference datetime.
     * 
     * @param input     String to be parsed
     * @param reference <code>LocalDateTime</code> that serves as a reference
     *                  point for parsers that deal with relative date/time
     *                  expressions, such as
     *                  {@link io.github.hashadex.naturaldateinput.parsers.en.ENRelativeWordParser ENRelativeWordParser}
     * @return <code>ParseResult</code> composed from all returned <code>
     *         ParsedComponents</code>. Might be empty.
     * @since 1.0.0
     */
    public final ParseResult parse(String input, LocalDateTime reference) {
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(reference, "reference must not be null");

        List<ParsedComponent> components = parsers.stream()
            .flatMap(parser -> parser.parse(input, reference))
            .sorted(
                // Sort components by their end index,
                // so that components that appear later in the string
                // would be first in the stream
                (c1, c2) -> {
                    if (c1.endIndex() > c2.endIndex()) {
                        return -1;
                    } else if (c1.endIndex() < c2.endIndex()) {
                        return 1;
                    } else { // end indexes are equal
                        // If the end indexes of two components are equal,
                        // put the component with the biggest length first.
                        if (c1.length() > c2.length()) {
                            return -1;
                        } else if (c1.length() < c2.length()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }
            )
            .toList();
        
        // Iterate through the sorted list and obtain date and time
        // from the latest components
        List<ParsedComponent> usedComponents = new ArrayList<>(2); // Only up to two components are possible to be used at once
        
        LocalDate date = null;
        LocalTime time = null;

        for (ParsedComponent component : components) {
            if (component.date().isPresent() && component.time().isPresent()) {
                // Use a component that has both time and date only if both
                // time and date "slots" are null
                if (time == null && date == null) {
                    date = component.date().get();
                    time = component.time().get();

                    usedComponents.add(component);
                }
            } else if (component.date().isPresent() && date == null) {
                date = component.date().get();

                usedComponents.add(component);
            } else if (component.time().isPresent() && time == null) {
                time = component.time().get();

                usedComponents.add(component);
            }

            if (date != null && time != null) {
                break;
            }
        }

        return new ParseResult(usedComponents, date, time, reference, input);
    }

    /**
     * Parses the provided string using
     * {@link java.time.LocalDateTime#now()} as reference datetime with all
     * {@link io.github.hashadex.naturaldateinput.parsers.Parser Parsers} in the
     * configuration and combines returned
     * {@link io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent ParsedComponents}
     * into a single {@link ParseResult}.
     * <p>
     * Only up to last (by occurrence in text) two <code>ParsedComponents</code>
     * are used for the <code>ParseResult</code> {@link ParseResult#date() date}
     * and {@link ParseResult#time() time} fields. Meaning, if there are multiple
     * <code>ParsedComponents</code> containing a date returned by the parsers,
     * then only the last component is used for the date field in the <code>
     * ParseResult</code>.
     * <p>
     * Use the {@link #parse(String, LocalDateTime)} method to use a different
     * <code>LocalDateTime</code> as reference.
     * 
     * @param input String to be parsed
     * @return <code>ParseResult</code> composed from all returned <code>
     *         ParsedComponents</code>. Might be empty.
     * @since 1.0.0
     */
    public final ParseResult parse(String input) {
        return parse(input, LocalDateTime.now());
    }
}
