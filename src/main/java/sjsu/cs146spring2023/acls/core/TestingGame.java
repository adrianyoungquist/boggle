package sjsu.cs146spring2023.acls.core;

import java.util.ArrayList;

public class TestingGame extends Game {
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
        if (foundWords.contains(word)) {
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
            if (scoreWord(word) > 0 ) {
                total += scoreWord(word);
            }
        }
        return total;
    }
}
