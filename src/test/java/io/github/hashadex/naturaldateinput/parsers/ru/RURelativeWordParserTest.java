package io.github.hashadex.naturaldateinput.parsers.ru;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.hashadex.naturaldateinput.parsers.ParserTest;

public class RURelativeWordParserTest extends ParserTest {
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new RURelativeWordParser();

        reference = LocalDateTime.of(2025, 5, 8, 12, 0, 0);
        referenceDate = reference.toLocalDate();
    }

    @Test
    void parse_Ereyesterday_ReturnsCorrectDate() {
        LocalDate ereyesterday = referenceDate.minusDays(2);

        assertParsesAs("позавчера", ereyesterday);
    }

    @Test
    void parse_Yesterday_ReturnsCorrectDate() {
        LocalDate yesterday = referenceDate.minusDays(1);

        assertParsesAs("вчера", yesterday);
    }

    @Test
    void parse_Today_ReturnsCorrectDate() {
        LocalDate today = referenceDate;

        assertParsesAs("сегодня", today);
    }

    @Test
    void parse_Tomorrow_ReturnsCorrectDate() {
        LocalDate tomorrow = referenceDate.plusDays(1);

        assertParsesAs("завтра", tomorrow);
    }

    @Test
    void parse_Overmorrow_ReturnsCorrectDate() {
        LocalDate overmorrow = referenceDate.plusDays(2);

        assertParsesAs("послезавтра", overmorrow);
    }

    @Test
    void parse_UppercaseWord_Parses() {
        assertParses("СЕГОДНЯ");
    }

    @Test
    void parse_InvalidCharactersBeforeInput_ReturnsNoResults() {
        assertDoesNotParse("invalidсегодня");
    }

    @Test
    void parse_InvalidCharactersAfterInput_ReturnsNoResults() {
        assertDoesNotParse("сегодняinvalid");
    }
}
