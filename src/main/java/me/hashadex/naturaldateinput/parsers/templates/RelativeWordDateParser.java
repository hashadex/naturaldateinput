package me.hashadex.naturaldateinput.parsers.templates;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.ParsedComponent;
import me.hashadex.naturaldateinput.parsers.Parser;

public abstract class RelativeWordDateParser extends Parser {
    private HashMap<String, Integer> wordOffsets;

    protected RelativeWordDateParser(HashMap<String, Integer> wordOffsets) {
        super(
            """
            (?<=^|\\s) # Left boundary check
            (?<word>%s)
            (?=$|\\s) # Right boundary check
            """.formatted(setToRegexAlternate(wordOffsets.keySet()))
        );

        this.wordOffsets = wordOffsets;
    }

    @Override
    protected Optional<ParsedComponent> parseMatch(MatchResult match, LocalDateTime reference, String source) {
        String word = match.group("word").toLowerCase();
        int offset = wordOffsets.get(word);
        LocalDate result = reference.toLocalDate().plusDays(offset);

        return Optional.of(
            new ParsedComponent(reference, source, match, result)
        );
    }
}
