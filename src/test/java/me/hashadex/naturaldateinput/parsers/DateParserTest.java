package me.hashadex.naturaldateinput.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import me.hashadex.naturaldateinput.ParseResult;

public abstract class DateParserTest extends ParserTest {
    protected DateParser parser;

    protected void assertParses(String input, DateParser parser) {
        assertNotEquals(Collections.EMPTY_LIST, parser.parse(input, reference));
    }

    protected void assertParses(String input) {
        assertParses(input, parser);
    }

    protected void assertDoesNotParse(String input, DateParser parser) {
        assertEquals(Collections.EMPTY_LIST, parser.parse(input, reference));
    }

    protected void assertDoesNotParse(String input) {
        assertDoesNotParse(input, parser);
    }

    protected void assertParsedDateEquals(LocalDate expectedResult, String input, DateParser parser) {
        ArrayList<ParseResult<LocalDate>> results = parser.parse(input, reference);

        if (results.isEmpty()) {
            fail("Parser returned empty array for input '%s'".formatted(input));
        } else {
            assertEquals(expectedResult, results.get(0).result());
        }
    }

    protected void assertParsedDateEquals(LocalDate expectedResult, String input) {
        assertParsedDateEquals(expectedResult, input, parser);
    }
}
