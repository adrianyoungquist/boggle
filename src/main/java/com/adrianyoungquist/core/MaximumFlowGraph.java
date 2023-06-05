package com.adrianyoungquist.core;

/*
Uses Ford-Fulkerson algorithm
 */

import java.util.*;

public class MaximumFlowGraph<E extends Comparable<? super E>> {
    protected HashSet<FlowVertex<E>> vertexSet;
    FlowVertex<E> startVert, endVert;

    private boolean solved;

    public MaximumFlowGraph() {
        vertexSet = new HashSet<>();
        startVert = null;
        endVert = null;
        solved = false;
    }

    // basic graph methods ---------------------------------------------------
    public void clear() {
        vertexSet.clear();
        solved = false;
    }

    public boolean isEmpty() {
        return vertexSet.isEmpty();
    }

    public void addEdge(E source, E dest, double cost) {
        FlowVertex<E> src, dst;

        src = addToVertexSet(source);
        dst = addToVertexSet(dest);

        src.addToFlowAdjList(dst, 0.0);
        src.addToResAdjList(dst, cost);
        dst.addToResAdjList(src, 0.0); // the reverse edge
        solved = false;
    }

    public void addEdge(E source, E dest, int cost)
    {
        addEdge(source, dest, (double) cost);
    }

    public FlowVertex<E> addToVertexSet(E x) {
        FlowVertex<E> vertex;
        boolean success;

        vertex = new FlowVertex<>(x);
        success = vertexSet.add(vertex);

        if (success) {
            return vertex;
        }
        else { // already in the set, so find the vertex
            for (FlowVertex<E> vert : vertexSet) {
                if (vert.equals(vertex)) {
                    return vert;
                }
            }
        }
        return null; // something bad happened
    }

    protected FlowVertex<E> getVertexWithThisData(E x) {
        FlowVertex<E> toFind = new FlowVertex<>(x);

        for (FlowVertex<E> vert : vertexSet) {
            if (vert.equals(toFind)) {
                return vert;
            }
        }
        return null; // not found
    }
    public boolean setStartVert(E x) {
        FlowVertex<E> vert = getVertexWithThisData(x);
        if (vert == null) {
            return false;
        }
        startVert = vert;
        solved = false;
        return true;
    }

    public boolean setEndVert(E x) {
        FlowVertex<E> vert = getVertexWithThisData(x);
        if (vert == null) {
            return false;
        }
        endVert = vert;
        solved = false;
        return true;
    }

    public void showFlowAdjTable() {
        System.out.println("------------------------ ");
        for (FlowVertex<E> v : vertexSet) {
            v.showFlowAdjList();
            System.out.println();
        }
    }

    public void showResAdjTable() {
        System.out.println("------------------------ ");
        for (FlowVertex<E> v : vertexSet) {
            v.showResAdjList();
            System.out.println();
        }
    }

    // methods for maximum flow ---------------------------------------------------

    public double getMaxFlow() {
        if (startVert == null || endVert == null) {
            return -1;
        }

        if (!solved) {
            maxFlow();
        }

        double flow = 0.0;

        for (VertexDistPair<FlowVertex<E>, Double> pair : startVert.flowAdjList) {
            flow += pair.getValue();
        }
        return flow;
    }

    public boolean maxFlow() {
        if (startVert == null || endVert == null) {
            return false;
        }
        double limit;
        while (makeFlowPath()) {
            limit = getLimitingFlow();
            adjustPathByCost(limit);
        }
        solved = true;
        return true;
    }

    public ArrayList<Pair<E, E>> getEdgesInFlow() {
        if (!solved) {
            if (!maxFlow()) {
                return null;
            }
        }
        ArrayList<Pair<E, E>> list = new ArrayList<>();
        for (FlowVertex<E> vertex : vertexSet) {
            for (VertexDistPair<FlowVertex<E>, Double> pair : vertex.flowAdjList) {
                if (pair.getValue() != 0) {
                    list.add(new Pair<>(vertex.data, pair.getKey().data));
                }
            }
        }
        return list;
    }

    private boolean makeFlowPath() {
        // like Dijkstra's
        if (startVert == null || endVert == null) {
            return false;
        }
        // initialize distances
        for (FlowVertex<E> v : vertexSet) {
            v.dist = FlowVertex.INFINITY;
        }

        Deque<FlowVertex<E>> partiallyProcessed = new LinkedList<>();
        startVert.dist = 0;

        partiallyProcessed.addLast(startVert);

        FlowVertex<E> v1, v2;
        double cost;
        while (!partiallyProcessed.isEmpty()) {
            v1 = partiallyProcessed.removeFirst();
            for (VertexDistPair<FlowVertex<E>, Double> pair : v1.resAdjList) {
                cost = pair.getValue();
                v2 = pair.getKey();
                if (cost == 0) { // skip
                    continue;
                }
                if (v2 == endVert) {
                    v2.nextInPath = v1;
                    return true;
                }
                if (v1.dist + cost < v2.dist) {
                    v2.dist = v1.dist + cost;
                    v2.nextInPath = v1;

                    partiallyProcessed.addLast(v2);
                }
            }
        }
        return false; // no path
    }

