package com.adrianyoungquist.core;

import java.util.Arrays;

public class ValidBoardChecker {
    FHflowGraph<String> graph;
    /*
    In the graph, nodes will be named L1, L2, ..., L16 and D1, D2, ..., D16 for letters and dice
     */
    Dice dice;

    public ValidBoardChecker() {
        this(true);
    }
    public ValidBoardChecker(boolean classic) {
        dice = new Dice(classic);
        graph = new FHflowGraph<>();
    }
    public boolean isValid(String letters, boolean classic) {
        makeGraph(letters, classic);
        double flow = graph.findMaxFlow();
        return flow == Dice.NUM_DICE;
    }

    public boolean isValid(String letters) {
        return isValid(letters, true);
    }
    /*
    public ArrayList<Pair<String, String>> getDiceMatching(String letters, boolean classic) {
        makeGraph(letters, classic);
        return graph.getEdgesInFlow();
    }

     */

    /*
    public ArrayList<Pair<String, String>> getDiceMatching(String letters) {
        return getDiceMatching(letters, true);
    }*/

    public void makeGraph(String letters) {
        makeGraph(letters, true);
    }
    public void makeGraph(String letters, boolean classic) {
        /*
        There is a node for each letter and one for each Die. They are connected if the Die
        contains the letter.
         */
        letters = letters.toLowerCase();
        int size = Dice.NUM_DICE;
        char c;
        String letterString, dieString;
        for (int i = 0; i < size; i++) {
            graph.addEdge("S", "L" + i, 1); // dummy start node
            c = letters.charAt(i);
            System.out.print(c);
            for (int j = 0; j < size; j++) {
                System.out.println(Arrays.toString(dice.get(j).getValues()));
                if (dice.get(j).contains(c)) {
                    graph.addEdge("L" + i, "D" + j, 1);
                }
            }
        }

        for (int i = 0; i < size; i++) {
            graph.addEdge("D" + i, "E", 1); // dummy end node
        }

        graph.setStartVert("S");
        graph.setEndVert("E");
    }

    public void solve() {
        System.out.println(graph.findMaxFlow());
    }

    public static void main(String[] args) {
        ValidBoardChecker boardChecker = new ValidBoardChecker();
        boardChecker.makeGraph("MNATNRIBAOPSQIDY");
        boardChecker.solve();
    }
}
