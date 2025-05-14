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

public class ENYearMonthDayParserTest extends ParserTest {
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new ENYearMonthDayParser();

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
    void parse_YearMonth_ReturnsFirstDayOfSpecifiedYearMonth() {
        assertAll(
            () -> assertParsesAs("2025 february", LocalDate.of(2025, 2, 1)),
            () -> assertParsesAs("2030 november", LocalDate.of(2030, 11, 1))
        );
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
    void parse_YearMonthDay_ReturnsSpecifiedDate() {
        assertAll(
            () -> assertParsesAs("2025 march 10", LocalDate.of(2025, 3, 10)),
            () -> assertParsesAs("2027 october 27", LocalDate.of(2027, 10, 27))
        );
    }

    @Test
    void parse_OrdinalIndicator_MatchIncludesOrdinalIndicator() {
        String testString = "august 10th";

        ParsedComponent result = parser.parse(testString, reference).findAny().get();

        assertEquals(testString.length(), result.getLength());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid",
        "invalidaugust",
        "augustinvalid"
    })
    void parse_InvalidInputs_ReturnsNoResults(String input) {
        assertDoesNotParse(input);
    }
}
