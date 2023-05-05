package sjsu.cs146spring2023.acls.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import sjsu.cs146spring2023.acls.core.DictionaryTrie;
import sjsu.cs146spring2023.acls.core.Game;
import sjsu.cs146spring2023.acls.core.TestingGame;

public class BoggleApp extends Application {
    public static void main(String[] args) {
        Game game = new TestingGame();
        game.setDictionaryTrie(new DictionaryTrie());
        game.makeRandomBoard();
        game.solveBoard();
        System.out.println(game.getBoard());
        System.out.println(game.getAllWords());
        System.out.println(game.addWord("hello"));
        System.out.println(game.addWord("niche"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
    }
}
