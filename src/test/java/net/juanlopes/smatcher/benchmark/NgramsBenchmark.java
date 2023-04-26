package net.juanlopes.smatcher.benchmark;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import net.juanlopes.smatcher.MatcherType;
import net.juanlopes.smatcher.StringMatcher;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Warmup(iterations = 5, time = 1000, timeUnit = MILLISECONDS)
@Measurement(iterations = 3, time = 5000, timeUnit = MILLISECONDS)
@Fork(value = 1)
public class NgramsBenchmark {
    @State(Scope.Thread)
    public static class CodecState {
        @Param({"NOUN"})
        public String needle;
        @Param({"DEFAULT", "KMP", "RABIN_KARP", "REGEX"})
        public String type;
        private StringMatcher matcher;
        private String s1;
        private int total;

        @Setup(Level.Trial)
        public void setup() throws Exception {
            s1 = Files.toString(new File("/Users/juan.lopes/Downloads/googlebooks-eng-all-1gram-20120701-0"), StandardCharsets.UTF_8);
            matcher = MatcherType.valueOf(type).getMatcher();
        }
    }

    @Benchmark
    public void indexAt(CodecState state) {
        StringMatcher.Instance instance = state.matcher.create(state.needle);
        int start = 0;
        while (true) {
            int index = instance.indexAt(state.s1, start);
            if (index < 0) break;
            start = index + state.needle.length();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(NgramsBenchmark.class.getSimpleName())
                .jvmArgsPrepend()
                //.addProfiler(GCProfiler.class)
                .build();

        new Runner(opt).run();
    }
}

