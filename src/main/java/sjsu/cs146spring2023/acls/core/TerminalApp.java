package sjsu.cs146spring2023.acls.core;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.Scanner;

public class TerminalApp {

    public static void main(String[] args) {
        TerminalGame game = new TerminalGame();
        game.play();
    }
}

class TerminalGame {
    private static final int NUM_MENU_OPTIONS = 4;

    Scanner scanner;
    Game game;

    public TerminalGame() {
        scanner = new Scanner(System.in);
        game = new BoggleGame();
    }

    private static boolean isDigit(String s) {
        try {
            int intVal = Integer.parseInt(s);
            if (0 <= intVal && intVal <= 9) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    private static boolean isNumeric(String input) {
        return input.chars().allMatch(Character::isDigit);
    }

    private void menu() {
        System.out.printf("Menu:%n1: Play new game%n2: Change minimum word length (currently %d)%n3: Enter board%n4: Quit%n", game.getMinWordLength()
        );
    }

    private int getMenuOption() {
        int choice = 0;
        String input;
        while (choice < 1 || choice > NUM_MENU_OPTIONS) {
            System.out.printf("Enter a choice between %d and %d: ", 1, NUM_MENU_OPTIONS);
            input = scanner.nextLine();
            if (!isNumeric(input)) {
                System.out.println("\tNot a number. Please try again.");
            }
            choice = Integer.parseInt(input);
        }
        System.out.println();
        return choice;
    }

    private void setMinLength() {
        System.out.printf("Current minimum length is %d letters. Enter new min length: ", game.getMinWordLength());
        String input = scanner.nextLine();
        if (isNumeric(input)) {
            game.setMinWordLength(Integer.parseInt(input));
        } else {
            System.out.printf("%s is not a valid word length.", input);
        }
    }

    private void roundPlay() {
        System.out.printf("Enter words. Words must be at least %d letters in length.%nWhen you are finished, enter a single digit.%n", game.getBoard().getMinWordLength());

        JFrame frame = getBoardFrame();
        frame.setVisible(true);

        String wordInput = scanner.nextLine();
        while (!isDigit(wordInput)) {
            System.out.println("\t" + game.addWordPrintOutput(wordInput));
            wordInput = scanner.next();
        }

        if (game.getFoundWords().size() == 1) {
            System.out.printf("\nCongratulations, you found 1 word, with a total score of %d!%n", game.getScore());
        } else {
            System.out.printf("\nCongratulations, you found %d words, with a total score of %d!%n", game.getFoundWords().size(), game.getScore());
        }
        System.out.print("Would you like to see all possible words? (y/n): ");
        String s = scanner.next();
        if ("y".equals(s) || "Y".equals(s) || "yes".equals(s) || "Yes".equals(s)) {
            System.out.println("\nAll words (asterisk indicates words you found):");
            System.out.println(getAllWordsString(50));
            System.out.printf("There were %d possible words and a total of %d possible points.%n", game.getAllWords().size(), game.totalPointsInBoard());
        }
        System.out.println("\nInput a single digit to close board and continue.");
        String input = scanner.nextLine();
        while (!isDigit(input)) {
            input = scanner.nextLine();
        }
        frame.dispose();
    }

    private void runRound() {
        game.reset();
        game.makeRandomBoard();
        roundPlay();
    }

    public void play() {
        System.out.println("Welcome to boggle.");
        boolean stop = false;
        int status;

        while (!stop) {
            menu();
            status = getMenuOption();
            switch (status) {
                case 1 -> runRound();
                case 2 -> setMinLength();
                case 3 -> setLetters();
                case 4 -> stop = true;
            }
            System.out.println();
        }
        System.out.println("\nThank you for playing boggle.");
    }

    private void setLetters() {
        System.out.println("Enter letters as a string of 16 letters like MNATNRIBAOPSQIDY. Input 'Q' for 'Qu'.");
        System.out.print("Letters: ");
        String input = scanner.nextLine();
        int expectedSize = game.getBoard().getDim() * game.getBoard().getDim();
        if (input.contains(" ")) {
            System.out.println("Letters should be entered in a row with no spaces");
        }
        else if (input.length() != expectedSize) {
            System.out.printf("Expected an input of length %d. Found %d.%n", expectedSize, input.length());
        }
        else if (!game.getBoard().setLetters(input)) {
            System.out.println("Invalid letters.");
        }
        else {
            System.out.println("Board successfully entered.");
            game.reset();
            game.solveBoard();
            roundPlay();
        }
    }

    private JFrame getBoardFrame() {
        JFrame frame = new JFrame("Boggle Board");
        int dim = game.getBoard().getCharGrid().length;
        frame.add(getBoardPanel());
        frame.setSize(new Dimension(100 * dim, 100 * dim));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        return frame;
    }

    private JPanel getBoardPanel() {
        char[][] grid = game.getBoard().getCharGrid();
        JButton button;
        String buttonText;
        Font font = new Font("Helvetica", Font.BOLD, 40);
        JPanel gridPanel = new JPanel(new GridLayout(grid.length, grid.length));
        for (char[] row : grid) {
            for (char c : row) {
                if (c == 'q') {
                    buttonText = "Qu";
                } else {
                    buttonText = String.valueOf(c).toUpperCase();
                }
                button = new JButton(buttonText);
                button.setFont(font);
                button.setFocusable(false);
                button.setRolloverEnabled(false);
                button.setEnabled(false);

                gridPanel.add(button);
            }
        }
        return gridPanel;
    }

    private String getAllWordsString(int maxWidth) {
        ArrayList<String> list = game.getAllWords();
        StringBuilder sb = new StringBuilder();
        OptionalInt tmpLongestLength = list.stream().mapToInt(String::length).max();
        int longestLength = tmpLongestLength.isPresent() ? tmpLongestLength.getAsInt() : 5;
        longestLength++;
        int inRow = maxWidth / (longestLength + 1);
        for (int i = 0; i < list.size(); i++) {
            if (game.hasFound(list.get(i))) {
                sb.append(String.format("%-" + longestLength + "s ", "*" + list.get(i)));
            } else {
                sb.append(String.format("%-" + longestLength + "s ", list.get(i)));
            }
            if (i % inRow == inRow - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
