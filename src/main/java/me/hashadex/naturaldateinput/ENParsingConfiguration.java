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

public final class ENParsingConfiguration extends ParsingConfiguration {
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
