package me.hashadex.naturaldateinput.parsers.en;

import me.hashadex.naturaldateinput.constants.ENConstants;
import me.hashadex.naturaldateinput.parsers.templates.RelativeWordParser;

public class ENRelativeWordParser extends RelativeWordParser {
    public ENRelativeWordParser() {
        super(
            """
            (?<=^|\\s)  # Left boundary check
            (?<word>%s) # Relative word (like today, tomorrow, etc.)
            (?=$|\\s)   # Right boundary check
            """.formatted(
                setToRegexAlternate(ENConstants.relativeWordOffsetMap.keySet())
            ),
            ENConstants.relativeWordOffsetMap
        );
    }
}
