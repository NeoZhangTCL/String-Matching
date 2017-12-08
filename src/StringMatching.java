import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class StringMatching {

    private final static int ALGO_NUMS = 5;

    public static boolean naive(String str, String pattern) {

        for (int i = 0; i < str.length(); i++) {
            int j = 0, k = i;
            while (j < pattern.length() &&
                    k < str.length() &&
                    pattern.charAt(j) == str.charAt(k)) {
                j++;
                k++;
            }
            if (j == pattern.length()) return true;
        }

        return false;
    }

    //-----------------------------------------------
    public static boolean randomize(String str, String pattern) {
        int n = str.length(), m = pattern.length();

        final double limit =  n * n * m * Math.log(n * n * m);
        int randomPrime = getRandomPrime(limit);

        int subStrFp = createFp(str, 0, m), ptFp = createFp(pattern, 0, m);
        if ((subStrFp % randomPrime) == (ptFp % randomPrime)) {
            return true;
        }

        for (int nextIndex = m; nextIndex < n; nextIndex++) {
            subStrFp = updateFp(str, nextIndex - m, nextIndex, subStrFp);
            if ((subStrFp % randomPrime) == (ptFp % randomPrime)) {
                return true;
            }
        }
        return false;
    }

    private static int createFp(String str, int start, int end) {
        int res = 0;
        for (int i = start; i < end; i++) {
            res += (int)(str.charAt(i)) * Math.pow(2, i);
        }
        return res;
    }

    private static int updateFp(String str, int removeInt, int addIndex,int res) {
        res -= (int)(str.charAt(removeInt)) * Math.pow(2, removeInt);
        res += (int)(str.charAt(addIndex)) * Math.pow(2, addIndex);
        return res;
    }

    private static int getRandomPrime(double limit) {
        int i = (int) (Math.random() * limit) - 1;
        while (!isPrime(i)) {
            i = (int) (Math.random() * limit) - 1;
        }
        return i;
    }

    public static boolean isPrime(int n) {
        if (n < 2) return false;
        int maxIteration = (int) Math.ceil(Math.sqrt(n));
        for (int i = 2; i < maxIteration; i++) {
            if(n % i == 0)
                return false;
        }
        return true;
    }

    //-----------------------------------------------
    public static boolean kmp(String str, String pattern) {

        if (pattern.equals("")) return true;

        int[] next = createNextTable(pattern);

        int i = 0;
        int j = 0;

        while (i < str.length()) {
            if (str.charAt(i) == pattern.charAt(j)) {
                j++;
                if (j == pattern.length()) {
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

    private static int[] createNextTable(String pattern) {

        int len = pattern.length();
        if (len == 0) return new int[0];

        int[] next = new int[len];

        for (int i = 0, j = 1; j < len; ) {

            while (i != 0 && pattern.charAt(i) != pattern.charAt(j)) {
                i = next[i-1];
            }
            if (pattern.charAt(i) != pattern.charAt(j)) {
                next[j++] = 0;
            } else {
                next[j++] = ++i;
            }
        }

        return next;
    }

    //-----------------------------------------------
    public static boolean boyerMoore(String str, String pattern) {

        if (pattern.equals("")) return true;

        // build last occurrence index
        int[] occerrence = createOccurrenceTable(pattern);

        // searching
        int start = pattern.length() - 1;
        int end = str.length();
        int i, j;

        // search from left to right
        while (start < end) {
            i = start;
            for (j = pattern.length() - 1; j >= 0; j--) {
                if (str.charAt(i) != pattern.charAt(j)) {
                    // check the last occurrence index
                    if (occerrence[str.charAt(i)] != -1) {
                        if (j - occerrence[str.charAt(i)] > 0) start += j - occerrence[str.charAt(i)];
                        else start += 1;
                    } else {
                        start += j + 1;
                    }
                    break;
                }
                if (j == 0) {
                    return true;
                }
                i--;
            }
        }

        return false;
    }

    private static int[] createOccurrenceTable(String pattern) {
        final int SIZE = 256;
        int occurrence[] = new int[SIZE];
        int length = pattern.length();
        for (int i = 0; i < SIZE; i++) {
            occurrence[i] = -1;
        }
        for (int i = 0; i < length; i++) {
            occurrence[pattern.charAt(i)] = i;
        }
        return occurrence;
    }

    //-----------------------------------------------
    public static boolean internal(String str, String pattern) {
        int found = str.indexOf(pattern);
        if (found == -1)
            return false;
        else
            return true;
    }

    public static void main(String[] args) throws IOException {

//        experiment2();
        experiment1(1000, 1000, "experiment1.csv");

    }

    private static void experiment1(int lengthLimit, int repeat, String fileName)
            throws IOException {

        final int strRadix = 36;
        final int baseStrBlockLen = Integer.toString(Integer.MAX_VALUE, strRadix).length();
        Random rand = new Random();

        int ctr = 0;
        long[][] record = new long[lengthLimit][ALGO_NUMS];

        for (int j = 0; j < lengthLimit; j++) {
            long[] timeRecord = new long[ALGO_NUMS];
            for (int i = 0; i < repeat; i++) {

                StringBuilder basestrb = new StringBuilder();
                for (int k = 0; k <= j; k++) {
                    int baseBlockInt = Math.abs(rand.nextInt());
                    basestrb.append(Integer.toString(baseBlockInt, strRadix));
                }
                String baseStr = basestrb.toString();

                int len = baseStr.length();
                int a = randInt(0, baseStrBlockLen),
                        b = randInt(len * (baseStrBlockLen - 1) / baseStrBlockLen, len),
                        c = randInt(0, len),
                        d = randInt(0, len);
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
                System.out.println("Randomize method time consumption is " + timeRand
                        + " and result is " + resRand);

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
                System.out.println("Internal Java method time consumption is " + timeInternal
                        + " and result is " + resInternal);

//                System.out.println("---------------------");
            }
            record[j] = timeRecord;
        }

        writeCsv(fileName, record);
    }

    private static void experiment2() {

        final int strRadix = 36;
        Random rand = new Random();

        int ctr = 0;

        long[] timeRecord = new long[5];
        for (int i = 0; i < 1000000; i++) {

            String str1 = Integer.toString(Math.abs(rand.nextInt()), strRadix) + Integer.toString(Math.abs(rand.nextInt()), strRadix);
            String str2 = Integer.toString(Math.abs(rand.nextInt()), strRadix);

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
//                System.out.println("Naive method time consumption is " + timeNaive
//                        + " and result is " + resNaive);

            final long startTimeRand = System.nanoTime();
            boolean resRand = randomize(str, pattern);
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
        System.out.println("naive: " + timeRecord[0]);
        System.out.println("randomized: " + timeRecord[1]);
        System.out.println("kmp: " + timeRecord[2]);
        System.out.println("Boyer-Mooer: " + timeRecord[3]);
        System.out.println("Internal: " + timeRecord[4]);
        System.out.println("__________________");
    }

    private static int randInt(int min, int max) {
        return Math.abs((int)(Math.random() * (max - min) + min - 1));
    }

    private static void writeCsv(String fileName, long[][] record)
            throws IOException {
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.println("Naive,Randomized,KMP,Boyer-Mooer,String.indexOf");
        for (long[] r: record) {
            String line = Arrays.stream(r)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(","));
            writer.println(line);
        }
        writer.close();
    }
}
