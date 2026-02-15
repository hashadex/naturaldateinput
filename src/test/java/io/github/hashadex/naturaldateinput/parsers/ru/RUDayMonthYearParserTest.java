package io.github.hashadex.naturaldateinput.parsers.ru;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.hashadex.naturaldateinput.parsers.ParserTest;
import io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;

public class RUDayMonthYearParserTest extends ParserTest {
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new RUDayMonthYearParser();
        reference = LocalDateTime.of(2025, 5, 11, 0, 0, 0);

        referenceDate = reference.toLocalDate();
    }

    public static Stream<Arguments> provideMonths() {
        return RUConstants.monthMap.entrySet().stream().map(
            entry -> Arguments.of(entry.getKey(), entry.getValue())
        );
    }

    @ParameterizedTest
    @MethodSource("provideMonths")
    void parse_Month_ReturnsFirstDayOfNextSpecifiedMonth(String testMonthString, Month expectedMonth) {
        LocalDate expectedDate = referenceDate.with(expectedMonth).withDayOfMonth(1);
        if (expectedDate.isBefore(referenceDate)) {
            expectedDate = expectedDate.plusYears(1);
        }

        assertParsesAs(testMonthString, expectedDate);
    }

    @Test
    void parse_UppercaseMonth_Parses() {
        assertParses("АВГУСТ");
    }

    @Test
    void parse_DayMonth_ReturnsNextSpecifiedDayMonth() {
        assertAll(
            () -> assertParsesAs("1 января", LocalDate.of(2026, 1, 1)), // Before reference
            () -> assertParsesAs("11 мая", referenceDate), // Reference
            () -> assertParsesAs("31 декабря", LocalDate.of(2025, 12, 31)) // After reference
        );
    }

    @Test
    void parse_DayMonthWithInvalidDay_ShiftsStartIndexAndIgnoresDay() {
        ParsedComponent result = parser.parse("32 августа", reference).findAny().get();

        assertAll(
            () -> assertEquals(result.date().get().getDayOfMonth(), 1),
            () -> assertEquals(result.startIndex(), 3)
        );
    }

    @Test
    void parse_MonthYear_ReturnsFirstDayOfSpecifiedMonthYear() {
        assertAll(
            () -> assertParsesAs("февраль 2025", LocalDate.of(2025, 2, 1)),
            () -> assertParsesAs("ноябрь 2030", LocalDate.of(2030, 11, 1))
        );
    }

    @Test
    void parse_DayMonthYear_ReturnsSpecifiedDate() {
        assertAll(
            () -> assertParsesAs("10 марта 2025", LocalDate.of(2025, 3, 10)),
            () -> assertParsesAs("27 октября 2027", LocalDate.of(2027, 10, 27))
        );
    }

    @Test
    void parse_OrdinalIndicatorAndYearWord_MatchIncludesOrdinalIndicatorAndYearWord() {
        String testString = "30-е марта 2025-го года";

        ParsedComponent result = parser.parse(testString, reference).findAny().get();

        assertEquals(testString.length(), result.length());
    }

    @Test
    void parse_InvalidMonth_ReturnsNoResults() {
        assertDoesNotParse("10-е invalid");
    }

    @Test
    void parse_InvalidCharactersBeforeDay_ShiftsStartIndexAndIgnoresDay() {
        String testString = "invalid10 декабря 2026";

        ParsedComponent component = parser.parse(testString, reference).findAny().get();

        assertAll(
            () -> assertEquals(10, component.startIndex()),
            () -> assertEquals(LocalDate.of(2026, 12, 1), component.date().get())
        );
    }

    @Test
    void parse_InvalidCharactersAfterYear_ShiftsEndIndexAndIgnoresYear() {
        String testString = "10 декабря 2026invalid";

        ParsedComponent component = parser.parse(testString, reference).findAny().get();

        assertAll(
            () -> assertEquals(10, component.endIndex()),
            () -> assertEquals(LocalDate.of(2025, 12, 10), component.date().get())
        );
    }
}
