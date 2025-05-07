package me.hashadex.naturaldateinput.parsers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.hashadex.naturaldateinput.parsers.Parser.ParsedComponent;
import me.hashadex.naturaldateinput.parsers.Parser.ParsedComponentBuilder;

public class ParsedComponentBuilderTest {
    private final LocalDateTime reference = LocalDateTime.of(2025, 4, 12, 0, 0, 0);
    private final String source = "tomorrow";

    private final int startIndex = 0;
    private final int endIndex = 7;

    private final LocalDate startDate = LocalDate.of(2025, 4, 12);
    private final LocalTime startTime = LocalTime.of(17, 35);
    private final LocalDate endDate = LocalDate.of(2025, 4, 13);
    private final LocalTime endTime = LocalTime.of(18, 35);

    private final LocalDateTime startDateTime = startDate.atTime(startTime);
    private final LocalDateTime endDateTime = endDate.atTime(endTime);

    private MatchResult matchResult;

    @BeforeEach
    public void setup() {
        Pattern pattern = Pattern.compile(".+");
        Matcher matcher = pattern.matcher(source);
        
        matcher.find();
        matchResult = matcher.toMatchResult();
    }

    @Test
    void constructor_MatchResultParameter_ConstructsBuilder() {
        ParsedComponentBuilder builder = new ParsedComponentBuilder(reference, source, startIndex, endIndex);

        assertInstanceOf(ParsedComponentBuilder.class, builder);
    }

    @Test
    void constructor_StartAndEndIndexParameters_ConstructsBuilder() {
        ParsedComponentBuilder builder = new ParsedComponentBuilder(reference, source, matchResult);

        assertInstanceOf(ParsedComponentBuilder.class, builder);
    }

    @Test
    void constructor_NullParameters_ThrowsNullPointerException() {
        assertAll(
            () -> assertThrows(NullPointerException.class, () -> new ParsedComponentBuilder(null, null, 0, 0)),
            () -> assertThrows(NullPointerException.class, () -> new ParsedComponentBuilder(null, null, null))
        );
    }

    @Test
    void build_AllDateTimeFieldsNotNull_ReturnsParsedComponent() {
        ParsedComponentBuilder builder = new ParsedComponentBuilder(reference, source, matchResult);

        builder.start(startDateTime).end(endDateTime);

        ParsedComponent component = builder.build();

        assertAll(
            () -> assertEquals(startDate, component.getStartDate().get()),
            () -> assertEquals(startTime, component.getStartTime().get()),
            () -> assertEquals(endDate, component.getEndDate().get()),
            () -> assertEquals(endTime, component.getEndTime().get())
        );
    }

    @Test
    void build_OneDateTimeFieldNotNull_ReturnsParsedComponent() {
        ParsedComponentBuilder builder = new ParsedComponentBuilder(reference, source, matchResult);

        builder.start(startDate);

        ParsedComponent component = builder.build();

        assertAll(
            () -> assertEquals(startDate, component.getStartDate().get())
        );
    }

    @Test
    void build_AllDateTimeFieldsNull_ThrowsException() {
        ParsedComponentBuilder builder = new ParsedComponentBuilder(reference, source, matchResult);

        assertThrows(
            IllegalArgumentException.class,
            () -> builder.build()
        );
    }
}
