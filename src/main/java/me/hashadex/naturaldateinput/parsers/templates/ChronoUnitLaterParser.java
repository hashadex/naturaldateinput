package me.hashadex.naturaldateinput.parsers.templates;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

public abstract class ChronoUnitLaterParser extends Parser {
    private final Map<String, ChronoUnit> chronoUnitMap;
    private final Map<String, Integer> cardinalNumberMap;

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

    protected ChronoUnitLaterParser(
        String regex,
        Map<String, ChronoUnit> chronoUnitMap,
        Map<String, Integer> cardinalNumberMap
    ) {
        super(regex);

        this.chronoUnitMap = chronoUnitMap;
        this.cardinalNumberMap = cardinalNumberMap;
    }

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