    private double getLimitingFlow() {
        // assumes path has been set

        if (startVert == null || endVert == null) {
            return 0.0; // error?
        }
        double minCost;
        FlowVertex<E> v1, v2;

        v2 = endVert;
        v1 = v2.nextInPath;
        minCost = getCostOfResEdge(v1, v2);
        double tmp;
        while (v2 != startVert) {
            v1 = v2.nextInPath;
            tmp = getCostOfResEdge(v1, v2);
            if (tmp < minCost) {
                minCost = tmp;
            }
            v2 = v1;
        }
        return minCost;
    }

    private boolean adjustPathByCost(double cost) {
        // assumes path has been set

        if (startVert == null || endVert == null) {
            return false;
        }

        FlowVertex<E> v1, v2;

        v2 = endVert;
        while (v2 != startVert) {
            v1 = v2.nextInPath;
            addCostToFlowEdge(v1, v2, cost);
            addCostToResEdge(v1, v2, -cost);
            addCostToResEdge(v2, v1, cost);
            v2 = v1;
        }
        return true;
    }
    private double getCostOfResEdge(FlowVertex<E> src, FlowVertex<E> dst) {
        if (src == null || dst == null) {
            return 0;
        }
        for (VertexDistPair<FlowVertex<E>, Double> pair : src.resAdjList) {
            if (pair.getKey() == dst) {
                return pair.getValue();
            }
        }
        return 0;
    }

    private boolean addCostToResEdge(FlowVertex<E> src, FlowVertex<E> dst, double cost) {
        if (src == null || dst == null) {
            return false;
        }
        for (VertexDistPair<FlowVertex<E>, Double> pair : src.resAdjList) {
            if (pair.getKey() == dst) {
                pair.setValue(pair.getValue() + cost);
                return true;
            }
        }
        return false;
    }

    private boolean addCostToFlowEdge(FlowVertex<E> src, FlowVertex<E> dst, double cost) {
        if (src == null || dst == null) {
            return false;
        }
        for (VertexDistPair<FlowVertex<E>, Double> pair : src.flowAdjList) {
            if (pair.getKey() == dst) {
                pair.setValue(pair.getValue() + cost);
                return true;
            }
        }

        // not found --> check reverse edges
        for (VertexDistPair<FlowVertex<E>, Double> pair : dst.flowAdjList) {
            if (pair.getKey() == dst) {
                pair.setValue(pair.getValue() - cost);
                return true;
            }
        }

        // not found
        return false;
    }

    public HashSet<FlowVertex<E>> getVertexSet() {
        return vertexSet;
    }

    public FlowVertex<E> getStartVert() {
        return startVert;
    }

    public FlowVertex<E> getEndVert() {
        return endVert;
    }

    public boolean isSolved() {
        return solved;
    }

    static class FlowVertex<E> {
        public static final double INFINITY = Double.MAX_VALUE;
        HashSet<VertexDistPair<FlowVertex<E>, Double>> flowAdjList;
        HashSet<VertexDistPair<FlowVertex<E>, Double>> resAdjList;

        E data;

        double dist;

        FlowVertex<E> nextInPath;

        public FlowVertex() {
            this(null);
        }

        public FlowVertex(E x) {
            flowAdjList = new HashSet<>();
            resAdjList = new HashSet<>();
            data = x;
            dist = INFINITY;
            nextInPath = null;
        }

        public void addToFlowAdjList(FlowVertex<E> neighbor, double cost)
        {
            flowAdjList.add(new VertexDistPair<>(neighbor, cost));
        }

        public void addToFlowAdjList(FlowVertex<E> neighbor, int cost)
        {
            addToFlowAdjList(neighbor, (double) cost);
        }

        public void addToResAdjList(FlowVertex<E> neighbor, double cost)
        {
            resAdjList.add(new VertexDistPair<>(neighbor, cost));
        }

        public void addToResAdjList(FlowVertex<E> neighbor, int cost)
        {
            addToResAdjList(neighbor, (double) cost);
        }

        public void showFlowAdjList() {
            System.out.print("Adj flow List for " + data + ": ");
            for (VertexDistPair<FlowVertex<E>, Double> pair : flowAdjList) {
                System.out.print(pair.getKey().data + "(" +
                        String.format("%3.1f", pair.getValue()) + ") ");
            }
            System.out.println();
        }

        public void showResAdjList() {
            System.out.print("Adj res List for " + data + ": ");
            for (VertexDistPair<FlowVertex<E>, Double> pair : resAdjList) {
                System.out.print(pair.getKey().data + "(" +
                        String.format("%3.1f", pair.getValue()) + ") ");
            }
            System.out.println();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FlowVertex<?> that = (FlowVertex<?>) o;
            return Objects.equals(data, that.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }
    }

    static class VertexDistPair<K, V> extends Pair<K, V>{
        public VertexDistPair() {
            super();
        }
        public VertexDistPair(K key, V val) {
            super(key, val);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VertexDistPair<?, ?> pair = (VertexDistPair<?, ?>) o;
            return Objects.equals(getKey(), pair.getKey());
        }
    }
}
