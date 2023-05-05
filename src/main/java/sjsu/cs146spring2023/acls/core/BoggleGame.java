package sjsu.cs146spring2023.acls.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

public class BoggleGame extends Game {
    TreeSet<String> foundWords;

    // word of length 0 = 0, length 1 = 0, etc. 8+ = 11
    public static final int[] SCORING_SCALE = new int[]{0, 0, 0, 1, 1, 2, 3, 5, 11};

    public BoggleGame() {
        foundWords = new TreeSet<>(new WordLengthComparator());
        this.board = new BoggleBoard();
        makeRandomBoard();
    }

    @Override
    public boolean hasFound(String word) {
        return foundWords.contains(word);
    }

    @Override
    public int scoreWord(String word) {
        /*
        returns score of word if valid. Otherwise, returns error code.
        error codes:
            0 = already found
            -1 = too short
            -2 = not a word
            -3 = cannot make word
         */
        if (hasFound(word)) {
            return 0;
        }
        if (word.length() < board.getMinWordLength()) {
            return -1;
        }
        if (!dictionaryTrie.contains(word)) {
            return -2;
        }
        if (!board.wordIsValid(word)) {
            return -3;
        }
        return rawScore(word);
    }

    @Override
    public int addWord(String word) {
        int score = scoreWord(word);
        if (score > 0) {
            foundWords.add(word);
            totalScore += score;
        }
        return score;
    }

    @Override
    public ArrayList<String> getFoundWords() {
        return new ArrayList<>(foundWords);
    }

    @Override
    public void clearFoundWords() {
        foundWords.clear();
        totalScore = 0;
    }

    @Override
    public int totalPointsInBoard() {
        int total = 0;
        for (String word : getAllWords()) {
            total += rawScore(word);
        }
        return total;
    }

    private static int rawScore(String word) {
        if (word == null) {
            return 0;
        }
        if (word.length() < SCORING_SCALE.length) {
            return SCORING_SCALE[word.length()];
        }
        else { // max score
            return SCORING_SCALE[SCORING_SCALE.length - 1];
        }
    }
}
