package io.github.hashadex.naturaldateinput.parsers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import io.github.hashadex.naturaldateinput.parsers.Parser;
import io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;

public abstract class ParserTest {
    protected LocalDateTime reference;
    protected Parser parser;

    @BeforeEach
    public abstract void setup();

    protected void assertParses(String input) {
        assertTrue(
            parser.parse(input, reference).findAny().isPresent(),
            "Parser returned empty stream for input '%s'".formatted(input)
        );
    }

    protected void assertDoesNotParse(String input) {
        assertTrue(
            parser.parse(input, reference).findAny().isEmpty(),
            "Parser returned non-empty stream for input '%s'".formatted(input)
        );
    }

    protected void assertParsesAs(String input, LocalDate expectedDate, LocalTime expectedTime) {
        List<ParsedComponent> results = parser.parse(input, reference).toList();

        assertEquals(1, results.size(), "Expected exactly one result, but found: " + results.size());

        ParsedComponent result = results.get(0);

        assertAll(
            () -> assertEquals(expectedDate, result.date().orElse(null)),
            () -> assertEquals(expectedTime, result.time().orElse(null))
        );
    }

    protected void assertParsesAs(String input, LocalDate expectedDate) {
        assertParsesAs(input, expectedDate, null);
    }

    protected void assertParsesAs(String input, LocalTime expectedTime) {
        assertParsesAs(input, null, expectedTime);
    }

    protected void assertParsesAs(String input, LocalDateTime expectedDateTime) {
        assertParsesAs(input, expectedDateTime.toLocalDate(), expectedDateTime.toLocalTime());
    }
}
