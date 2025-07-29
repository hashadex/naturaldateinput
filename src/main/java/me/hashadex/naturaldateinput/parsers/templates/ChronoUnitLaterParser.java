package me.hashadex.naturaldateinput.parsers.templates;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

public abstract class ChronoUnitLaterParser extends Parser {
    private final Map<String, ChronoUnit> chronoUnitMap;

    protected ChronoUnitLaterParser(String regex, Map<String, ChronoUnit> chronoUnitMap, int flags) {
        super(regex, flags);

        this.chronoUnitMap = chronoUnitMap;
    }

    protected ChronoUnitLaterParser(String regex, Map<String, ChronoUnit> chronoUnitMap) {
        super(regex);

        this.chronoUnitMap = chronoUnitMap;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        int amount;
        try {
            amount = Integer.parseInt(match.group("amount")); // TODO: support for cardinal numbers (one, two, etc.)
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        ChronoUnit unit = chronoUnitMap.get(match.group("unit").toLowerCase());

        LocalDateTime result = reference.plus(amount, unit);

        ParsedComponentBuilder builder = new ParsedComponentBuilder(reference, source, match);
        if (unit.isDateBased()) {
            builder.date(result.toLocalDate());
        } else {
            builder.dateTime(result);
        }

        return Optional.of(builder.build());
    }
}
