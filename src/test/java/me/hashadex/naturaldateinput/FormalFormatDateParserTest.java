package me.hashadex.naturaldateinput;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import me.hashadex.naturaldateinput.parsers.common.FormalFormatDateParser;
import me.hashadex.naturaldateinput.parsers.common.FormalFormatDateParser.DateFormat;

public class FormalFormatDateParserTest {
    private final LocalDateTime reference = LocalDateTime.of(2025, 9, 2, 0, 0, 0);

    private final DateTimeFormatter ddmmyyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter ddmmyy = DateTimeFormatter.ofPattern("dd.MM.yy");

    private final DateTimeFormatter mmddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final DateTimeFormatter mmddyy = DateTimeFormatter.ofPattern("MM/dd/yy");

    private final DateTimeFormatter yyyymmdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DateTimeFormatter ddmm = DateTimeFormatter.ofPattern("dd/MM");
    private final DateTimeFormatter mmdd = DateTimeFormatter.ofPattern("MM/dd");

    private final FormalFormatDateParser dayMonthParser = new FormalFormatDateParser(DateFormat.DAY_MONTH);
    private final FormalFormatDateParser monthDayParser = new FormalFormatDateParser(DateFormat.MONTH_DAY);

    public static Stream<Arguments> provideArgumentsForTestParser() {
        return Stream.of(
            Arguments.of(LocalDate.of(2025, 12, 31), false),
            Arguments.of(LocalDate.of(2025, 10, 10), false),
            Arguments.of(LocalDate.of(2025, 12, 10), true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForTestParser")
    void testParser(LocalDate testQuery, boolean ambiguous) {
        assertAll(
            () -> assertEquals(testQuery, dayMonthParser.parse(testQuery.format(yyyymmdd), reference).get(0).result()),
            () -> assertEquals(testQuery, monthDayParser.parse(testQuery.format(yyyymmdd), reference).get(0).result())
        );

        if (ambiguous) {
            assertAll(
                // dayMonthParser
                () -> assertEquals(testQuery, dayMonthParser.parse(testQuery.format(ddmmyyyy), reference).get(0).result()),
                () -> assertEquals(testQuery, dayMonthParser.parse(testQuery.format(ddmmyy), reference).get(0).result()),
                () -> assertEquals(testQuery, dayMonthParser.parse(testQuery.format(ddmm), reference).get(0).result()),
                // monthDayParser
                () -> assertEquals(testQuery, monthDayParser.parse(testQuery.format(mmddyyyy), reference).get(0).result()),
                () -> assertEquals(testQuery, monthDayParser.parse(testQuery.format(mmddyy), reference).get(0).result()),
                () -> assertEquals(testQuery, monthDayParser.parse(testQuery.format(mmdd), reference).get(0).result())
            );
        } else {
            assertAll(
                // dayMonthParser
                () -> assertEquals(testQuery, dayMonthParser.parse(testQuery.format(ddmmyyyy), reference).get(0).result()),
                () -> assertEquals(testQuery, dayMonthParser.parse(testQuery.format(mmddyyyy), reference).get(0).result()),
                () -> assertEquals(testQuery, dayMonthParser.parse(testQuery.format(ddmmyy), reference).get(0).result()),
                () -> assertEquals(testQuery, dayMonthParser.parse(testQuery.format(mmddyy), reference).get(0).result()),
                () -> assertEquals(testQuery, dayMonthParser.parse(testQuery.format(ddmm), reference).get(0).result()),
                () -> assertEquals(testQuery, dayMonthParser.parse(testQuery.format(mmdd), reference).get(0).result()),
                // monthDayParser
                () -> assertEquals(testQuery, monthDayParser.parse(testQuery.format(ddmmyyyy), reference).get(0).result()),
                () -> assertEquals(testQuery, monthDayParser.parse(testQuery.format(mmddyyyy), reference).get(0).result()),
                () -> assertEquals(testQuery, monthDayParser.parse(testQuery.format(ddmmyy), reference).get(0).result()),
                () -> assertEquals(testQuery, monthDayParser.parse(testQuery.format(mmddyy), reference).get(0).result()),
                () -> assertEquals(testQuery, monthDayParser.parse(testQuery.format(ddmm), reference).get(0).result()),
                () -> assertEquals(testQuery, monthDayParser.parse(testQuery.format(mmdd), reference).get(0).result())
            );
        }
    }

    @Test
    void testIncorrect() {
        assertAll(
            () -> assertTrue(dayMonthParser.parse("foobarbaz", reference).isEmpty()),
            () -> assertTrue(dayMonthParser.parse("10.10.10.10", reference).isEmpty()),
            () -> assertTrue(dayMonthParser.parse("foo09.02.2025", reference).isEmpty()),
            () -> assertTrue(dayMonthParser.parse("09.02.2025bar", reference).isEmpty())
        );
    }
}
