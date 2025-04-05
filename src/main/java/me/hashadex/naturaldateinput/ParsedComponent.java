package me.hashadex.naturaldateinput;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.regex.MatchResult;

public class ParsedComponent {
    private final LocalDateTime reference;

    private final String source;
    private final int startIndex;
    private final int endIndex;

    private final LocalDate startDate;
    private final LocalTime startTime;

    private final LocalDate endDate;
    private final LocalTime endTime;

    public static class Builder {
        private LocalDateTime reference;

        private String source;
        private int startIndex;
        private int endIndex;

        private LocalDate startDate;
        private LocalTime startTime;

        private LocalDate endDate;
        private LocalTime endTime;

        public Builder(LocalDateTime reference, String source, int startIndex, int endIndex) {
            this.reference = reference;

            this.source = source;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public Builder(LocalDateTime reference, String source, MatchResult matchResult) {
            this(reference, source, matchResult.start(), matchResult.end());
        }

        public Builder start(LocalDate start) {
            this.startDate = start;

            return this;
        }

        public Builder start(LocalTime start) {
            this.startTime = start;

            return this;
        }

        public Builder start(LocalDateTime start) {
            this.start(start.toLocalDate());
            this.start(start.toLocalTime());

            return this;
        }

        public Builder end(LocalDate end) {
            this.endDate = end;

            return this;
        }

        public Builder end(LocalTime end) {
            this.endTime = end;

            return this;
        }

        public Builder end(LocalDateTime end) {
            this.end(end.toLocalDate());
            this.end(end.toLocalTime());

            return this;
        }

        public ParsedComponent build() throws IllegalArgumentException {
            if (
                startDate == null &&
                startTime == null &&
                endDate == null &&
                endTime == null
            ) {
                throw new IllegalArgumentException("At least one date/time field must be non-null");
            }

            return new ParsedComponent(this);
        }
    }

    private ParsedComponent(Builder builder) {
        this.reference = builder.reference;

        this.source = builder.source;
        this.startIndex = builder.startIndex;
        this.endIndex = builder.endIndex;

        this.startDate = builder.startDate;
        this.startTime = builder.startTime;

        this.endDate = builder.endDate;
        this.endTime = builder.endTime;
    }

    public LocalDateTime getReference() {
        return reference;
    }

    public String getSource() {
        return source;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public String getText() {
        return source.substring(startIndex, endIndex);
    }

    public Optional<LocalDate> getStartDate() {
        return Optional.ofNullable(startDate);
    }

    public Optional<LocalTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    public Optional<LocalDate> getEndDate() {
        return Optional.ofNullable(endDate);
    }

    public Optional<LocalTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }
}
