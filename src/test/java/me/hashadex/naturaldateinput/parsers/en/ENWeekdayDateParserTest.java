package me.hashadex.naturaldateinput.parsers.en;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.parsers.DateParserTest;

public class ENWeekdayDateParserTest extends DateParserTest {
    private LocalDate thisThursday;
    private LocalDate thisFriday;
    private LocalDate thisSaturday;
    private LocalDate thisSunday;

    private LocalDate nextMonday;
    private LocalDate nextTuesday;
    private LocalDate nextWednesday;
    private LocalDate nextThursday;
    private LocalDate nextFriday;
    private LocalDate nextSaturday;
    private LocalDate nextSunday;

    private LocalDate nextNextMonday;
    private LocalDate nextNextTuesday;
    private LocalDate nextNextWednesday;

    @Override
    @BeforeEach
    public void setup() {
        parser = new ENWeekdayDateParser();
        reference = LocalDateTime.of(2025, 2, 20, 0, 0, 0); // Thursday

        thisThursday = LocalDate.of(2025, 2, 20);
        thisFriday = LocalDate.of(2025, 2, 21);
        thisSaturday = LocalDate.of(2025, 2, 22);
        thisSunday = LocalDate.of(2025, 2, 23);

        nextMonday = LocalDate.of(2025, 2, 24);
        nextTuesday = LocalDate.of(2025, 2, 25);
        nextWednesday = LocalDate.of(2025, 2, 26);
        nextThursday = LocalDate.of(2025, 2, 27);
        nextFriday = LocalDate.of(2025, 2, 28);
        nextSaturday = LocalDate.of(2025, 3, 1);
        nextSunday = LocalDate.of(2025, 3, 2);

        nextNextMonday = LocalDate.of(2025, 3, 3);
        nextNextTuesday = LocalDate.of(2025, 3, 4);
        nextNextWednesday = LocalDate.of(2025, 3, 5);
    }

    @ParameterizedTest
    @ValueSource(strings = {"monday", "mon", "MONDAY"})
    void parse_Monday_ReturnsNextMonday(String input) {
        assertParsedDateEquals(nextMonday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"tuesday", "tue", "TUESDAY"})
    void parse_Tuesday_ReturnsNextTuesday(String input) {
        assertParsedDateEquals(nextTuesday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"wednesday", "wed", "WEDNESDAY"})
    void parse_Wednesday_ReturnsNextWednessday(String input) {
        assertParsedDateEquals(nextWednesday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"thursday", "thu", "THURSDAY"})
    void parse_Thursday_ReturnsThisThursday(String input) {
        assertParsedDateEquals(thisThursday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"friday", "fri", "FRIDAY"})
    void parse_Friday_ReturnsThisFriday(String input) {
        assertParsedDateEquals(thisFriday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"saturday", "sat", "SATURDAY"})
    void parse_Saturday_ReturnsThisSaturday(String input) {
        assertParsedDateEquals(thisSaturday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"sunday", "SUNDAY"})
    void parse_Sunday_ReturnsThisSunday(String input) {
        assertParsedDateEquals(thisSunday, input);
    }
    
    // Next...
    @ParameterizedTest
    @ValueSource(strings = {"next monday", "next mon", "NEXT MONDAY"})
    void parse_NextMonday_ReturnsNextNextMonday(String input) {
        assertParsedDateEquals(nextNextMonday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"next tuesday", "next tue", "NEXT TUESDAY"})
    void parse_NextTuesday_ReturnsNextNextTuesday(String input) {
        assertParsedDateEquals(nextNextTuesday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"next wednesday", "next wed", "NEXT WEDNESDAY"})
    void parse_NextWednesday_ReturnsNextNextWednesday(String input) {
        assertParsedDateEquals(nextNextWednesday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"next thursday", "next thu", "NEXT THURSDAY"})
    void parse_NextThursday_ReturnsNextThursday(String input) {
        assertParsedDateEquals(nextThursday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"next friday", "next fri", "NEXT FRIDAY"})
    void parse_NextFriday_ReturnsNextFriday(String input) {
        assertParsedDateEquals(nextFriday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"next saturday", "next sat", "NEXT SATURDAY"})
    void parse_NextSaturday_ReturnsNextSaturday(String input) {
        assertParsedDateEquals(nextSaturday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"next sunday", "NEXT SUNDAY"})
    void parse_NextSunday_ReturnsNextSunday(String input) {
        assertParsedDateEquals(nextSunday, input);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid",
        "invalidmonday",
        "mondayinvalid",
        "wedding",
        "sun",
        "french fries"
    })
    void parse_InvalidInputs_ReturnsEmptyArrayList(String input) {
        assertDoesNotParse(input);
    }
}
