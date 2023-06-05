package com.adrianyoungquist.core;/*
 *******************************************************************************
 * -----------------------------------------------------------------------------
 * @version1.0 03-17-2021
 * @Author: Sarah Youngquist
 * File Name: Foothill.java
 * File contains public class Foothill and default-access FHflowGraph<E> and 
 *    FHflowVertex<E>
 * For Foothill College Class: CS 1C Adv Data Struct/Algorms Java Winter 2021
 * Professor Jim Lai
 * Assignment #9: Maximum Flow Problem, Due 03-19-2021
 * This program defines a graph data type and solves the maximum flow problem
 */

import java.util.*;

import com.adrianyoungquist.core.cs_1c.Pair;

// Class Foothill --------------------------------------------------------------
public class Foothill
{
   // -------  main --------------------------------------
   public static void main(String[] args) throws Exception
   {  
      // Assignment test run
      System.out.println("Assignment test run:");
      double finalFlow;

      // build graph
      FHflowGraph<String> myG = new FHflowGraph<String>();

      myG.addEdge("s","a", 3);    myG.addEdge("s","b", 2); 
      myG.addEdge("a","b", 1);    myG.addEdge("a","c", 3); 
      myG.addEdge("a","d", 4); 
      myG.addEdge("b","d", 2);
      myG.addEdge("c","t", 2); 
      myG.addEdge("d","t", 3);  

      // show the original flow graph
      myG.showResAdjTable();
      myG.showFlowAdjTable();

      myG.setStartVert("s");
      myG.setEndVert("t");
      finalFlow = myG.findMaxFlow();

      System.out.println("Final flow: " + finalFlow);

      myG.showResAdjTable();
      myG.showFlowAdjTable();
      
      
      // Module test run
      System.out.println("\nGraph from the module: ");
      FHflowGraph<String> moduleGraph = new FHflowGraph<String>();
      
      moduleGraph.addEdge("s", "a", 3);
      moduleGraph.addEdge("s", "b", 2);
      moduleGraph.addEdge("a", "b", 1);
      moduleGraph.addEdge("a", "c", 3);
      moduleGraph.addEdge("a", "d", 4);
      moduleGraph.addEdge("b", "d", 2);
      moduleGraph.addEdge("c", "t", 2);
      moduleGraph.addEdge("d", "t", 3);
      
      moduleGraph.setStartVert("s");
      moduleGraph.setEndVert("t");
      
      moduleGraph.showResAdjTable();
      moduleGraph.showFlowAdjTable();
      
      System.out.println("Final flow: " + moduleGraph.findMaxFlow());
      
      moduleGraph.showResAdjTable();
      moduleGraph.showFlowAdjTable();
      
      
      // another test
      System.out.println("\nTest run #3: ");
      
      FHflowGraph<Character> graph1 = new FHflowGraph<Character>();
      
      graph1.addEdge('A', 'B', 4);
      graph1.addEdge('A', 'C', 3);
      graph1.addEdge('A', 'D', 1);
      graph1.addEdge('B', 'E', 1);
      graph1.addEdge('C', 'E', 2);
      graph1.addEdge('D', 'C', 2);
      graph1.addEdge('D', 'F', 2);
      graph1.addEdge('E', 'I', 4);
      graph1.addEdge('F', 'C', 4);
      graph1.addEdge('F', 'G', 2);
      graph1.addEdge('F', 'H', 1);
      graph1.addEdge('G', 'E', 5);
      graph1.addEdge('G', 'I', 7);
      graph1.addEdge('H', 'E', 4);
      graph1.addEdge('H', 'I', 1);
      
      graph1.showResAdjTable();
      graph1.showFlowAdjTable();
      
      graph1.setStartVert('A');
      graph1.setEndVert('I');
      
      System.out.println("Final flow: " + graph1.findMaxFlow());
      
      graph1.showResAdjTable();
      graph1.showFlowAdjTable();
      
      
      // another test
      System.out.println("\nTest run #4: ");
      
      FHflowGraph<Character> graph2 = new FHflowGraph<Character>();
      
      graph2.addEdge('s', 'a', 6);
      graph2.addEdge('s', 'e', 6);
      graph2.addEdge('s', 'h', 2);
      graph2.addEdge('a', 'b', 3);
      graph2.addEdge('a', 'e', 3);
      graph2.addEdge('b', 'c', 2);
      graph2.addEdge('b', 'e', 6);
      graph2.addEdge('b', 'g', 3);
      graph2.addEdge('c', 'd', 4);
      graph2.addEdge('d', 'g', 4);
      graph2.addEdge('d', 't', 7);
      graph2.addEdge('e', 'f', 3);
      graph2.addEdge('e', 'h', 2);
      graph2.addEdge('f', 'g', 2);
      graph2.addEdge('f', 'h', 1);
      graph2.addEdge('g', 't', 3);
      graph2.addEdge('h', 't', 6);

      graph2.setStartVert('s');
      graph2.setEndVert('t');
      
      graph2.showResAdjTable();
      graph2.showFlowAdjTable();
      
      System.out.println("Final flow: " + graph2.findMaxFlow());
      
      graph2.showResAdjTable();
      graph2.showFlowAdjTable();
   }
}

