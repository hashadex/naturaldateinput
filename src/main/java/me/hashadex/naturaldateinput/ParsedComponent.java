package me.hashadex.naturaldateinput;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.MatchResult;

public class ParsedComponent {
    private final LocalDateTime reference;
    private final String source;
    private final MatchResult matchInfo;

    private final LocalDate date;
    private final LocalTime time;

    private ParsedComponent(LocalDateTime reference, String source, MatchResult matchInfo, LocalDate date, LocalTime time) {
        Objects.requireNonNull(reference);
        Objects.requireNonNull(source);
        Objects.requireNonNull(matchInfo);

        this.reference = reference;
        this.source = source;
        this.matchInfo = matchInfo;

        this.date = date;
        this.time = time;
    }

    public ParsedComponent(LocalDateTime reference, String source, MatchResult matchInfo, LocalDate date) {
        this(reference, source, matchInfo, date, null);

        Objects.requireNonNull(date);
    }

    public ParsedComponent(LocalDateTime reference, String source, MatchResult matchInfo, LocalTime time) {
        this(reference, source, matchInfo, null, time);

        Objects.requireNonNull(time);
    }

    public ParsedComponent(LocalDateTime reference, String source, MatchResult matchInfo, LocalDateTime dateTime) {
        this(reference, source, matchInfo, dateTime.toLocalDate(), dateTime.toLocalTime());
    }

    public LocalDateTime getReference() {
        return reference;
    }

    public String getSource() {
        return source;
    }

    public String getText() {
        return matchInfo.group();
    }

    public int getStartIndex() {
        return matchInfo.start();
    }

    public int getEndIndex() {
        return matchInfo.end();
    }

    public MatchResult getMatchInfo() {
        return matchInfo;
    }

    public Optional<LocalDate> getDate() {
        return Optional.ofNullable(date);
    }

    public Optional<LocalTime> getTime() {
        return Optional.ofNullable(time);
    }
}
