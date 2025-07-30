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

    private final LocalDate date = LocalDate.of(2025, 4, 12);
    private final LocalTime time = LocalTime.of(17, 35);

    private final LocalDateTime dateTime = date.atTime(time);

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

        builder.dateTime(dateTime);

        ParsedComponent component = builder.build();

        assertAll(
            () -> assertEquals(date, component.date().get()),
            () -> assertEquals(time, component.time().get())
        );
    }

    @Test
    void build_OneDateTimeFieldNotNull_ReturnsParsedComponent() {
        ParsedComponentBuilder builder = new ParsedComponentBuilder(reference, source, matchResult);

        builder.date(date);

        ParsedComponent component = builder.build();

        assertAll(
            () -> assertEquals(date, component.date().get())
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
