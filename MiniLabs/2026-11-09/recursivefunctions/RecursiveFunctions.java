import java.util.ArrayList;
import java.util.List;

public class RecursiveFunctions {
    public static void main(String[] args) {
        // 1. sumDigits(4096) -> 19
        System.out.println(sumDigits(4096));

        // 2. power(2, 10) -> 1024 (no Math.pow!)
        System.out.println(power(2, 10));

        // 3. countChar("banana", 'a') -> 3
        System.out.println(countChar("banana", 'a'));

        // 4. printBinary(10) -> 1010
        System.out.println(printBinary(10));

        // Stretch: sumList(List<Integer list, int index) without any loops
        List<Integer> listToSum = new ArrayList<>();
        listToSum.add(1);
        listToSum.add(2);
        listToSum.add(3);
        listToSum.add(4);
        // 1+2+3+4=10
        System.out.println(sumList(listToSum, 0));
    }

    private static int sumDigits(int numberToSum) {
        // 5. For each, write the base case first and trace one call on paper
        /*
         sumDigits(4096)
         => 6 + sumDigits(409)
         => 6 + 9 + sumDigits(40)
         => 6 + 9 + 0 + sumDigits(4)
         => 6 + 9 + 0 + 4
         => 19
        */
        if(numberToSum < 10) {
            return numberToSum;
        }
        return numberToSum % 10 + sumDigits(numberToSum / 10);
    }

    private static int power(int base, int exponent) {
        // 5. For each, write the base case first and trace one call on paper
        /*
         power(2, 10)
         => 2 * power(2, 9)
         => 2 * 2 * power(2, 8)
         => 2 * 2 * 2 * power(2, 7)
         => 2 * 2 * 2 * 2 * power(2, 6)
         => 2 * 2 * 2 * 2 * 2 * power(2, 5)
         => 2 * 2 * 2 * 2 * 2 * 2 * power(2, 4)
         => 2 * 2 * 2 * 2 * 2 * 2 * 2 * power(2, 3)
         => 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * power(2, 2)
         => 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * power(2, 1)
         => 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2
         => 1024
        */
        if(exponent == 0) { return 1; }
        if(exponent == 1) { return base; }
        return base * power(base, exponent - 1);
    }

    private static int countChar(String textToCount, char charToCount) {
        // 5. For each, write the base case first and trace one call on paper
        /*
         countChar("banana", 'a')
         => countChar("b", 'a') + countChar("anana", 'a')
         => 0 + countChar("a", 'a') + countChar("nana", 'a')
         => 0 + 1 + countChar("n", 'a') + countChar("ana", 'a')
         => 0 + 1 + 0 + countChar("a", 'a') + countChar("na", 'a')
         => 0 + 1 + 0 + 1 + countChar("n", 'a') + countChar("a", 'a')
         => 0 + 1 + 0 + 1 + 0 + 1
         => 3
        */
        if(textToCount.isEmpty()) {
            return 0;
        }
        if(textToCount.length() == 1) {
            if(textToCount.charAt(0) == charToCount) {
                return 1;
            }
            return 0;
        }
        return countChar(textToCount.substring(0, 1), charToCount) + countChar(textToCount.substring(1), charToCount);
    }

    private static String printBinary(int numberToBinary) {
        // 5. For each, write the base case first and trace one call on paper
        /*
         printBinary(10)
         => printBinary(5) + printBinary(0)
         => printBinary(2) + printBinary(1) + "0"
         => printBinary(1) + printBinary(0) + "1" + "0"
         => "1" + "0" + "1" + "0"
         => "1010"
        */
        if(numberToBinary == 0 || numberToBinary == 1) {
            return String.valueOf(numberToBinary);
        }
        return printBinary(numberToBinary / 2) + printBinary(numberToBinary % 2);
    }

    private static int sumList(List<Integer> listToSum, int currentIndex) {
        // 5. For each, write the base case first and trace one call on paper
        /*
         sumList([1; 2; 3; 4;], 0)
         => 1 + sumList([1; 2; 3; 4;], 1)
         => 1 + 2 + sumList([1; 2; 3; 4;], 2)
         => 1 + 2 + 3 + sumList([1; 2; 3; 4;], 3)
         => 1 + 2 + 3 + 4 + sumList([1; 2; 3; 4;], 4)
         => 1 + 2 + 3 + 4 + 0
         => 10
        */
        if(currentIndex >= listToSum.size()) {
            return 0;
        }
        return listToSum.get(currentIndex) + sumList(listToSum, currentIndex + 1);
    }
}
