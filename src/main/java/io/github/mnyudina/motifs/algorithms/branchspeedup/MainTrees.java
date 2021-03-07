package io.github.mnyudina.motifs.algorithms.branchspeedup;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.graph.Graph;
import io.github.mnyudina.motifs.exception.GraphStatsException;
import io.github.mnyudina.motifs.graph.MySparseGraph;
import org.apache.commons.collections15.Factory;

import java.io.IOException;

/**
 * @author Gleepa
 */
public class MainTrees {

    /**
     * @return the factory object
     * @author Gleepa
     */
    private static Factory<Integer> createIntegerFactory() {
        return new Factory<Integer>() {
            private int n = 0;

            @Override
            public Integer create() {
                return n++;
            }
        };
    }

    static Factory<Graph<Integer, Integer>> graphFactory = new Factory<Graph<Integer, Integer>>() {

        @Override
        public Graph<Integer, Integer> create() {
            return new MySparseGraph();
        }

    };

    //_DEBUG UseBA
    public static void main(String[] args) throws IOException, GraphStatsException { // for DEBUG
        Graph<Integer, Integer> graph;
        //String path="graphs\\PathwayCommons.net";
        // String path="graphs\\my_polBlog.net";
        //String path = "graphs\\GenReg.net";
        //   String path="graphs\\MyAs.net";

        System.out.println("Branch Test2");

        int N = 1000;

        GraphGenerator<Integer, Integer> gn = new PAGenerator(graphFactory, createIntegerFactory(),
                createIntegerFactory(), new double[]{0, 0, 1}, k -> (int) k * 1., 100000);
        graph = gn.create();
        for (int i = 10000; i < 200000; i = i + 10000)
        {
            long startTime;

            startTime = System.currentTimeMillis();
            BranchsWithLayers p4 = new BranchsWithLayers<>(graph, i);
            p4.execute();
            System.out.print(";" + (System.currentTimeMillis() - startTime) + "; ;");

            startTime = System.currentTimeMillis();
            BranchsDirectlyMap p6 = new BranchsDirectlyMap<>(graph, i);
            p6.execute();
            System.out.print(";" + (System.currentTimeMillis() - startTime));
           // System.out.println("-------------Деревья-----------------");

            startTime = System.currentTimeMillis();
            BranchsWithTreeLayers p5 = new BranchsWithTreeLayers<>(graph, i);
            p5.execute();
            System.out.println(";" + (System.currentTimeMillis() - startTime));

            startTime = System.currentTimeMillis();
            BranchsDirectlyTreeMap p7 = new BranchsDirectlyTreeMap<>(graph, i);
            p7.execute();
            System.out.println(";" + (System.currentTimeMillis() - startTime));
        }


    }

    public static void main2(String[] args) throws IOException, GraphStatsException { // for DEBUG
        Graph<Integer, Integer> graph;
        //String path="graphs\\PathwayCommons.net";
        // String path="graphs\\my_polBlog.net";
        String path = "graphs\\GenReg.net";
        //   String path="graphs\\MyAs.net";

        System.out.println("Branch Test");

        int N = 1000;

        GraphGenerator<Integer, Integer> gn = new PAGenerator(graphFactory, createIntegerFactory(),
                createIntegerFactory(), new double[]{0, 0, 1}, k -> (int) k * 1., 100000);
        graph = gn.create();
        for (int i = 10000; i < 200000; i = i + 10000) {
            long startTime;

            startTime = System.currentTimeMillis();
            BranchsWithLayers p4 = new BranchsWithLayers<>(graph, i);
            p4.execute();
            System.out.print(";" + (System.currentTimeMillis() - startTime) + "; ;");

            startTime = System.currentTimeMillis();
            BranchsDirectly p5 = new BranchsDirectly<>(graph, i);
            p5.execute();
            System.out.print("" + (System.currentTimeMillis() - startTime));


            startTime = System.currentTimeMillis();
            BranchsDirectlyMap p6 = new BranchsDirectlyMap<>(graph, i);
            p6.execute();
            System.out.println(";" + (System.currentTimeMillis() - startTime));
            //System.out.println("------------------------------");

        }


    }

}