package io.github.hashadex.naturaldateinput.parsers.en;

import java.util.Map;

import io.github.hashadex.naturaldateinput.parsers.templates.RelativeWordParser;

/**
 * English parser that handles relative words like "today", "tomorrow" and
 * "yesterday". Abbreviations of these words are also supported: "ytd" for
 * yesterday, "tod" for today, "tmrw" and "tmr" for tomorrow.
 * 
 * @author hashadex
 * @since 1.0.0
 */
public final class ENRelativeWordParser extends RelativeWordParser {
    /**
     * Constructs the parser.
     * 
     * @since 1.0.0
     */
    public ENRelativeWordParser() {
        super(
            """
            (?<=^|\\s)  # Left boundary check
            (?<word>%s) # Relative word (like today, tomorrow, etc.)
            (?=$|\\s)   # Right boundary check
            """.formatted(
                toRegexAlternate(ENConstants.relativeWordOffsetMap.keySet())
            ),
            Map.of("word", 1),
            ENConstants.relativeWordOffsetMap
        );
    }
}
