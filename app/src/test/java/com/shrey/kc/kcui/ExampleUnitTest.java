package com.shrey.kc.kcui;

import com.shrey.kc.kcui.algos.common.AlphaTrie;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testTrie() {
        AlphaTrie at = new AlphaTrie();
        at.addWord("property");
        at.addWord("prop");
        at.addWord("prove");
        at.addWord("provess");
        at.addWord("abra");
        at.addWord("carba");
        at.printTheTrie();
    }
}