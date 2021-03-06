package com.shrey.kc.kcui.algos.common;

import java.util.HashMap;

public class AlphaTrieNode {
    HashMap<String, AlphaTrieNode> branch = new HashMap<>();
    boolean endsHere = false; // does a word end here?!
    String upto;

    public AlphaTrieNode(String upto) {
        this.upto = upto;
    }

    public boolean markWord() {
        // true if no word ended here before, false otherwise
        endsHere = true;
        return true;
    }

}
