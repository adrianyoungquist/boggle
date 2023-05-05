package sjsu.cs146spring2023.acls.core;

import java.util.ArrayList;

public class TestingGameDriver {
    public static void main(String[] args) {
        Game game = new BoggleGame();
        game.setDictionaryTrie(new DictionaryTrie("dictionary.txt"));
        game.makeRandomBoard();
        game.solveBoard();
        System.out.println(game.getBoard());
        ArrayList<String> words = game.getAllWords();
        System.out.println(words);
        System.out.println(game.addWord(words.get(0)));
        System.out.println(game.addWord(words.get(words.size() - 1)));
        System.out.println(game.addWord("hello"));
        System.out.println(game.addWord("sdlkfjsl"));
        System.out.println(game.addWord("at"));
        System.out.println(game.getFoundWords());
        System.out.println(game.getScore());
        System.out.printf("Possible score: %d%n", game.totalPointsInBoard());
    }
}
