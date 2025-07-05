package me.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import me.hashadex.naturaldateinput.parsers.ParserTest;
import me.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser.DayMonthOrder;

public class SlashDateFormatParserTest extends ParserTest {
    @Override
    @BeforeEach
    public void setup() {
        reference = LocalDateTime.of(2025, 7, 5, 12, 0, 0);
    }

    void setupParser(DayMonthOrder order) {
        this.parser = new SlashDateFormatParser(order);
    }

    @ParameterizedTest
    @EnumSource
    void parse_DotDelimeter_Parses(DayMonthOrder order) {
        setupParser(order);

        assertParses("31.12.2030");
    }

    @ParameterizedTest
    @EnumSource
    void parse_SlashDelimeter_Parser(DayMonthOrder order) {
        setupParser(order);

        assertParses("12/31/2030");
    }

    @ParameterizedTest
    @EnumSource
    void parse_InvalidDelimeter_DoesNotParse(DayMonthOrder order) {
        setupParser(order);

        assertDoesNotParse("12?31?2030");
    }

    @ParameterizedTest
    @EnumSource
    void parse_MixedDelimeters_DoesNotParse(DayMonthOrder order) {
        setupParser(order);

        assertDoesNotParse("12/31.2030");
    }

    @ParameterizedTest
    @EnumSource
    void parse_SingleDigitDayMonth_Parses(DayMonthOrder order) {
        setupParser(order);

        assertParsesAs("1.1.2030", LocalDate.of(2030, 1, 1));
    }

    @ParameterizedTest
    @EnumSource
    void parse_SingleDigitDayMonthZeroPadded_Parses(DayMonthOrder order) {
        setupParser(order);

        assertParsesAs("01.01.2030", LocalDate.of(2030, 1, 1));
    }

    @ParameterizedTest
    @EnumSource
    void parse_TwoDigitYear_ReturnsCorrectDateWith21CenturyYear(DayMonthOrder order) {
        setupParser(order);

        assertParsesAs("10.10.30", LocalDate.of(2030, 10, 10));
    }

    @ParameterizedTest
    @EnumSource
    void parse_UnambiguousDayMonthYear_ReturnsCorrectDate(DayMonthOrder order) {
        setupParser(order);

        assertParsesAs("31.12.2030", LocalDate.of(2030, 12, 31));
    }

    @ParameterizedTest
    @EnumSource
    void parse_UnambiguousMonthDayYear_ReturnsCorrectDate(DayMonthOrder order) {
        setupParser(order);

        assertParsesAs("12.31.2030", LocalDate.of(2030, 12, 31));
    }

    @ParameterizedTest
    @EnumSource
    void parse_AmbiguousDateWithYear_ReturnsCorrectDate(DayMonthOrder order) {
        setupParser(order);

        LocalDate expectedDate;
        if (order == DayMonthOrder.DAY_MONTH) {
            expectedDate = LocalDate.of(2030, 2, 1);
        } else {
            expectedDate = LocalDate.of(2030, 1, 2);
        }

        assertParsesAs("01.02.2030", expectedDate);
    }

    @ParameterizedTest
    @EnumSource
    void parse_UnambiguousDateWithoutYearBeforeReference_ReturnsCorrectDateInNextYear(DayMonthOrder order) {
        setupParser(order);

        assertParsesAs("1/31", LocalDate.of(2026, 1, 31));
    }

    @ParameterizedTest
    @EnumSource
    void parse_UnambiguousDateWithoutYearAfterReference_ReturnsCorrectDateInReferenceYear(DayMonthOrder order) {
        setupParser(order);

        assertParsesAs("8/20", LocalDate.of(2025, 8, 20));
    }

    @ParameterizedTest
    @EnumSource
    void parse_Non2Or4DigitYear_DoesNotParse(DayMonthOrder order) {
        setupParser(order);

        assertDoesNotParse("31.12.999");
    }

    @ParameterizedTest
    @EnumSource
    void parse_OutOfRangeDay_DoesNotParse(DayMonthOrder order) {
        setupParser(order);

        assertDoesNotParse("32.12.2025");
    }

    @ParameterizedTest
    @EnumSource
    void parse_OutOfRangeMonth_DoesNotParse(DayMonthOrder order) {
        setupParser(order);

        assertDoesNotParse("31.13.2025");
    }

    @ParameterizedTest
    @EnumSource
    void parse_February29OnNonLeapYear_DoesNotParse(DayMonthOrder order) {
        setupParser(order);

        assertDoesNotParse("29.02.2025");
    }

    @ParameterizedTest
    @EnumSource
    void parse_February29OnLeapYear_ReturnsCorrectDate(DayMonthOrder order) {
        setupParser(order);

        assertParsesAs("29.02.2024", LocalDate.of(2024, 2, 29));
    }

    @ParameterizedTest
    @EnumSource
    void parse_InvalidCharactersBeforeDate_DoesNotParse(DayMonthOrder order) {
        setupParser(order);

        assertDoesNotParse("invalid31.12.2025");
    }

    @ParameterizedTest
    @EnumSource
    void parse_InvalidCharactersAfterDate_DoesNotParse(DayMonthOrder order) {
        setupParser(order);

        assertDoesNotParse("31.12.2025invalid");
    }
}
