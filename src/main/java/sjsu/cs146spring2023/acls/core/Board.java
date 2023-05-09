package sjsu.cs146spring2023.acls.core;


import java.util.ArrayList;
import java.util.Arrays;

public abstract class Board {
    protected char[][] board;
    protected DictionaryTrie dictionaryTrie;
    boolean solved;
    int minWordLength;
    int dim;

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

    public char[][] getCharGrid() {
        return board;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public int getDim() {
        return dim;
    }

    public DictionaryTrie getDictionaryTrie() {
        return dictionaryTrie;
    }

    public void setDictionaryTrie(DictionaryTrie dictionaryTrie) {
        this.dictionaryTrie = dictionaryTrie;
    }

    public abstract boolean setLetters(char[] letters);

    public abstract boolean setLetters(char[][] letterGrid);

    public abstract boolean setLetters(String letters);

    public DictionaryTrie getDictionary() {
        return dictionaryTrie;
    }

    public void setDictionary(DictionaryTrie dictionaryTrie) {
        this.dictionaryTrie = dictionaryTrie;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public void setMinWordLength(int minWordLength) {
        this.minWordLength = minWordLength;
    }

    public abstract void solve();

    public abstract ArrayList<String> getWordList();

    public abstract void setRandom();

    public abstract boolean canMake(String word);

    public abstract void reset();

    @Override
    public String toString() {
        return Arrays.deepToString(board);
    }
}
