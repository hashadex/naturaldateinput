package io.github.hashadex.naturaldateinput;

import java.util.Set;

import io.github.hashadex.naturaldateinput.parsers.common.ISODateParser;
import io.github.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser;
import io.github.hashadex.naturaldateinput.parsers.common.TwentyFourHourTimeParser;
import io.github.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser.DayMonthOrder;
import io.github.hashadex.naturaldateinput.parsers.en.ENChronoUnitLaterParser;
import io.github.hashadex.naturaldateinput.parsers.en.ENDayMonthYearParser;
import io.github.hashadex.naturaldateinput.parsers.en.ENMonthDayYearParser;
import io.github.hashadex.naturaldateinput.parsers.en.ENRelativeWordParser;
import io.github.hashadex.naturaldateinput.parsers.en.ENTimeOfDayWordParser;
import io.github.hashadex.naturaldateinput.parsers.en.ENTwelveHourTimeParser;
import io.github.hashadex.naturaldateinput.parsers.en.ENWeekdayParser;
import io.github.hashadex.naturaldateinput.parsers.en.ENYearMonthDayParser;

/**
 * English {@link io.github.hashadex.naturaldateinput.ParsingConfiguration ParsingConfiguration}
 * that contains all English parsers and all common parsers.
 * <p>
 * List of parsers included in the configuration:
 * <ul>
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.common.ISODateParser ISODateParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser SlashDateFormatParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.common.TwentyFourHourTimeParser TwentyFourHourTimeParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.en.ENChronoUnitLaterParser ENChronoUnitLaterParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.en.ENDayMonthYearParser ENDayMonthYearParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.en.ENMonthDayYearParser ENMonthDayYearParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.en.ENRelativeWordParser ENRelativeWordParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.en.ENTimeOfDayWordParser ENTimeOfDayWordParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.en.ENTwelveHourTimeParser ENTwelveHourTimeParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.en.ENWeekdayParser ENWeekdayParser}
 * <li>{@link io.github.hashadex.naturaldateinput.parsers.en.ENYearMonthDayParser ENYearMonthDayParser}
 * </ul>
 * 
 * @author hashadex
 * @since 1.0.0
 */
public final class ENParsingConfiguration extends ParsingConfiguration {
    /**
     * Constructs the configuration and sets the preferred day-month order for the
     * {@link io.github.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser SlashDateFormatParser}.
     * See <code>SlashDateFormatParser</code> doc comment for explanation on
     * what the chosen day-month order affects.
     * 
     * @param preferredDayMonthOrder Preferred day-month order for the <code>
     *                               SlashDateFormatParser</code>
     * @since 1.0.0
     */
    public ENParsingConfiguration(DayMonthOrder preferredDayMonthOrder) {
        super(Set.of(
            // Common
            new ISODateParser(),
            new SlashDateFormatParser(preferredDayMonthOrder),
            new TwentyFourHourTimeParser(),
            // EN
            new ENChronoUnitLaterParser(),
            new ENDayMonthYearParser(),
            new ENMonthDayYearParser(),
            new ENRelativeWordParser(),
            new ENTimeOfDayWordParser(),
            new ENTwelveHourTimeParser(),
            new ENWeekdayParser(),
            new ENYearMonthDayParser()
        ));
    }
}
