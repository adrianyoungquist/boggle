package com.adrianyoungquist.datastructures;

import java.util.ArrayList;

public class AlphaTrieNode {
    public static final int NUM_CHILDREN = 26; // the 26 letters


    protected char value;
    protected boolean isWord;
    protected AlphaTrieNode parent;
    protected AlphaTrieNode[] children;

    public AlphaTrieNode() {
        this('-', false, null);

    }

    public AlphaTrieNode(char value) {
        this(value, false, null);
    }

    public AlphaTrieNode(char value, boolean isWord) {
        this(value, isWord, null);
    }

    public AlphaTrieNode(char value, boolean isWord, AlphaTrieNode parent) {
        value = Character.toLowerCase(value);
        this.value = value;
        this.isWord = isWord;
        this.parent = parent;
        children = new AlphaTrieNode[NUM_CHILDREN];
    }

    public AlphaTrieNode[] getChildren() {
        return children.clone(); // shallow copy
    }

    public ArrayList<Character> nextLetters() {
        ArrayList<Character> characters = new ArrayList<>();
        for (int i = 0; i < NUM_CHILDREN; i++) {
            if (children[i] != null) {
                characters.add((char) ('a' + i));
            }
        }
        return characters;
    }

    public AlphaTrieNode getParent() {
        return parent;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = Character.toLowerCase(value);
    }

    public boolean isWord() {
        return isWord;
    }

    public void setIsWord(boolean word) {
        isWord = word;
    }

    public AlphaTrieNode getChildAtChar(char c) {
        c = Character.toLowerCase(c);
        return children[c - 'a'];
    }

    public AlphaTrieNode getChildAtChar(String word, int index) {
        return children[word.charAt(index) - 'a'];
    }

    public int getNumChildren() {
        int count = 0;
        for (int i = 0; i < NUM_CHILDREN; i++) {
            if (children[i] != null) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return "AlphaTrieNode{" +
                "value=" + value +
                ", isWord=" + isWord +
                ", numChildren=" + getNumChildren() +
                '}';
    }
}
