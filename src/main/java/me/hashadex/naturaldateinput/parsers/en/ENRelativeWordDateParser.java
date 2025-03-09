package me.hashadex.naturaldateinput.parsers.en;

import java.util.HashMap;

import me.hashadex.naturaldateinput.parsers.templates.RelativeWordDateParser;

public class ENRelativeWordDateParser extends RelativeWordDateParser {
    private static HashMap<String, Integer> wordOffsets = new HashMap<>();

    static {
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
        super(wordOffsets);
    }
}
