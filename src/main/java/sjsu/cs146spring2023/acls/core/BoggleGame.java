package sjsu.cs146spring2023.acls.core;

public class BoggleGame extends Game {
    @Override
    public boolean hasFound(String word) {
        return false;
    }

    @Override
    public int scoreWord(String word) {
        return 0;
    }

    @Override
    public int addWord(String word) {
        return 0;
    }
}
