package com.adrianyoungquist.core;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BoggleGame extends Game {
    // word of length 0 = 0, length 1 = 0, etc. 8+ = 11
    public static final int[] SCORING_SCALE = new int[]{0, 0, 0, 1, 1, 2, 3, 5, 11};
    private static final Random rand = new Random();
    TreeSet<String> foundWords;

    public BoggleGame() {
        foundWords = new TreeSet<>(new WordLengthComparator());
        dictionaryTrie = new DictionaryTrie();
        dictionaryTrie.buildTrieFromFile();
        this.board = new BoggleBoard(dictionaryTrie);
        makeRandomBoard();
    }

    private static int rawScore(String word) {
        if (word == null) {
            return 0;
        }
        if (word.length() < SCORING_SCALE.length) {
            return SCORING_SCALE[word.length()];
        } else { // max score
            return SCORING_SCALE[SCORING_SCALE.length - 1];
        }
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
        return rawScore(word);
    }

    @Override
    public int addWord(String word) {
        if (word == null) {
            return -1;
        }
        word = word.toLowerCase();
        int score = scoreWord(word);
        if (score > 0) {
            foundWords.add(word);
            totalScore += score;
        }
        return score;
    }

    @Override
    public String addWordPrintOutput(String word) {
        String[] possiblePhrases = new String[]{"Great!", "Awesome!", "Nice!", "Right on!"};
        int score = addWord(word);
        if (score == 1) {
            int index = rand.nextInt(possiblePhrases.length);
            return possiblePhrases[index] + " 1 point!";
        }
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

    @Override
    public void setMinWordLength(int minLength) {
        board.setMinWordLength(minLength);
        foundWords = foundWords.stream().filter(s -> s.length() >= minLength).collect(Collectors.toCollection(TreeSet::new));
        totalScore = foundWords.stream().mapToInt(BoggleGame::rawScore).sum();
    }
}
