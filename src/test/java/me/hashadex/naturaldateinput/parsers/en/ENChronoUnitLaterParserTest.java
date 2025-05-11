package me.hashadex.naturaldateinput.parsers.en;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.parsers.ParserTest;

public class ENChronoUnitLaterParserTest extends ParserTest {
    private LocalDate referenceDate;

    @Override
    @BeforeEach
    public void setup() {
        parser = new ENChronoUnitLaterParser();
        reference = LocalDateTime.of(2025, 5, 8, 12, 0, 0);

        referenceDate = reference.toLocalDate();
    }

    public static IntStream provideAmounts() {
        return IntStream.of(0, 1, 2, 10, 365);
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Decades_ReturnsCorrectDate(int amount) {
        assertParsesAs("%s decades".formatted(amount), referenceDate.plus(amount, ChronoUnit.DECADES));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Years_ReturnsCorrectDate(int amount) {
        assertParsesAs("%s years".formatted(amount), referenceDate.plusYears(amount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Months_ReturnsCorrectDate(int amount) {
        assertParsesAs("%s months".formatted(amount), referenceDate.plusMonths(amount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Weeks_ReturnsCorrectDate(int amount) {
        assertParsesAs("%s weeks".formatted(amount), referenceDate.plusWeeks(amount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Days_ReturnsCorrectDate(int amount) {
        assertParsesAs("%s days".formatted(amount), referenceDate.plusDays(amount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Halfdays_ReturnsCorrectDate(int amount) {
        assertAll(
            () -> assertParsesAs("%s half-days".formatted(amount), reference.plus(amount, ChronoUnit.HALF_DAYS)),
            () -> assertParsesAs("%s halfdays".formatted(amount), reference.plus(amount, ChronoUnit.HALF_DAYS))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Hours_ReturnsCorrectDate(int amount) {
        assertParsesAs("%s hours".formatted(amount), reference.plusHours(amount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Minutes_ReturnsCorrectDate(int amount) {
        assertParsesAs("%s minutes".formatted(amount), reference.plusMinutes(amount));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void parse_Seconds_ReturnsCorrectDate(int amount) {
        assertParsesAs("%s seconds".formatted(amount), reference.plusSeconds(amount));
    }

    @Test
    void parse_UppercaseInputs_ReturnsCorrectDate() {
        assertParsesAs("10 DAYS", referenceDate.plusDays(10));
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
    void parse_InvalidInputs_ReturnsNoResults(String input) {
        assertDoesNotParse(input);
    }
}
