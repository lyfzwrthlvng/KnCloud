package com.shrey.kc.kcui.algos;

import com.shrey.kc.kcui.algos.common.AlphaTrie;

import java.util.ArrayList;

public class TrieForSearch {
    AlphaTrie theTrie = new AlphaTrie();

    public TrieForSearch(ArrayList<String> allTheTags) {
        // construct the trie here
        for(String tag: allTheTags) {
            theTrie.addWord(tag);
        }
    }

    public ArrayList<String> suggest(String partial) {
        return theTrie.getWordsNextTo(partial);
    }
}
