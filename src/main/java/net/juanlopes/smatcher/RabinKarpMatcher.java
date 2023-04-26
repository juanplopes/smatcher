package net.juanlopes.smatcher;

public class RabinKarpMatcher implements StringMatcher {
    @Override
    public Instance create(String needle) {
        return new MyInstance(needle);
    }

    private static class MyInstance implements Instance {
        private final String needle;
        private final int hash;
        private final int rolling;

        public MyInstance(String needle) {
            this.needle = needle;
            this.hash = hash(needle, 0, needle.length());

            int rolling = 1;
            for (int i = 0; i < needle.length() - 1; i++)
                rolling *= 71;

            this.rolling = rolling;
        }

        private static int hash(String needle, int from, int to) {
            int hash = 0;
            for (int i = from; i < to; i++) {
                hash *= 71;
                hash += needle.charAt(i);
            }
            return hash;
        }

        @Override
        public int indexAt(String haystack, int fromIndex) {
            if (needle.isEmpty()) return fromIndex;
            if (fromIndex + this.needle.length() >= haystack.length()) return -1;
            String needle = this.needle;
            int hash = this.hash;
            int rolling = this.rolling;

            int end = haystack.length() - needle.length();
            int currentHash = hash(haystack, fromIndex, fromIndex + needle.length() - 1);
            int ptr = fromIndex + needle.length() - 1;

            for (int i = fromIndex; i <= end; i++) {
                currentHash *= 71;
                currentHash += haystack.charAt(ptr++);

                if (currentHash == hash && compare(needle, haystack, i)) return i;
                currentHash -= rolling * haystack.charAt(i);
            }
            return -1;
        }

        private boolean compare(String needle, String haystack, int i) {
            for (int j = 0; j < needle.length(); j++) {
                if (needle.charAt(j) != haystack.charAt(j + i))
                    return false;
            }
            return true;
        }
    }
}
