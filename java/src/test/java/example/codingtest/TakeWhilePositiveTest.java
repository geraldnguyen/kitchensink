package example.codingtest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TakeWhilePositiveTest {

    @ParameterizedTest
    @MethodSource("testCases")
    void takeWhile1(List<Integer> list, List<Integer> expected) {
        assertEquals(
            expected,
            TakeWhilePositive.takeWhile1(list)
        );
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
            List.<Integer>of(),
            List.of(1),
            List.of(1, 2, 3),
            List.of(1, 2, 3, -1),
            List.of(1, 2, -1, 3),
            List.of(1, 2, 0, 3),
            List.of(1, 2, 4, 0),
            List.of(0, 2, 4, 0)
        ).map(ints -> Arguments.of(
            ints,
            ints.stream().takeWhile(i -> i > 0).collect(Collectors.toList()))
        );
    }
}