package me.hashadex.naturaldateinput;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import me.hashadex.naturaldateinput.parsers.en.ENWeekdayDateParser;

public class ENWeekdayDateParserTest {
    private final ENWeekdayDateParser parser = new ENWeekdayDateParser();
    private final LocalDateTime reference = LocalDateTime.of(2025, 2, 6, 0, 0, 0); // Thursday

    public static Stream<Arguments> provideArgumentsForTestParser() {
        return Stream.of(
            Arguments.of("monday", LocalDate.of(2025, 2, 10)),
            Arguments.of("tuesday", LocalDate.of(2025, 2, 11)),
            Arguments.of("wednesday", LocalDate.of(2025, 2, 12)),
            Arguments.of("thursday", LocalDate.of(2025, 2, 6)),
            Arguments.of("friday", LocalDate.of(2025, 2, 7)),
            Arguments.of("saturday", LocalDate.of(2025, 2, 8)),
            Arguments.of("sunday", LocalDate.of(2025, 2, 9)),
            // next...
            Arguments.of("next monday", LocalDate.of(2025, 2, 17)),
            Arguments.of("next tuesday", LocalDate.of(2025, 2, 18)),
            Arguments.of("next wednesday", LocalDate.of(2025, 2, 19)),
            Arguments.of("next thursday", LocalDate.of(2025, 2, 13)),
            Arguments.of("next friday", LocalDate.of(2025, 2, 14)),
            Arguments.of("next saturday", LocalDate.of(2025, 2, 15)),
            Arguments.of("next sunday", LocalDate.of(2025, 2, 16))
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForTestParser")
    void testParser(String testQuery, LocalDate expectedResult) {
        assertAll(
            () -> assertEquals(expectedResult, parser.parse(testQuery, reference).get(0).result()),
            () -> assertEquals(expectedResult, parser.parse("on " + testQuery, reference).get(0).result()),
            () -> assertEquals(expectedResult, parser.parse("the " + testQuery, reference).get(0).result()),
            () -> assertEquals(expectedResult, parser.parse("on the " + testQuery, reference).get(0).result())
        );
    }
}
