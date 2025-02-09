package me.hashadex.naturaldateinput.parsers.en;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.MatchResult;

import me.hashadex.naturaldateinput.ParseResult;
import me.hashadex.naturaldateinput.parsers.DateParser;

public class ENRelativeWordDateParser extends DateParser {
    private static HashMap<String, Integer> wordOffsets = new HashMap<>();

    static {
        // Init wordOffsets
        // 0 = today
        // 1 = tomorrow (today + 1 day)
        // -1 = yesterday (today - 1 day)

        // today
        wordOffsets.put("today", 0);
        wordOffsets.put("tod", 0);
        // tomorrow
        wordOffsets.put("tomorrow", 1);
        wordOffsets.put("tmr", 1);
        wordOffsets.put("tmrw", 1);
        // yesterday
        wordOffsets.put("yesterday", -1);
        wordOffsets.put("ytd", -1);
    }

    public ENRelativeWordDateParser() {
        super(
            "\\b(" + keySetToRegexAlternate(wordOffsets.keySet()) + ")\\b"
        );
    }

    @Override
    public ArrayList<ParseResult<LocalDate>> parse(String input, LocalDateTime reference) {
        ArrayList<ParseResult<LocalDate>> results = new ArrayList<>();
        ArrayList<MatchResult> matches = findAllMatches(input);

        for (MatchResult match : matches) {
            results.add(
                new ParseResult<LocalDate>(
                    reference,
                    input,
                    match,
                    reference.plusDays(wordOffsets.get(match.group(1).toLowerCase())).toLocalDate()
                )
            );
        }

        return results;
    }
}
