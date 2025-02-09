package me.hashadex.naturaldateinput;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import me.hashadex.naturaldateinput.parsers.common.FormalFormatDateParser;
import me.hashadex.naturaldateinput.parsers.common.FormalFormatDateParser.DateFormat;

public class FormalFormatDateParserTest {
    public static Stream<Arguments> provideArgumentsForTestParse() {
        return Stream.of(
            Arguments.of(LocalDate.of(2025, 12, 31), false),
            Arguments.of(LocalDate.of(2025, 10, 10), false),
            Arguments.of(LocalDate.of(2025, 12, 10), true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForTestParse")
    void testParse(LocalDate testQuery, boolean ambiguous) {
        LocalDateTime reference = LocalDateTime.of(2025, 9, 2, 0, 0, 0);

        DateTimeFormatter ddmmyyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter ddmmyy = DateTimeFormatter.ofPattern("dd.MM.yy");

        DateTimeFormatter mmddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter mmddyy = DateTimeFormatter.ofPattern("MM/dd/yy");

        DateTimeFormatter yyyymmdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        DateTimeFormatter ddmm = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter mmdd = DateTimeFormatter.ofPattern("MM/dd");

        FormalFormatDateParser dayMonthParser = new FormalFormatDateParser(DateFormat.DayMonth);
        FormalFormatDateParser monthDayParser = new FormalFormatDateParser(DateFormat.MonthDay);

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
}
