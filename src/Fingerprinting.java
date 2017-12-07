import java.util.*;

public class Fingerprinting {

    public static boolean randomized(String str, String pattern) {
        int n = str.length(), m = pattern.length();

        final double limit =  n * n * m * Math.log(n * n * m);
        int randomPrime = getRandomPrime(limit);

        int strFp = createFp(str, 0, m), ptFp = createFp(pattern, 0, m);
        if ((strFp % randomPrime) == (ptFp % randomPrime)) {
            return true;
        }

        for (int nextIndex = m; nextIndex < n; nextIndex++) {
            ptFp = createFp(str, nextIndex - m, nextIndex);
            if ((strFp % randomPrime) == (ptFp % randomPrime)) {
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

    public static void main(String[] args) {
        Fingerprinting fp = new Fingerprinting();
        System.out.println(fp.randomized("abcabcabcabcd", "abcd"));
    }
}
