package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDateTime;

import me.hashadex.naturaldateinput.parseresults.DateParseResult;

public abstract class DateParser extends Parser {
    public DateParser(String regex, int flags) {
        super(regex, flags);
    }

    public abstract DateParseResult[] parse(String text, LocalDateTime reference);
}
