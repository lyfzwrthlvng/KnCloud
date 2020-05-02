package com.shrey.kc.kcui.algos.graph;

import java.util.ArrayList;
import java.util.Map;

import de.blox.graphview.Graph;
import de.blox.graphview.Node;

public class TagGraphM extends Graph {
    public TagGraphM() {
        super();
    }

    public void addNode(String sourceTag, ArrayList<String> connectedTags) {
        final Node sourceNode = getNode(sourceTag) == null ? new Node(sourceTag) : getNode(sourceTag);
        connectedTags.forEach(targetTag -> {
            Node targetNode = getNode(targetTag) == null ? new Node(targetTag) : getNode(targetTag);
            this.addEdge(sourceNode, targetNode);
        });
    }
}