// --- FHvertex class ----------------------------------------------------------
class FHflowVertex<E>
{
   public static Stack<Integer> keyStack = new Stack<Integer>();
   public static final int KEY_ON_DATA = 0, KEY_ON_DIST = 1;
   public static int keyType = KEY_ON_DATA;
   public static final double INFINITY = Double.MAX_VALUE;
   public HashSet<com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double>> flowAdjList =
         new HashSet<com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double>>();
   public HashSet<com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double>> resAdjList =
         new HashSet<com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double>>();
   public E data;
   public double dist;
   public FHflowVertex<E> nextInPath; // for client-specific info

   public FHflowVertex(E x)
   {
      data = x;
      dist = INFINITY;
      nextInPath = null;
   }

   public FHflowVertex()
   {
      this(null);
   }

   public void addToFlowAdjList(FHflowVertex<E> neighbor, double cost)
   {
      flowAdjList.add(new com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double>(neighbor, cost));
   }

   public void addToFlowAdjList(FHflowVertex<E> neighbor, int cost)
   {
      addToFlowAdjList(neighbor, (double) cost);
   }
   
   public void addToResAdjList(FHflowVertex<E> neighbor, double cost)
   {
      resAdjList.add(new com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double>(neighbor, cost));
   }

   public void addToResAdjList(FHflowVertex<E> neighbor, int cost)
   {
      addToResAdjList(neighbor, (double) cost);
   }

   public boolean equals(Object rhs)
   {
      FHflowVertex<E> other = (FHflowVertex<E>) rhs;
      switch (keyType)
      {
      case KEY_ON_DIST:
         return (dist == other.dist);
      case KEY_ON_DATA:
         return (data.equals(other.data));
      default:
         return (data.equals(other.data));
      }
   }

   public int hashCode()
   {
      switch (keyType)
      {
      case KEY_ON_DIST:
         Double d = dist;
         return (d.hashCode());
      case KEY_ON_DATA:
         return (data.hashCode());
      default:
         return (data.hashCode());
      }
   }

   // displays the flow adjacency list
   public void showFlowAdjList()
   {
      Iterator<com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double>> iter;
      com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double> pair;

      System.out.print("Adj flow List for " + data + ": ");
      for (iter = flowAdjList.iterator(); iter.hasNext();)
      {
         pair = iter.next();
         System.out.print(pair.first.data + "(" + 
                  String.format("%3.1f", pair.second) + ") ");
      }
      System.out.println();
   }
   
   // displays the residual adjacency list
   public void showResAdjList()
   {
      Iterator<com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double>> iter;
      com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double> pair;

      System.out.print("Adj res List for " + data + ":  ");
      for (iter = resAdjList.iterator(); iter.hasNext();)
      {
         pair = iter.next();
         System.out.print(pair.first.data + "(" + 
                  String.format("%3.1f", pair.second) + ") ");
      }
      System.out.println();
   }

   public static boolean setKeyType(int whichType)
   {
      switch (whichType)
      {
      case KEY_ON_DATA:
      case KEY_ON_DIST:
         keyType = whichType;
         return true;
      default:
         return false;
      }
   }

   public static void pushKeyType()
   {
      keyStack.push(keyType);
   }

   public static void popKeyType()
   {
      keyType = keyStack.pop();
   };
}

