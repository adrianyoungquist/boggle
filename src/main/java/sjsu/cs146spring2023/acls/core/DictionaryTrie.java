package sjsu.cs146spring2023.acls.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DictionaryTrie extends AlphabetTrie {
    String fileName; // relative to resources

    public DictionaryTrie() {
        this(null);
    }

    public DictionaryTrie(String fileName) {
        super();
        this.fileName = fileName;
    }

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

    public boolean buildTrieFromFile() {
        if (fileName == null) {
            return false;
        }
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("src/main/resources/" + fileName));
            String line;
            while ((line = br.readLine()) != null) {
                insert(line);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
