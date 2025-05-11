package me.hashadex.naturaldateinput.parsers.en;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.parsers.ParserTest;

public class ENRelativeWordParserTest extends ParserTest {
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new ENRelativeWordParser();

        reference = LocalDateTime.of(2025, 5, 8, 12, 0, 0);
        referenceDate = reference.toLocalDate();
    }

    @ParameterizedTest
    @ValueSource(strings = {"yesterday", "ytd"})
    void parse_Yesterday_ReturnsCorrectDate(String input) {
        LocalDate yesterday = referenceDate.minusDays(1);

        assertParsesAs(input, yesterday);
    }

    @ParameterizedTest
    @ValueSource(strings = {"today", "tod"})
    void parse_Today_ReturnsCorrectDate(String input) {
        LocalDate today = referenceDate;

        assertParsesAs(input, today);
    }

    @ParameterizedTest
    @ValueSource(strings = {"tomorrow", "tmrw", "tmr"})
    void parse_Tomorrow_ReturnsCorrectDate(String input) {
        LocalDate tomorrow = referenceDate.plusDays(1);

        assertParsesAs(input, tomorrow);
    }

    @Test
    void parse_UppercaseWord_Parses() {
        assertParses("TODAY");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Meeting with todd",
        "invalidtoday",
        "invalidtomorrow",
        "invalidtmr",
        "invalidtmrw",
        "invalidyesterday",
        "invalidytd",
        "todayinvalid",
        "tomorrowinvalid",
        "tmrinvalid",
        "tmrwinvalid",
        "yesterdayinvalid",
        "ytdinvalid"
    })
    void parse_InvalidInputs_ReturnsNoResults(String input) {
        assertDoesNotParse(input);
    }
}
