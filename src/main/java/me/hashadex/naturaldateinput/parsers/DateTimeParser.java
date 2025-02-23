package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import me.hashadex.naturaldateinput.ParseResult;

public abstract class DateTimeParser extends Parser {
    protected DateTimeParser(String regex, int flags) {
        super(regex, flags);
    }

    protected DateTimeParser(String regex) {
        super(regex);
    }

    public abstract ArrayList<ParseResult<LocalDateTime>> parse(String input, LocalDateTime reference);
}
