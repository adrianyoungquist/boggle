package sjsu.cs146spring2023.acls.core;

import java.util.ArrayList;

public abstract class DictionaryTrie {
    public abstract void buildTrie();
    public abstract boolean contains(String word);
}
