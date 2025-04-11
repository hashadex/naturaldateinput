package me.hashadex.naturaldateinput.parsers.templates;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

public abstract class WeekdayParser extends Parser {
    private final Map<String, DayOfWeek> weekdayMap;

    protected WeekdayParser(String regex, Map<String, DayOfWeek> weekdayMap, int flags) {
        super(regex, flags);

        this.weekdayMap = weekdayMap;
    }

    protected WeekdayParser(String regex, Map<String, DayOfWeek> weekdayMap) {
        super(regex);

        this.weekdayMap = weekdayMap;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        boolean nextModifier = false;

        if (match.group("nextmodifier") != null) {
            nextModifier = true;
        }

        DayOfWeek weekday = weekdayMap.get(match.group("weekday"));

        TemporalAdjuster adjuster;
        if (nextModifier) {
            adjuster = TemporalAdjusters.next(weekday);
        } else {
            adjuster = TemporalAdjusters.nextOrSame(weekday);
        }

        LocalDate result = reference.toLocalDate().with(adjuster);

        return Optional.of(
            new ParsedComponentBuilder(reference, source, match).start(result).build()
        );
    }
}
