package me.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.parsers.DateParserTest;
import me.hashadex.naturaldateinput.parsers.common.FormalFormatDateParser.DateFormat;

public class FormalFormatDateParserTest extends DateParserTest {
    @Override
    @BeforeEach
    public void setup() {
        parser = new FormalFormatDateParser(DateFormat.DAY_MONTH);
        reference = LocalDateTime.of(2025, 2, 20, 0, 0, 0);
    }

    public static Stream<LocalDate> provideUnambiguousDates() {
        return Stream.of(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 1, 13),
            LocalDate.of(2025, 10, 10),
            LocalDate.of(2025, 12, 31)
        );
    }

    public static Stream<LocalDate> provideAmbiguousDates() {
        return Stream.of(
            LocalDate.of(2026, 1, 12),
            LocalDate.of(2026, 1, 8),
            LocalDate.of(2025, 12, 10),
            LocalDate.of(2025, 12, 1)
        );
    }
    
    @ParameterizedTest
    @MethodSource({"provideUnambiguousDates", "provideAmbiguousDates"})
    void parse_YearMonthDay_ReturnsValidDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

        assertParsedDateEquals(date, formatter.format(date));
    }

    @ParameterizedTest
    @MethodSource({"provideUnambiguousDates", "provideAmbiguousDates"})
    void parse_DayMonthYear_ReturnsValidDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");

        assertParsedDateEquals(date, formatter.format(date));
    }

    @ParameterizedTest
    @MethodSource({"provideUnambiguousDates", "provideAmbiguousDates"})
    void parse_DayMonthYearWithDoubleDigitYear_ReturnsValidDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yy");

        assertParsedDateEquals(date, formatter.format(date));
    }

    @ParameterizedTest
    @MethodSource({"provideUnambiguousDates", "provideAmbiguousDates"})
    void parse_MonthDayYear_ReturnsValidDate(LocalDate date) {
        FormalFormatDateParser monthDayParser = new FormalFormatDateParser(DateFormat.MONTH_DAY);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M.d.yyyy");

        assertParsedDateEquals(date, formatter.format(date), monthDayParser);
    }

    @ParameterizedTest
    @MethodSource({"provideUnambiguousDates", "provideAmbiguousDates"})
    void parse_MonthDayYearWithDoubleDigitYear_ReturnsValidDate(LocalDate date) {
        FormalFormatDateParser monthDayParser = new FormalFormatDateParser(DateFormat.MONTH_DAY);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M.d.yy");

        assertParsedDateEquals(date, formatter.format(date), monthDayParser);
    }

    @ParameterizedTest
    @MethodSource({"provideUnambiguousDates", "provideAmbiguousDates"})
    void parse_DayMonth_ReturnsValidDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M");

        assertParsedDateEquals(date, formatter.format(date));
    }

    @ParameterizedTest
    @MethodSource({"provideUnambiguousDates", "provideAmbiguousDates"})
    void parse_MonthDay_ReturnsValidDate(LocalDate date) {
        FormalFormatDateParser monthDayParser = new FormalFormatDateParser(DateFormat.MONTH_DAY);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");

        assertParsedDateEquals(date, formatter.format(date), monthDayParser);
    }

    public static Stream<Arguments> provideDatesWithInvalidDays() {
        return Stream.of(
            Arguments.of("31/02/2025", LocalDate.of(2025, 3, 3)),
            Arguments.of("31/04/2025", LocalDate.of(2025, 5, 1)),
            Arguments.of("31/06/2025", LocalDate.of(2025, 7, 1)),
            Arguments.of("31/09/2025", LocalDate.of(2025, 10, 1)),
            Arguments.of("31/11/2025", LocalDate.of(2025, 12, 1))
        );
    }

    @ParameterizedTest
    @MethodSource("provideDatesWithInvalidDays")
    void parse_InvalidDay_ConvertsAndReturnsValidDate(String input, LocalDate expectedResult) {
        assertParsedDateEquals(expectedResult, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid",
        "invalid20.02.2025",
        "20.02.2025invalid",
        "invalid20.02.2025invalid",
        "9999-99-99",
        "2025-31-31",
        "99.99.9999",
        "31.31.2025",
        "31.31.99",
        "99/99",
        "31/31",
        "1/-1",
        "10.10.10.10",
        "Between 10-12",
        "Windows 7/10"
    })
    void parse_InvalidInputs_ReturnsEmptyArrayList(String input) {
        assertDoesNotParse(input);
    }
}
