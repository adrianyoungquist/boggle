package sjsu.cs146spring2023.acls.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public class BoggleBoard extends Board {
    public static final int DEFAULT_DIM = 4;
    private final Random rand;
    protected ArrayList<String> words;
    protected int dim; // assumes square
    protected int[] dieAtIndexList; // stores which die # is at location in grid
    /*
    Grid is arranged:
    0   1   2   3
    4   5   6   7
    8   9   10  11
    12  13  14  15
     */
    protected boolean classicDice;
    protected Dice dice;

    public BoggleBoard() {
        this.dim = DEFAULT_DIM;
        board = new char[dim][dim];
        words = new ArrayList<>();
        solved = false;
        classicDice = true;
        rand = new Random();
        dice = new Dice(classicDice);
        dieAtIndexList = new int[dim * dim];
    }

    /*
    public BoggleBoard(int dim) {

    }
    */

    /*
    public BoggleBoard(char[][] board) {
        // todo: verify valid boggle board
        this();
        Board.copy2D(board, this.board);
    }
     */

    public static void main(String[] args) {
        BoggleBoard board = new BoggleBoard();
        board.setRandom();
        System.out.println(board);
        WordBuilder wb = board.getWordBuilder();
        boolean res;
        int popped;
        int pushing;

        pushing = 0;
        res = wb.push(pushing);
        System.out.printf("%d: %s; %s; current: %d; %s%n", pushing, res, wb, wb.currentPosition, wb.word());
        pushing = 1;
        res = wb.push(pushing);
        System.out.printf("%d: %s; %s; current: %d; %s%n", pushing, res, wb, wb.currentPosition, wb.word());
        pushing = 5;
        res = wb.push(pushing);
        System.out.printf("%d: %s; %s; current: %d; %s%n", pushing, res, wb, wb.currentPosition, wb.word());
        pushing = 11;
        res = wb.push(pushing);
        System.out.printf("%d: %s; %s; current: %d; %s%n", pushing, res, wb, wb.currentPosition, wb.word());
        pushing = 1;
        res = wb.push(pushing);
        System.out.printf("%d: %s; size=%d; %s; current: %d; %s%n", pushing, res, wb.stackSize, wb, wb.currentPosition, wb.word());
        popped = wb.pop();
        System.out.printf("%d: %s; size=%d; %s; current: %d; %s%n", popped, res, wb.stackSize, wb, wb.currentPosition, wb.word());
        popped = wb.pop();
        System.out.printf("%d: %s; size=%d; %s; current: %d; %s%n", popped, res, wb.stackSize, wb, wb.currentPosition, wb.word());
    }

    @Override
    public boolean setLettersFromList(ArrayList<Character> letters) {
        // todo: verify valid boggle board
        if (letters.size() != dim * dim) {
            return false;
        }
        for (int i = 0; i < letters.size(); i++) {
            board[i / dim][i % dim] = letters.get(i);
        }
        return true;
    }

    @Override
    public boolean setLetters(char[][] letterGrid) {
        // todo: verify valid boggle board
        // rows != 0, so if rows == letterGrid.length, letterGrid[0] will never be out of bounds
        if (dim != letterGrid.length || dim != letterGrid[0].length) {
            return false;
        }
        Board.copy2D(letterGrid, board);
        return true;
    }

    @Override
    public void solve() {

    }

    @Override
    public ArrayList<String> getWords() {
        return null;
    }

    @Override
    public void setRandom() {
        dice.randomizeDiceSides(rand);
        RandomDiceOrderFisherYates();

        for (int i = 0; i < dim * dim; i++) {
            System.out.printf("i=%d, dieNum=%d, die=%s, side=%d, val=%c%n", i, dieAtIndexList[i], new String(dieFromLocationIndex(i).getValues()), dieFromLocationIndex(i).getSide(), dieFromLocationIndex(i).getValue());
            board[i / dim][i % dim] = dieFromLocationIndex(i).getValue();
        }
    }

    @Override
    public boolean wordIsValid(String word) {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c] == 'q') {
                    sb.append("Qu  ");
                } else {
                    sb.append(Character.toUpperCase(board[r][c])).append("   ");
                }
            }
            sb.append("\n");
        }

        return sb.substring(0, sb.length() - 1);
    }

    public WordBuilder getWordBuilder() {
        return new WordBuilder();
    }

    private void RandomDiceOrderFisherYates() {
        int swapIndex;
        int tmp;
        // initialize
        for (int i = 0; i < dim * dim; i++) {
            dieAtIndexList[i] = i;
        }

        for (int i = 0; i < dim * dim - 1; i++) {
            swapIndex = rand.nextInt(i, dim * dim);
            tmp = dieAtIndexList[swapIndex];
            dieAtIndexList[swapIndex] = dieAtIndexList[i];
            dieAtIndexList[i] = tmp;
        }
    }

    private Die dieFromLocationIndex(int index) {
        return dice.get(dieAtIndexList[index]);
    }

    class WordBuilder {
        private static final int[][] NEIGHBORS = new int[][]{
                {1, 4, 5},                      // 0
                {0, 2, 4, 5, 6},                // 1
                {1, 3, 5, 6, 7},                // 2
                {2, 6, 7},                      // 3
                {0, 1, 5, 8, 9},                // 4
                {0, 1, 2, 4, 6, 8, 9, 10},      // 5
                {1, 2, 3, 5, 7, 9, 10, 11},     // 6
                {2, 3, 6, 10, 11},              // 7
                {4, 5, 9, 12, 13},              // 8
                {4, 5, 6, 8, 10, 12, 13, 14},   // 9
                {5, 6, 7, 9, 11, 13, 14, 15},   // 10
                {6, 7, 10, 14, 15},             // 11
                {8, 9, 13},                     // 12
                {8, 9, 10, 12, 14},             // 13
                {9, 10, 11, 13, 15},            // 14
                {10, 11, 14}                    // 15
        };

        boolean[] used; // whether a letter at a position has already been used in a word

        int[] letterStack; // will give the word, and letters can be popped off and pushed on

        int currentPosition;
        int stackSize;

        public WordBuilder() {
            used = new boolean[dim * dim];
            letterStack = new int[dim * dim];
            currentPosition = 0;
            stackSize = 0;
        }

        public boolean push(int location) {
            if (used[location])
                return false;
            if (stackSize > 0 && Arrays.stream(NEIGHBORS[currentPosition]).noneMatch(num -> num == location)) { // not adjacent
                return false;
            }
            letterStack[stackSize++] = location; // can never overflow because there aren't duplicates
            currentPosition = location;
            used[location] = true;
            return true;
        }

        public int pop() {
            if (stackSize == 0) {
                throw new NoSuchElementException("Stack is empty");
            }
            stackSize--;
            int ret = letterStack[stackSize];
            currentPosition = letterStack[stackSize - 1];
            used[ret] = false;
            return ret;
        }

        public void reset() {
            for (int i = 0; i < dim * dim; i++) {
                used[i] = false;
            }
            stackSize = 0;
            currentPosition = 0;
        }

        public String word() {
            char[] chars = new char[stackSize];
            for (int i = 0; i < stackSize; i++) {
                chars[i] = dieFromStackIndex(i).getValue();
            }
            return new String(chars);
        }

        private Die dieFromStackIndex(int index) {
            return dice.get(dieAtIndexList[letterStack[index]]);
        }

        @Override
        public String toString() {
            if (stackSize == 0) {
                return "empty wordBuilder";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("wb: ");
            for (int i = 0; i < stackSize; i++) {
                sb.append(letterStack[i]).append(": ").append(dieFromStackIndex(i)).append(", ");
            }
            return sb.substring(0, sb.length() - 2);
        }
    }
}
