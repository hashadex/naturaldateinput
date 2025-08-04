package io.github.hashadex.naturaldateinput.parsers.templates;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import io.github.hashadex.naturaldateinput.parsers.Parser;

/**
 * Base for language parsers that handle expressions like "on wednesday",
 * "on the next saturday".
 * <p>
 * This parser works by using
 * {@link java.time.temporal.TemporalAdjusters#next(DayOfWeek) next} and
 * {@link java.time.temporal.TemporalAdjusters#nextOrSame(DayOfWeek) nextOrSame}
 * {@link java.time.temporal.TemporalAdjuster TemporalAdjusters} on the
 * reference date.
 * <p>
 * The names of weekday names and their respective {@link java.time.DayOfWeek}
 * are stored in a map passed to the constructor as the <code>weekdayMap</code>
 * parameter.
 * <p>
 * The concrete parser's regex must contain a <code>weekday</code> named
 * capturing group that must only capture weekdays from the
 * <code>weekdayMap</code>. The regex may also optionally contain a
 * <code>nextmodifier</code> capturing group. If this capturing group captures
 * anything, the parser uses the <code>next</code> <code>TemporalAdjuster</code>
 * instead of <code>nextOrSame</code>
 * 
 * @author hashadex
 * @since 1.0.0
 */
public abstract class WeekdayParser extends Parser {
    private final Map<String, DayOfWeek> weekdayMap;

    /**
     * Constructs the parser using custom regex flags. See the
     * {@link WeekdayParser class doc comment} for requirements for the regexes
     * and maps.
     * 
     * @param regex      Regex for the parser
     * @param weekdayMap Map of names of weekdays in your language to their
     *                   <code>DayOfWeek</code>
     * @param flags      Bit mask of the regex flags that will be passed
     *                   to {@link java.util.regex.Pattern#compile(String, int)}
     */
    protected WeekdayParser(String regex, Map<String, DayOfWeek> weekdayMap, int flags) {
        super(regex, flags);

        this.weekdayMap = weekdayMap;
    }

    /**
     * Constructs the parser using default regex flags. See the
     * {@link WeekdayParser class doc comment} for requirements for the regexes
     * and maps.
     * 
     * @param regex      Regex for the parser
     * @param weekdayMap Map of names of weekdays in your language to their
     *                   <code>DayOfWeek</code>
     */
    protected WeekdayParser(String regex, Map<String, DayOfWeek> weekdayMap) {
        super(regex);

        this.weekdayMap = weekdayMap;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        boolean nextModifier = false;

        if (match.namedGroups().containsKey("nextmodifier") && match.group("nextmodifier") != null) {
            nextModifier = true;
        }

        DayOfWeek weekday = weekdayMap.get(match.group("weekday").toLowerCase());

        TemporalAdjuster adjuster;
        if (nextModifier) {
            adjuster = TemporalAdjusters.next(weekday);
        } else {
            adjuster = TemporalAdjusters.nextOrSame(weekday);
        }

        LocalDate result = reference.toLocalDate().with(adjuster);

        return Optional.of(
            new ParsedComponentBuilder(reference, source, match).date(result).build()
        );
    }
}