// class FHflowGraph<E> --------------------------------------------------------
class FHflowGraph<E>
{
   // the graph data is all here --------------------------
   protected HashSet<FHflowVertex<E>> vertexSet;
   FHflowVertex<E> startVert, endVert;

   // public graph methods --------------------------------
   public FHflowGraph()
   {
      vertexSet = new HashSet<FHflowVertex<E>>();
      startVert = endVert = null;
   }

   public void addEdge(E source, E dest, double cost)
   {
      FHflowVertex<E> src, dst;

      // put both source and dest into vertex list(s) if not already there
      src = addToVertexSet(source);
      dst = addToVertexSet(dest);

      // add dest to flow and res adjacency lists
      src.addToFlowAdjList(dst, 0.);
      src.addToResAdjList(dst, cost);
      dst.addToResAdjList(src, 0.); // adds the reverse edge
   }

   public void addEdge(E source, E dest, int cost)
   {
      addEdge(source, dest, (double) cost);
   }

   public boolean setStartVert(E x)
   {
      FHflowVertex<E> s;
      s = getVertexWithThisData(x);
      if (s == null)
         return false;
      startVert = s;
      return true;
   }
   
   public boolean setEndVert(E x)
   {
      FHflowVertex<E> s;
      s = getVertexWithThisData(x);
      if (s == null)
         return false;
      endVert = s;
      return true;
   }
   
   // adds vertex with x in it, and returns ref to it
   public FHflowVertex<E> addToVertexSet(E x)
   {
      FHflowVertex<E> retVal, vert;
      boolean successfulInsertion;
      Iterator<FHflowVertex<E>> iter;

      // save sort key for client
      FHflowVertex.pushKeyType();
      FHflowVertex.setKeyType(FHflowVertex.KEY_ON_DATA);

      // build and insert vertex into master list
      retVal = new FHflowVertex<E>(x);
      successfulInsertion = vertexSet.add(retVal);

      if (successfulInsertion)
      {
         FHflowVertex.popKeyType(); // restore client sort key
         return retVal;
      }

      // the vertex was already in the set, so get its ref
      for (iter = vertexSet.iterator(); iter.hasNext();)
      {
         vert = iter.next();
         if (vert.equals(retVal))
         {
            FHflowVertex.popKeyType(); // restore client sort key
            return vert;
         }
      }

      FHflowVertex.popKeyType(); // restore client sort key
      return null; // should never happen
   }

   public void showFlowAdjTable()
   {
      Iterator<FHflowVertex<E>> iter;

      System.out.println("------------------------ ");
      for (iter = vertexSet.iterator(); iter.hasNext();)
         (iter.next()).showFlowAdjList();
      System.out.println();
   }
   
   public void showResAdjTable()
   {
      Iterator<FHflowVertex<E>> iter;

      System.out.println("------------------------ ");
      for (iter = vertexSet.iterator(); iter.hasNext();)
         (iter.next()).showResAdjList();
      System.out.println();
   }

   public void clear()
   {
      vertexSet.clear();
   }

   protected FHflowVertex<E> getVertexWithThisData(E x)
   {
      FHflowVertex<E> searchVert, vert;
      Iterator<FHflowVertex<E>> iter;

      // save sort key for client
      FHflowVertex.pushKeyType();
      FHflowVertex.setKeyType(FHflowVertex.KEY_ON_DATA);

      // build vertex with data = x for the search
      searchVert = new FHflowVertex<E>(x);

      // the vertex was already in the set, so get its ref
      for (iter = vertexSet.iterator(); iter.hasNext();)
      {
         vert = iter.next();
         if (vert.equals(searchVert))
         {
            FHflowVertex.popKeyType();
            return vert;
         }
      }

      FHflowVertex.popKeyType();
      return null; // not found
   }
   
   // Methods for maximum flow algorithm ---------------------------
   // public algorithm that returns the max flow
   public double findMaxFlow()
   {
      double limit, flow = 0.;
      com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double> pair;
      Iterator<com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double>> iter;
      
      if (startVert == null || endVert == null)
         return -1.;
      
      while (establishNextFlowPath())
      {
         limit = getLimitingFlowOnResPath();
         adjustPathByCost(limit);
      }
      
      // get total flow
      for (iter = startVert.flowAdjList.iterator(); iter.hasNext(); )
      {
         pair = iter.next();
         flow += pair.second;
      }
      return flow;
   }
   
