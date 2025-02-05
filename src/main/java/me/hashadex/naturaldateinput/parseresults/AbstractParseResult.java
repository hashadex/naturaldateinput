package me.hashadex.naturaldateinput.parseresults;

import java.time.LocalDateTime;

public abstract class AbstractParseResult {
    private LocalDateTime referenceDateTime;

    private String originalString;
    private int matchStartIndex;
    private int matchEndIndex;

    public AbstractParseResult(LocalDateTime referenceDateTime, String originalString, int matchStartIndex, int matchEndIndex) {
        this.referenceDateTime = referenceDateTime;

        this.originalString = originalString;
        this.matchStartIndex = matchStartIndex;
        this.matchEndIndex = matchEndIndex;
    }

    public LocalDateTime getReferenceDateTime() {
        return referenceDateTime;
    }

    public String getOriginalString() {
        return originalString;
    }

    public int getMatchStartIndex() {
        return matchStartIndex;
    }

    public int getMatchEndIndex() {
        return matchEndIndex;
    }

    public int getMatchLength() {
        return matchEndIndex - matchStartIndex;
    }
}
