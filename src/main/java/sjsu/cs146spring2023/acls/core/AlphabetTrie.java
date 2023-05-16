package sjsu.cs146spring2023.acls.core;

import java.util.ArrayList;

public class AlphabetTrie {
    public char ROOT_CHAR = '-';
    // Trie where the only possible values
    protected int size; // number of nodes, root not included
    protected int wordCount;
    protected AlphaTrieNode root; // root does not have data, it is just the parent of an arr of nodes

    public AlphabetTrie() {
        root = new AlphaTrieNode(ROOT_CHAR);
    }

    public static int getIndex(char c) {
        c = Character.toLowerCase(c);
        return c - 'a';
    }

    public static char getLetterFromIndex(int index) {
        return (char) ('a' + index);
    }

    public static void main(String[] args) {
        AlphabetTrie trie = new AlphabetTrie();
        trie.insert("sandstone");
        System.out.println(trie);
        System.out.println(trie.allWords());

        trie.insert("sank");
        System.out.println(trie);
        System.out.println(trie.allWords());

        trie.insert("armadillo");
        System.out.println(trie);
        System.out.println(trie.allWords());

        trie.insert("yellow");
        System.out.println(trie);
        System.out.println(trie.allWords());

        trie.insert("yell");
        System.out.println(trie);
        System.out.println(trie.allWords());

        trie.insert("yelp");
        System.out.println(trie);
        System.out.println(trie.allWords());

        trie.insert("yell");
        System.out.println(trie);
        System.out.println(trie.allWords());

        trie.insert("sand");
        System.out.println(trie);
        System.out.println(trie.allWords());
    }

    private static boolean notAlphanumeric(String word) {
        return word.toLowerCase().chars().anyMatch(num -> (num < 'a' || num > 'z'));
    }

    public AlphaTrieNode getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public int wordCount() {
        return wordCount;
    }

    public boolean contains(String word) {
        if (notAlphanumeric(word)) {
            return false;
        }
        word = word.toLowerCase();
        Pair<AlphaTrieNode, Integer> foundPair = findLocation(word);
        return foundPair.getValue() == word.length() && foundPair.getKey().isWord();
    }

    public boolean containsPrefix(String prefix) {
        if (notAlphanumeric(prefix)) {
            return false;
        }
        prefix = prefix.toLowerCase();
        Pair<AlphaTrieNode, Integer> foundPair = findLocation(prefix);
        return (foundPair.getValue() == prefix.length());
    }

    public AlphaTrieNode getPrefix(String prefix) {
        prefix = prefix.toLowerCase();
        if (notAlphanumeric(prefix)) {
            return null;
        }
        return findLocation(prefix).getKey();
    }

    public AlphaTrieNode insert(String word) {
        word = word.toLowerCase();
        if (notAlphanumeric(word)) {
            return null;
        }

        Pair<AlphaTrieNode, Integer> foundPair = findLocation(word);
        if (foundPair.getValue() == word.length()) { // full word
            if (!foundPair.getKey().isWord()) {
                wordCount++;
                foundPair.getKey().setIsWord(true);
            }
            return foundPair.getKey();
        }
        return insertFullSuffix(foundPair.getKey(), word, foundPair.getValue());
    }

    public boolean remove(String word) { // soft delete
        if (notAlphanumeric(word)) {
            return false;
        }

        word = word.toLowerCase();
        Pair<AlphaTrieNode, Integer> foundPair = findLocation(word);
        if (foundPair.getValue() < word.length()) {
            return false;
        }
        if (!foundPair.getKey().isWord()) { // no word to remove
            return false;
        }
        foundPair.getKey().setIsWord(false); // soft deletion
        return true;
    }

    public ArrayList<String> allWords() {
        return allWords(root, new ArrayList<>(), "");
    }

    public String lastWord() {
        StringBuilder sb = new StringBuilder();
        sb = lastWord(root, sb);
        return sb.substring(1);
    }

    private StringBuilder lastWord(AlphaTrieNode node, StringBuilder sb) {
        if (node == null) {
            return sb;
        }
        sb.append(node.value);
        for (int i = AlphaTrieNode.NUM_CHILDREN - 1; i >= 0; i--) {
            if (node.children[i] != null) {
                return lastWord(node.children[i], sb);
            }
        }
        return sb;
    }

    private ArrayList<String> allWords(AlphaTrieNode node, ArrayList<String> currentWords, String prefix) {
        if (node == null) {
            return currentWords;
        }

        if (node.isWord()) {
            currentWords.add(prefix);
        }
        for (int i = 0; i < AlphaTrieNode.NUM_CHILDREN; i++) {
            currentWords = allWords(node.children[i], currentWords, prefix + getLetterFromIndex(i));
        }

        return currentWords;
    }

    private Pair<AlphaTrieNode, Integer> findLocation(String toFind) {
        // returns the node that is the word or is where the rest of the word would go
        // and how many letters match (so one more than the last index)
        toFind = toFind.toLowerCase();

        int index = 0;
        AlphaTrieNode node = root;
        AlphaTrieNode prev = node;

        while (node != null && index < toFind.length()) {
            prev = node;
            node = node.getChildAtChar(toFind, index);
            index++;
        }
        if (node != null) {
            return new Pair<>(node, index);
        }
        return new Pair<>(prev, index - 1);
    }

    private AlphaTrieNode insertFullSuffix(AlphaTrieNode node, String word, int index) {
        AlphaTrieNode cur = node;
        AlphaTrieNode newNode;
        for (int i = index; i < word.length(); i++) {
            newNode = new AlphaTrieNode(word.charAt(i), false, cur);
            cur.children[getIndex(word.charAt(i))] = newNode;
            cur = newNode;
            size++;
        }
        cur.setIsWord(true);
        wordCount++;
        return cur;
    }

    @Override
    public String toString() {
        return "AlphabetTrie: " +
                "size=" + size +
                ", wordCount=" + wordCount +
                ", last word=" + lastWord();
    }
}


class AlphaTrieNode {
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