   // Find the next flow path. Similar to dijkstra's
   private boolean establishNextFlowPath()
   {
      FHflowVertex<E> w, v;
      Iterator<com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double >> adjIter;
      com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double> pair;
      Iterator<FHflowVertex<E>> iter;
      Double costVW;
      Deque<FHflowVertex<E>> partiallyProcessedVerts = new LinkedList<FHflowVertex<E>>();
      
      if (startVert == null || endVert == null)
         return false;
      
      // initialize the vertex list
      for (iter = vertexSet.iterator(); iter.hasNext(); )
      {
         iter.next().dist = FHflowVertex.INFINITY;
      }
      startVert.dist = 0;
      partiallyProcessedVerts.addLast(startVert);
      
      // outer loop
      while (!partiallyProcessedVerts.isEmpty())
      {
         v = partiallyProcessedVerts.removeFirst();
         
         // inner loop
         for (adjIter = v.resAdjList.iterator(); adjIter.hasNext(); )
         {
            pair = adjIter.next();
            costVW = pair.second;
            if (costVW == 0) // skip when cost is 0
               continue;
            w = pair.first;
            
            if (w == endVert)
            {
               w.nextInPath = v;
               return true;
            }
            
            if (v.dist + costVW < w.dist)
            {
               w.dist = v.dist + costVW;
               w.nextInPath = v;
               
               // add w to PPV queue
               partiallyProcessedVerts.addLast(w);
            }
         }
      }
      return false;
   }
   
   private double getLimitingFlowOnResPath()
   {
      double minCost, temp;
      FHflowVertex<E> vert1, vert2;
      if (startVert == null || endVert == null)
         return 0.0; // should throw error?
      
      vert2 = endVert;
      vert1 = vert2.nextInPath;
      minCost = getCostOfResEdge(vert1, vert2);
      for (; vert2 != startVert; vert2 = vert1)
      {
         vert1 = vert2.nextInPath;
         temp = getCostOfResEdge(vert1, vert2);
         if (temp < minCost)
            minCost = temp;
      }
      return minCost;
   }
   
   private boolean adjustPathByCost(double cost)
   {
      // TEMP TODO
      FHflowVertex<E> vert1, vert2;
      
      if (startVert == null || endVert == null)
         return false;
      
      for (vert2 = endVert; vert2 != startVert; vert2 = vert1)
      {
         vert1 = vert2.nextInPath;
         addCostToFlowEdge(vert1, vert2, cost);
         addCostToResEdge(vert1, vert2, -cost);
         addCostToResEdge(vert2, vert1, cost); // reverse edge
      }
      
      return true;
   }
   
   private double getCostOfResEdge(FHflowVertex<E> src, FHflowVertex<E> dst)
   {
      Iterator<com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double >> iter;
      com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double> pair;
      
      if (src == null || dst == null)
         return 0.;
      for (iter = src.resAdjList.iterator(); iter.hasNext();)
      {
         pair = iter.next();
         if (pair.first == dst)
            return pair.second;
      }
      return 0.; 
   }
   
   private boolean addCostToResEdge(FHflowVertex<E> src, FHflowVertex<E> dst, 
         double cost)
   {
      Iterator<com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double >> iter;
      com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double> pair;
      
      if (src == null || dst == null)
         return false;
      for (iter = src.resAdjList.iterator(); iter.hasNext();)
      {
         pair = iter.next();
         if (pair.first == dst)
         {
            pair.second += cost;
            return true;
         }
      }
      return false;
   }
   
   private boolean addCostToFlowEdge(FHflowVertex<E> src, FHflowVertex<E> dst, 
         double cost)
   {
      Iterator<com.adrianyoungquist.core.cs_1c.Pair<FHflowVertex<E>, Double >> iter;
      Pair<FHflowVertex<E>, Double> pair;
      
      if (src == null || dst == null)
         return false;
      for (iter = src.flowAdjList.iterator(); iter.hasNext();)
      {
         pair = iter.next();
         if (pair.first == dst)
         {
            pair.second += cost;
            return true;
         }
      }
      
      // not found - means it's a reverse edge
      for (iter = dst.flowAdjList.iterator(); iter.hasNext();)
      {
         pair = iter.next();
         if (pair.first == src)
         {
            pair.second -= cost;
            return true;
         }
      }
      // not found at all
      return false; 
   }
}


