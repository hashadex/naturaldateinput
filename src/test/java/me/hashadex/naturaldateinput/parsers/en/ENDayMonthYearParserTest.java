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
import me.hashadex.naturaldateinput.parsers.ParserTest;
import me.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;

public class ENDayMonthYearParserTest extends ParserTest {
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new ENDayMonthYearParser();
        reference = LocalDateTime.of(2025, 5, 11, 0, 0, 0);

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
    void parse_DayMonth_ReturnsNextSpecifiedDayMonth() {
        assertAll(
            () -> assertParsesAs("1 january", LocalDate.of(2026, 1, 1)), // Before reference
            () -> assertParsesAs("11 may", referenceDate), // Reference
            () -> assertParsesAs("31 december", LocalDate.of(2025, 12, 31)) // After reference
        );
    }

    @Test
    void parse_DayMonthWithInvalidDay_ShiftsStartIndexAndIgnoresDay() {
        ParsedComponent result = parser.parse("32 august", reference).findAny().get();

        assertAll(
            () -> assertEquals(result.getStartDate().get().getDayOfMonth(), 1),
            () -> assertEquals(result.getStartIndex(), 3)
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
    void parse_DayMonthYear_ReturnsSpecifiedDate() {
        assertAll(
            () -> assertParsesAs("10 march 2025", LocalDate.of(2025, 3, 10)),
            () -> assertParsesAs("27 october 2027", LocalDate.of(2027, 10, 27))
        );
    }

    @Test
    void parse_OrdinalIndicatorAndOf_MatchIncludesOrdinalIndicatorAndOf() {
        String testString = "30th of march of 2025";

        ParsedComponent result = parser.parse("30th of march of 2025", reference).findAny().get();

        assertEquals(testString.length(), result.getLength());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid",
        "invalidaugust",
        "invalid10 augustinvalid",
        "10",
        "2025"
    })
    void parse_InvalidInput_ReturnsNoResults(String input) {
        assertDoesNotParse(input);
    }
}
