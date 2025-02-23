package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import me.hashadex.naturaldateinput.ParseResult;

public abstract class TimeParser extends Parser {
    protected TimeParser(String regex, int flags) {
        super(regex, flags);
    }

    protected TimeParser(String regex) {
        super(regex);
    }

    public abstract ArrayList<ParseResult<LocalTime>> parse(String input, LocalDateTime reference);
}
