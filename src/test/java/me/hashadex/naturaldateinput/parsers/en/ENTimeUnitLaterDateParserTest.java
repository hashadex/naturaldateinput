package me.hashadex.naturaldateinput.parsers.en;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.parsers.DateParserTest;

public class ENTimeUnitLaterDateParserTest extends DateParserTest {
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new ENTimeUnitLaterDateParser();
        reference = LocalDateTime.of(2025, 2, 21, 0, 0, 0);

        referenceDate = reference.toLocalDate();
    }

    public static Stream<Integer> provideAmounts() {
        return Stream.of(0, 1, 10, 99, 365);
    }

    @ParameterizedTest(name = "[{index}] {0} days")
    @MethodSource("provideAmounts")
    void parse_Days_ReturnsValidDate(int amount) {
        assertParsedDateEquals(referenceDate.plusDays(amount), "%s days".formatted(amount));
    }

    @ParameterizedTest(name = "[{index}] {0} weeks")
    @MethodSource("provideAmounts")
    void parse_Weeks_ReturnsValidDate(int amount) {
        assertParsedDateEquals(referenceDate.plusWeeks(amount), "%s weeks".formatted(amount));
    }

    @ParameterizedTest(name = "[{index}] {0} months")
    @MethodSource("provideAmounts")
    void parse_Months_ReturnsValidDate(int amount) {
        assertParsedDateEquals(referenceDate.plusMonths(amount), "%s months".formatted(amount));
    }

    @ParameterizedTest(name = "[{index}] {0} years")
    @MethodSource("provideAmounts")
    void parse_Years_ReturnsValidDate(int amount) {
        assertParsedDateEquals(referenceDate.plusYears(amount), "%s years".formatted(amount));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid",
        "1 invaliddays",
        "1 invalidweeks",
        "1 invalidmonths",
        "1 invalidyears",
        "1 daysinvalid",
        "1 weeksinvalid",
        "1 monthsinvalid",
        "1 yearsinvalid",
        "-1 days",
        "-1 weeks",
        "-1 months",
        "-1 years"
    })
    void parse_InvalidInputs_ReturnsEmptyArrayList(String input) {
        assertDoesNotParse(input);
    }
}
