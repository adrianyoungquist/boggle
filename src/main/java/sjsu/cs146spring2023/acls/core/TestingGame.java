package sjsu.cs146spring2023.acls.core;

import java.util.ArrayList;
import java.util.Random;

public class TestingGame extends Game {
    private static final Random rand = new Random();

    ArrayList<String> foundWords;

    public TestingGame() {
        this(new TestingBoard());
    }

    public TestingGame(Board board) {
        this(board, null);
    }

    public TestingGame(Board board, DictionaryTrie dictionaryTrie) {
        this.board = board;
        foundWords = new ArrayList<>();
        this.dictionaryTrie = dictionaryTrie;
    }

    @Override
    public boolean hasFound(String word) {
        return foundWords.contains(word);
    }

    @Override
    public int scoreWord(String word) {
        /*
        returns score of word if valid. Otherwise, returns error code.
         */
        if (hasFound(word)) {
            return SCORE_ISSUE_ALREADY_FOUND;
        }
        if (word == null || word.length() < board.getMinWordLength()) {
            return SCORE_ISSUE_TOO_SHORT;
        }
        if (!dictionaryTrie.contains(word)) {
            return SCORE_ISSUE_NOT_WORD;
        }
        if (!board.canMake(word)) {
            return SCORE_ISSUE_CANNOT_MAKE;
        }
        return word.length();
    }

    @Override
    public int addWord(String word) {
        int score = scoreWord(word);
        if (score > 0) {
            foundWords.add(word);
        }
        return score;
    }

    @Override
    public ArrayList<String> getFoundWords() {
        return foundWords;
    }

    @Override
    public void clearFoundWords() {
        foundWords.clear();
        totalScore = 0;
    }

    @Override
    public int totalPointsInBoard() {
        int total = 0;
        for (String word : board.getWordList()) {
            if (scoreWord(word) > 0) {
                total += scoreWord(word);
            }
        }
        return total;
    }

    @Override
    public String addWordPrintOutput(String word) {
        String[] possiblePhrases = new String[]{"Great!", "Awesome!", "Nice!", "Right on!"};
        int score = addWord(word);
        if (score > 0) {
            int index = rand.nextInt(possiblePhrases.length);
            return possiblePhrases[index] + " " + score + " points!";
        }
        if (score == SCORE_ISSUE_ALREADY_FOUND) {
            return "You already found this word!";
        }
        if (score == SCORE_ISSUE_TOO_SHORT) {
            return "Sorry, words must be at least " + board.getMinWordLength() + " letters in length.";
        }
        if (score == SCORE_ISSUE_NOT_WORD) {
            return "Sorry, that word is not in the dictionary.";
        }
        if (score == SCORE_ISSUE_CANNOT_MAKE) {
            return "Sorry, that word cannot be made.";
        }
        return "Something odd happened.";
    }
}
