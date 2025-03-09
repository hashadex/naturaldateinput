package me.hashadex.naturaldateinput.parsers.en;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.parsers.ParserTest;

public class ENRelativeWordDateParserTest extends ParserTest {
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new ENRelativeWordDateParser();
        reference = LocalDateTime.of(2025, 2, 21, 0, 0, 0);

        referenceDate = reference.toLocalDate();
    }

    @ParameterizedTest
    @ValueSource(strings = {"today", "tod", "TODAY"})
    void parse_TodayCaseInsensitive_ReturnsValidDate(String input) {
        assertParsedDateEquals(referenceDate, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"tomorrow", "tmr", "tmrw", "TOMORROW"})
    void parse_TomorrowCaseInsensitive_ReturnsValidDate(String input) {
        LocalDate tomorrow = referenceDate.plusDays(1);
        
        assertParsedDateEquals(tomorrow, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"yesterday", "ytd", "YESTERDAY"})
    void parse_YesterdayCaseInsensitive_ReturnsValidDate(String input) {
        LocalDate yesterday = referenceDate.minusDays(1);

        assertParsedDateEquals(yesterday, input);
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
    void parse_InvalidInputs_ReturnsEmptyArrayList(String input) {
        assertDoesNotParse(input);
    }
}
