package example.codingtest;

public final class LongestPrefixSuffix {
    public static String solve(String str) {
        int length = str.length();

        int upperPrefixIdx;
        int lowerSuffixIdx;
        if (length % 2 == 0){
            upperPrefixIdx = length / 2 - 1;
            lowerSuffixIdx = length / 2;
        } else {
            upperPrefixIdx = length / 2 - 1;
            lowerSuffixIdx = length / 2 + 1;
        }

        for(int i = upperPrefixIdx, j = lowerSuffixIdx; i >= 0 && j < length; i--, j++) {
            String prefix = str.substring(0, i + 1);
            String suffix = str.substring(j, length);
            if (prefix.equals(suffix)) {
                return prefix;
            }
        }

        return "";
    }
}
