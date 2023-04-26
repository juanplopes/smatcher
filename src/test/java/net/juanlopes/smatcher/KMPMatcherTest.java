package net.juanlopes.smatcher;

import org.junit.Test;

public class KMPMatcherTest {
    @Test
    public void test() {
        KMPMatcher m = new KMPMatcher();
        m.create("aabaab").indexAt("aaaaaabaaaaaa");
    }
}