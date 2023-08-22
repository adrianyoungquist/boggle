package com.adrianyoungquist.utils;

import java.util.Comparator;

public class WordLengthComparator implements Comparator<String> {
    @Override
    public int compare(String w1, String w2) {
        if (w1.length() > w2.length()) {
            return 1;
        }
        if (w1.length() < w2.length()) {
            return -1;
        }
        return w1.compareTo(w2);
    }
}