package sjsu.cs146spring2023.acls.core;

import java.util.ArrayList;

public class BoggleBoard extends Board {
    public static final int DEFAULT_DIM = 4;

    ArrayList<String> words;
    int rows; int cols;

    public BoggleBoard() {
        this(DEFAULT_DIM, DEFAULT_DIM);
    }

    public BoggleBoard(int rows, int cols) {
        if (rows == 0 || cols == 0) {
            throw new IllegalArgumentException("board must have positive dimensions");
        }
        this.rows = rows;
        this.cols = cols;
        board = new char[rows][cols];
        words = new ArrayList<>();
        solved = false;
    }

    public BoggleBoard(int dim) {
        this(dim, dim);
    }

    public BoggleBoard(char[][] board) {
        // todo: verify valid boggle board
        this(board.length, board.length == 0 ? 0 : board[0].length);
        Board.copy2D(board, this.board);
    }

    @Override
    public boolean setLettersFromList(ArrayList<Character> letters) {
        // todo: verify valid boggle board
        if (letters.size() != rows * cols) {
            return false;
        }
        for (int i = 0; i < letters.size(); i++) {
            board[i / rows][i % rows] = letters.get(i);
        }
        return true;
    }

    @Override
    public boolean setLetters(char[][] letterGrid) {
        // todo: verify valid boggle board
        // rows != 0, so if rows == letterGrid.length, letterGrid[0] will never be out of bounds
        if (rows != letterGrid.length || cols != letterGrid[0].length) {
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
                }
                else {
                    sb.append(Character.toUpperCase(board[r][c])).append("   ");
                }
            }
            sb.append("\n");
        }

        return sb.substring(0, sb.length() - 1);
    }
}
