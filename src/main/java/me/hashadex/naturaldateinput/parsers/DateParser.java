package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDateTime;

import me.hashadex.naturaldateinput.parseresults.DateParseResult;

public abstract class DateParser extends Parser {
    public DateParser(String regex, int flags) {
        super(regex, flags);
    }

    public DateParser(String regex) {
        super(regex);
    }

    public abstract ArrayList<ParseResult<LocalDate>> parse(String input, LocalDateTime reference);
}
