package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDateTime;

import me.hashadex.naturaldateinput.parseresults.TimeParseResult;

public abstract class TimeParser extends Parser {
    public TimeParser(String regex, int flags) {
        super(regex, flags);
    }

    public abstract TimeParseResult[] parse(String text, LocalDateTime reference);
}
