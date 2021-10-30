package example.codingtest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DistinctCharacters {

    public static List<Character> distinctChars(String sentence) {
        Set<Character> characters = new HashSet<>();
        for (char c : sentence.toCharArray()) {
            if (c == ' ') continue;
            characters.add(c);
        }
        List<Character> result = new ArrayList<>(characters);
        result.sort((c1, c2) -> c1 - c2);  
        return result;
    }

    public static List<Character> distinctCharsStream(String sentence) {
        return Arrays.stream(sentence.split(" "))
            .flatMap(word -> Stream.of(word.split("")))
            .map(s -> s.charAt(0))
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    public static List<Character> distinctCharsStream2(String sentence) {
        return Arrays.stream(sentence.split(""))
            .map(s -> s.charAt(0))
            .filter(i -> i != ' ')
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
}
