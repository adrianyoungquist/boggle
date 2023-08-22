package com.adrianyoungquist.analysis;

import com.adrianyoungquist.model.Dice;
import com.adrianyoungquist.datastructures.MaximumFlowGraph;
import com.adrianyoungquist.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ValidBoardChecker {
    MaximumFlowGraph<String> graph;
    String letters;
    /*
    In the graph, nodes will be named L1, L2, ..., L16 and D1, D2, ..., D16 for letters and dice
     */
    boolean classic;

    Dice dice;

    public ValidBoardChecker() {
        this(true);
    }

    public ValidBoardChecker(boolean classic, String letters) {
        this.classic = classic;
        this.letters = letters;
        dice = new Dice(classic);
        graph = new MaximumFlowGraph<>();
        makeGraph();
    }

    public ValidBoardChecker(String letters) {
        this(true, letters);
    }

    public ValidBoardChecker(boolean classic) {
        this.classic = classic;
        this.letters = null;
        dice = new Dice(classic);
        graph = new MaximumFlowGraph<>();
    }

    public boolean isValid() {
        double flow = graph.getMaxFlow();
        return flow == Dice.NUM_DICE;
    }

    public ArrayList<Pair<String, String>> getLetterDiePairs() {
        if (graph.isEmpty()) {
            if (letters == null) {
                return null;
            }
            makeGraph();
        }
        graph.getMaxFlow();
        ArrayList<Pair<String, String>> edges = graph.getEdgesInFlow();

        // get the pairs where the pairs are a letter and a die
        return (ArrayList<Pair<String, String>>) edges.stream().
                filter(p -> !Objects.equals(p.getKey(), "S") && !Objects.equals(p.getValue(), "E")).
                sorted(Comparator.comparing(Pair::getKey)).collect(Collectors.toList());

    }

    public String getDiceMatchingString() {
        ArrayList<Pair<String, String>> pairs = getLetterDiePairs();
        System.out.println(pairs);
        StringBuilder sb = new StringBuilder();
        String s;
        int letterNumber, dieNumber;
        for (Pair<String, String> p : pairs) {
            letterNumber =  Integer.parseInt(p.getKey().substring(1));
            dieNumber =  Integer.parseInt(p.getValue().substring(1));
            sb.append(letters.charAt(letterNumber)).append(": ");
            sb.append(Arrays.toString(dice.get(dieNumber).getValues())).append(" (").append(dieNumber).append(")\n");
        }
        System.out.println("Dice:" + pairs.stream().map(Pair::getValue).sorted().toList());
        return sb.substring(0, sb.length() - 1);
    }

    public void makeGraph() {
        /*
        There is a node for each letter and one for each Die. They are connected if the Die
        contains the letter.
         */
        if (letters == null) {
            throw new IllegalStateException("Letters not set");
        }

        letters = letters.toLowerCase();
        int size = Dice.NUM_DICE;
        char c;
        String letterString, dieString;
        for (int i = 0; i < size; i++) {
            graph.addEdge("S", getPadded("L", i), 1); // dummy start node
            c = letters.charAt(i);
            for (int j = 0; j < size; j++) {
                if (dice.get(j).contains(c)) {
                    graph.addEdge(getPadded("L", i), getPadded("D", j), 1);
                }
            }
        }

        for (int i = 0; i < size; i++) {
            graph.addEdge(getPadded("D", i), "E", 1); // dummy end node
        }

        graph.setStartVert("S");
        graph.setEndVert("E");
    }

    private static String getPadded(String s, int i) {
        return String.format("%s%02d", s, i);
    }
    public void solve() {
        graph.getMaxFlow();
    }

    public static void main(String[] args) {
        ValidBoardChecker boardChecker = new ValidBoardChecker("XXIDEFARYYUULSNR");
        boardChecker.makeGraph();
        boardChecker.solve();
        //boardChecker.graph.showFlowAdjTable();
        //boardChecker.graph.showResAdjTable();
        //System.out.println(boardChecker.getDiceMatchingString());
        System.out.print("Valid board: " + boardChecker.isValid());
    }
}
