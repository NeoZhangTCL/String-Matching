import java.util.Arrays;
import java.util.Random;

public class StringMatching {

    public static boolean naive(String str, String pattern) {

        char[] strs = str.toCharArray();
        char[] patterns = pattern.toCharArray();

        for (int i = 0; i < strs.length; i++) {
            int j = 0, k = i;
            while (j < patterns.length &&
                    k < strs.length &&
                    patterns[j] == strs[k]) {
                j++;
                k++;
            }
            if (j == patterns.length) return true;
        }

        return false;
    }

    public static boolean randomize(String str, String pattern) {

        char[] strs = str.toCharArray();
        char[] patterns = pattern.toCharArray();

        return false;

    }

    public static boolean kmp(String str, String pattern) {

        if (pattern.equals("")) return true;

        char[] strs = str.toCharArray();
        char[] patterns = pattern.toCharArray();

        int[] next = createNextTable(patterns);
        
        int i = 0; 
        int j = 0; 

        while (i < strs.length) {
            if (strs[i] == patterns[j]) {
                j++;
                if (j == patterns.length) {
                    return true;
                }
                i++;
            } else if (j > 0) {
                j = next[j-1];
            } else {
                i++;
            }
        }
        return false;
    }

    private static int[] createNextTable(char[] patterns) {

        int len = patterns.length;
        if (len == 0) return new int[0];

        int[] next = new int[len];

        for (int i = 0, j = 1; j < len; ) {

            while (i != 0 && patterns[i] != patterns[j]) {
                i = next[i-1];
            }
            if (patterns[i] != patterns[j]) {
                next[j++] = 0;
            } else {
                next[j++] = ++i;
            }
        }

        return next;
    }

    public static boolean boyerMoore(String str, String pattern) {
//
//        char[] strs = str.toCharArray();
//        char[] patterns = pattern.toCharArray();
//        int n = str.length();
//        int m = pattern.length();
//        int[] occurrence = createOccurenceTable(pattern);
//        int skip;
//
//        for (int i = 0; i <= n - m; i += skip) {
//            skip = 0;
//            for (int j = m-1; j >= 0; j--) {
//                if (patterns[j] != strs[i+j]) {
//                    skip = Math.max(1, j - occurrence[strs[i+j]]);
//                    break;
//                }
//            }
////            if (skip == 0) return i;
//        }
////        return n;
        return false;

    }
//
//    private static int[] createOccurenceTable(String pattern) {
//        int BASE = 256;
//
//        int[] occurrence = new int[BASE];
//        for (int c = 0; c < BASE; c++)
//            occurrence[c] = -1;
//        for (int j = 0; j < pattern.length(); j++)
//            occurrence[pattern.charAt(j)] = j;
//
//        return occurrence;
//    }

    public static boolean internal(String str, String pattern) {

        int found = str.indexOf(pattern);

        if (found == -1)
            return false;
        else
            return true;

    }

    public static void main(String[] args) {

        final int strRadix = 36;
        final int baseStrBlockLen = Integer.toString(Integer.MAX_VALUE, strRadix).length();
        long[][] record = new long[10][5];
        Random rand = new Random();

        int ctr = 0;

        for (int j = 20; j < 21; j++) {
            long[] timeRecord = new long[5];
            for (int i = 0; i < 100; i++) {

                StringBuilder baseStrSb = new StringBuilder();
                for (int k = 0; k <= j; k++) {
                    int baseBlockInt = Math.abs(rand.nextInt());
                    baseStrSb.append(Integer.toString(baseBlockInt, strRadix));
                }
                String baseStr = baseStrSb.toString();

                int len = baseStr.length();
                int a = randInt(0, baseStrBlockLen),
                        b = randInt(len * 5 / 6, len),
                        c = randInt(0, baseStrBlockLen),
                        d = randInt(len * 5 / 6, len);
                String str1 = a < b ? baseStr.substring(a, b) : baseStr.substring(b, a);
                String str2 = c < d ? baseStr.substring(c, d) : baseStr.substring(d, c);
                String str, pattern;
                if (str1.length() > str2.length()) {
                    str = str1;
                    pattern = str2;
                } else {
                    str = str2;
                    pattern = str1;
                }
                System.out.println(ctr++ + ": String is '" + str + "' and pattern is '" + pattern + "'");

                final long startTimeNaive = System.nanoTime();
                boolean resNaive = naive(str, pattern);
                final long endTimeNaive = System.nanoTime();
                long timeNaive = endTimeNaive - startTimeNaive;
                timeRecord[0] += timeNaive;
                System.out.println("Naive method time consumption is " + timeNaive
                        + " and result is " + resNaive);

                final long startTimeRand = System.nanoTime();
                boolean resRand = naive(str, pattern);
                final long endTimeRand = System.nanoTime();
                long timeRand = endTimeRand - startTimeRand;
                timeRecord[1] += timeRand;
//                System.out.println("Randomize method time consumption is " + timeRand
//                        + " and result is " + resRand);

                final long startTimeKmp = System.nanoTime();
                boolean resKmp = kmp(str, pattern);
                final long endTimeKmp = System.nanoTime();
                long timeKmp = endTimeKmp - startTimeKmp;
                timeRecord[2] += timeKmp;
                System.out.println("KMP method time consumption is " + timeKmp
                        + " and result is " + resKmp);

                final long startTimeBm = System.nanoTime();
                boolean resBm = boyerMoore(str, pattern);
                final long endTimeBm = System.nanoTime();
                long timeBm = endTimeBm - startTimeBm;
                timeRecord[3] += timeBm;
//                System.out.println("BoyerMoore method time consumption is " + timeBm
//                        + " and result is " + resBm);

                final long startTimeInternal = System.nanoTime();
                boolean resInternal = internal(str, pattern);
                final long endTimeInternal = System.nanoTime();
                long timeInternal = endTimeInternal - startTimeInternal;
                timeRecord[4] += timeInternal;
//                System.out.println("Internal Java method time consumption is " + timeInternal
//                        + " and result is " + resInternal);

//                System.out.println("---------------------");
            }
            record[j] = timeRecord;
            System.out.println("naive: " + timeRecord[0]);
            System.out.println("randomized: " + timeRecord[1]);
            System.out.println("kmp: " + timeRecord[2]);
            System.out.println("Boyer-Mooer: " + timeRecord[3]);
            System.out.println("Internal: " + timeRecord[4]);
            System.out.println("__________________");
        }

    }

    private static int randInt(int min, int max) {
        return Math.abs((int)(Math.random() * (max - min) + min - 1));
    }

}
