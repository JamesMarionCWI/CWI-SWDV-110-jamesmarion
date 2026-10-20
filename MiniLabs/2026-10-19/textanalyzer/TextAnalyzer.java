import java.util.Scanner;

public class TextAnalyzer {
    public static void main(String[] args) {
        final String BORDER_SYMBOL = "=";
        // 6. Print a 40-character = border above and below with repeat()
        System.out.println(BORDER_SYMBOL.repeat(40));

        System.out.println("Give me a sentence:");
        try(Scanner scanner = new Scanner(System.in)) {
            // 1. Read a sentence from the user
            String userSentence = scanner.nextLine();
            
            // 2. Print counts of words, letters, digits, and vowels
            String[] sentenceAsWords = userSentence.split(" ");
            int numberLetters = 0;
            int numberVowels = 0;
            int numberDigits = 0;
            int numberWords = sentenceAsWords.length;
            // Loop through each character to test its qualifications in each category
            for(int i = 0; i < userSentence.length(); i++) {
                if(Character.isLetter(userSentence.charAt(i))) {
                    numberLetters++;
                    if("aeiou".indexOf(Character.toLowerCase(userSentence.charAt(i))) >= 0) {
                        numberVowels++;
                    }
                } else if(Character.isDigit(userSentence.charAt(i))) {
                    numberDigits++;
                }
            }
            System.out.printf("Letters: %d%nVowels: %d%nDigits: %d%nWords: %d%n", numberLetters, numberVowels, numberDigits, numberWords);
            
            // 3. Print the longest word
            if(sentenceAsWords.length > 0) {
                String longestWord = sentenceAsWords[0];
                for(String word : sentenceAsWords) {
                    longestWord = word.length() > longestWord.length() ? word : longestWord;
                }
                // Print the result
                System.out.println("Longest Word: " + longestWord);
            }

            // 4. Print the sentence in Title Case
            // Pre-allocate a StringBuilder with a certain capacity
            StringBuilder titleCasedWords = new StringBuilder(userSentence.length());
            // Loop through each word to capitalize the first letter
            for(int i = 0; i < sentenceAsWords.length; i++) {
                char[] chars = sentenceAsWords[i].toCharArray();
                if(chars.length > 0) {
                    // Uppercase the first character of the word
                    chars[0] = Character.toUpperCase(chars[0]);
                }
                titleCasedWords.append(chars);
                titleCasedWords.append(' ');
            }
            // Delete the final trailing space
            if(titleCasedWords.length() > 1) {
                titleCasedWords.deleteCharAt(titleCasedWords.length() - 1);
            }
            // Print
            System.out.println(titleCasedWords.toString());
            
            // 5. Print the words in reverse order using StringBuilder
            // Pre-allocate a StringBuilder with a certain capacity
            StringBuilder sentenceAsWordsBackwards = new StringBuilder(userSentence.length());
            // Loop through the array backwards
            for(int i = sentenceAsWords.length - 1; i >= 0; i--) {
                sentenceAsWordsBackwards.append(sentenceAsWords[i]);
                sentenceAsWordsBackwards.append(' ');
            }
            // Print the sentence. One might think to use .reverse() here, but that reverses all the characters inside the words.
            System.out.println(sentenceAsWordsBackwards.toString());

            // Stretch: report whether the sentence is a palindrome
            StringBuilder palindromeCheckerSB = new StringBuilder();
            for (int i = 0; i < userSentence.length(); i++) {
                if(Character.isLetterOrDigit(userSentence.charAt(i))) {
                    palindromeCheckerSB.append(Character.toLowerCase(userSentence.charAt(i)));
                }
            }
            System.out.println("Is Palindrome: " + palindromeCheckerSB.toString().equals(palindromeCheckerSB.reverse().toString()));
        }
        // 6. Print a 40-character = border above and below with repeat()
        System.out.println(BORDER_SYMBOL.repeat(40));
    }
}