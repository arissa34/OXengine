package com.badlogic.gdx.ai.indexed;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;

public class MyIndexedAStarPathFinder<N> extends IndexedAStarPathFinder<N> {

    public MyIndexedAStarPathFinder(IndexedGraph<N> graph) {
        super(graph);
    }

    public MyIndexedAStarPathFinder(IndexedGraph<N> graph, boolean calculateMetrics) {
        super(graph, calculateMetrics);
    }

    public void updateIndexedGraph(IndexedGraph<N> graph){
        this.graph = graph;
        this.nodeRecords = (NodeRecord<N>[])new NodeRecord[graph.getNodeCount()];
    }
}
