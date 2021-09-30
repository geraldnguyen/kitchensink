package example.codingtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LongestPrefixSuffixTest {

    @ParameterizedTest
    @CsvSource({ "aabcdaabc,aabc", "a,", "aa,a", "aaa,a", "aaaa,aa", ",", "abc,", "aac,"})
    void solve(String input, String expected) {
        input = input == null ? "" : input;
        expected = expected == null ? "" : expected;
        assertEquals(expected, LongestPrefixSuffix.solve(input));
    }
}