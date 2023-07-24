package example.codingtest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FizzBuzzTest {

    @Test
    void both_Iterative_and_Stream_implementation_are_equivalent() {
        var fb = new FizzBuzz();
        assertEquals(
                fb.fizzBuzzIterative(),
                fb.fizzBuzzStream()
        );
    }

    @Test
    void fizzBuzzIterative_Defective_hasNo_FIZZBUZZ() {
        var fb = new FizzBuzz();

        assertNotEquals(
                fb.fizzBuzzStream(),
                fb.fizzBuzzIterative_defective()
        );

        assertFalse(fb.fizzBuzzIterative_defective().contains("FIZZ BUZZ"));
    }

    @Test
    void fizzBuzzStream_Defective_range_0_99() {
        var fb = new FizzBuzz();

        assertNotEquals(
                fb.fizzBuzzStream(),
                fb.fizzBuzzStream_defective()
        );

        assertEquals(
                fb.fizzBuzzStream().subList(0, FizzBuzz.MAX - 1),
                fb.fizzBuzzStream_defective().subList(1, FizzBuzz.MAX)
        );
    }
}