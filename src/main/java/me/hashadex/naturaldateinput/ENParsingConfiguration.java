package me.hashadex.naturaldateinput;

import java.util.Set;

import me.hashadex.naturaldateinput.parsers.common.ISODateParser;
import me.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser;
import me.hashadex.naturaldateinput.parsers.common.TwentyFourHourTimeParser;
import me.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser.DayMonthOrder;
import me.hashadex.naturaldateinput.parsers.en.ENChronoUnitLaterParser;
import me.hashadex.naturaldateinput.parsers.en.ENDayMonthYearParser;
import me.hashadex.naturaldateinput.parsers.en.ENMonthDayYearParser;
import me.hashadex.naturaldateinput.parsers.en.ENRelativeWordParser;
import me.hashadex.naturaldateinput.parsers.en.ENTimeOfDayWordParser;
import me.hashadex.naturaldateinput.parsers.en.ENTwelveHourTimeParser;
import me.hashadex.naturaldateinput.parsers.en.ENWeekdayParser;
import me.hashadex.naturaldateinput.parsers.en.ENYearMonthDayParser;

/**
 * English {@link me.hashadex.naturaldateinput.ParsingConfiguration ParsingConfiguration}
 * that contains all English parsers and all common parsers.
 * <p>
 * List of parsers included in the configuration:
 * <ul>
 * <li>{@link me.hashadex.naturaldateinput.parsers.common.ISODateParser ISODateParser}
 * <li>{@link me.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser SlashDateFormatParser}
 * <li>{@link me.hashadex.naturaldateinput.parsers.common.TwentyFourHourTimeParser TwentyFourHourTimeParser}
 * <li>{@link me.hashadex.naturaldateinput.parsers.en.ENChronoUnitLaterParser ENChronoUnitLaterParser}
 * <li>{@link me.hashadex.naturaldateinput.parsers.en.ENDayMonthYearParser ENDayMonthYearParser}
 * <li>{@link me.hashadex.naturaldateinput.parsers.en.ENMonthDayYearParser ENMonthDayYearParser}
 * <li>{@link me.hashadex.naturaldateinput.parsers.en.ENRelativeWordParser ENRelativeWordParser}
 * <li>{@link me.hashadex.naturaldateinput.parsers.en.ENTimeOfDayWordParser ENTimeOfDayWordParser}
 * <li>{@link me.hashadex.naturaldateinput.parsers.en.ENTwelveHourTimeParser ENTwelveHourTimeParser}
 * <li>{@link me.hashadex.naturaldateinput.parsers.en.ENWeekdayParser ENWeekdayParser}
 * <li>{@link me.hashadex.naturaldateinput.parsers.en.ENYearMonthDayParser ENYearMonthDayParser}
 * </ul>
 * 
 * @author hashadex
 * @since 1.0.0
 */
public final class ENParsingConfiguration extends ParsingConfiguration {
    /**
     * Constructs the configuration and sets the preferred day-month order for the
     * {@link me.hashadex.naturaldateinput.parsers.common.SlashDateFormatParser SlashDateFormatParser}.
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
