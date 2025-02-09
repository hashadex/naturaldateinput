package me.hashadex.naturaldateinput.parsers;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Parser {
    private final Pattern pattern;

    protected Parser(String regex, int flags) {
        pattern = Pattern.compile(regex, flags);
    }

    protected Parser(String regex) {
        this(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    protected ArrayList<MatchResult> findAllMatches(String input) {
        Matcher matcher = pattern.matcher(input);
        ArrayList<MatchResult> matchList = new ArrayList<MatchResult>();

        while (matcher.find()) {
            matchList.add(matcher.toMatchResult());
        }

        return matchList;
    }
}
