import java.util.Random;

public class StringMatching {

    public static boolean naive(String str, String pattern) {

        char[] strs = str.toCharArray();
        char[] patterns = pattern.toCharArray();

        int i = 0, j = 0;
        while (i < strs.length) {
            while (j < patterns.length) {
                if (strs[i] == strs[j]) {
                    i++;
                    j++;
                }
            }
            if (j == patterns.length) return true;
            i++;
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

        int q = -1;
        for (int i = 1; i < len; i++ ) {
            while (q > 0 && patterns[i] != patterns[q+1]) {
                q = next[q];
            }
            if (patterns[i] == patterns[q+1]) {
                q = i + 1;
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

    public static void main(String[] args) {

        Random rand = new Random();
        Long strLong = Math.abs(rand.nextLong());
        String randomStr = Long.toString(strLong, 36);
        System.out.println(randomStr);
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
        System.out.println("String is '" + str + "' and pattern is '" + pattern + "'");

        final long startTimeNaive = System.nanoTime();
        boolean resNaive = naive(str, pattern);
        final long endTimeNaive = System.nanoTime();
        System.out.println("Naive method time consumption is "
                + (endTimeNaive - startTimeNaive)
                + " and result is " + resNaive);

        final long startTimeRand = System.nanoTime();
        boolean resRand = naive(str, pattern);
        final long endTimeRand = System.nanoTime();
        System.out.println("Randomize method time consumption is "
                + (startTimeRand - endTimeRand)
                + " and result is " + resRand);

        final long startTimeKmp = System.nanoTime();
        boolean resKmp = kmp(str, pattern);
        final long endTimeKmp = System.nanoTime();
        System.out.println("KMP method time consumption is "
                + (endTimeKmp - startTimeKmp)
                + " and result is " + resKmp);

        final long startTimeBm = System.nanoTime();
        boolean resBm = boyerMoore(str, pattern);
        final long endTimeBm = System.nanoTime();
        System.out.println("BoyerMoore method time consumption is "
                + (endTimeBm - startTimeBm)
                + " and result is " + resBm);

        final long startTimeInternal = System.nanoTime();
        boolean resInternal = internal(str, pattern);
        final long endTimeInternal = System.nanoTime();
        System.out.println("Internal Java method time consumption is "
                + (endTimeInternal - startTimeInternal)
                + " and result is " + resInternal);
    }
}
