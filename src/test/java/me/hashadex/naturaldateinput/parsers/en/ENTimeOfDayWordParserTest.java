package me.hashadex.naturaldateinput.parsers.en;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.hashadex.naturaldateinput.parsers.ParserTest;

public class ENTimeOfDayWordParserTest extends ParserTest {
    @Override
    @BeforeEach
    public void setup() {
        parser = new ENTimeOfDayWordParser();

        reference = LocalDateTime.of(2025, 6, 22, 12, 0, 0);
    }

    @Test
    void parse_Midnight_Returns0000() {
        assertParsesAs("midnight", LocalTime.of(0, 0));
    }

    @Test
    void parse_Morning_Returns0600() {
        assertParsesAs("morning", LocalTime.of(6, 0));
    }

    @Test
    void parse_Noon_Returns1200() {
        assertParsesAs("noon", LocalTime.of(12, 0));
    }

    @Test
    void parse_Midday_Returns1200() {
        assertParsesAs("midday", LocalTime.of(12, 0));
    }

    @Test
    void parse_Afternoon_Returns1500() {
        assertParsesAs("afternoon", LocalTime.of(15, 0));
    }

    @Test
    void parse_Evening_Returns2000() {
        assertParsesAs("evening", LocalTime.of(20, 0));
    }

    @Test
    void parse_UppercaseWord_Parses() {
        assertAll(
            () -> assertParses("Noon"),
            () -> assertParses("NOON")
        );
    }

    @Test
    void parse_InvalidCharactersBeforeInput_ReturnsNoResults() {
        assertDoesNotParse("invalidnoon");
    }

    @Test
    void parse_InvalidCharactersAfterInput_ReturnsNoResults() {
        assertDoesNotParse("nooninvalid");
    }
}
