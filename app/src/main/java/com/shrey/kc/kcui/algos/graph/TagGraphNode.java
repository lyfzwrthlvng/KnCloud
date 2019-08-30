package com.shrey.kc.kcui.algos.graph;

import java.util.ArrayList;

public class TagGraphNode {

    int weight; // number of knowledges this is present i

    ArrayList<TagGraphNode> neighbors; // other tags in the knowledges it's present in

    // len neighbors = weight
    String tag;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public ArrayList<TagGraphNode> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<TagGraphNode> neighbors) {
        this.neighbors = neighbors;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
