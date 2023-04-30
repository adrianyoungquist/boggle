package sjsu.cs146spring2023.acls.core;


import java.util.ArrayList;

public abstract class Game {
    protected Board board;
    protected ArrayList<String> foundWords;

    protected int score;

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

    public ArrayList<String> getFoundWords() {
        return foundWords;
    }

    public int getScore() {
        return score;
    }

    public void clearFoundWords() {
        foundWords.clear();
        score = 0;
    }

    public void makeRandomBoard() {
        board.setRandom();
    }

    public void solveBoard() {
        board.solve();
    }

    public ArrayList<String> getAllWords() {
        return board.getWords();
    }

    public abstract boolean hasFound(String word);

    public abstract int scoreWord(String word);

    public abstract int addWord(String word);


    @Override
    public String toString() {
        return "Game\n" +
                "board:\n" + board +
                "\nfoundWords:" + foundWords;
    }
}
