package net.juanlopes.smatcher;

public class DefaultMatcher implements StringMatcher {
    @Override
    public Instance create(String needle) {
        return (haystack, fromIndex) -> haystack.indexOf(needle, fromIndex);
    }
}
