package example.codereview;

import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CodeReview1Test {

    @Test
    void calculate() {
        var tcs = List.of(
            Pair.of("a", ""),
            Pair.of("ab", ""),
            Pair.of("aa", "a"),
            Pair.of("aaa", "a"),
            Pair.of("aaaa", "aa"),
            Pair.of("aaaaa", "aa"),
            Pair.of("abab", ""),
            Pair.of("abba", "ab"),
            Pair.of("abxasdsba", "ab"),
            Pair.of("fsfs34343222sff", "f")
        );

        CodeReview1 codeReview1 = new CodeReview1();
        tcs.forEach(tc -> {
            assertEquals(tc.getSecond(), codeReview1.calculate(tc.getFirst()),
                String.format("Expect calculate(%s) = `%s`", tc.getFirst(), tc.getSecond()));
        });
    }
}