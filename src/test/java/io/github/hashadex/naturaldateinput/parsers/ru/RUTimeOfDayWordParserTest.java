package io.github.hashadex.naturaldateinput.parsers.ru;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.hashadex.naturaldateinput.parsers.ParserTest;

public class RUTimeOfDayWordParserTest extends ParserTest {
    @Override
    @BeforeEach
    public void setup() {
        parser = new RUTimeOfDayWordParser();

        reference = LocalDateTime.of(2025, 6, 22, 12, 0, 0);
    }

    @Test
    void parse_Midnight_Returns0000() {
        assertParsesAs("полночь", LocalTime.of(0, 0));
    }

    @Test
    void parse_Morning_Returns0600() {
        assertAll(
            () -> assertParsesAs("утро", LocalTime.of(6, 0)),
            () -> assertParsesAs("утром", LocalTime.of(6, 0))
        );
    }

    @Test
    void parse_Noon_Returns1200() {
        assertParsesAs("полдень", LocalTime.of(12, 0));
    }

    @Test
    void parse_Evening_Returns2000() {
        assertAll(
            () -> assertParsesAs("вечер", LocalTime.of(20, 0)),
            () -> assertParsesAs("вечером", LocalTime.of(20, 0))
        );
    }

    @Test
    void parse_UppercaseWord_Parses() {
        assertAll(
            () -> assertParses("Полдень"),
            () -> assertParses("ПОЛДЕНЬ")
        );
    }

    @Test
    void parse_InvalidCharactersBeforeInput_ReturnsNoResults() {
        assertDoesNotParse("invalidполдень");
    }

    @Test
    void parse_InvalidCharactersAfterInput_ReturnsNoResults() {
        assertDoesNotParse("полденьinvalid");
    }
}
