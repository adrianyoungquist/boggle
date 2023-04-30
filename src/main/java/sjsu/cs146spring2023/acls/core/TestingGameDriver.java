package sjsu.cs146spring2023.acls.core;

import sjsu.cs146spring2023.acls.core.Game;
import sjsu.cs146spring2023.acls.core.TestingDictionaryTrie;
import sjsu.cs146spring2023.acls.core.TestingGame;

public class TestingGameDriver {
    public static void main(String[] args) {
        Game game = new TestingGame();
        game.setDictionaryTrie(new TestingDictionaryTrie());
        game.makeRandomBoard();
        game.solveBoard();
        System.out.println(game.getBoard());
        System.out.println(game.getAllWords());
        System.out.println(game.addWord("hello"));
        System.out.println(game.addWord("niche"));
    }
}
