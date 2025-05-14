package me.hashadex.naturaldateinput.parsers.en;

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
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.constants.ENConstants;
import me.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;
import me.hashadex.naturaldateinput.parsers.ParserTest;

public class ENMonthDayYearParserTest extends ParserTest{
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new ENMonthDayYearParser();

        reference = LocalDateTime.of(2025, 5, 14, 0, 0, 0);
        referenceDate = reference.toLocalDate();
    }

    public static Stream<Arguments> provideMonths() {
        return ENConstants.monthMap.entrySet().stream().map(
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
        assertParses("AUGUST");
    }

    @Test
    void parse_MonthDay_ReturnsNextSpecifiedMonthDay() {
        assertAll(
            () -> assertParsesAs("january 1", LocalDate.of(2026, 1, 1)), // Before reference
            () -> assertParsesAs("may 14", referenceDate), // Reference
            () -> assertParsesAs("december 31", LocalDate.of(2025, 12, 31)) // After reference
        );
    }

    @Test
    void parse_MonthDayWithInvalidDay_ShiftsEndIndexAndIgnoresDay() {
        ParsedComponent result = parser.parse("august 32", reference).findAny().get();

        assertAll(
            () -> assertEquals(result.getStartDate().get().getDayOfMonth(), 1),
            () -> assertEquals(result.getEndIndex(), 6)
        );
    }

    @Test
    void parse_MonthYear_ReturnsFirstDayOfSpecifiedMonthYear() {
        assertAll(
            () -> assertParsesAs("february 2025", LocalDate.of(2025, 2, 1)),
            () -> assertParsesAs("november 2030", LocalDate.of(2030, 11, 1))
        );
    }

    @Test
    void parse_MonthDayYear_ReturnsSpecifiedDate() {
        assertAll(
            () -> assertParsesAs("january 14 2025", LocalDate.of(2025, 1, 14)),
            () -> assertParsesAs("june 7 2030", LocalDate.of(2030, 6, 7))
        );
    }

    @Test
    void parse_MonthDayYearWithInvalidDay_ShiftsEndIndexAndIgnoresDayAndYear() {
        ParsedComponent result = parser.parse("march 32 2024", reference).findAny().get();

        assertAll(
            () -> assertEquals(result.getStartDate().get().getYear(), 2026),
            () -> assertEquals(result.getStartDate().get().getDayOfMonth(), 1),
            () -> assertEquals(result.getEndIndex(), 5)
        );
    }

    @Test
    void parse_OrdinalIndicatorCommaOf_MatchIncludesOrdinalIndicatorCommaOf() {
        String testString = "may 14th, 2025";

        ParsedComponent result = parser.parse(testString).findAny().get();

        assertEquals(testString.length(), result.getLength());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid",
        "invalidaugust",
        "augustinvalid",
        "invalidaugust 10",
        "10",
        "2025"        
    })
    void parse_InvalidInput_ReturnsNoResults(String input) {
        assertDoesNotParse(input);
    }
}
