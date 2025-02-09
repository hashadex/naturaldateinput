package me.hashadex.naturaldateinput.parsers.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import me.hashadex.naturaldateinput.ParseResult;
import me.hashadex.naturaldateinput.parsers.common.FormalFormatDateParser.DateFormat;

public class FormalFormatDateParserTest {
    LocalDate parseAndGetFirst(String input, LocalDateTime reference, DateFormat preferredFormat) {
        FormalFormatDateParser p = new FormalFormatDateParser(preferredFormat);
        ArrayList<ParseResult<LocalDate>> results = p.parse(input, reference);

        if (results.size() == 0) {
            return null;
        } else {
            return results.get(0).result();
        }
    }

    LocalDate parseAndGetFirst(String input) {
        return parseAndGetFirst(input, LocalDateTime.now(), DateFormat.DayMonth);
    }

    @Test
    void testDDMMYYYY() {
        assertEquals(
            LocalDate.of(2025, 12, 31),
            parseAndGetFirst("31.12.2025")
        );
        assertEquals(
            LocalDate.of(2025, 12, 31),
            parseAndGetFirst("12.31.2025")
        );
        assertEquals(
            LocalDate.of(2025, 12, 31),
            parseAndGetFirst("12.31.25")
        );

        assertEquals(
            LocalDate.of(2025, 1, 1),
            parseAndGetFirst("1.1.25")
        );

        assertEquals(
            LocalDate.of(2025, 12, 10),
            parseAndGetFirst("10.12.2025", LocalDateTime.now(), DateFormat.DayMonth)
        );
        assertEquals(
            LocalDate.of(2025, 10, 12),
            parseAndGetFirst("10/12/2025", LocalDateTime.now(), DateFormat.MonthDay)
        );

        assertEquals(
            LocalDate.of(2025, 3, 1),
            parseAndGetFirst("29.02.2025")
        );

        assertNull(parseAndGetFirst("99.99.2025"));
        assertNull(parseAndGetFirst("99.1.2025"));
        assertNull(parseAndGetFirst("1.99.2025"));
    }

    @Test
    void testYYYYMMDD() {
        assertEquals(
            LocalDate.of(2025, 12, 31),
            parseAndGetFirst("2025-12-31")
        );
        assertEquals(
            LocalDate.of(2025, 10, 12),
            parseAndGetFirst("2025-10-12")
        );
        assertEquals(
            LocalDate.of(2025, 3, 1),
            parseAndGetFirst("2025-02-29")
        );

        assertEquals(
            LocalDate.of(1900, 1, 1),
            parseAndGetFirst("1900-01-01")
        );
        assertEquals(
            LocalDate.of(2200, 1, 1),
            parseAndGetFirst("2200-01-01")
        );

        assertNull(parseAndGetFirst("1000-01-01"));
    }

    @Test
    void testMMDD() {
        LocalDateTime reference = LocalDateTime.of(2025, 2, 9, 12, 0, 0);

        assertEquals(
            LocalDate.of(2025, 12, 31),
            parseAndGetFirst("31/12", reference, DateFormat.DayMonth)
        );
        assertEquals(
            LocalDate.of(2025, 12, 31),
            parseAndGetFirst("12/31", reference, DateFormat.DayMonth)
        );
        
        assertEquals(
            LocalDate.of(2025, 10, 10),
            parseAndGetFirst("10/10", reference, DateFormat.DayMonth)
        );
        assertEquals(
            LocalDate.of(2025, 10, 10),
            parseAndGetFirst("10/10", reference, DateFormat.MonthDay)
        );

        assertEquals(
            LocalDate.of(2026, 2, 8),
            parseAndGetFirst("8/2", reference, DateFormat.DayMonth)
        );
        assertEquals(
            LocalDate.of(2025, 2, 9),
            parseAndGetFirst("9/2", reference, DateFormat.DayMonth)
        );

        assertEquals(
            LocalDate.of(2025, 3, 1),
            parseAndGetFirst("29/2", reference, DateFormat.DayMonth)
        );

        assertNull(parseAndGetFirst("12/32", reference, DateFormat.DayMonth));
        assertNull(parseAndGetFirst("32/12", reference, DateFormat.DayMonth));
    }
}
