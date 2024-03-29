package com.adrianyoungquist.ui;

import com.adrianyoungquist.model.Game;
import javafx.application.Application;
import javafx.stage.Stage;
import com.adrianyoungquist.model.BoggleGame;
import com.adrianyoungquist.datastructures.DictionaryTrie;

import java.util.ArrayList;

public class BoggleApp extends Application {
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

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
    }
}
