package com.adrianyoungquist.model;


import com.adrianyoungquist.datastructures.DictionaryTrie;

import java.util.ArrayList;

public abstract class Game {
    public static final int SCORE_ISSUE_ALREADY_FOUND = 0;
    public static final int SCORE_ISSUE_TOO_SHORT = -1;
    public static final int SCORE_ISSUE_NOT_WORD = -2;
    public static final int SCORE_ISSUE_CANNOT_MAKE = -3;

    protected Board board;
    protected int totalScore;
    protected DictionaryTrie dictionaryTrie;

    public DictionaryTrie getDictionaryTrie() {
        return dictionaryTrie;
    }

    public void setDictionaryTrie(DictionaryTrie dictionaryTrie) {
        this.dictionaryTrie = dictionaryTrie;
        board.dictionaryTrie = dictionaryTrie;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public abstract ArrayList<String> getFoundWords();

    public int getScore() {
        return totalScore;
    }

    public abstract void clearFoundWords();

    public void makeRandomBoard() {
        board.setRandom();
        totalScore = 0;
    }

    public void solveBoard() {
        board.solve();
    }

    public ArrayList<String> getAllWords() {
        return board.getWordList();
    }

    public abstract boolean hasFound(String word);

    public abstract int scoreWord(String word);

    public abstract int addWord(String word);

    public abstract String addWordPrintOutput(String word);

    public abstract int totalPointsInBoard();

    public void reset() {
        clearFoundWords();
        board.reset();
    }

    public void showBoard() {
        System.out.println(board);
    }

    public int getMinWordLength() {
        return board.getMinWordLength();
    }

    public void setMinWordLength(int minLength) {
        board.setMinWordLength(minLength);
    }

    @Override
    public String toString() {
        return "Game\n" +
                "board:\n" + board +
                "\nfoundWords:" + getFoundWords();
    }
}
