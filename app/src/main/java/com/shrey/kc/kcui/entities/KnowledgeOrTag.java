package com.shrey.kc.kcui.entities;

import java.io.Serializable;

public class KnowledgeOrTag implements Serializable {
    private String tag;
    private String knowledge;

    public String getTag() {
        return tag;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public boolean isPrintTag() {
        return printTag;
    }

    private boolean printTag;

    public KnowledgeOrTag(String tag, String knowledge, boolean printTag) {
        this.tag = tag;
        this.knowledge =knowledge;
        this.printTag = printTag;
    }

}
