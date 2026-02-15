package io.github.hashadex.naturaldateinput.parsers.ru;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import io.github.hashadex.naturaldateinput.parsers.ParserTest;
import io.github.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;

public class RUWeekdayParserTest extends ParserTest {
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
        parser = new RUWeekdayParser();
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
                () -> assertParsesAs("понедельник", expectedResult),
                () -> assertParsesAs("пн", expectedResult)
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
                () -> assertParsesAs("вторник", expectedResult),
                () -> assertParsesAs("вт", expectedResult)
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
                () -> assertParsesAs("среда", expectedResult),
                () -> assertParsesAs("среду", expectedResult),
                () -> assertParsesAs("ср", expectedResult)
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
                () -> assertParsesAs("четверг", expectedResult),
                () -> assertParsesAs("чт", expectedResult)
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
                () -> assertParsesAs("пятница", expectedResult),
                () -> assertParsesAs("пятницу", expectedResult),
                () -> assertParsesAs("пт", expectedResult)
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
                () -> assertParsesAs("суббота", expectedResult),
                () -> assertParsesAs("субботу", expectedResult),
                () -> assertParsesAs("сб", expectedResult)
            );
        }

        @ParameterizedTest
        @EnumSource(ThisWeek.class)
        void parse_Sunday_ReturnsCorrectDate(ThisWeek weekday) {
            reference = weekday.date.atStartOfDay();

            LocalDate expectedResult = ThisWeek.SUNDAY.date;

            assertAll(
                () -> assertParsesAs("воскресенье", expectedResult),
                () -> assertParsesAs("вс", expectedResult)
            );
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
                () -> assertParsesAs("следующий понедельник", expectedResult),
                () -> assertParsesAs("след пн", expectedResult)
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
                () -> assertParsesAs("следующий вторник", expectedResult),
                () -> assertParsesAs("след вт", expectedResult)
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
                () -> assertParsesAs("следующую среду", expectedResult),
                () -> assertParsesAs("след ср", expectedResult)
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
                () -> assertParsesAs("следующий четверг", expectedResult),
                () -> assertParsesAs("след чт", expectedResult)
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
                () -> assertParsesAs("следующую пятницу", expectedResult),
                () -> assertParsesAs("след пт", expectedResult)
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
                () -> assertParsesAs("следующую субботу", expectedResult),
                () -> assertParsesAs("след сб", expectedResult)
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
                () -> assertParsesAs("следующее воскресенье", expectedResult),
                () -> assertParsesAs("след вс", expectedResult)
            );
        }
    }

    @Test
    void parse_UppercaseWeekday_Parses() {
        reference = ThisWeek.MONDAY.date.atStartOfDay();

        assertParses("ПОНЕДЕЛЬНИК");
    }

    @Test
    void parse_On_MatchIncludesOn() {
        reference = ThisWeek.MONDAY.date.atStartOfDay();

        String testString = "в следующий понедельник";

        ParsedComponent result = parser.parse(testString, reference).findAny().get();

        assertEquals(testString.length(), result.length());
    }

    @Test
    void parse_InvalidCharactersBeforeInput_ReturnsNoResults() {
        reference = ThisWeek.MONDAY.date.atStartOfDay();

        assertDoesNotParse("invalidпонедельник");
    }

    @Test
    void parse_InvalidCharactersAfterInput_ReturnsNoResults() {
        reference = ThisWeek.MONDAY.date.atStartOfDay();

        assertDoesNotParse("понедельникinvalid");
    }
}
