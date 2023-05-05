package sjsu.cs146spring2023.acls.core;


import java.util.ArrayList;

public abstract class Game {
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

    public abstract int totalPointsInBoard();

    @Override
    public String toString() {
        return "Game\n" +
                "board:\n" + board +
                "\nfoundWords:" + getFoundWords();
    }
}
