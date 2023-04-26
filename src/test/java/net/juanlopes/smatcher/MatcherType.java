package net.juanlopes.smatcher;

public enum MatcherType {
    KMP(new KMPMatcher()),
    RABIN_KARP(new RabinKarpMatcher()),
    REGEX(new RegexMatcher()),
    DEFAULT(new DefaultMatcher());

    private final StringMatcher matcher;

    MatcherType(StringMatcher matcher) {
        this.matcher = matcher;
    }

    public StringMatcher getMatcher() {
        return matcher;
    }
}
