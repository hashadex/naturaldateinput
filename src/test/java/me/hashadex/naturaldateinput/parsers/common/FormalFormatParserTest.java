package me.hashadex.naturaldateinput.parsers.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import me.hashadex.naturaldateinput.parsers.ParserTest;
import me.hashadex.naturaldateinput.parsers.common.FormalFormatParser.DateFormat;

public class FormalFormatParserTest extends ParserTest {
    @Override
    @BeforeEach
    public void setup() {
        reference = LocalDateTime.of(2025, 2, 20, 0, 0, 0);
    }

    public static class UnambiguousDatesProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 13),
                LocalDate.of(2025, 10, 10),
                LocalDate.of(2025, 12, 31)
            ).map(Arguments::of);
        }
    }

    public static class AmbiguousDatesProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                LocalDate.of(2026, 1, 12),
                LocalDate.of(2026, 1, 8),
                LocalDate.of(2025, 12, 10),
                LocalDate.of(2025, 12, 1)
            ).map(Arguments::of);
        }
    }

    @Nested
    class DayMonthParserTests {
        @BeforeEach
        public void setupDayMonthParser() {
            parser = new FormalFormatParser(DateFormat.DAY_MONTH);
        }

        @ParameterizedTest
        @ArgumentsSource(UnambiguousDatesProvider.class)
        @ArgumentsSource(AmbiguousDatesProvider.class)
        void parse_YearMonthDay_ReturnsValidDate(LocalDate date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

            assertParsesAs(formatter.format(date), date);
        }

        @ParameterizedTest
        @ArgumentsSource(UnambiguousDatesProvider.class)
        @ArgumentsSource(AmbiguousDatesProvider.class)
        void parse_DayMonthYear_ReturnsValidDate(LocalDate date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");

            assertParsesAs(formatter.format(date), date);
        }

        @ParameterizedTest
        @ArgumentsSource(UnambiguousDatesProvider.class)
        @ArgumentsSource(AmbiguousDatesProvider.class)
        void parse_DayMonthYearWithDoubleDigitYear_ReturnsValidDate(LocalDate date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yy");

            assertParsesAs(formatter.format(date), date);
        }

        @ParameterizedTest
        @ArgumentsSource(UnambiguousDatesProvider.class)
        @ArgumentsSource(AmbiguousDatesProvider.class)
        void parse_DayMonth_ReturnsValidDate(LocalDate date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M");

            assertParsesAs(formatter.format(date), date);
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "invalid",
            "invalid20.02.2025",
            "20.02.2025invalid",
            "invalid20.02.2025invalid",
            "9999-99-99",
            "2025-31-31",
            "99.99.9999",
            "31.31.2025",
            "31.31.99",
            "99/99",
            "31/31",
            "1/-1",
            "10.10.10.10",
        })
        void parse_InvalidInputs_ReturnsEmptyArrayList(String input) {
            assertDoesNotParse(input);
        }
    }

    @Nested
    class MonthDayParserTests {
        @BeforeEach
        public void setupMonthDayParser() {
            parser = new FormalFormatParser(DateFormat.MONTH_DAY);
        }

        @ParameterizedTest
        @ArgumentsSource(UnambiguousDatesProvider.class)
        @ArgumentsSource(AmbiguousDatesProvider.class)
        void parse_MonthDayYear_ReturnsValidDate(LocalDate date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M.d.yyyy");

            assertParsesAs(formatter.format(date), date);
        }

        @ParameterizedTest
        @ArgumentsSource(UnambiguousDatesProvider.class)
        @ArgumentsSource(AmbiguousDatesProvider.class)
        void parse_MonthDayYearWithDoubleDigitYear_ReturnsValidDate(LocalDate date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M.d.yy");

            assertParsesAs(formatter.format(date), date);
        }

        @ParameterizedTest
        @ArgumentsSource(UnambiguousDatesProvider.class)
        @ArgumentsSource(AmbiguousDatesProvider.class)
        void parse_MonthDay_ReturnsValidDate(LocalDate date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");

            assertParsesAs(formatter.format(date), date);
        }
    }
}
