package me.hashadex.naturaldateinput.parsers;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;

public abstract class ParserTest {
    protected Parser parser;
    protected LocalDateTime reference;

    @BeforeEach
    public abstract void setup();
}
