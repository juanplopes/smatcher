package net.juanlopes.smatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatcher implements StringMatcher {
    @Override
    public Instance create(String needle) {
        Pattern pattern = Pattern.compile(Pattern.quote(needle));
        return (haystack, fromIndex) -> {
            Matcher matcher = pattern.matcher(haystack);
            if (!matcher.find(fromIndex)) return -1;
            return matcher.start();
        };
    }
}
