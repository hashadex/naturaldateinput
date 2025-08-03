package me.hashadex.naturaldateinput.parsers.templates;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

/**
 * Base for language parsers that handle date/time expressions in the form of
 * {@code "in <amount> <timeunit>"}, for example "in 10 days".
 * <p>
 * This template parser works by adding the specified amount of specified units
 * to the reference date.
 * <p>
 * Support for using cardinal numbers (like "one", "two", "three") instead of
 * integers as the amount can optionally be enabled by passing a
 * {@link java.util.Map Map} of the names of the numbers in your language to
 * their respective integers as the <code>cardinalNumberMap</code> parameter in
 * the constructor call.
 * <p>
 * The regex for the concrete parser must contain <code>amount</code> and
 * <code>unit</code> named capturing groups. The <code>amount</code> capturing
 * group must match digits and optionally, cardinal numbers from the <code>
 * cardinalNumberMap</code> if you wish to support them. The <code>unit</code>
 * group must only match timeunits from the <code>chronoUnitMap</code>, a map
 * of the names of timeunits in your language to their respective
 * {@link java.time.temporal.ChronoUnit ChronoUnits}, passed to the constructor.
 * 
 * @author hashadex
 * @since 1.0.0
 */
public abstract class ChronoUnitLaterParser extends Parser {
    private final Map<String, ChronoUnit> chronoUnitMap;
    private final Map<String, Integer> cardinalNumberMap;

    /**
     * Constructs the parser with cardinal number support and custom regex flags.
     * See the {@link ChronoUnitLaterParser class doc comment} for requirements
     * for the regexes and maps.
     * 
     * @param regex             Regex for the parser
     * @param chronoUnitMap     Map of the names of timeunits in your language to
     *                          their respective
     *                          {@link java.time.temporal.ChronoUnit ChronoUnits}
     * @param cardinalNumberMap Map of the names of cardinal numbers in your
     *                          language to their respective integers
     * @param flags             Bit mask of the regex flags that will be passed
     *                          to {@link java.util.regex.Pattern#compile(String, int)}
     * @since 1.0.0
     */
    protected ChronoUnitLaterParser(
        String regex,
        Map<String, ChronoUnit> chronoUnitMap,
        Map<String, Integer> cardinalNumberMap,
        int flags
    ) {
        super(regex, flags);

        this.chronoUnitMap = chronoUnitMap;
        this.cardinalNumberMap = cardinalNumberMap;
    }

    /**
     * Constructs the parser with cardinal number support and default regex
     * flags. See the {@link ChronoUnitLaterParser class doc comment} for
     * requirements for the regexes and maps.
     * 
     * @param regex             Regex for the parser
     * @param chronoUnitMap     Map of the names of timeunits in your language to
     *                          their respective
     *                          {@link java.time.temporal.ChronoUnit ChronoUnits}
     * @param cardinalNumberMap Map of the names of cardinal numbers in your
     *                          language to their respective integers
     * @since 1.0.0
     */
    protected ChronoUnitLaterParser(
        String regex,
        Map<String, ChronoUnit> chronoUnitMap,
        Map<String, Integer> cardinalNumberMap
    ) {
        super(regex);

        this.chronoUnitMap = chronoUnitMap;
        this.cardinalNumberMap = cardinalNumberMap;
    }

    /**
     * Constructs the parser without cardinal number support and default regex
     * flags. See the {@link ChronoUnitLaterParser class doc comment} for
     * requirements for the regexes and maps.
     * 
     * @param regex         Regex for the parser
     * @param chronoUnitMap Map of the names of timeunits in your language to
     *                      their respective
     *                      {@link java.time.temporal.ChronoUnit ChronoUnits}
     * @since 1.0.0
     */
    protected ChronoUnitLaterParser(String regex, Map<String, ChronoUnit> chronoUnitMap) {
        this(regex, chronoUnitMap, Map.of());
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        int amount;
        if (cardinalNumberMap.containsKey(match.group("amount"))) {
            amount = cardinalNumberMap.get(match.group("amount"));
        } else {
            try {
                amount = Integer.parseInt(match.group("amount"));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }

        ChronoUnit unit = chronoUnitMap.get(match.group("unit").toLowerCase());

        LocalDateTime result;
        try {
            result = reference.plus(amount, unit);
        } catch (DateTimeException e) {
            return Optional.empty();
        }

        ParsedComponentBuilder builder = new ParsedComponentBuilder(reference, source, match);
        if (unit.isDateBased()) {
            builder.date(result.toLocalDate());
        } else {
            builder.dateTime(result);
        }

        return Optional.of(builder.build());
    }
}