/*
OUTPUT
--------------------------------------------------------------------------------
Assignment test run:
------------------------ 
Adj res List for a:  b(1.0) s(0.0) c(3.0) d(4.0) 
Adj res List for b:  a(0.0) s(0.0) d(2.0) 
Adj res List for s:  a(3.0) b(2.0) 
Adj res List for c:  a(0.0) t(2.0) 
Adj res List for d:  a(0.0) b(0.0) t(3.0) 
Adj res List for t:  c(0.0) d(0.0) 

------------------------ 
Adj flow List for a: b(0.0) c(0.0) d(0.0) 
Adj flow List for b: d(0.0) 
Adj flow List for s: a(0.0) b(0.0) 
Adj flow List for c: t(0.0) 
Adj flow List for d: t(0.0) 
Adj flow List for t: 

Final flow: 5.0
------------------------ 
Adj res List for a:  b(1.0) s(3.0) c(1.0) d(3.0) 
Adj res List for b:  a(0.0) s(2.0) d(0.0) 
Adj res List for s:  a(0.0) b(0.0) 
Adj res List for c:  a(2.0) t(0.0) 
Adj res List for d:  a(1.0) b(2.0) t(0.0) 
Adj res List for t:  c(2.0) d(3.0) 

------------------------ 
Adj flow List for a: b(0.0) c(2.0) d(1.0) 
Adj flow List for b: d(2.0) 
Adj flow List for s: a(3.0) b(2.0) 
Adj flow List for c: t(2.0) 
Adj flow List for d: t(3.0) 
Adj flow List for t: 


Graph from the module: 
------------------------ 
Adj res List for a:  b(1.0) s(0.0) c(3.0) d(4.0) 
Adj res List for b:  a(0.0) s(0.0) d(2.0) 
Adj res List for s:  a(3.0) b(2.0) 
Adj res List for c:  a(0.0) t(2.0) 
Adj res List for d:  a(0.0) b(0.0) t(3.0) 
Adj res List for t:  c(0.0) d(0.0) 

------------------------ 
Adj flow List for a: b(0.0) c(0.0) d(0.0) 
Adj flow List for b: d(0.0) 
Adj flow List for s: a(0.0) b(0.0) 
Adj flow List for c: t(0.0) 
Adj flow List for d: t(0.0) 
Adj flow List for t: 

Final flow: 5.0
------------------------ 
Adj res List for a:  b(1.0) s(3.0) c(1.0) d(3.0) 
Adj res List for b:  a(0.0) s(2.0) d(0.0) 
Adj res List for s:  a(0.0) b(0.0) 
Adj res List for c:  a(2.0) t(0.0) 
Adj res List for d:  a(1.0) b(2.0) t(0.0) 
Adj res List for t:  c(2.0) d(3.0) 

------------------------ 
Adj flow List for a: b(0.0) c(2.0) d(1.0) 
Adj flow List for b: d(2.0) 
Adj flow List for s: a(3.0) b(2.0) 
Adj flow List for c: t(2.0) 
Adj flow List for d: t(3.0) 
Adj flow List for t: 


Test run #3: 
------------------------ 
Adj res List for A:  B(4.0) C(3.0) D(1.0) 
Adj res List for B:  A(0.0) E(1.0) 
Adj res List for C:  A(0.0) D(0.0) E(2.0) F(0.0) 
Adj res List for D:  A(0.0) C(2.0) F(2.0) 
Adj res List for E:  B(0.0) C(0.0) G(0.0) H(0.0) I(4.0) 
Adj res List for F:  C(4.0) D(0.0) G(2.0) H(1.0) 
Adj res List for G:  E(5.0) F(0.0) I(7.0) 
Adj res List for H:  E(4.0) F(0.0) I(1.0) 
Adj res List for I:  E(0.0) G(0.0) H(0.0) 

------------------------ 
Adj flow List for A: B(0.0) C(0.0) D(0.0) 
Adj flow List for B: E(0.0) 
Adj flow List for C: E(0.0) 
Adj flow List for D: C(0.0) F(0.0) 
Adj flow List for E: I(0.0) 
Adj flow List for F: C(0.0) G(0.0) H(0.0) 
Adj flow List for G: E(0.0) I(0.0) 
Adj flow List for H: E(0.0) I(0.0) 
Adj flow List for I: 

Final flow: 4.0
------------------------ 
Adj res List for A:  B(3.0) C(1.0) D(0.0) 
Adj res List for B:  A(1.0) E(0.0) 
Adj res List for C:  A(2.0) D(0.0) E(0.0) F(0.0) 
Adj res List for D:  A(1.0) C(2.0) F(1.0) 
Adj res List for E:  B(1.0) C(2.0) G(0.0) H(0.0) I(1.0) 
Adj res List for F:  C(4.0) D(1.0) G(1.0) H(1.0) 
Adj res List for G:  E(5.0) F(1.0) I(6.0) 
Adj res List for H:  E(4.0) F(0.0) I(1.0) 
Adj res List for I:  E(3.0) G(1.0) H(0.0) 

------------------------ 
Adj flow List for A: B(1.0) C(2.0) D(1.0) 
Adj flow List for B: E(1.0) 
Adj flow List for C: E(2.0) 
Adj flow List for D: C(0.0) F(1.0) 
Adj flow List for E: I(3.0) 
Adj flow List for F: C(0.0) G(1.0) H(0.0) 
Adj flow List for G: E(0.0) I(1.0) 
Adj flow List for H: E(0.0) I(0.0) 
Adj flow List for I: 


Test run #4: 
------------------------ 
Adj res List for a:  b(3.0) s(0.0) e(3.0) 
Adj res List for b:  a(0.0) c(2.0) e(6.0) g(3.0) 
Adj res List for s:  a(6.0) e(6.0) h(2.0) 
Adj res List for c:  b(0.0) d(4.0) 
Adj res List for d:  c(0.0) t(7.0) g(4.0) 
Adj res List for t:  d(0.0) g(0.0) h(0.0) 
Adj res List for e:  a(0.0) b(0.0) s(0.0) f(3.0) h(2.0) 
Adj res List for f:  e(0.0) g(2.0) h(1.0) 
Adj res List for g:  b(0.0) d(0.0) t(3.0) f(0.0) 
Adj res List for h:  s(0.0) t(6.0) e(0.0) f(0.0) 

------------------------ 
Adj flow List for a: b(0.0) e(0.0) 
Adj flow List for b: c(0.0) e(0.0) g(0.0) 
Adj flow List for s: a(0.0) e(0.0) h(0.0) 
Adj flow List for c: d(0.0) 
Adj flow List for d: t(0.0) g(0.0) 
Adj flow List for t: 
Adj flow List for e: f(0.0) h(0.0) 
Adj flow List for f: g(0.0) h(0.0) 
Adj flow List for g: t(0.0) 
Adj flow List for h: t(0.0) 

Final flow: 10.0
------------------------ 
Adj res List for a:  b(0.0) s(3.0) e(3.0) 
Adj res List for b:  a(3.0) c(0.0) e(6.0) g(2.0) 
Adj res List for s:  a(3.0) e(1.0) h(0.0) 
Adj res List for c:  b(2.0) d(2.0) 
Adj res List for d:  c(2.0) t(5.0) g(4.0) 
Adj res List for t:  d(2.0) g(3.0) h(5.0) 
Adj res List for e:  a(0.0) b(0.0) s(5.0) f(0.0) h(0.0) 
Adj res List for f:  e(3.0) g(0.0) h(0.0) 
Adj res List for g:  b(1.0) d(0.0) t(0.0) f(2.0) 
Adj res List for h:  s(2.0) t(1.0) e(2.0) f(1.0) 

------------------------ 
Adj flow List for a: b(3.0) e(0.0) 
Adj flow List for b: c(2.0) e(0.0) g(1.0) 
Adj flow List for s: a(3.0) e(5.0) h(2.0) 
Adj flow List for c: d(2.0) 
Adj flow List for d: t(2.0) g(0.0) 
Adj flow List for t: 
Adj flow List for e: f(3.0) h(2.0) 
Adj flow List for f: g(2.0) h(1.0) 
Adj flow List for g: t(3.0) 
Adj flow List for h: t(5.0) 

*/