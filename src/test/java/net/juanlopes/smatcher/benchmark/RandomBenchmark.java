package net.juanlopes.smatcher.benchmark;

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

import java.util.Random;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Warmup(iterations = 5, time = 1000, timeUnit = MILLISECONDS)
@Measurement(iterations = 3, time = 5000, timeUnit = MILLISECONDS)
@Fork(value = 1)
public class RandomBenchmark {
    @State(Scope.Thread)
    public static class CodecState {
        public static final Random RANDOM = new Random();
        @Param({"1000000"})
        public String size;
        @Param({"DEFAULT", "KMP", "RABIN_KARP", "REGEX"})
        public String type;
        private StringMatcher matcher;
        private String s1;
        private String s2;
        private int total;
        private StringMatcher.Instance instance;

        @Setup(Level.Trial)
        public void setup() throws Exception {
            int size = Integer.parseInt(this.size);
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < size; i++) {
                b.append((char) ('a' + RANDOM.nextInt(26)));
            }
            s1 = b.toString();
            s2 = s1.substring(size / 10000);
            matcher = MatcherType.valueOf(type).getMatcher();
            instance = matcher.create(s2);
        }
    }

    @Benchmark
    public void indexAt(CodecState state) {
        state.total += state.instance.indexAt(state.s1);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(RandomBenchmark.class.getSimpleName())
                .jvmArgsPrepend()
                //.addProfiler(GCProfiler.class)
                .build();

        new Runner(opt).run();
    }
}

