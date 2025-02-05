package me.hashadex.naturaldateinput.parseresults;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeParseResult extends AbstractParseResult {
    private LocalTime resultTime;

    public TimeParseResult(LocalDateTime referenceDateTime, String originalString, int matchStartIndex, int matchEndIndex, LocalTime resultTime) {
        super(referenceDateTime, originalString, matchStartIndex, matchEndIndex);

        this.resultTime = resultTime;
    }

    public LocalTime getResultTime() {
        return resultTime;
    }
}
