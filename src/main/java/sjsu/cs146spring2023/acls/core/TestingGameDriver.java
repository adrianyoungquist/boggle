package sjsu.cs146spring2023.acls.core;

public class TestingGameDriver {
    public static void main(String[] args) {
        Game game = new TestingGame();
        game.setDictionaryTrie(new DictionaryTrie("dictionary.txt"));
        game.makeRandomBoard();
        game.solveBoard();
        System.out.println(game.getBoard());
        System.out.println(game.getAllWords());
        System.out.println(game.addWord("hello"));
        System.out.println(game.addWord("niche"));
    }
}
