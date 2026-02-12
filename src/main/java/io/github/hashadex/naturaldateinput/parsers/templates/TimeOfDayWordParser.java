package io.github.hashadex.naturaldateinput.parsers.templates;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import io.github.hashadex.naturaldateinput.parsers.Parser;

/**
 * Base for language parsers that handle time-of-day words like "morning", "noon".
 * <p>
 * This template parser works by looking up the time-of-day word in the map of
 * time-of-day words in your language to their {@link java.time.LocalTime LocalTimes}
 * passed to the constructor, and returns the respective <code>LocalTime</code>.
 * <p>
 * The regex for the concrete parser must contain the <code>word</code> capturing
 * group that only captures the words in the <code>timeOfDayWordMap</code>.
 * 
 * @author hashadex
 * @since 1.0.0
 */
public class TimeOfDayWordParser extends Parser {
    private final Map<String, LocalTime> timeOfDayWordMap;

    /**
     * Constructs the parser using custom regex flags. See the
     * {@link TimeOfDayWordParser class doc comment} for requirements for the
     * regexes and maps.
     * 
     * @param regex            Regex for the parser
     * @param namedGroupMap    Map of capturing groups' names to their indexes
     * @param timeOfDayWordMap Map of time-of-day words to their meanings, as
     *                         <code>LocalDate</code>
     * @param flags            Bit mask of the regex flags that will be passed
     *                         to {@link java.util.regex.Pattern#compile(String, int)}
     * @since 2.0.0
     */
    public TimeOfDayWordParser(
        String regex,
        Map<String, Integer> namedGroupMap,
        Map<String, LocalTime> timeOfDayWordMap,
        int flags
    ) {
        super(regex, namedGroupMap, flags);

        this.timeOfDayWordMap = timeOfDayWordMap;
    }

    /**
     * Constructs the parser using default regex flags. See the
     * {@link TimeOfDayWordParser class doc comment} for requirements for the
     * regexes and maps.
     * 
     * @param regex            Regex for the parser
     * @param namedGroupMap    Map of capturing groups' names to their indexes
     * @param timeOfDayWordMap Map of time-of-day words to their meanings, as
     *                         <code>LocalDate</code>
     * @since 2.0.0
     */
    public TimeOfDayWordParser(
        String regex,
        Map<String, Integer> namedGroupMap,
        Map<String, LocalTime> timeOfDayWordMap
    ) {
        super(regex, namedGroupMap);

        this.timeOfDayWordMap = timeOfDayWordMap;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        LocalTime result = timeOfDayWordMap.get(match.group(namedGroupMap.get("word")).toLowerCase());

        return Optional.of(
            new ParsedComponentBuilder(reference, source, match).time(result).build()
        );
    }
}