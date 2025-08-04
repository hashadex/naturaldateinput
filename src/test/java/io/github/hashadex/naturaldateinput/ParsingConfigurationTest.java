package io.github.hashadex.naturaldateinput;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import io.github.hashadex.naturaldateinput.ParsingConfiguration;
import io.github.hashadex.naturaldateinput.ParsingConfiguration.ParseResult;
import io.github.hashadex.naturaldateinput.parsers.Parser;

public class ParsingConfigurationTest {
    private static LocalDateTime reference = LocalDateTime.of(2025, 7, 2, 12, 0, 0);

    @Test
    void constructor_NullSet_ThrowsNullPointerException() {
        class MockParsingConfiguration extends ParsingConfiguration {
            MockParsingConfiguration() {
                super(null);
            }
        }

        assertThrows(NullPointerException.class, () -> new MockParsingConfiguration());
    }

    @Test
    void parse_NullParameters_ThrowsNullPointerException() {
        class MockParsingConfiguration extends ParsingConfiguration {
            MockParsingConfiguration() {
                super(Set.of());
            }
        }

        ParsingConfiguration conf = new MockParsingConfiguration();

        assertAll(
            () -> assertThrows(NullPointerException.class, () -> conf.parse(null, LocalDateTime.of(2025, 7, 2, 12, 0, 0))),
            () -> assertThrows(NullPointerException.class, () -> conf.parse("test", null))
        );
    }

    @Test
    void parse_MultipleComponents_UsesLastComponents() {
        LocalDate expectedCorrectDate = LocalDate.of(2030, 1, 1);
        LocalDate incorrectDate = LocalDate.of(2020, 9, 9);
        
        String exampleSource = "1 2";

        class MockParser extends Parser {
            MockParser() {
                super("");
            }

            @Override
            protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
                return Optional.empty();
            }

            @Override
            public Stream<ParsedComponent> parse(String input, LocalDateTime reference) {
                return Stream.of(
                    new ParsedComponentBuilder(reference, exampleSource, 0, 1).date(incorrectDate).build(),
                    new ParsedComponentBuilder(reference, exampleSource, 2, 3).date(expectedCorrectDate).build()
                );
            }
        }

        class MockParsingConfiguration extends ParsingConfiguration {
            MockParsingConfiguration() {
                super(Set.of(
                    new MockParser()
                ));
            }
        }

        ParsingConfiguration conf = new MockParsingConfiguration();

        ParseResult result = conf.parse(exampleSource, reference);

        assertEquals(expectedCorrectDate, result.date().get());
    }

    @Test
    void parse_MultipleComponentsWithSameEndIndex_UsesLongerComponent() {
        LocalDate expectedCorrectDate = LocalDate.of(2030, 1, 1);
        LocalDate incorrectDate = LocalDate.of(2020, 9, 9);
        
        String exampleSource = "12";

        class MockParser extends Parser {
            MockParser() {
                super("");
            }

            @Override
            protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
                return Optional.empty();
            }

            @Override
            public Stream<ParsedComponent> parse(String input, LocalDateTime reference) {
                return Stream.of(
                    new ParsedComponentBuilder(reference, exampleSource, 0, 2).date(expectedCorrectDate).build(),
                    new ParsedComponentBuilder(reference, exampleSource, 1, 2).date(incorrectDate).build()
                );
            }
        }

        class MockParsingConfiguration extends ParsingConfiguration {
            MockParsingConfiguration() {
                super(Set.of(
                    new MockParser()
                ));
            }
        }

        ParsingConfiguration conf = new MockParsingConfiguration();

        ParseResult result = conf.parse(exampleSource, reference);

        assertEquals(expectedCorrectDate, result.date().get());
    }

    @Test
    void parse_DateTimeComponentDoesNotFit_IgnoresDateTimeComponent() {
        LocalDate exampleDate = LocalDate.of(2020, 1, 1);
        LocalTime exampleTime = LocalTime.of(14, 0);
        
        LocalDateTime exampleDateTime = LocalDateTime.of(2025, 9, 9, 7, 8, 9);

        String exampleSource = "1 2 3";

        class MockParser extends Parser {
            MockParser() {
                super("");
            }

            @Override
            protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
                return Optional.empty();
            }

            @Override
            public Stream<ParsedComponent> parse(String input, LocalDateTime reference) {
                return Stream.of(
                    new ParsedComponentBuilder(reference, exampleSource, 4, 5).date(exampleDate).build(),
                    new ParsedComponentBuilder(reference, exampleSource, 2, 3).dateTime(exampleDateTime).build(),
                    new ParsedComponentBuilder(reference, exampleSource, 0, 1).time(exampleTime).build()
                );
            }
        }

        class MockParsingConfiguration extends ParsingConfiguration {
            MockParsingConfiguration() {
                super(Set.of(
                    new MockParser()
                ));
            }
        }

        ParsingConfiguration conf = new MockParsingConfiguration();

        ParseResult result = conf.parse(exampleSource, reference);

        LocalDate resultDate = result.date().get();
        LocalTime resultTime = result.time().get();

        assertAll(
            () -> assertEquals(exampleDate, resultDate),
            () -> assertEquals(exampleTime, resultTime)
        );
    }
}
