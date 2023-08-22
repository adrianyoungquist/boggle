package com.adrianyoungquist.datastructures;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public abstract class AbstractWordBuilder {
    /*
    wordBuilder is a util that builds words up to a set length that only allows letters that are
    defined as neighbors. It does NOT check the validity of the word. It is only
    used to build words.

    Each letter is defined by its index. AbstractWordBuilder actually never uses letters.
    The only time letters are used is in the abstract method getLetter.

    letterStack stores the indices of the letters in the word, not the letters themselves.
     */
    public static final int DEFAULT_MAX_WORD_LENGTH = 10;

    protected boolean[] used; // whether a letter at a position has already been used in a word

    protected int[] letterStack; // will give the word, and letters can be popped off and pushed on

    protected int currentPosition;
    protected int stackSize;

    protected int maxWordLength;

    public AbstractWordBuilder() {
        this(DEFAULT_MAX_WORD_LENGTH);
    }
    public AbstractWordBuilder(int maxWordLength) {
        this.maxWordLength = maxWordLength;
        used = new boolean[maxWordLength];
        letterStack = new int[maxWordLength];
        currentPosition = 0;
        stackSize = 0;
    }

    protected abstract char letterAtIndex(int letterIndex);

    protected abstract int[] currentPositionNeighbors();

    public boolean push(int location) {
        if (used[location])
            return false;
        if (stackSize > 0 && Arrays.stream(currentPositionNeighbors()).noneMatch(num -> num == location)) { // not adjacent
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
        for (int i = 0; i < maxWordLength; i++) {
            used[i] = false;
        }
        stackSize = 0;
        currentPosition = 0;
    }

    public String word() {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < stackSize; i++) {
            c = letterAtIndex(letterStack[i]);
            sb.append(c);
        }
        return sb.toString();
    }

    public int[] validNext() {
        if (stackSize == 0) {
            return IntStream.range(0, maxWordLength).toArray();
        }
        return Arrays.stream(currentPositionNeighbors()).filter(num -> !used[num]).toArray();
    }

    @Override
    public String toString() {
        if (stackSize == 0) {
            return "empty wordBuilder";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("wb: ");
        for (int i = 0; i < stackSize; i++) {
            sb.append(letterStack[i]).append(": ").append(letterAtIndex(letterStack[i])).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getStackSize() {
        return stackSize;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }
}