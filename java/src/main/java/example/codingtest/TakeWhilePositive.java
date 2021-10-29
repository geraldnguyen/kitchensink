package example.codingtest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TakeWhilePositive {
    public static List<Integer> takeWhile1(List<Integer> ints) {
        Map<String, Boolean> stopFlag = new HashMap<>();

        return ints.stream()
            .map(i -> {
                if (i > 0 && !stopFlag.containsKey("stop")) {
                    return i;
                } else {
                    stopFlag.put("stop", true);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList())
            ;
    }

    public static List<Integer> takeWhile2(List<Integer> ints) {
        int firstNonPositiveIdx = IntStream.range(0, ints.size())
            .filter(idx -> ints.get(idx) <= 0)
            .findFirst().orElse(-1);
        if (firstNonPositiveIdx < 0) {
            return ints;
        }

        return ints.stream().limit(firstNonPositiveIdx).collect(Collectors.toList());
        // alternatively we can `IntStream.range(0, firstNonPositiveIdx)` to just pick the element out of ints
    }
}
