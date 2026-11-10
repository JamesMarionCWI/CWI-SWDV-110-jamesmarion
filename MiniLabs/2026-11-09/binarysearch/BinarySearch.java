public class BinarySearch {
    // 3. Add a counter that prints how many checks each search takes
    private static int algorithmIterations = 0;

    // 1. Write a recursive binarySearch(int[] a, int target, int low, int high)
    private static int binarySearch(int[] arrayToSearch, int targetElement, int lowIndex, int highIndex) {
        algorithmIterations++;
        
        // 2. Identify both base cases: found, and low > high

        // Sadly couldn't find the target, so return a not found
        if(lowIndex > highIndex) {
            return -1; // Standard number for the "not found" index
        }

        int middleIndex = (lowIndex+highIndex)/2;
        // Target found, return the index
        if(arrayToSearch[middleIndex] == targetElement) {
            return middleIndex;
        }

        // Sadly need another check to find our missing element, sooo it's time to use recursion
        if(targetElement > arrayToSearch[middleIndex]) {
            // If the target element is bigger than the currently found item
            // Search the larger half
            return binarySearch(arrayToSearch, targetElement, middleIndex+1, highIndex);
        } else {
            // Otherwise search the lower half
            return binarySearch(arrayToSearch, targetElement, lowIndex, middleIndex-1);
        }
    }

    // Make it so the user doesn't HAVE to specify the default lowIndex and the highIndex if they don't want to
    public static int binarySearch(int[] arrayToSearch, int targetElement) {
        return binarySearch(arrayToSearch, targetElement, 0, arrayToSearch.length - 1);
    }

    // Linear search for comparison
    public static int linearSearch(int[] arrayToSearch, int targetElement) {
        for(int i = 0; i < arrayToSearch.length; i++) {
            algorithmIterations++;
            if(arrayToSearch[i] == targetElement) { return i; } 
        }
        
        return -1;
    }

    public static void main(String[] args) {
        // 4. Test with a sorted array of 1,000 numbers: first, last, middle, missing
        int[] numbers = new int[1000];
        for(int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
        }

        int indexFound;
        int numToFind;

        System.out.println("Welcome to the binary search program!");

        // Empty line for formatting purposes
        System.out.println();
        
        // First
        algorithmIterations = 0;
        numToFind = 0;
        indexFound = binarySearch(numbers, numToFind);
        System.out.printf("%d found at index %d after %d binary search iterations!%n", numToFind, indexFound, algorithmIterations);
        // 5. Compare against your linear search counts
        algorithmIterations = 0;
        indexFound = linearSearch(numbers, numToFind);
        System.out.printf("%d found at index %d after %d linear search iterations!%n", numToFind, indexFound, algorithmIterations);

        // Empty line for formatting purposes
        System.out.println();

        // Last
        algorithmIterations = 0;
        numToFind = 999;
        indexFound = binarySearch(numbers, 999);
        System.out.printf("%d found at index %d after %d binary search iterations!%n", numToFind, indexFound, algorithmIterations);
        // 5. Compare against your linear search counts
        algorithmIterations = 0;
        indexFound = linearSearch(numbers, numToFind);
        System.out.printf("%d found at index %d after %d linear search iterations!%n", numToFind, indexFound, algorithmIterations);

        // Empty line for formatting purposes
        System.out.println();

        // Middle
        algorithmIterations = 0;
        numToFind = 499;
        indexFound = binarySearch(numbers, 499);
        System.out.printf("%d found at index %d after %d binary search iterations!%n", numToFind, indexFound, algorithmIterations);
        // 5. Compare against your linear search counts
        algorithmIterations = 0;
        indexFound = linearSearch(numbers, numToFind);
        System.out.printf("%d found at index %d after %d linear search iterations!%n", numToFind, indexFound, algorithmIterations);

        // Empty line for formatting purposes
        System.out.println();

        // Missing
        algorithmIterations = 0;
        numToFind = -2;
        indexFound = binarySearch(numbers, -2);
        System.out.printf("%d not found so index %d returned after %d binary search iterations!%n", numToFind, indexFound, algorithmIterations);
        // 5. Compare against your linear search counts
        algorithmIterations = 0;
        indexFound = linearSearch(numbers, numToFind);
        System.out.printf("%d not found so index %d returned after %d linear search iterations!%n", numToFind, indexFound, algorithmIterations);
    }
}
