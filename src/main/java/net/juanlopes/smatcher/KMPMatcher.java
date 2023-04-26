package net.juanlopes.smatcher;

public class KMPMatcher implements StringMatcher {
    @Override
    public Instance create(String needle) {
        return new MyInstance(needle);
    }

    private static class MyInstance implements Instance {
        private final String needle;
        private final int[] F;

        public MyInstance(String needle) {
            this.needle = needle;
            this.F = new int[needle.length() + 1];
            int i = 1, j = 0;
            while (i < needle.length()) {
                if (needle.charAt(i) == needle.charAt(j))
                    F[++i] = ++j;
                else if (j == 0)
                    F[++i] = 0;
                else
                    j = F[j];
            }
        }

        @Override
        public int indexAt(String haystack, int i) {
            int m = needle.length();
            if (m == 0) return i;
            int n = haystack.length();
            int max = n - m;

            char first = needle.charAt(0);
            for (int j = 0; i - j <= max; j = F[j]) {
                if (j == 0) {
                    while (i <= max && haystack.charAt(i) != first) i++;
                    if (i > max) break;
                }
                do {
                    j++;
                    i++;
                    if (j == m) return i - m;
                } while (needle.charAt(j) == haystack.charAt(i));
            }
            return -1;
        }
    }
}
