package me.hashadex.naturaldateinput.parsers.en;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.parsers.ParserTest;
import me.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;

public class ENWeekdayParserTest extends ParserTest {
    public static enum ThisWeek {
        MONDAY(LocalDate.of(2025, 5, 5)),
        TUESDAY(LocalDate.of(2025, 5, 6)),
        WEDNESDAY(LocalDate.of(2025, 5, 7)),
        THURSDAY(LocalDate.of(2025, 5, 8)),
        FRIDAY(LocalDate.of(2025, 5, 9)),
        SATURDAY(LocalDate.of(2025, 5, 10)),
        SUNDAY(LocalDate.of(2025, 5, 11));

        public final LocalDate date;

        private ThisWeek(LocalDate date) {
            this.date = date;
        }
    }

    public static enum NextWeek {
        MONDAY(LocalDate.of(2025, 5, 12)),
        TUESDAY(LocalDate.of(2025, 5, 13)),
        WEDNESDAY(LocalDate.of(2025, 5, 14)),
        THURSDAY(LocalDate.of(2025, 5, 15)),
        FRIDAY(LocalDate.of(2025, 5, 16)),
        SATURDAY(LocalDate.of(2025, 5, 17)),
        SUNDAY(LocalDate.of(2025, 5, 18));

        public final LocalDate date;

        private NextWeek(LocalDate date) {
            this.date = date;
        }
    }

    @Override
    @BeforeEach
    public void setup() {
        parser = new ENWeekdayParser();
    }

    @Nested
    class NextOrSameWeekdayTests {
        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_Monday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();

            LocalDate expectedResult;
            if (reference.getDayOfWeek() == DayOfWeek.MONDAY) {
                expectedResult = ThisWeek.MONDAY.date;
            } else {
                expectedResult = NextWeek.MONDAY.date;
            }

