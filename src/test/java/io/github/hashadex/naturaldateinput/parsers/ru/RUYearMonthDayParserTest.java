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

public class RUYearMonthDayParserTest extends ParserTest {
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new RUYearMonthDayParser();

        reference = LocalDateTime.of(2025, 5, 14, 0, 0, 0);
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
    void parse_YearMonth_ReturnsFirstDayOfSpecifiedYearMonth() {
        assertAll(
            () -> assertParsesAs("2025 февраль", LocalDate.of(2025, 2, 1)),
            () -> assertParsesAs("2030 ноябрь", LocalDate.of(2030, 11, 1))
        );
    }

    @Test
    void parse_MonthDay_ReturnsNextSpecifiedMonthDay() {
        assertAll(
            () -> assertParsesAs("январь 1", LocalDate.of(2026, 1, 1)), // Before reference
            () -> assertParsesAs("май 14", referenceDate), // Reference
            () -> assertParsesAs("декабрь 31", LocalDate.of(2025, 12, 31)) // After reference
        );
    }

    @Test
    void parse_MonthDayWithInvalidDay_ShiftsEndIndexAndIgnoresDay() {
        ParsedComponent result = parser.parse("август 32", reference).findAny().get();

        assertAll(
            () -> assertEquals(result.date().get().getDayOfMonth(), 1),
            () -> assertEquals(result.endIndex(), 6)
        );
    }

    @Test
    void parse_YearMonthDay_ReturnsSpecifiedDate() {
        assertAll(
            () -> assertParsesAs("2025 март 10", LocalDate.of(2025, 3, 10)),
            () -> assertParsesAs("2027 октябрь 27", LocalDate.of(2027, 10, 27))
        );
    }

    @Test
    void parse_OrdinalIndicatorsYearWord_MatchIncludesOrdinalIndicatorsYearWord() {
        String testString = "2025-го года августа 10-е";

        ParsedComponent result = parser.parse(testString, reference).findAny().get();

        assertEquals(testString.length(), result.length());
    }

    @Test
    void parse_InvalidMonth_ReturnsNoResults() {
        assertDoesNotParse("2025 invalid 10");
    }

    @Test
    void parse_InvalidCharactersBeforeYear_ShiftsStartIndexAndIgnoresYear() {
        String testString = "invalid2026 декабрь 10";

        ParsedComponent component = parser.parse(testString, reference).findAny().get();

        assertAll(
            () -> assertEquals(12, component.startIndex()),
            () -> assertEquals(LocalDate.of(2025, 12, 10), component.date().get())
        );
    }

    @Test
    void parse_InvalidCharactersAfterDay_ShiftsEndIndexAndIgnoresDay() {
        String testString = "2026 декабрь 10invalid";

        ParsedComponent component = parser.parse(testString, reference).findAny().get();

        assertAll(
            () -> assertEquals(12, component.endIndex()),
            () -> assertEquals(LocalDate.of(2026, 12, 1), component.date().get())
        );
    }
}
