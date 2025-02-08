package me.hashadex.naturaldateinput;

import java.time.LocalDateTime;
import java.util.regex.MatchResult;

public class ParseResult<T> {
    public final LocalDateTime reference;
    public final String source;
    public final MatchResult matchInfo;

    public final T result;

    public ParseResult(LocalDateTime reference, String source, MatchResult matchInfo, T result) {
        this.reference = reference;
        this.source = source;
        this.matchInfo = matchInfo;

        this.result = result;
    }

    public int getMatchStartIndex() {
        return matchInfo.start();
    }

    public int getMatchEndIndex() {
        return matchInfo.end();
    }
}
