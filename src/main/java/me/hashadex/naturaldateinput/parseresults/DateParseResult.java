package me.hashadex.naturaldateinput.parseresults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.MatchResult;

public class DateParseResult extends ParseResult {
    public final LocalDate result;

    public DateParseResult(LocalDateTime reference, String source, MatchResult matchInfo, LocalDate result) {
        super(reference, source, matchInfo);

        this.result = result;
    }
}
