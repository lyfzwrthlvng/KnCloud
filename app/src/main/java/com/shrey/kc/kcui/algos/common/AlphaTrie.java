package com.shrey.kc.kcui.algos.common;

import java.util.ArrayList;

public class AlphaTrie {
    AlphaTrieNode theRoot = new AlphaTrieNode(""); // no word can end at the root

    private void addAndProceed(String word, int pos, AlphaTrieNode currentNode) {
        if(!currentNode.branch.containsKey(String.valueOf(word.charAt(pos)))) {
            currentNode.branch.put(String.valueOf(word.charAt(pos)), new AlphaTrieNode(word.substring(0, pos+1)));
        }
        if(pos == word.length() - 1) {
            // done
            currentNode.branch.get(String.valueOf(word.charAt(pos))).endsHere = true;
            return;
        } else {
            addAndProceed(word, pos+1, currentNode.branch.get(String.valueOf(word.charAt(pos))));
        }
        return;
    }

    public boolean addWord(String word) {
        // add this word to the trie
        if(word == null || word.length()==0) {
            return false;
        }
        addAndProceed(word, 0, theRoot);
        return true;
    }

    public String getWordsNextTo(String nextTo) {
        // first find this
        AlphaTrieNode searchNode = search(nextTo);
        if(searchNode == null) {
            return null;
        }
        // game on
        //for(searchNode.branch.keySet()) {
        //    String
        //}
        return "";
    }

    private AlphaTrieNode search(String searchThis) {
        if(searchThis == null || searchThis.length()==0) {
            return null;
        }
        AlphaTrieNode currentNode = theRoot;
        for(int pos=0; pos<searchThis.length(); pos++) {
            if(!currentNode.branch.containsKey(searchThis.charAt(pos))) {
                // search stopped here, exit!!
                return null;
            } else {
                currentNode = currentNode.branch.get(searchThis.charAt(pos));
            }
        }
        return currentNode;
    }

    public void printTheTrie() {
        // print words in the trie, in bfs manner
        ArrayList<AlphaTrieNode> bq = new ArrayList<>();
        AlphaTrieNode currentNode = theRoot;
        //String currentLot = "";
        while(currentNode != null) {
            for(String alpha: currentNode.branch.keySet()) {
                bq.add(currentNode.branch.get(alpha));
                //currentLot += alpha;
                if(currentNode.branch.get(alpha).endsHere) {
                    // a word ends here, print
                    System.out.println(currentNode.branch.get(alpha).upto);
                }
            }
            currentNode = (!bq.isEmpty()) ? bq.remove(0) : null;
        }
    }
}
