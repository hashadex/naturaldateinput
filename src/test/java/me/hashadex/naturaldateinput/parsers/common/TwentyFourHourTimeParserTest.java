package me.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.parsers.ParserTest;

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

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid",
        "invalid9:30",
        "9:30invalid",
        "invalid9:30invalid",
        "99:10:10",
        "10:99:10",
        "10:10:99",
        "59:59:59"
    })
    void parse_InvalidInputs_ReturnsNoResults(String input) {
        assertDoesNotParse(input);
    }
}
