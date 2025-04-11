package me.hashadex.naturaldateinput.parsers.templates;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

public abstract class RelativeWordParser extends Parser {
    private final Map<String, Integer> relativeWordOffsetMap;

    protected RelativeWordParser(String regex, Map<String, Integer> relativeWordOffsetMap, int flags) {
        super(regex, flags);

        this.relativeWordOffsetMap = relativeWordOffsetMap;
    }

    protected RelativeWordParser(String regex, Map<String, Integer> relativeWordOffsetMap) {
        super(regex);

        this.relativeWordOffsetMap = relativeWordOffsetMap;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        LocalDate result = reference.toLocalDate().plusDays(
            relativeWordOffsetMap.get(match.group("word"))
        );

        return Optional.of(
            new ParsedComponentBuilder(reference, source, match).start(result).build()
        );
    }
}
