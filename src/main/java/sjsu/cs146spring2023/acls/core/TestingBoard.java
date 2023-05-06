package sjsu.cs146spring2023.acls.core;

import java.util.ArrayList;
import java.util.Collections;

public class TestingBoard extends Board {
    ArrayList<String> words;
    int rows;
    int cols;
    boolean solved;

    public TestingBoard() {
        board = new char[4][4];
        rows = 4;
        cols = 4;
        words = new ArrayList<>();
        solved = false;
    }

    @Override
    public boolean setLettersFromList(char[] letters) {
        return true;
    }

    @Override
    public boolean setLetters(char[][] letterGrid) {
        return true;
    }

    @Override
    public void solve() {
        words.clear();
        Collections.addAll(words, "bys", "che", "chef", "cit", "cliche", "cliches", "clinch", "clinches", "eft", "ehs", "feh", "fehm", "fehs", "fes", "heft", "heh", "hehs", "hes", "ich", "iches", "ichs", "ichthys", "inby", "inch", "inches", "itch", "itches", "itchy", "lich", "liches", "licht", "lin", "linch", "linches", "lit", "lith", "lithe", "lithes", "liths", "niche", "niches", "nicht", "nil", "nit", "she", "shh", "shy", "the", "thy", "tic", "tich", "tiches", "tichy", "til", "tin", "vehm", "wich", "wiches", "wilt", "win", "winch", "winches", "wit", "witch", "witches", "witchy", "with", "withe", "withes", "withs", "withy");
        solved = true;
    }

    @Override
    public ArrayList<String> getWordList() {
        if (!solved) {
            solve();
        }
        return words;
    }

    @Override
    public void setRandom() {
        char[][] letters = {{'w', 'n', 'b', 'y'}, {'i', 'c', 'h', 's'}, {'l', 't', 'h', 'e'}, {'c', 'm', 'f', 'v'}};
        Board.copy2D(letters, board);
    }

    @Override
    public boolean wordIsValid(String word) {
        if (!solved) {
            solve();
        }
        return words.contains(word);
    }

    @Override
    public void reset() {
        solved = false;
        words.clear();
    }
}
