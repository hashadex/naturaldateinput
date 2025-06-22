package me.hashadex.naturaldateinput.parsers.templates;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.parsers.Parser;

public class TimeOfDayWordParser extends Parser {
    private final Map<String, LocalTime> timeOfDayWordMap;

    public TimeOfDayWordParser(String regex, Map<String, LocalTime> timeOfDayWordMap, int flags) {
        super(regex, flags);

        this.timeOfDayWordMap = timeOfDayWordMap;
    }

    public TimeOfDayWordParser(String regex, Map<String, LocalTime> timeOfDayWordMap) {
        super(regex);

        this.timeOfDayWordMap = timeOfDayWordMap;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        LocalTime result = timeOfDayWordMap.get(match.group("word").toLowerCase());

        return Optional.of(
            new ParsedComponentBuilder(reference, source, match).time(result).build()
        );
    }
}