package net.juanlopes.smatcher.benchmark;

import com.google.common.base.Strings;
import net.juanlopes.smatcher.DefaultMatcher;
import net.juanlopes.smatcher.KMPMatcher;
import net.juanlopes.smatcher.RabinKarpMatcher;
import net.juanlopes.smatcher.RegexMatcher;
import net.juanlopes.smatcher.StringMatcher;

public class TimingPlayground {
    public static void main(String[] args) {
        int total = runTest(1000000, new DefaultMatcher(), new RegexMatcher(), new RabinKarpMatcher(), new KMPMatcher());
        System.out.println(total);
    }

    private static int runTest(int max, StringMatcher... matchers) {
        int total = 0;
        System.out.printf("%10s", "n");
        for (StringMatcher matcher : matchers) {
            System.out.printf("%12s", matcher.getClass().getSimpleName().replace("Matcher", ""));
        }
        System.out.println();

        for (int i = 0; i < max; i += 10000) {
            String s1 = Strings.repeat("aa", i) + "b" + Strings.repeat("aa", i);
            String s2 = Strings.repeat("a", i) + "b" + Strings.repeat("a", i);

            System.out.printf("%10d", i);
            for (StringMatcher matcher : matchers) {
                long start1 = System.nanoTime();
                total += matcher.create(s2).indexAt(s1);
                long end1 = System.nanoTime() - start1;

                System.out.printf("%12.6f", end1 / 1e9);
            }
            System.out.println();
        }
        return total;
    }
}