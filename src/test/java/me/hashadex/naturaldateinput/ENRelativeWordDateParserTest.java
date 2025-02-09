package me.hashadex.naturaldateinput;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import me.hashadex.naturaldateinput.parsers.en.ENRelativeWordDateParser;

public class ENRelativeWordDateParserTest {
    private final LocalDateTime reference = LocalDateTime.of(2025, 2, 9, 0, 0, 0);
    private final ENRelativeWordDateParser parser = new ENRelativeWordDateParser();

    @Test
    void testParser() {
        assertEquals(reference.toLocalDate(), parser.parse("today", reference).get(0).result());
        assertEquals(reference.toLocalDate(), parser.parse("tod", reference).get(0).result());

        assertEquals(reference.toLocalDate().plusDays(1), parser.parse("tomorrow", reference).get(0).result());
        assertEquals(reference.toLocalDate().plusDays(1), parser.parse("tmr", reference).get(0).result());
        assertEquals(reference.toLocalDate().plusDays(1), parser.parse("tmrw", reference).get(0).result());

        assertEquals(reference.toLocalDate().minusDays(1), parser.parse("yesterday", reference).get(0).result());
        assertEquals(reference.toLocalDate().minusDays(1), parser.parse("ytd", reference).get(0).result());
    }

    @Test
    void testCaseInsensitive() {
        assertEquals(reference.toLocalDate(), parser.parse("TODAY", reference).get(0).result());
        assertEquals(reference.toLocalDate().plusDays(1), parser.parse("Tomorrow", reference).get(0).result());
        assertEquals(reference.toLocalDate().minusDays(1), parser.parse("YeStErDaY", reference).get(0).result());
    }

    @Test
    void testIncorrect() {
        assertAll(
            () -> assertTrue(parser.parse("fooyesterday", reference).isEmpty()),
            () -> assertTrue(parser.parse("yesterdayfoo", reference).isEmpty())
        );
    }
}
