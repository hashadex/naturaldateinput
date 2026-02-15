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

public class RUMonthDayYearParserTest extends ParserTest{
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new RUMonthDayYearParser();

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
    void parse_MonthYear_ReturnsFirstDayOfSpecifiedMonthYear() {
        assertAll(
            () -> assertParsesAs("февраль 2025", LocalDate.of(2025, 2, 1)),
            () -> assertParsesAs("ноябрь 2030", LocalDate.of(2030, 11, 1))
        );
    }

    @Test
    void parse_MonthDayYear_ReturnsSpecifiedDate() {
        assertAll(
            () -> assertParsesAs("январь 14 2025", LocalDate.of(2025, 1, 14)),
            () -> assertParsesAs("июнь 7 2030", LocalDate.of(2030, 6, 7))
        );
    }

    @Test
    void parse_MonthDayYearWithInvalidDay_ShiftsEndIndexAndIgnoresDayAndYear() {
        ParsedComponent result = parser.parse("март 32 2024", reference).findAny().get();

        assertAll(
            () -> assertEquals(result.date().get().getYear(), 2026),
            () -> assertEquals(result.date().get().getDayOfMonth(), 1),
            () -> assertEquals(result.endIndex(), 4)
        );
    }

    @Test
    void parse_OrdinalIndicatorComma_MatchIncludesOrdinalIndicatorComma() {
        String testString = "мая 14-е, 2025";

        ParsedComponent result = parser.parse(testString).findAny().get();

        assertEquals(testString.length(), result.length());
    }

    @Test
    void parse_InvalidMonth_ReturnsNoResults() {
        assertDoesNotParse("invalid 10");
    }

    @Test
    void parse_InvalidCharactersBeforeDate_ReturnsNoResults() {
        assertDoesNotParse("invalidмарт 10");
    }

    @Test
    void parse_InvalidCharactersAfterDay_ShiftsEndIndexAndIgnoresDay() {
        String testString = "декабрь 10invalid";

        ParsedComponent component = parser.parse(testString, reference).findAny().get();

        assertAll(
            () -> assertEquals(7, component.endIndex()),
            () -> assertEquals(LocalDate.of(2025, 12, 1), component.date().get())
        );
    }
}
