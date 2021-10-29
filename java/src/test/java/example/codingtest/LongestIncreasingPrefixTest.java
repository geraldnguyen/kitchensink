package example.codingtest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongestIncreasingPrefixTest {

    @ParameterizedTest
    @MethodSource("testCases")
    void lis(List<Integer> input, List<Integer> expected) {
        assertEquals(
            expected,
            LongestIncreasingPrefix.lis(input)
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void lis2(List<Integer> input, List<Integer> expected) {
        assertEquals(
            expected,
            LongestIncreasingPrefix.lis2(input)
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void lis3_stream(List<Integer> input, List<Integer> expected) {
        assertEquals(
            expected,
            LongestIncreasingPrefix.lis3Stream(input)
        );
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
            Arguments.of(List.of(), List.of()),
            Arguments.of(List.of(1), List.of(1)),
            Arguments.of(List.of(1, 2, 3), List.of(1, 2, 3)),
            Arguments.of(List.of(1, 3, 2), List.of(1, 3)),
            Arguments.of(List.of(1, 3, 3), List.of(1, 3)),
            Arguments.of(List.of(3, 2, 2), List.of(3)),
            Arguments.of(List.of(3, 2, 10), List.of(3))
        );
    }
}