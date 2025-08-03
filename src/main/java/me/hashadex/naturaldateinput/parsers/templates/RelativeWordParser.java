package me.hashadex.naturaldateinput.parsers.templates;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

/**
 * Base for language parsers that handle relative words like "today" and
 * "tomorrrow".
 * <p>
 * This template parser works by adding a number of days to the reference date.
 * This number, called the offset, changes depending on the relative word. For
 * example, in English the offset for "today" is 0, the offset for
 * "tomorrow" is 1 and the offset for "yesterday" is -1. These offsets are
 * stored in a map passed to the constructor as the
 * <code>relativeWordOffsetMap</code> parameter.
 * <p>
 * The regex for the concrete parser must contain the <code>word</code> named
 * capturing group. This group must only capture the relative words specified
 * in the <code>relativeWordOffsetMap</code>.
 * 
 * @author hashadex
 * @since 1.0.0
 */
public abstract class RelativeWordParser extends Parser {
    private final Map<String, Integer> relativeWordOffsetMap;

    /**
     * Constructs the parser with custom regex flags. See the
     * {@link RelativeWordParser class doc comment} for requirements for the
     * regexes and maps.
     * 
     * @param regex                 Regex for the parser
     * @param relativeWordOffsetMap Map of relative words to their offsets
     * @param flags                 Bit mask of the regex flags that will be passed
     *                              to {@link java.util.regex.Pattern#compile(String, int)}
     * @since 1.0.0
     */
    protected RelativeWordParser(String regex, Map<String, Integer> relativeWordOffsetMap, int flags) {
        super(regex, flags);

        this.relativeWordOffsetMap = relativeWordOffsetMap;
    }

    /**
     * Constructs the parser with default regex flags. See the
     * {@link RelativeWordParser class doc comment} for requirements for the
     * regexes and maps.
     * 
     * @param regex                 Regex for the parser
     * @param relativeWordOffsetMap Map of relative words to their offsets
     * @since 1.0.0
     */
    protected RelativeWordParser(String regex, Map<String, Integer> relativeWordOffsetMap) {
        super(regex);

        this.relativeWordOffsetMap = relativeWordOffsetMap;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        LocalDate result = reference.toLocalDate().plusDays(
            relativeWordOffsetMap.get(match.group("word").toLowerCase())
        );

        return Optional.of(
            new ParsedComponentBuilder(reference, source, match).date(result).build()
        );
    }
}