            assertAll(
                () -> assertParsesAs("monday", expectedResult),
                () -> assertParsesAs("mon", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_Tuesday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();
            LocalDate referenceDate = reference.toLocalDate();

            LocalDate expectedResult;
            if (referenceDate.isAfter(ThisWeek.TUESDAY.date)) {
                expectedResult = NextWeek.TUESDAY.date;
            } else {
                expectedResult = ThisWeek.TUESDAY.date;
            }

            assertAll(
                () -> assertParsesAs("tuesday", expectedResult),
                () -> assertParsesAs("tue", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_Wednesday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();
            LocalDate referenceDate = reference.toLocalDate();

            LocalDate expectedResult;
            if (referenceDate.isAfter(ThisWeek.WEDNESDAY.date)) {
                expectedResult = NextWeek.WEDNESDAY.date;
            } else {
                expectedResult = ThisWeek.WEDNESDAY.date;
            }

            assertAll(
                () -> assertParsesAs("wednesday", expectedResult),
                () -> assertParsesAs("wed", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_Thursday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();
            LocalDate referenceDate = reference.toLocalDate();

            LocalDate expectedResult;
            if (referenceDate.isAfter(ThisWeek.THURSDAY.date)) {
                expectedResult = NextWeek.THURSDAY.date;
            } else {
                expectedResult = ThisWeek.THURSDAY.date;
            }

            assertAll(
                () -> assertParsesAs("thursday", expectedResult),
                () -> assertParsesAs("thu", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_Friday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();
            LocalDate referenceDate = reference.toLocalDate();

            LocalDate expectedResult;
            if (referenceDate.isAfter(ThisWeek.FRIDAY.date)) {
                expectedResult = NextWeek.FRIDAY.date;
            } else {
                expectedResult = ThisWeek.FRIDAY.date;
            }

            assertAll(
                () -> assertParsesAs("friday", expectedResult),
                () -> assertParsesAs("fri", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_Saturday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();
            LocalDate referenceDate = reference.toLocalDate();

            LocalDate expectedResult;
            if (referenceDate.isAfter(ThisWeek.SATURDAY.date)) {
                expectedResult = NextWeek.SATURDAY.date;
            } else {
                expectedResult = ThisWeek.SATURDAY.date;
            }

            assertAll(
                () -> assertParsesAs("saturday", expectedResult),
                () -> assertParsesAs("sat", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_Sunday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();

            LocalDate expectedResult = ThisWeek.SUNDAY.date;

            assertParsesAs("sunday", expectedResult);
        }
    }

    @Nested
    class NextWeekdayTests {
        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_NextMonday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();

            LocalDate expectedResult = NextWeek.MONDAY.date;

            assertAll(
                () -> assertParsesAs("next monday", expectedResult),
                () -> assertParsesAs("next mon", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_NextTuesday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();
            LocalDate referenceDate = reference.toLocalDate();

            LocalDate expectedResult;
            if (referenceDate.isBefore(ThisWeek.TUESDAY.date)) {
                expectedResult = ThisWeek.TUESDAY.date;
            } else {
                expectedResult = NextWeek.TUESDAY.date;
            }

            assertAll(
                () -> assertParsesAs("next tuesday", expectedResult),
                () -> assertParsesAs("next tue", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_NextWednesday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();
            LocalDate referenceDate = reference.toLocalDate();

            LocalDate expectedResult;
            if (referenceDate.isBefore(ThisWeek.WEDNESDAY.date)) {
                expectedResult = ThisWeek.WEDNESDAY.date;
            } else {
                expectedResult = NextWeek.WEDNESDAY.date;
            }

            assertAll(
                () -> assertParsesAs("next wednesday", expectedResult),
                () -> assertParsesAs("next wed", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_NextThursday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();
            LocalDate referenceDate = reference.toLocalDate();

            LocalDate expectedResult;
            if (referenceDate.isBefore(ThisWeek.THURSDAY.date)) {
                expectedResult = ThisWeek.THURSDAY.date;
            } else {
                expectedResult = NextWeek.THURSDAY.date;
            }

            assertAll(
                () -> assertParsesAs("next thursday", expectedResult),
                () -> assertParsesAs("next thu", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_NextFriday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();
            LocalDate referenceDate = reference.toLocalDate();

            LocalDate expectedResult;
            if (referenceDate.isBefore(ThisWeek.FRIDAY.date)) {
                expectedResult = ThisWeek.FRIDAY.date;
            } else {
                expectedResult = NextWeek.FRIDAY.date;
            }

            assertAll(
                () -> assertParsesAs("next friday", expectedResult),
                () -> assertParsesAs("next fri", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_NextSaturday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();
            LocalDate referenceDate = reference.toLocalDate();

            LocalDate expectedResult;
            if (referenceDate.isBefore(ThisWeek.SATURDAY.date)) {
                expectedResult = ThisWeek.SATURDAY.date;
            } else {
                expectedResult = NextWeek.SATURDAY.date;
            }

            assertAll(
                () -> assertParsesAs("next saturday", expectedResult),
                () -> assertParsesAs("next sat", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_NextSunday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();
            LocalDate referenceDate = reference.toLocalDate();

            LocalDate expectedResult;
            if (referenceDate.isBefore(ThisWeek.SUNDAY.date)) {
                expectedResult = ThisWeek.SUNDAY.date;
            } else {
                expectedResult = NextWeek.SUNDAY.date;
            }

            assertAll(
                () -> assertParsesAs("next sunday", expectedResult)
            );
        }
    }

    @Test
    void parse_UppercaseWeekday_Parses() {
        reference = ThisWeek.MONDAY.date.atStartOfDay();

        assertParses("MONDAY");
    }

    @Test
    void parse_OnThe_MatchIncludesOnThe() {
        reference = ThisWeek.MONDAY.date.atStartOfDay();

        String testString = "on the next sunday";

        Optional<ParsedComponent> result = parser.parse(testString, reference).findAny();
        
        assertTrue(result.isPresent(), "Parser returned empty stream");

        assertEquals(testString.length(), result.get().getLength());
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
    void parse_InvalidInputs_ReturnsNoResults(String input) {
        reference = ThisWeek.MONDAY.date.atStartOfDay();

        assertDoesNotParse(input);
    }
}
