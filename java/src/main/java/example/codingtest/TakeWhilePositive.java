package example.codingtest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TakeWhilePositive {

    public static List<Integer> takeWhile0(List<Integer> ints) {
        List<Integer> result = new ArrayList<>();

        for (int integer : ints) {
            if (integer > 0) {
                result.add(integer);
            } else break;
        }

        return result;
    }

    public static List<Integer> takeWhile1(List<Integer> ints) {
        boolean stop = false;
        List<Integer> result = new ArrayList<>();

        for (Integer i : ints) {
            if (i > 0 && !stop) {
                result.add(i);
            } else {
                stop = true;
            }
        }

        return result;
    }

    public static List<Integer> takeWhile1Stream(List<Integer> ints) {
        Map<String, Boolean> stopFlag = new HashMap<>();
        List<Integer> result = new ArrayList<>();

        for (Integer i : ints) {
            if (i > 0 && !stopFlag.containsKey("stop")) {
                result.add(i);
            } else {
                stopFlag.put("stop", true);
            }
        }

        return result;
    }


    public static List<Integer> takeWhile2(List<Integer> ints) {
        Map<String, Boolean> stopFlag = new HashMap<>();

        return ints.stream()
            .peek(i -> {        // NOSONAR
                if (i <= 0) {
                    stopFlag.put("stop", true);
                }
            })
            .filter(i -> !stopFlag.containsKey("stop"))
            .collect(Collectors.toList())
            ;
    }

    public static List<Integer> takeWhile3(List<Integer> ints) {
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

    public static List<Integer> takeWhile4(List<Integer> ints) {
        int firstNonPositiveIdx = -1;
        for (int i = 0; i < ints.size(); i++) {
            if (ints.get(i) <= 0) {
                firstNonPositiveIdx = i;
                break;
            }
        }
        if (firstNonPositiveIdx < 0) {
            return ints;
        }
        return ints.subList(0, firstNonPositiveIdx);
    }

    public static List<Integer> takeWhile5(List<Integer> ints) {
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
