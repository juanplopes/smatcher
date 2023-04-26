package net.juanlopes.smatcher;

public interface StringMatcher {
    Instance create(String needle);

    interface Instance {
        default int indexAt(String haystack) {
            return indexAt(haystack, 0);
        }

        int indexAt(String haystack, int fromIndex);
    }
}
