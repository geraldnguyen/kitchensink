package example.codingtest;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupByAndAggregateTest {
    public static final String MATH = "Math";
    public static final String SCIENCE = "Science";
    public static final String ALICE = "Alice";
    public static final String BOB = "Bob";

    @Data
    @Builder
    static class TestScore {
        private String studentName;
        private String subject;
        private int score;
    }

    private final TestScore ALICE_MATH_80 = TestScore.builder()
            .studentName(ALICE).subject(MATH).score(80)
            .build();
    private final TestScore ALICE_SCIENCE_100 = TestScore.builder()
            .studentName(ALICE).subject(SCIENCE).score(100)
            .build();
    private final TestScore BOB_MATH_78 = TestScore.builder()
            .studentName(BOB).subject(MATH).score(78)
            .build();
    private final List<TestScore> testScores = List.of(
            ALICE_MATH_80,
            ALICE_SCIENCE_100,
            BOB_MATH_78
    );

    @Test
    void toMap_solution() {
        assertEquals(
                Map.of(
                        ALICE, ALICE_SCIENCE_100,
                        BOB, BOB_MATH_78
                ),
                testScores.stream().collect(toMap(
                        TestScore::getStudentName,
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(TestScore::getScore))
                ))
        );
    }

    @Test
    void groupingBy_solution1_unwrap_Optional() {
        Map<String,TestScore> expectedResult = Map.of(
                ALICE, ALICE_SCIENCE_100,
                BOB, BOB_MATH_78
        );

        Map<String, Optional<TestScore>> intermediateResult = testScores.stream().collect(groupingBy(
                TestScore::getStudentName,
                maxBy(Comparator.comparing(TestScore::getScore))
        ));
        Map<String, TestScore> actualResult = new HashMap<>();
        intermediateResult.forEach((studentName, optionalWrapper) -> {
            actualResult.put(studentName, optionalWrapper.get());
        });

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void groupingBy_solution2_implement_your_own_collector() {
        var comparingTestScore = BinaryOperator.maxBy(Comparator.comparing(TestScore::getScore));
        var maxByTestScore = Collector.of(
                () -> new TestScore[1],
                (a, t) -> { if (a[0] == null) {a[0] = (TestScore) t;} a[0] = comparingTestScore.apply(a[0], (TestScore) t); },
                (a, b) -> { a[0] = comparingTestScore.apply(a[0], b[0]); return a; },
                a -> a[0]
        );

        assertEquals(
                Map.of(
                        ALICE, ALICE_SCIENCE_100,
                        BOB, BOB_MATH_78
                ),
                testScores.stream().collect(groupingBy(
                        TestScore::getStudentName,
                        maxByTestScore
                ))
        );
    }

    <T> BinaryOperator<T> nonNull(BinaryOperator<T> operator) {
        return (a, b) -> {
            if (a == null && b == null) {
                throw new RuntimeException();
            }
            else if (a == null) return b;
            else if (b == null) return a;
            else return operator.apply(a, b);
        };
    }

    @Test
    void groupingBy_solution3_handle_the_null_initialValue() {
        var comparator = BinaryOperator.maxBy(Comparator.comparing(TestScore::getScore));
        var maxByTestScore = reducing(null, nonNull(comparator));

        assertEquals(
                Map.of(
                        ALICE, ALICE_SCIENCE_100,
                        BOB, BOB_MATH_78
                ),
                testScores.stream().collect(groupingBy(
                        TestScore::getStudentName,
                        maxByTestScore
                ))
        );
    }
}
