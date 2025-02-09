package me.hashadex.naturaldateinput;

import java.time.LocalDateTime;
import java.util.regex.MatchResult;

public record ParseResult<T>(LocalDateTime reference, String source, MatchResult matchInfo, T result) {
    public int getMatchStartIndex() {
        return matchInfo.start();
    }

    public int getMatchEndIndex() {
        return matchInfo.end();
    }
}
