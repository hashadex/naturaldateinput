package me.hashadex.naturaldateinput.parsers.en;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.parsers.DateParserTest;

public class ENMonthDateParserTest extends DateParserTest {
    @Override
    @BeforeEach
    public void setup() {
        parser = new ENMonthDateParser();
        reference = LocalDateTime.of(2025, 6, 15, 0, 0, 0);
    }

    private static String getMonthFullDisplayNameFromDate(LocalDate date) {
        return date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    public static Stream<LocalDate> provideDatesBeforeReferenceMonth() {
        return Stream.of(
            // January
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 1, 15),
            // February
            LocalDate.of(2026, 2, 1),
            LocalDate.of(2026, 2, 15),
            // March
            LocalDate.of(2026, 3, 1),
            LocalDate.of(2026, 3, 15),
            // April
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 15),
            // May
            LocalDate.of(2026, 5, 1),
            LocalDate.of(2026, 5, 15)
        );
    }

    public static Stream<LocalDate> provideDatesInReferenceMonth() {
        return Stream.of(
            // June
            LocalDate.of(2026, 6, 1),
            LocalDate.of(2026, 6, 14),
            LocalDate.of(2025, 6, 15),
            LocalDate.of(2025, 6, 16)
        );
    }

    public static Stream<LocalDate> provideDatesAfterReferenceMonth() {
        return Stream.of(
            // July
            LocalDate.of(2025, 7, 1),
            LocalDate.of(2025, 7, 15),
            // August
            LocalDate.of(2025, 8, 1),
            LocalDate.of(2025, 8, 15),
            // September
            LocalDate.of(2025, 9, 1),
            LocalDate.of(2025, 9, 15),
            // October
            LocalDate.of(2025, 10, 1),
            LocalDate.of(2025, 10, 15),
            // November
            LocalDate.of(2025, 11, 1),
            LocalDate.of(2025, 11, 15),
            // December
            LocalDate.of(2025, 12, 1),
            LocalDate.of(2025, 12, 15)
        );
    }
    
    @ParameterizedTest
    @MethodSource({"provideDatesBeforeReferenceMonth", "provideDatesAfterReferenceMonth"})
    void parse_MonthBeforeAndAfterReferenceMonthAllStyles_ReturnsValidDate(LocalDate date) {
        String fullMonth = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String shortMonth = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        assertAll(
            () -> assertParsedDateEquals(date.withDayOfMonth(1), fullMonth),
            () -> assertParsedDateEquals(date.withDayOfMonth(1), shortMonth)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDatesInReferenceMonth")
    void parse_MonthInReferenceMonth_ReturnsValidDate(LocalDate date) {
        assertParsedDateEquals(
            date.withDayOfMonth(1).withYear(2026),
            getMonthFullDisplayNameFromDate(date)
        );
    }

    @ParameterizedTest
    @MethodSource({"provideDatesBeforeReferenceMonth", "provideDatesInReferenceMonth", "provideDatesAfterReferenceMonth"})
    void parse_MonthDay_ReturnsValidDate(LocalDate date) {
        assertParsedDateEquals(date, String.format("%s %s", getMonthFullDisplayNameFromDate(date), date.getDayOfMonth()));
    }

    @ParameterizedTest
    @MethodSource({"provideDatesBeforeReferenceMonth", "provideDatesInReferenceMonth", "provideDatesAfterReferenceMonth"})
    void parse_MonthYear_ReturnsValidDate(LocalDate date) {
        assertParsedDateEquals(date.withDayOfMonth(1), String.format("%s %s", getMonthFullDisplayNameFromDate(date), date.getYear()));
    }

    @ParameterizedTest
    @MethodSource({"provideDatesBeforeReferenceMonth", "provideDatesInReferenceMonth", "provideDatesAfterReferenceMonth"})
    void parse_MonthDayYearWithAllOrdinalIndicators_ReturnsValidDate(LocalDate date) {
        String month = getMonthFullDisplayNameFromDate(date);
        int day = date.getDayOfMonth();
        int year = date.getYear();

        String noIndicatorDate = "%s %s, %s".formatted(month, day, year);
        String thIndicatorDate = "%s %sth, %s".formatted(month, day, year);
        String stIndicatorDate = "%s %sst, %s".formatted(month, day, year);
        String ndIndicatorDate = "%s %snd, %s".formatted(month, day, year);
        String rdIndicatorDate = "%s %srd, %s".formatted(month, day, year);

        assertAll(
            () -> assertParsedDateEquals(date, noIndicatorDate),
            () -> assertParsedDateEquals(date, thIndicatorDate),
            () -> assertParsedDateEquals(date, stIndicatorDate),
            () -> assertParsedDateEquals(date, ndIndicatorDate),
            () -> assertParsedDateEquals(date, rdIndicatorDate)
        );
    }

    @ParameterizedTest
    @MethodSource({"provideDatesBeforeReferenceMonth", "provideDatesInReferenceMonth", "provideDatesAfterReferenceMonth"})
    void parse_DayMonth_ReturnsValidDate(LocalDate date) {
        assertParsedDateEquals(date, String.format("%s %s", date.getDayOfMonth(), getMonthFullDisplayNameFromDate(date)));
    }

    @ParameterizedTest
    @MethodSource({"provideDatesBeforeReferenceMonth", "provideDatesInReferenceMonth", "provideDatesAfterReferenceMonth"})
    void parse_DayMonthYear_ReturnsValidDate(LocalDate date) {
        assertParsedDateEquals(
            date, String.format("%s %s %s", date.getDayOfMonth(), getMonthFullDisplayNameFromDate(date), date.getYear())
        );
    }

    @ParameterizedTest
    @MethodSource({"provideDatesBeforeReferenceMonth", "provideDatesInReferenceMonth", "provideDatesAfterReferenceMonth"})
    void parse_YearMonth_ReturnsValidDate(LocalDate date) {
        assertParsedDateEquals(date.withDayOfMonth(1), String.format("%s %s", date.getYear(), getMonthFullDisplayNameFromDate(date)));
    }

    @ParameterizedTest
    @MethodSource({"provideDatesBeforeReferenceMonth", "provideDatesInReferenceMonth", "provideDatesAfterReferenceMonth"})
    void parse_YearMonthDay_ReturnsValidDate(LocalDate date) {
        assertParsedDateEquals(
            date, String.format("%s %s %s", date.getYear(), getMonthFullDisplayNameFromDate(date), date.getDayOfMonth())
        );
    }

    public static Stream<Arguments> provideDatesWithInvalidDays() {
        return Stream.of(
            Arguments.of("31 February 2025", LocalDate.of(2025, 3, 3)),
            Arguments.of("31 April 2025", LocalDate.of(2025, 5, 1)),
            Arguments.of("31 June 2025", LocalDate.of(2025, 7, 1)),
            Arguments.of("31 September 2025", LocalDate.of(2025, 10, 1)),
            Arguments.of("31 November 2025", LocalDate.of(2025, 12, 1))
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
        "invalidFebruary",
        "invalidFebruary 21",
        "invalidFebruary 2025",
        "invalidFebruary 21st, 2025",
        "Februaryinvalid",
        "February 99",
        "February 99th, 9999",
        "February 32nd"
    })
    void parse_InvalidInput_ReturnsEmptyArrayList(String input) {
        assertDoesNotParse(input);
    }
}