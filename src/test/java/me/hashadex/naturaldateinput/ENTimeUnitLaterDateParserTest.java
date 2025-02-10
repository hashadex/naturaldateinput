package me.hashadex.naturaldateinput;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import me.hashadex.naturaldateinput.parsers.en.ENTimeUnitLaterDateParser;

public class ENTimeUnitLaterDateParserTest {
    private final ENTimeUnitLaterDateParser parser = new ENTimeUnitLaterDateParser();
    private static final LocalDateTime reference = LocalDateTime.of(2025, 2, 10, 0, 0, 0);

    public static Stream<Arguments> provideArgumentsForTestParser() {
        LocalDate referenceDate = reference.toLocalDate();

        return Stream.of(
            // days
            Arguments.of("1 day", referenceDate.plusDays(1)),
            Arguments.of("10 day", referenceDate.plusDays(10)),
            // weeks
            Arguments.of("1 week", referenceDate.plusWeeks(1)),
            Arguments.of("10 week", referenceDate.plusWeeks(10)),
            // months
            Arguments.of("1 month", referenceDate.plusMonths(1)),
            Arguments.of("10 month", referenceDate.plusMonths(10)),
            // years
            Arguments.of("1 year", referenceDate.plusYears(1)),
            Arguments.of("10 year", referenceDate.plusYears(10))
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForTestParser")
    void testParser(String testQuery, LocalDate expectedResult) {
        assertAll(
            () -> assertEquals(expectedResult, parser.parse(testQuery + "s", reference).get(0).result()),
            () -> assertEquals(expectedResult, parser.parse(testQuery + "s", reference).get(0).result()),
            () -> assertEquals(expectedResult, parser.parse("in " + testQuery + "s", reference).get(0).result()),
            () -> assertEquals(expectedResult, parser.parse("after " + testQuery + "s", reference).get(0).result()),
            () -> assertEquals(expectedResult, parser.parse(testQuery + "s later", reference).get(0).result()),
            () -> assertEquals(expectedResult, parser.parse(testQuery + "s after", reference).get(0).result()),
            () -> assertEquals(expectedResult, parser.parse(testQuery + "s from now", reference).get(0).result())
        );
    }
}
