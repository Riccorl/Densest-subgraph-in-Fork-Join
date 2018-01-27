package it.ric.uny.densestsubgraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;
import javax.sound.midi.Soundbank;

public class UndirectedGraph implements Graph {

  private static final String COMMENT_CHAR = "#";
  private static ForkJoinPool fjPool = new ForkJoinPool();

  // Grafo.
  private HashMap<Integer, HashSet<Integer>> graph;

  // Grafo con archi duplicati per ogni nodo, utile per calcolo del grado.
  private HashMap<Integer, HashSet<Integer>> connections;

  // Grado associato ad ogni nodo (u, deg(u)).
  private HashMap<Integer, Integer> degreeMapPar;

  // Densità del grafo
  private int graphDensity;

  public UndirectedGraph(String filename) {

    this.graph = new HashMap<>();
    this.connections = new HashMap<>();

    this.fileToGraph(filename);

    //graphDensity = calcDensity(graph);
  }

  private int calcDensity(HashMap<Integer, HashSet<Integer>> graph) {

    int nEdge = graph.keySet().stream().mapToInt(x -> x).map(x -> graph.get(x).size()).sum();
    int nNode = graph.keySet().size() * (graph.keySet().size() - 1);

    return nEdge / nNode;
  }

  public HashMap<Integer, HashSet<Integer>> inducedEdge(HashSet<Integer> nodes) {
    HashMap<Integer, HashSet<Integer>> square = new HashMap<>();

    for (Integer n : nodes) {
      HashSet<Integer> intersect = new HashSet<>(connections.get(n));
      intersect.retainAll(nodes);
      square.put(n, intersect);
    }

    return square;
  }

  @Override
  public int degree(int n) {
    return degreeMapPar.get(n);
  }

  /**
   * Wrapper for prepareParallel method
   */
  public void degreePrepareParallel() {
    this.degreeMapPar = prepareParallel();
  }

  /**
   * Precalculation of all nodes' degree in parallel
   *
   * @return degreeMap with degrees
   */
  private HashMap<Integer, Integer> prepareParallel() {

    return fjPool.invoke(new ParallelDegree(connections));
  }

  /**
   * Reads from file and generates data structure for the graph
   *
   * @param filename file to read
   */
  private void fileToGraph(String filename) {

    try (Scanner sc = new Scanner(new File(filename), "UTF-8")) {
      while (sc.hasNextLine()) {
        String[] row = sc.nextLine().split("[\t ]");

        int n1 = Integer.parseInt(row[0]);
        int n2 = Integer.parseInt(row[1]);

        addEdge(n1, n2);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
/*    try (Stream<String> stream = Files.lines(Paths.get(filename))) {
      stream.forEach(x -> {

        if (x.startsWith(COMMENT_CHAR)) {
          return;
        }

        String[] row = x.split("[\t ]");
        int n1 = Integer.parseInt(row[0]);
        int n2 = Integer.parseInt(row[1]);

        addEdge(n1, n2);

      });

    } catch (IOException e) {
      e.printStackTrace();
    }*/

    System.out.println("Lettura ok");

  }

  private void addEdge(int u, int v) {
    if (!connections.containsKey(u)) {
      connections.put(u, new HashSet<>());
    }

    if (!connections.containsKey(v)) {
      connections.put(v, new HashSet<>());
    }

    connections.get(u).add(v);
    connections.get(v).add(u);

  }

  //-------------------------------------------- GETTER --------------------------------------------

  public HashMap<Integer, Integer> getDegreeMapPar() {
    return degreeMapPar;
  }

  public HashMap<Integer, HashSet<Integer>> getGraph() {
    return graph;
  }

  public HashMap<Integer, HashSet<Integer>> getConnections() {
    return connections;
  }

  public int getGraphDensity() {
    return graphDensity;
  }
}
