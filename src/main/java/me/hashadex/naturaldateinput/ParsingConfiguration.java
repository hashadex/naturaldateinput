package me.hashadex.naturaldateinput;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.hashadex.naturaldateinput.parsers.Parser;
import me.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;

public abstract class ParsingConfiguration {
    private final Set<Parser> parsers;

    protected ParsingConfiguration(Set<Parser> parsers) {
        Objects.requireNonNull(parsers, "parsers must not be null");

        for (Parser parser : parsers) {
            if (parser == null) {
                throw new IllegalArgumentException("Parser set must not contain null elements");
            }
        }

        this.parsers = parsers;
    }

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

        public List<ParsedComponent> getComponents() {
            return components;
        }

        public Optional<LocalDate> getDate() {
            return Optional.ofNullable(date);
        }

        public Optional<LocalTime> getTime() {
            return Optional.ofNullable(time);
        }

        public LocalDateTime getReference() {
            return reference;
        }

        public String getSource() {
            return source;
        }

        public boolean isEmpty() {
            return date == null && time == null;
        }

        public boolean isPresent() {
            return date != null || time != null;
        }

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

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ParseResult)) {
                return false;
            }

            ParseResult result = (ParseResult) obj;

            return (
                components.equals(result.getComponents()) &&

                getDate().equals(result.getDate()) &&
                getTime().equals(result.getTime()) &&

                reference.equals(result.getReference()) &&
                source.equals(result.getSource())
            );
        }

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

    public final Stream<ParsedComponent> parseAll(String input, LocalDateTime reference) {
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(reference, "reference must not be null");

        return parsers.stream().flatMap(parser -> parser.parse(input, reference));
    }

    public final ParseResult parse(String input, LocalDateTime reference) {
        List<ParsedComponent> components = parseAll(input, reference).sorted(
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
        ).toList();
        
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

    public final ParseResult parse(String input) {
        return parse(input, LocalDateTime.now());
    }
}
