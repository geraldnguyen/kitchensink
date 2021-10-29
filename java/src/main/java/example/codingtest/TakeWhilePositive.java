package example.codingtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TakeWhilePositive {
    public static List<Integer> takeWhile1(List<Integer> ints) {
        List<Integer> positives = new ArrayList<>();
        Map<String, Boolean> flag = new HashMap<>();
        flag.put("stop", false);
        ints.forEach(i -> {
            if (i > 0 && Boolean.TRUE.equals(!flag.get("stop"))) {
                positives.add(i);
            } else {
                flag.put("stop", true);
            }
        });
        return positives;
    }
}
