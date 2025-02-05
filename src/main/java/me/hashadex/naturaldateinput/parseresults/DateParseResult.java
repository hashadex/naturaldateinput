package me.hashadex.naturaldateinput.parseresults;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateParseResult extends AbstractParseResult {
    private LocalDate resultDate;

    public DateParseResult(LocalDateTime referenceDateTime, String originalString, int matchStartIndex, int matchEndIndex, LocalDate resultDate) {
        super(referenceDateTime, originalString, matchStartIndex, matchEndIndex);

        this.resultDate = resultDate;
    }

    public LocalDate getResultDate() {
        return resultDate;
    }
}
