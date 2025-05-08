package me.hashadex.naturaldateinput.parsers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import me.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;

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

    protected void assertParsesAs(
        String input,
        LocalDate expectedStartDate,
        LocalTime expectedStartTime,
        LocalDate expectedEndDate,
        LocalTime expectedEndTime
    ) {
        List<ParsedComponent> results = parser.parse(input, reference).toList();

        assertEquals(1, results.size(), "Expected exactly one result, but found: " + results.size());

        ParsedComponent result = results.get(0);

        assertAll(
            () -> assertEquals(expectedStartDate, result.getStartDate().orElse(null)),
            () -> assertEquals(expectedStartTime, result.getStartTime().orElse(null)),
            () -> assertEquals(expectedEndDate, result.getEndDate().orElse(null)),
            () -> assertEquals(expectedEndTime, result.getEndTime().orElse(null))
        );
    }

    protected void assertParsesAs(String input, LocalDate expectedStartDate, LocalDate expectedEndDate) {
        assertParsesAs(input, expectedStartDate, null, expectedEndDate, null);
    }

    protected void assertParsesAs(String input, LocalTime expectedStartTime, LocalTime expectedEndTime) {
        assertParsesAs(input, null, expectedStartTime, null, expectedEndTime);
    }

    protected void assertParsesAs(String input, LocalDateTime expectedStartDateTime, LocalDateTime expectedEndDateTime) {
        LocalDate expectedStartDate = null;
        LocalTime expectedStartTime = null;
        if (expectedStartDateTime != null) {
            expectedStartDate = expectedStartDateTime.toLocalDate();
            expectedStartTime = expectedStartDateTime.toLocalTime();
        }

        LocalDate expectedEndDate = null;
        LocalTime expectedEndTime = null;
        if (expectedEndDateTime != null) {
            expectedEndDate = expectedEndDateTime.toLocalDate();
            expectedEndTime = expectedEndDateTime.toLocalTime();
        }
        
        assertParsesAs(
            input,
            expectedStartDate,
            expectedStartTime,
            expectedEndDate,
            expectedEndTime
        );
    }

    protected void assertParsesAs(String input, LocalDate expectedStartDate) {
        assertParsesAs(input, expectedStartDate, null);
    }

    protected void assertParsesAs(String input, LocalTime expectedStartTime) {
        assertParsesAs(input, expectedStartTime, null);
    }

    protected void assertParsesAs(String input, LocalDateTime expectedStartDateTime) {
        assertParsesAs(input, expectedStartDateTime, null);
    }
}
