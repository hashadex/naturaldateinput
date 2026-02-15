package io.github.hashadex.naturaldateinput.parsers.ru;

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

import io.github.hashadex.naturaldateinput.parsers.ParserTest;
import io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;

public class RUChronoUnitLaterParserTest extends ParserTest {
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new RUChronoUnitLaterParser();
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
            Arguments.of("одно", 1),
            Arguments.of("один", 1),
            Arguments.of("одна", 1),
            Arguments.of("две", 2),
            Arguments.of("два", 2),
            Arguments.of("двадцать", 20)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Decades_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertAll(
            () -> assertParsesAs(
                testString + " десятилетий", referenceDate.plus(expectedAmount, ChronoUnit.DECADES)
            ),
            () -> assertParsesAs(
                testString + " десятилетие", referenceDate.plus(expectedAmount, ChronoUnit.DECADES)
            ),
            () -> assertParsesAs(
                testString + " десятилетия", referenceDate.plus(expectedAmount, ChronoUnit.DECADES)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Years_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertAll(
            () -> assertParsesAs(testString + " лет", referenceDate.plusYears(expectedAmount)),
            () -> assertParsesAs(testString + " год", referenceDate.plusYears(expectedAmount)),
            () -> assertParsesAs(testString + " года", referenceDate.plusYears(expectedAmount))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Months_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertAll(
            () -> assertParsesAs(testString + " месяцев", referenceDate.plusMonths(expectedAmount)),
            () -> assertParsesAs(testString + " месяц", referenceDate.plusMonths(expectedAmount)),
            () -> assertParsesAs(testString + " месяца", referenceDate.plusMonths(expectedAmount))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Weeks_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertAll(
            () -> assertParsesAs(testString + " недель", referenceDate.plusWeeks(expectedAmount)),
            () -> assertParsesAs(testString + " неделя", referenceDate.plusWeeks(expectedAmount)),
            () -> assertParsesAs(testString + " недели", referenceDate.plusWeeks(expectedAmount))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Days_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertAll(
            () -> assertParsesAs(testString + " дней", referenceDate.plusDays(expectedAmount)),
            () -> assertParsesAs(testString + " день", referenceDate.plusDays(expectedAmount)),
            () -> assertParsesAs(testString + " дня", referenceDate.plusDays(expectedAmount))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Hours_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertAll(
            () -> assertParsesAs(testString + " часов", reference.plusHours(expectedAmount)),
            () -> assertParsesAs(testString + " час", reference.plusHours(expectedAmount)),
            () -> assertParsesAs(testString + " часа", reference.plusHours(expectedAmount))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Minutes_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertAll(
            () -> assertParsesAs(testString + " минут", reference.plusMinutes(expectedAmount)),
            () -> assertParsesAs(testString + " минута", reference.plusMinutes(expectedAmount)),
            () -> assertParsesAs(testString + " минуты", reference.plusMinutes(expectedAmount))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Seconds_ReturnsCorrectDate(String testString, int expectedAmount) {
        assertAll(
            () -> assertParsesAs(testString + " секунд", reference.plusSeconds(expectedAmount)),
            () -> assertParsesAs(testString + " секунда", reference.plusSeconds(expectedAmount)),
            () -> assertParsesAs(testString + " секунды", reference.plusSeconds(expectedAmount))
        );
    }

    @Test
    void parse_OptionalInBeforeInput_IncludedInMatch() {
        String testString = "через 1 день";

        ParsedComponent component = parser.parse(testString, reference).findAny().get();

        assertEquals(testString.length(), component.length());
    }

    @Test
    void parse_UppercaseUnit_Parses() {
        assertParses("10 ДНЕЙ");
    }

    @Test
    void parse_OutOfIntRangeAmount_DoesNotThrowAnything() {
        assertDoesNotThrow(() -> parser.parse("2147483648 дней", reference).toList());
    }

    @Test
    void parse_OutOfRangeDate_DoesNotThrowAnything() {
        assertDoesNotThrow(() -> parser.parse("2147483647 десятилетий", reference).toList());
    }

    @Test
    void parse_NegativeAmount_ReturnsNoResults() {
        assertDoesNotParse("-1 день");
    }

    @Test
    void parse_InvalidChronoUnit_ReturnsNoResults() {
        assertDoesNotParse("1 invalid");
    }

    @Test
    void parse_InvalidCharactersBeforeInput_ReturnsNoResults() {
        assertDoesNotParse("invalid1 день");
    }

    @Test
    void parse_InvalidCharactersAfterInput_ReturnsNoResults() {
        assertDoesNotParse("1 деньinvalid");
    }
}

