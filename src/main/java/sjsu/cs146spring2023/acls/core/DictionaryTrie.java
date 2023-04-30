package sjsu.cs146spring2023.acls.core;

public abstract class DictionaryTrie {
    public abstract void buildTrie();

    public abstract boolean contains(String word);
}
