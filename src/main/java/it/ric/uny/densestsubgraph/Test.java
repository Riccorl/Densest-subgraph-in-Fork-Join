package it.ric.uny.densestsubgraph;

import com.google.common.graph.MutableGraph;
import it.ric.uny.densestsubgraph.utils.GraphParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {

        MutableGraph<Integer> graphGuava = null;
        //String filename = "data/dummy_graph.txt";
        String filename = "data/dummy_graph2.txt"; // nEdge = 11, nNode = 8
        //String filename = "data/facebook_combined.txt";
        //String filename = "data/ca-CondMat.txt";
        //String filename = "data/ca-AstroPh.txt"; // 84424
        //String filename = "data/roadNet-CA.txt"; // 0, nEdge = 2766607, nNodes = 1965206
        //String filename = "data/as-skitter.txt"; // nEdges = 11095298, nNodes = 1696415
        //String filename = "data/cit-Patents.txt"; // 3858266, nEdges = 16518948, nNodes = 3774768
        //String filename = "data/wiki-topcats.txt"; // 0, nEdges = 28511807, nNodes = 1791489
        //String filename = "data/soc-LiveJournal1.txt"; // 0, nEdge = 68993773, nNodes = 4847571

        int node = 0;
        double nEdges = 11;
        double nNodes = 8;

        // Guava
        /*try {
            graphGuava = GraphParser.parseGuava(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long startTimeG = System.currentTimeMillis();
        int deg = graphGuava.degree(node);
        long endTimeG = System.currentTimeMillis();
        double guavaTime = endTimeG - startTimeG;
        System.out.println("Guava Degree: " + deg);
        System.out.println("Guava Time: " + guavaTime + "ms");

        System.out.println("");*/

        // Sequenziale
        long startTime = System.nanoTime();
        UndirectedGraphSeq myGraph = new UndirectedGraphSeq(filename, nEdges, nNodes);
        //myGraph.degreeSeq(myGraph.getEdges());
       /* long endTime = System.nanoTime();
        long time = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("Sequential Time: " + time + "ms");

        int degSeq = myGraph.degree(node);
        System.out.println("Degree Sequential: " + degSeq);*/
        System.out.println("dG = " + myGraph.calcDensity(nEdges, nNodes));
        Set<Integer> densest = myGraph.densestSubgraph(0);
        System.out.println(densest);

        /*long startTime = System.nanoTime();
        UndirectedGraphArrays graphArrays = new UndirectedGraphArrays(filename, nEdges, nNodes);
        graphArrays.degreeConc();
        long endTime = System.nanoTime();
        long time = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("Fill Time: " + time + " ms");

        ArrayList<Integer> valTempi = new ArrayList<>();*/

        /*for (int i = 0; i < 10; i++) {
            long startTimeA = System.nanoTime();

            graphArrays.degreeConc();

            long endTimeA = System.nanoTime();
            long timeA = TimeUnit.NANOSECONDS.toMillis(endTimeA - startTimeA);
            System.out.println("Array Time: " + timeA + " ms");

            valTempi.add((int) timeA);

            if (i < 9) {
                graphArrays.setDegreeMap(new ConcurrentHashMap<>(nNodes,
                    0.99f));
            }

        }*/

        //Integer max = valTempi.stream().mapToInt(Integer::intValue).max().getAsInt();
        //valTempi.remove(max);
        //int media = valTempi.stream().mapToInt(Integer::intValue).sum() / valTempi.size();
        //System.out.println("Media tempi: " + media + " ms");
        //int degPar = graphArrays.degree(node);
        //System.out.println("Degree Array: " + degPar);

        // Induced Edge Set

/*      HashSet<Integer> s = new HashSet<>();
        s.add(0);
        s.add(1);
        s.add(2);

        System.out.println(myGraph.inducedEdge(s));*/

    }
}
