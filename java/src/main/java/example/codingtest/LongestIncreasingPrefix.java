package example.codingtest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Find and return the longest increasing prefix of a list of integers
 * Example:
 * [] -> []
 * [1] -> [1]
 * [1, 2, 3] -> [1, 2, 3]
 * [1, 3, 2] -> [1, 3]
 */
public final class LongestIncreasingPrefix {
    public static List<Integer> lis(List<Integer> integers) {
        if (integers.isEmpty()) {
            return integers;
        }

        int firstNonincreasingIdx = 1;
        for(int i = firstNonincreasingIdx; i < integers.size(); i++) {
            if (integers.get(i-1) >= integers.get(i)) {
                firstNonincreasingIdx = i;
                break;
            }
            firstNonincreasingIdx++;
        }

        return integers.subList(0, firstNonincreasingIdx);
    }

    public static List<Integer> lis2(List<Integer> integers) {
        if (integers.isEmpty()) {
            return integers;
        }

        List<Integer> prefix = new ArrayList<>();
        prefix.add(integers.get(0));
        for(int i = 1; i < integers.size(); i++) {
            if (integers.get(i - 1) < integers.get(i)) {
                prefix.add(integers.get(i));
            } else break;
        }

        return prefix;
    }

    public static List<Integer> lis3Stream(List<Integer> integers) {
        if (integers.isEmpty()) {
            return integers;
        }

        return IntStream.range(0, integers.size())
            .takeWhile(idx -> idx == 0 || integers.get(idx -1) < integers.get(idx))
            .map(integers::get)
            .boxed()
            .collect(Collectors.toList());
    }
}
