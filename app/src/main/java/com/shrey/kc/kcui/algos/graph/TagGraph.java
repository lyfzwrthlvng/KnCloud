package com.shrey.kc.kcui.algos.graph;

import java.util.ArrayList;

public class TagGraph {

    int numVertices;
    ArrayList<ArrayList<Integer>> adjMatrix = new ArrayList<>();
    ArrayList<TagGraphNode> nodes = new ArrayList<>();

    TagGraph(int initVertexCount) {
        numVertices = initVertexCount;
        for(int currVertex = 0; currVertex < initVertexCount; currVertex++ ) {
            adjMatrix.add(new ArrayList<Integer>());
            for(int iv =0; iv< initVertexCount; iv++) {
                adjMatrix.get(currVertex).add(0);
            }
        }
        // done. add edges next
    }

    private void addNewNode() {
        adjMatrix.add(new ArrayList<Integer>());
        for(int iv =0; iv< numVertices; iv++) {
            adjMatrix.get(numVertices).add(0);
        }
    }

    public void connect(int vertexS, int vertexD) {
        // add to adjMatrix
        adjMatrix.get(vertexS).set(vertexD, adjMatrix.get(vertexS).get(vertexD) + 1);
    }

    public void addUnconnectedNode(TagGraphNode node) {
        // a new node has entered the graph, it's unconnected for now
        numVertices++;
        nodes.add(node);
        // resize the adjMatrix
        addNewNode();
    }
}
