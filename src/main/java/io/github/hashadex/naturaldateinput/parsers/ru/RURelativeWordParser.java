package io.github.hashadex.naturaldateinput.parsers.ru;

import java.util.Map;

import io.github.hashadex.naturaldateinput.parsers.templates.RelativeWordParser;

/**
 * Russian parser that handles relative words like "сегодня" ("today"),
 * "завтра" ("tomorrow") and "вчера" ("yesterday").
 * 
 * @author hashadex
 * @since 2.1.0
 */
public final class RURelativeWordParser extends RelativeWordParser {
    /**
     * Constructs the parser.
     * 
     * @since 2.1.0
     */
    public RURelativeWordParser() {
        super(
            """
            (?<=^|\\s)  # Left boundary check
            (?<word>%s) # Relative word (like today, tomorrow, etc.)
            (?=$|\\s)   # Right boundary check
            """.formatted(
                toRegexAlternate(RUConstants.relativeWordOffsetMap.keySet())
            ),
            Map.of("word", 1),
            RUConstants.relativeWordOffsetMap
        );
    }
}