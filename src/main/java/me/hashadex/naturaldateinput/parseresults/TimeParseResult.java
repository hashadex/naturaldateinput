package me.hashadex.naturaldateinput.parseresults;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.MatchResult;

public class TimeParseResult extends ParseResult {
    public final LocalTime result;

    public TimeParseResult(LocalDateTime reference, String source, MatchResult matchInfo, LocalTime result) {
        super(reference, source, matchInfo);

        this.result = result;
    }
}
