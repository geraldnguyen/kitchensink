package example.codingtest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DistinctCharactersTest {

    @ParameterizedTest
    @CsvSource({
//        ",",
        "a,a",
        "aaaa,a",
        "abc,abc",
        "abbbccbbcc,abc",
        "hello worlds,dehlorsw"
    })
    void distinctChars(String input, String expected) {
        assertEquals(
            Stream.of(expected.split("")).map(c -> c.charAt(0)).collect(Collectors.toList()),
            DistinctCharacters.distinctChars(input)
        );

        assertEquals(
            Stream.of(expected.split("")).map(c -> c.charAt(0)).collect(Collectors.toList()),
            DistinctCharacters.distinctCharsStream(input)
        );

        assertEquals(
            Stream.of(expected.split("")).map(c -> c.charAt(0)).collect(Collectors.toList()),
            DistinctCharacters.distinctCharsStream2(input)
        );

    }
}