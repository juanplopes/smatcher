package net.juanlopes.smatcher.benchmark;

import com.google.common.base.Strings;
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

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Warmup(iterations = 5, time = 1000, timeUnit = MILLISECONDS)
@Measurement(iterations = 3, time = 5000, timeUnit = MILLISECONDS)
@Fork(value = 1)
public class AllMatchesBenchmark {
    @State(Scope.Thread)
    public static class CodecState {
        @Param({"1000"})
        public String size;
        @Param({"KMP", "RABIN_KARP", "DEFAULT", "REGEX"})
        public String type;
        private StringMatcher matcher;
        private String s1;
        private String s2;

        @Setup(Level.Trial)
        public void setup() throws Exception {
            int size = Integer.parseInt(this.size);
            s1 = Strings.repeat(
                    Strings.repeat("aa", size) + "b" + Strings.repeat("aa", size), size);
            s2 = Strings.repeat("a", size) + "b" + Strings.repeat("a", size);
            matcher = MatcherType.valueOf(type).getMatcher();
        }
    }

    @Benchmark
    public void indexAt(CodecState state) {
        StringMatcher.Instance instance = state.matcher.create(state.s2);
        int start = 0;
        while (true) {
            int index = instance.indexAt(state.s1, start);
            if (index < 0) break;
            start = index + state.s2.length();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AllMatchesBenchmark.class.getSimpleName())
                .jvmArgsPrepend()
                //.addProfiler(GCProfiler.class)
                .build();

        new Runner(opt).run();
    }
}

