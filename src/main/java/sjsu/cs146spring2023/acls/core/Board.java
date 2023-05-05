package sjsu.cs146spring2023.acls.core;


import java.util.ArrayList;
import java.util.Arrays;

public abstract class Board {
    protected char[][] board;
    protected DictionaryTrie dictionaryTrie;
    boolean solved;

    public static void copy2D(char[][] src, char[][] dst) {
        if (src.length != dst.length) {
            throw new IllegalArgumentException("Source and destination arrays must be same size.");
        }
        if (src.length == 0) {
            return;
        }
        if (src[0].length != dst[0].length) {
            throw new IllegalArgumentException("Source and destination arrays must be same size.");
        }
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, src[i].length);
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public DictionaryTrie getDictionaryTrie() {
        return dictionaryTrie;
    }

    public void setDictionaryTrie(DictionaryTrie dictionaryTrie) {
        this.dictionaryTrie = dictionaryTrie;
    }

    public abstract boolean setLettersFromList(char[] letters);

    public abstract boolean setLetters(char[][] letterGrid);

    public DictionaryTrie getDictionary() {
        return dictionaryTrie;
    }

    public void setDictionary(DictionaryTrie dictionaryTrie) {
        this.dictionaryTrie = dictionaryTrie;
    }

    public abstract void solve();

    public abstract ArrayList<String> getWordList();

    public abstract void setRandom();

    public abstract boolean wordIsValid(String word);

    @Override
    public String toString() {
        return Arrays.deepToString(board);
    }
}
