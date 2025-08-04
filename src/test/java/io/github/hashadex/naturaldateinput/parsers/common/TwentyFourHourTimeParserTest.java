package io.github.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.hashadex.naturaldateinput.parsers.ParserTest;

public class TwentyFourHourTimeParserTest extends ParserTest {
    @Override
    @BeforeEach
    public void setup() {
        parser = new TwentyFourHourTimeParser();
        reference = LocalDateTime.of(2025, 6, 18, 0, 0, 0);
    }

    @Test
    void parse_HourMinuteSecond_ReturnsCorrectTime() {
        assertParsesAs("12:34:56", LocalTime.of(12, 34, 56));
    }

    @Test
    void parse_HourMinute_ReturnsCorrectTimeWith0Seconds() {
        assertParsesAs("12:34", LocalTime.of(12, 34));
    }

    @Test
    void parse_ZeroPaddedNumbers_ReturnsCorrectTime() {
        assertParsesAs("01:01:01", LocalTime.of(1, 1, 1));
    }

    @Test
    void parse_SingleDigitHour_ReturnsCorrectTime() {
        assertParsesAs("9:30:00", LocalTime.of(9, 30, 0));
    }

    @Test
    void parse_OutOfRangeHour_ReturnsNoResults() {
        assertDoesNotParse("25:10:10");
    }

    @Test
    void parse_OutOfRangeMinute_ReturnsNoResults() {
        assertDoesNotParse("10:61:10");
    }

    @Test
    void parse_OutOfRangeSecond_ReturnsNoResults() {
        assertDoesNotParse("10:10:61");
    }

    @Test
    void parse_InvalidCharactersBeforeTime_DoesNotParse() {
        assertDoesNotParse("invalid9:30");
    }

    @Test
    void parse_InvalidCharactersAfterTime_DoesNotParse() {
        assertDoesNotParse("9:30invalid");
    }
}
