package me.hashadex.naturaldateinput.parsers.en;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.parsers.ParserTest;

public class ENTwelveHourTimeParserTest extends ParserTest {
    @Override
    @BeforeEach
    public void setup() {
        parser = new ENTwelveHourTimeParser();

        reference = LocalDateTime.of(2025, 6, 23, 12, 0, 0);
    }

    @Test
    void parse_HourMinuteSecond_ReturnsCorrectTime() {
        assertParsesAs("6:30:55 am", LocalTime.of(6, 30, 55));
    }

    @Test
    void parse_HourMinute_ReturnsCorrectTimeWith0Seconds() {
        assertParsesAs("6:30 am", LocalTime.of(6, 30, 0));
    }

    @Test
    void parse_Hour_ReturnsCorrectTimeWith0Minutes0Seconds() {
        assertParsesAs("6 am", LocalTime.of(6, 0));
    }

    @Test
    void parse_Before12AM_ReturnsSameAs24H() {
        assertParsesAs("6:30 am", LocalTime.of(6, 30));
    }

    @Test
    void parse_12AM_ReturnsTimeWithHour0() {
        assertParsesAs("12:30 am", LocalTime.of(0, 30));
    }

    @Test
    void parse_Before12PM_ReturnsTimeWithHourPlusTwelve() {
        assertParsesAs("6:30 pm", LocalTime.of(18, 30));
    }

    @Test
    void parse_12PM_ReturnsSameAs24H() {
        assertParsesAs("12:30 pm", LocalTime.of(12, 30));
    }

    @Test
    void parse_OutOfRangeHour_ReturnsNoResults() {
        assertAll(
            () -> assertDoesNotParse("13:30 am"),
            () -> assertDoesNotParse("13:30 pm")
        );
    }

    @Test
    void parse_UppercaseAMPM_Parses() {
        assertAll(
            () -> assertParses("12:00 AM"),
            () -> assertParses("12:00 PM")
        );
    }

    @Test
    void parse_AMWithPeriod_Parses() {
        assertParses("12:00 a.m.");
    }

    @Test
    void parse_PMWithPeriod_Parses() {
        assertParses("12:00 p.m.");
    }

    @Test
    void parse_BothAMPM_ReturnsNoResults() {
        assertDoesNotParse("12:00 AMPM");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid",
        "12:00 AMinvalid",
        "invalid12:00 AM",
        "invalid12:00 AMinvalid",
        "12:99 AM",
        "12:00:99 AM",
        "12:00 AA",
        "59:59:59 AM"
    })
    void parse_InvalidInputs_ReturnsNoResults(String input) {
        assertDoesNotParse(input);
    }
}
