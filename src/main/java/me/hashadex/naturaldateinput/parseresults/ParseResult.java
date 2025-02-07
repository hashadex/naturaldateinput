package me.hashadex.naturaldateinput.parseresults;

import java.time.LocalDateTime;
import java.util.regex.MatchResult;

public abstract class ParseResult {
    public final LocalDateTime reference;
    public final String source;
    public final MatchResult matchInfo;

    protected ParseResult(LocalDateTime reference, String source, MatchResult matchInfo) {
        this.reference = reference;
        this.source = source;
        this.matchInfo = matchInfo;
    }

    public int getMatchStartIndex() {
        return matchInfo.start();
    }

    public int getMatchEndIndex() {
        return matchInfo.end();
    }
}
