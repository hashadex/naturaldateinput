package me.hashadex.naturaldateinput.parsers.en;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import me.hashadex.naturaldateinput.parsers.ParserTest;
import me.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;

public class ENChronoUnitLaterParserTest extends ParserTest {
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new ENChronoUnitLaterParser();
        reference = LocalDateTime.of(2025, 5, 8, 12, 0, 0);

        referenceDate = reference.toLocalDate();
    }

    public static Stream<Arguments> provideAmounts() {
        return Stream.of(
            Arguments.of("0", 0),
            Arguments.of("1", 1),
            Arguments.of("2", 2),
            Arguments.of("10", 10),
            Arguments.of("365", 365),
            Arguments.of("four", 4),
            Arguments.of("twenty", 20)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Decades_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertParsesAs(testString + " decades", referenceDate.plus(expectedAmount, ChronoUnit.DECADES));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Years_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertParsesAs(testString + " years", referenceDate.plusYears(expectedAmount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Months_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertParsesAs(testString + " months", referenceDate.plusMonths(expectedAmount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Weeks_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertParsesAs(testString + " weeks", referenceDate.plusWeeks(expectedAmount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Days_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertParsesAs(testString + " days", referenceDate.plusDays(expectedAmount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Halfdays_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertAll(
            () -> assertParsesAs(testString + " half-days", reference.plus(expectedAmount, ChronoUnit.HALF_DAYS)),
            () -> assertParsesAs(testString + " halfdays", reference.plus(expectedAmount, ChronoUnit.HALF_DAYS))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Hours_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertParsesAs(testString + " hours", reference.plusHours(expectedAmount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Minutes_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertParsesAs(testString + " minutes", reference.plusMinutes(expectedAmount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Seconds_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertParsesAs(testString + " seconds", reference.plusSeconds(expectedAmount));
    }

    @Test
    void parse_OptionalInBeforeInput_IncludedInMatch() {
        String testString = "in 1 day";

        ParsedComponent component = parser.parse(testString, reference).findAny().get();

        assertEquals(testString.length(), component.length());
    }

    @Test
    void parse_OptionalAfterBeforeInput_IncludedInMatch() {
        String testString = "after 1 day";

        ParsedComponent component = parser.parse(testString, reference).findAny().get();

        assertEquals(testString.length(), component.length());
    }

    @Test
    void parse_OptionalLaterAfterInput_IncludedInMatch() {
        String testString = "1 day later";

        ParsedComponent component = parser.parse(testString, reference).findAny().get();

        assertEquals(testString.length(), component.length());
    }

    @Test
    void parse_OptionalAfterAfterInput_IncludedInMatch() {
        String testString = "1 day after";

        ParsedComponent component = parser.parse(testString, reference).findAny().get();

        assertEquals(testString.length(), component.length());
    }

    @Test
    void parse_UppercaseUnit_Parses() {
        assertParses("10 DAYS");
    }

    @Test
    void parse_OutOfIntRangeAmount_DoesNotThrowAnything() {
        assertDoesNotThrow(() -> parser.parse("2147483648 days", reference).toList());
    }

    @Test
    void parse_OutOfRangeDate_DoesNotThrowAnything() {
        assertDoesNotThrow(() -> parser.parse("2147483647 decades", reference).toList());
    }

    @Test
    void parse_NegativeAmount_ReturnsNoResults() {
        assertDoesNotParse("-1 days");
    }

    @Test
    void parse_InvalidChronoUnit_ReturnsNoResults() {
        assertDoesNotParse("1 invalid");
    }

    @Test
    void parse_InvalidCharactersBeforeInput_ReturnsNoResults() {
        assertDoesNotParse("invalid1 day");
    }

    @Test
    void parse_InvalidCharactersAfterInput_ReturnsNoResults() {
        assertDoesNotParse("1 dayinvalid");
    }
}
