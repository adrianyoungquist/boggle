package sjsu.cs146spring2023.acls.core;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoggleBoard extends Board {
    public static final int DEFAULT_DIM = 4;
    public static final int DEFAULT_MIN_WORD_LENGTH = 3;
    private static final Random rand = new Random();
    protected TreeSet<String> wordList;
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
        this(DEFAULT_MIN_WORD_LENGTH, new DictionaryTrie());
    }

    public BoggleBoard(int minWordLength) {
        this(minWordLength, new DictionaryTrie());
    }

    public BoggleBoard(DictionaryTrie dictionaryTrie) {
        this(DEFAULT_MIN_WORD_LENGTH, dictionaryTrie);
    }

    public BoggleBoard(int minWordLength, DictionaryTrie dictionaryTrie) {
        this.minWordLength = minWordLength;
        this.dim = DEFAULT_DIM;
        board = new char[dim][dim];
        wordList = new TreeSet<>();
        solved = false;
        classicDice = true;
        dice = new Dice(classicDice);
        dieAtIndexList = new int[dim * dim];
        this.dictionaryTrie = dictionaryTrie;
        if (dictionaryTrie.size() == 0) {
            dictionaryTrie.buildTrieFromFile();
        }
    }

    /*
    public BoggleBoard(int dim) {

    }
    */

    /*
    public BoggleBoard(char[][] board) {
        this();
        Board.copy2D(board, this.board);
    }
     */

    public static void main(String[] args) {
        BoggleBoard board = new BoggleBoard();
        board.setRandom();
        System.out.println(board);

        System.out.println(board.dictionaryTrie);

        board.solve();

        ArrayList<String> words = board.getWordList();
        System.out.println(words);
        for (String s : words) {
            if (!board.canMake(s)) {
                System.out.println(s);
            }
        }
        System.out.println("Done with found words");
        String[] toSearch = new String[]{"hello", "cat", "a", "slksd", "a2l12", "aAbs", "al"};
        for (String s : toSearch) {
            if (board.canMake(s) && !words.contains(s)) {
                System.out.printf("%s false positive", s);
            }
            if (!board.canMake(s) && words.contains(s)) {
                System.out.printf("%s false negative", s);
            }
        }
        System.out.println("Done");

        BoggleBoard board1 = new BoggleBoard();
        board1.setLetters(new char[]{'q', 'a', 'c', 'k', 'a', 'r', 's', 'y', 'l', 'k', 'e', 'i', 'n', 'g', 'e', 'm'});
        System.out.println(board1);
        board1.solve();
        System.out.println(board1.getWordList());
        System.out.println(board1.canMake("qacky"));
        System.out.println(board1.canMake("quacky"));
    }

    @Override
    public boolean setLetters(char[] letters) {
        if (letters.length != dim * dim || !isAlphanumeric(letters)) {
            return false;
        }
        dice = new Dice(letters);
        initializeDieLocations();
        setCharBoard();
        wordList.clear();
        solved = false;

        return true;
    }

    @Override
    public boolean setLetters(char[][] letterGrid) {
        if (dim != letterGrid.length || dim != letterGrid[0].length) {
            return false;
        }
        Board.copy2D(letterGrid, board);

        char[] letters = new char[dim * dim];
        for (int i = 0; i < dim; i++) {
            System.arraycopy(letterGrid[i], 0, letters, i * 3, dim);
        }
        return setLetters(letters);
    }

    @Override
    public boolean setLetters(String letterGrid) {
        return setLetters(letterGrid.toCharArray());
    }

    @Override
    public void solve() {
        if (dictionaryTrie.size() == 0) {
            dictionaryTrie.buildTrieFromFile();
        }
        WordBuilder wb = new WordBuilder();
        Set<String> found = new HashSet<>();
        solveHelper(dictionaryTrie.getRoot(), wb, found);
        solved = true;
        wordList = new TreeSet<>(found);
    }

    @Override
    public ArrayList<String> getWordList() {
        if (!solved) {
            solve();
        }
        return (ArrayList<String>) wordList.stream().sorted(new WordLengthComparator()).collect(Collectors.toList());
    }

    @Override
    public void setMinWordLength(int minWordLength) {
        this.minWordLength = minWordLength;
        wordList.clear();
        solved = false;
    }

    @Override
    public void setRandom() {
        dice.randomizeDiceSides(rand);
        RandomDiceOrderFisherYates();
        setCharBoard();
        wordList.clear();
        solved = false;
    }

    @Override
    public boolean canMake(String word) {
        if (word == null || word.length() < minWordLength) {
            return false;
        }

        word = word.toLowerCase();
        if (!isAlphanumeric(word)) {
            System.out.println("Not alphanumeric");
            return false;
        }
        WordBuilder wb = new WordBuilder();
        return wordIsValid(word, 0, wb);
    }

    private boolean wordIsValid(String word, int index, WordBuilder wb) {
        if (index == word.length()) {
            return true;
        }

        int[] valid = wb.validNext();
        boolean found;
        for (int j : valid) {
            if (dieFromLocationIndex(j).getValue() == word.charAt(index)) {
                wb.push(j);
                if (word.charAt(index) == 'q') { // u must follow q
                    if (index == word.length() - 1 || word.charAt(index + 1) != 'u') {
                        return false;
                    } else {
                        index++;
                    }
                }
                found = wordIsValid(word, index + 1, wb);
                if (found) {
                    return true;
                }
                wb.pop();
            }
        }
        return false;
    }

    @Override
    public void reset() {
        solved = false;
        wordList.clear();
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

        initializeDieLocations();
        for (int i = 0; i < dim * dim - 1; i++) {
            swapIndex = i + rand.nextInt( dim * dim - i);
            tmp = dieAtIndexList[swapIndex];
            dieAtIndexList[swapIndex] = dieAtIndexList[i];
            dieAtIndexList[i] = tmp;
        }
    }

    private void initializeDieLocations() {
        for (int i = 0; i < dim * dim; i++) {
            dieAtIndexList[i] = i;
        }
    }

    private Die dieFromLocationIndex(int index) {
        return dice.get(dieAtIndexList[index]);
    }

    private void solveHelper(AlphaTrieNode node, WordBuilder wb, Set<String> words) {
        if (node == null) {
            return;
        }
        if (wb.stackSize >= minWordLength && node.isWord()) {
            words.add(wb.word());
        }

        AlphaTrieNode next;
        int[] validNext = wb.validNext();
        for (int j : validNext) {
            next = node.getChildAtChar(dieFromLocationIndex(j).getValue());
            if (next != null) {
                if (next.value == 'q') { // u must follow q
                    next = next.getChildAtChar('u');
                    if (next == null) { // if there are no words with a u after the q here
                        continue;
                    }
                }
                wb.push(j);
                solveHelper(next, wb, words);
                wb.pop();
            }
        }
    }

    private void setCharBoard() {
        for (int i = 0; i < dim * dim; i++) {
            board[i / dim][i % dim] = dieFromLocationIndex(i).getValue();
        }
    }

    private static boolean isAlphanumeric(String string) {
        return string.chars().noneMatch(s -> (s < 'a' || s > 'z'));
    }
    private static boolean isAlphanumeric(char[] chars) {
        for (char c : chars) {
            c = Character.toLowerCase(c);
            if (c < 'a' || c > 'z') {
                return false;
            }
        }
        return true;
    }
    class WordBuilder {
        private final int[][] NEIGHBORS = new int[][]{
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
            if (stackSize != 0) {
                currentPosition = letterStack[stackSize - 1];
            }
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
            StringBuilder sb = new StringBuilder();
            char c;
            for (int i = 0; i < stackSize; i++) {
                c = dieFromStackIndex(i).getValue();
                sb.append(c);
                if (c == 'q') {
                    sb.append('u');
                }
            }
            return sb.toString();
        }

        public int[] validNext() {
            if (stackSize == 0) {
                return IntStream.range(0, dim * dim).toArray();
            }
            return Arrays.stream(NEIGHBORS[currentPosition]).filter(num -> !used[num]).toArray();
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
