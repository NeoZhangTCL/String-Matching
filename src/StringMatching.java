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

        int i = 0, j = 1;
        while (j < strs.length) {
            if (strs[j] == patterns[i]) {
                i++;
                j++;
            }
            else {
                if (i == 0) j++;
                else i = next[i];
            }
            if (i == patterns.length) return true;
        }
        
        if (i == patterns.length) return true;
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

        char[] strs = str.toCharArray();
        char[] patterns = pattern.toCharArray();

        return false;

    }

    public static boolean internal(String str, String pattern) {

        int found = str.indexOf(pattern);

        if (found == -1)
            return false;
        else
            return true;

    }

    private static int randInt(int max) {
        return Math.abs((int)(Math.random() * max));
    }

    private static long[][] experiment() {
        long[][] record = new long[10][5];
        Random rand = new Random();

        int ctr = 0;

        for (int j = 0; j < 10; j++) {
            long[] timeRecord = new long[5];
            for (int i = 0; i < 100; i++) {

                int strInt = Math.abs(rand.nextInt());
                StringBuilder randomStrSb = new StringBuilder();
                for (int k = 0; k <= j; k++) {
                    randomStrSb.append(Integer.toString(strInt, 36));
                }
                String randomStr = randomStrSb.toString();

                int len = randomStr.length();
                int a = randInt(len), b = randInt(len), c = randInt(len), d = randInt(len);
                String str1 = a > b ? randomStr.substring(b, a) : randomStr.substring(a, b);
                String str2 = c > d ? randomStr.substring(d, c) : randomStr.substring(c, d);
                String str, pattern;
                if (str1.length() > str2.length()) {
                    str = str1;
                    pattern = str2;
                } else {
                    str = str2;
                    pattern = str1;
                }
//                System.out.println(ctr++ + ": String is '" + str + "' and pattern is '" + pattern + "'");

                final long startTimeNaive = System.nanoTime();
                boolean resNaive = naive(str, pattern);
                final long endTimeNaive = System.nanoTime();
                long timeNaive = endTimeNaive - startTimeNaive;
                timeRecord[0] += timeNaive;
//                System.out.println("Naive method time consumption is " + timeNaive
//                        + " and result is " + resNaive);

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
//                System.out.println("KMP method time consumption is " + timeKmp
//                        + " and result is " + resKmp);

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
            Arrays.stream(timeRecord).forEach(System.out::println);
            System.out.println("__________________");
        }
        return record;
    }

    public static void main(String[] args) {

        experiment();


    }
}
