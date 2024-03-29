package com.adrianyoungquist.datastructures;

import java.io.*;

public class DictionaryTrie extends AlphabetTrie {
    public String DEFAULT_FILE_NAME = "dictionary.txt";
    protected String fileName; // relative to resources

    public DictionaryTrie() {
        super();
        this.fileName = DEFAULT_FILE_NAME;
    }

    public DictionaryTrie(String fileName) {
        super();
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        root = null;
    }

    public boolean buildTrieFromFile() {
        if (fileName == null) {
            return false;
        }
        InputStream inputStream;
        inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            return false;
        }

        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                insert(line);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /*
    public static void main(String[] args) {
        DictionaryTrie trie = new DictionaryTrie("dictionary.txt");
        trie.buildTrieFromFile();
        ArrayList<String> allWords = trie.allWords();
        int total = 0;
        for (String word : allWords) {
            total += word.length();
        }
        System.out.println(allWords.subList(0, 100));
        System.out.println(trie);
        System.out.println("average length:" + total * 1.0 / allWords.size() + ", ratio size/wordCount: " + trie.size() * 1.0 / trie.getWordCount());
        System.out.println("total: " + total);
    }
     */
}
