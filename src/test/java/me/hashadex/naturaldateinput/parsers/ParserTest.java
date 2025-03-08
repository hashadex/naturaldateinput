package me.hashadex.naturaldateinput.parsers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;

import me.hashadex.naturaldateinput.ParsedComponent;

public abstract class ParserTest {
    protected Parser parser;
    protected LocalDateTime reference;

    protected void assertParses(String input) {
        assertTrue(
            parser.parse(input, reference).findAny().isPresent(),
            "Parser returned empty stream for input '%s'".formatted(input)
        );
    }

    protected void assertDoesNotParse(String input) {
        assertFalse(
            parser.parse(input, reference).findAny().isPresent(),
            "Parser returned non-empty stream for input '%s'".formatted(input)
        );
    }

    protected void assertParsedDateEquals(LocalDate expectedResult, String input) {
        List<ParsedComponent> results = parser.parse(input, reference).toList();

        assertEquals(1, results.size(), "Parser returned a stream of %s elements, expected 1".formatted(results.size()));

        Optional<LocalDate> result = results.get(0).getDate();

        assertTrue(result.isPresent(), "Returned ParsedComponent contains no date");
        assertEquals(expectedResult, result.get());
    }

    @BeforeEach
    public abstract void setup();
}
