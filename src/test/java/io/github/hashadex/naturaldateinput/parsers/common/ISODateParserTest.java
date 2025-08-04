package io.github.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.hashadex.naturaldateinput.parsers.ParserTest;

public class ISODateParserTest extends ParserTest {
    @Override
    @BeforeEach
    public void setup() {
        parser = new ISODateParser();

        reference = LocalDateTime.of(2025, 7, 4, 0, 0, 0);
    }

    @Test
    void parse_DashDelimeter_Parses() {
        assertParsesAs("2025-07-10", LocalDate.of(2025, 7, 10));
    }

    @Test
    void parse_DotDelimeter_Parses() {
        assertParsesAs("2025.07.10", LocalDate.of(2025, 7, 10));
    }

    @Test
    void parse_NoDelimeter_Parses() {
        assertParsesAs("20250710", LocalDate.of(2025, 7, 10));
    }

    @Test
    void parse_InvalidDelimeter_DoesNotParse() {
        assertDoesNotParse("2025?07?10");
    }

    @Test
    void parse_MixedDelimeters_DoesNotParse() {
        assertDoesNotParse("2025-07.10");
    }

    @Test
    void parse_YearMonthDay_ReturnsCorrectDate() {
        assertParsesAs("2025-07-10", LocalDate.of(2025, 7, 10));
    }

    @Test
    void parse_YearMonth_ReturnsDateWithDay1() {
        assertParsesAs("2025-07", LocalDate.of(2025, 7, 1));
    }

    @Test
    void parse_Year_ReturnsDateWithMonth1Day1() {
        assertParsesAs("2025", LocalDate.of(2025, 1, 1));
    }

    @Test
    void parse_Non4DigitYear_DoesNotParse() {
        assertDoesNotParse("99999-07-10");
    }

    @Test
    void parse_OutOfRangeMonth_DoesNotParse() {
        assertDoesNotParse("2025-13-10");
    }

    @Test
    void parse_OutOfRangeDay_DoesNotParse() {
        assertDoesNotParse("2025-07-32");
    }

    @Test
    void parse_February29OnNonLeapYear_DoesNotParse() {
        assertDoesNotParse("2025-02-29");
    }

    @Test
    void parse_February29OnLeapYear_ReturnsCorrectDate() {
        assertParsesAs("2024-02-29", LocalDate.of(2024, 2, 29));
    }

    @Test
    void parse_InvalidCharactersBeforeDate_DoesNotParse() {
        assertDoesNotParse("invalid2025-07-04");
    }

    @Test
    void parse_InvalidCharactersAfterDate_DoesNotParse() {
        assertDoesNotParse("2025-07-04invalid");
    }
}
