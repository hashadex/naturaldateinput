package io.github.hashadex.naturaldateinput;

import java.util.Set;

import io.github.hashadex.naturaldateinput.parsers.common.ISODateParser;
import io.github.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser;
import io.github.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser.DayMonthOrder;
import io.github.hashadex.naturaldateinput.parsers.ru.RUChronoUnitLaterParser;
import io.github.hashadex.naturaldateinput.parsers.ru.RUDayMonthYearParser;
import io.github.hashadex.naturaldateinput.parsers.ru.RUMonthDayYearParser;
import io.github.hashadex.naturaldateinput.parsers.ru.RURelativeWordParser;
import io.github.hashadex.naturaldateinput.parsers.ru.RUTimeOfDayWordParser;
import io.github.hashadex.naturaldateinput.parsers.ru.RUWeekdayParser;
import io.github.hashadex.naturaldateinput.parsers.ru.RUYearMonthDayParser;
import io.github.hashadex.naturaldateinput.parsers.common.TwentyFourHourTimeParser;

/**
 * Russian {@link io.github.hashadex.naturaldateinput.ParsingConfiguration ParsingConfiguration}
 * that contains all Russian parsers and all common parsers.
 * <p>
 * List of parsers included in the configuration:
 * <ul>
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.common.ISODateParser ISODateParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser SlashDateFormatParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.common.TwentyFourHourTimeParser TwentyFourHourTimeParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.ru.RUChronoUnitLaterParser RUChronoUnitLaterParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.ru.RUDayMonthYearParser RUDayMonthYearParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.ru.RUMonthDayYearParser RUMonthDayYearParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.ru.RURelativeWordParser RURelativeWordParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.ru.RUTimeOfDayWordParser RUTimeOfDayWordParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.ru.RUWeekdayParser RUWeekdayParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.ru.RUYearMonthDayParser RUYearMonthDayParser}
 * </ul>
 * 
 * @author hashadex
 * @since 2.1.0
 */
public final class RUParsingConfiguration extends ParsingConfiguration {
    /**
     * Constructs the configuration and sets the preferred day-month order for the
     * {@link io.github.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser SlashDateFormatParser}.
     * See <code>SlashDateFormatParser</code> doc comment for explanation on
     * what the chosen day-month order affects.
     * 
     * @param preferredDayMonthOrder Preferred day-month order for the <code>
     *                               SlashDateFormatParser</code>
     * @since 2.1.0
     */
    public RUParsingConfiguration(DayMonthOrder preferredDayMonthOrder) {
        super(Set.of(
            // Common
            new ISODateParser(),
            new SlashDateFormatParser(preferredDayMonthOrder),
            new TwentyFourHourTimeParser(),
            // RU
            new RUChronoUnitLaterParser(),
            new RUDayMonthYearParser(),
            new RUMonthDayYearParser(),
            new RURelativeWordParser(),
            new RUTimeOfDayWordParser(),
            new RUWeekdayParser(),
            new RUYearMonthDayParser()
        ));
    }
}
