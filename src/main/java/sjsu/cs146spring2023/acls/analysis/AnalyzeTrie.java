package sjsu.cs146spring2023.acls.analysis;

import sjsu.cs146spring2023.acls.core.DictionaryTrie;

import java.util.ArrayList;

public class AnalyzeTrie {
    public static long[] analyzeContains() {
        DictionaryTrie trie = new DictionaryTrie();
        trie.buildTrieFromFile();
        ArrayList<String> list = trie.allWords();

        DictionaryTrie toFindTrie = new DictionaryTrie("dictionary.txt");
        toFindTrie.buildTrieFromFile();
        ArrayList<String> toFind = toFindTrie.allWords();

        TimeContains timeContains = new TimeContains();

        long trieTime = timeContains.timeTrie(trie, toFind, 1000);
        long listTime = timeContains.timeList(list, toFind, 1000);

        return new long[]{trieTime, listTime};
    }

    public static double[] letterCountRatio() {
        String[] fileNames = new String[]{"small.txt", "medium.txt", "large.txt", "dictionary.txt"};
        DictionaryTrie trie;
        ArrayList<String> words;
        int totalLetters;

        double[] ratios = new double[4];
        for (int i = 0; i < fileNames.length; i++) {
            totalLetters = 0;
            trie = new DictionaryTrie(fileNames[i]);
            trie.buildTrieFromFile();
            words = trie.allWords();
            for (String w : words) {
                totalLetters += w.length();
            }
            ratios[i] = trie.size() * 1.0 / totalLetters;
        }
        return ratios;
    }

    public static void main(String[] args) {
        //long[] times = analyzeContains();
        //System.out.printf("Times: trie=%d, list=%d%n", times[0], times[1]);
        // result for toFind=dictionary.txt, reps=1000, search for word and word + "e":
        // Trie: 22244, list: 51412

        double[] ratios = letterCountRatio();
        System.out.printf("Letter ratio (# nodes/# letters): small=%.5f, medium=%.5f, large=%.5f, full=%.5f%n", ratios[0], ratios[1], ratios[2], ratios[3]);
    }
}

class TimeContains {
    public <E extends Comparable<? super E>> boolean binarySearch(ArrayList<E> arr, E toFind) {
        int start = 0;
        int end = arr.size();
        int mid;
        int cmp;

        while (end - start > 1) {
            mid = start + (end - start) / 2;
            cmp = toFind.compareTo(arr.get(mid));
            if (cmp == 0) {
                return true;
            }
            if (cmp < 0) {
                end = mid;
            } else {
                start = mid + 1;
            }
        }
        return arr.get(start).compareTo(toFind) == 0;
    }

    public long timeList(ArrayList<String> list, ArrayList<String> toFind, int reps) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < reps; i++) {
            for (String word : toFind) {
                binarySearch(list, word);
                binarySearch(list, word + "e");  // usually not a word
            }
        }
        long stop = System.currentTimeMillis();
        return stop - start;
    }

    public long timeTrie(DictionaryTrie trie, ArrayList<String> toFind, int reps) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < reps; i++) {
            for (String word : toFind) {
                trie.contains(word);
                trie.contains(word + "e");
            }
        }
        long stop = System.currentTimeMillis();
        return stop - start;
    }
}
