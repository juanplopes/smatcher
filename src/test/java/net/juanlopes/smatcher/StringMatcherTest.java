package net.juanlopes.smatcher;


import com.google.common.io.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

import static com.google.common.base.Strings.repeat;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class StringMatcherTest {
    private final StringMatcher matcher;

    public StringMatcherTest(MatcherType matcher) {
        this.matcher = matcher.getMatcher();
    }

    @Test
    public void testSimple() {
        assertThat(matcher.create("def").indexAt("abcdefghij")).isEqualTo(3);
    }

    @Test
    public void testSimpleWithStart() {
        assertThat(matcher.create("aaaaaab").indexAt("aaaaaabaaaaaab", 1)).isEqualTo(7);
    }

    @Test
    public void testFindAllInstances() {
        String needle = repeat("a", 99) + "b";
        String haystack = repeat(needle, 42);

        StringMatcher.Instance instance = matcher.create(needle);
        int start = 0;
        for (int i = 0; i < 42; i++) {
            int index = instance.indexAt(haystack, start);
            assertThat(index).isEqualTo(i * 100);
            start = index + 1;
        }
        assertThat(instance.indexAt(haystack, 4200)).isEqualTo(-1);
    }

    @Test
    public void testCenterInstance() {
        String needle = repeat("a", 99) + "b" + repeat("a", 99);
        String haystack = repeat("a", 199) + "b" + repeat("a", 199);

        assertThat(matcher.create(needle).indexAt(haystack)).isEqualTo(100);
    }

    @Test
    public void testNotFind() {
        String needle = repeat("a", 99) + "b";
        String haystack = repeat("a", 10000);

        StringMatcher.Instance instance = matcher.create(needle);
        assertThat(instance.indexAt(haystack)).isEqualTo(-1);
    }

    @Test
    public void testFindEmptyString() {
        String haystack = repeat("z", 199);
        assertThat(matcher.create("").indexAt(haystack)).isEqualTo(0);
        assertThat(matcher.create("").indexAt(haystack, 42)).isEqualTo(42);
        assertThat(matcher.create("").indexAt(haystack, 199)).isEqualTo(199);
    }

    @Test
    public void testNotFindAnyCharacter() {
        String needle = repeat("a", 10);
        String haystack = repeat("z", 199);

        assertThat(matcher.create(needle).indexAt(haystack)).isEqualTo(-1);
    }

    @Test
    public void testNotFindSingleChar() {
        String needle = "a";
        String haystack = repeat("z", 199);

        assertThat(matcher.create(needle).indexAt(haystack)).isEqualTo(-1);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<?> parameters() {
        return Arrays.asList(
                MatcherType.KMP,
                MatcherType.RABIN_KARP,
                MatcherType.DEFAULT,
                MatcherType.REGEX);
    }
}
