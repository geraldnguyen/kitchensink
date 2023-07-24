package example.codingtest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FizzBuzz {
    public static int MAX = 100;

    public List<String> fizzBuzzIterative() {
        var fizzBuzz = new ArrayList<String>();

        for (int i = 0; i < MAX; i++) {
            int number = i + 1;
            if (number % 15 == 0) {
                fizzBuzz.add("FIZZ BUZZ");
            } else if (number % 3 == 0) {
                fizzBuzz.add("FIZZ");
            } else if (number % 5 == 0) {
                fizzBuzz.add("BUZZ");
            } else {
                fizzBuzz.add(String.valueOf(number));
            }
        }
        return fizzBuzz;
    }

    public List<String> fizzBuzzStream() {
        return IntStream.range(0, MAX)
                .map(i -> i + 1)
                .mapToObj(number -> {
                    if (number % 15 == 0) {
                        return "FIZZ BUZZ";
                    } else if (number % 3 == 0) {
                        return "FIZZ";
                    } else if (number % 5 == 0) {
                        return "BUZZ";
                    } else {
                        return String.valueOf(number);
                    }
                })
                .collect(Collectors.toList());
    }

    // defective implementation
    public List<String> fizzBuzzIterative_defective() {
        var fizzBuzz = new ArrayList<String>();

        for (int i = 0; i < MAX; i++) {
            int number = i + 1;
            if (number % 3 == 0) {
                fizzBuzz.add("FIZZ");
            } else if (number % 5 == 0) {
                fizzBuzz.add("BUZZ");
            } else if (number % 15 == 0) {
                fizzBuzz.add("FIZZ BUZZ");
            } else {
                fizzBuzz.add(String.valueOf(number));
            }
        }
        return fizzBuzz;
    }

    public List<String> fizzBuzzStream_defective() {
        return IntStream.range(0, MAX)
                .map(i -> i++)
                .mapToObj(number -> {
                    if (number % 15 == 0) {
                        return "FIZZ BUZZ";
                    } else if (number % 3 == 0) {
                        return "FIZZ";
                    } else if (number % 5 == 0) {
                        return "BUZZ";
                    } else {
                        return String.valueOf(number);
                    }
                })
                .collect(Collectors.toList());
    }
}
