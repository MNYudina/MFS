package io.github.mnyudina.motifs.algorithms.branchspeedup;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.PajekNetReader;
import io.github.mnyudina.motifs.exception.GraphStatsException;
import io.github.mnyudina.motifs.graph.MySparseGraph;
import org.apache.commons.collections15.Factory;

import java.io.IOException;

/**
 * @author Gleepa
 */
public class Main {

    /**
     * @author Gleepa
     * @return the factory object
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
    public static void mainUseBA(String[] args) throws IOException, GraphStatsException { // for DEBUG
        Graph<Integer, Integer> graph;
        //String path="graphs\\PathwayCommons.net";
       // String path="graphs\\my_polBlog.net";
        String path="graphs\\GenReg.net";
     //   String path="graphs\\MyAs.net";

        System.out.println("Branch Test");

        int N=1000;

            GraphGenerator<Integer, Integer> gn = new PAGenerator(graphFactory, createIntegerFactory(),
                    createIntegerFactory(), new double[]{0, 0, 1}, k -> (int) k * 1., 100000);
            graph = gn.create();
            for (int i = 10000; i < 200000; i=i+10000) {
                long startTime;

             startTime = System.currentTimeMillis();
            BranchsWithLayers p4= new BranchsWithLayers<>(graph,i);
            p4.execute();
            System.out.print(";"+(System.currentTimeMillis() - startTime)+"; ;");

            startTime = System.currentTimeMillis();
            BranchsDirectly p5= new BranchsDirectly<>(graph,i);
            p5.execute();
            System.out.print(""+(System.currentTimeMillis() - startTime));


            startTime = System.currentTimeMillis();
            BranchsDirectlyMap p6= new BranchsDirectlyMap<>(graph,i);
            p6.execute();
            System.out.println(";"+(System.currentTimeMillis() - startTime));
            //System.out.println("------------------------------");

        }


    }

    public static void main(String[] args) throws IOException, GraphStatsException { // for DEBUG
        Graph<Integer, Integer> graph;
        //String path="graphs\\PathwayCommons.net";
        // String path="graphs\\my_polBlog.net";
        String path="graphs\\GenReg.net";
        //   String path="graphs\\MyAs.net";
        graph= new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new MySparseGraph<>());//AdjacencyListGraph

        System.out.println("Branch Test");

        int N=1000;

        /*GraphGenerator<Integer, Integer> gn = new PAGenerator(graphFactory, createIntegerFactory(),
                createIntegerFactory(), new double[]{0, 0, 1}, k -> (int) k * 1., 100000);
        graph = gn.create();*/
        int i = 100000;
        for (int j = 10000; j < 200000; j=j+10000)
        {
            long startTime;

            startTime = System.currentTimeMillis();
            BranchsWithLayers p4= new BranchsWithLayers<>(graph,i);
            p4.execute();
            System.out.print(";"+(System.currentTimeMillis() - startTime)+"; ;");

            startTime = System.currentTimeMillis();
            //BranchsDirectly p5= new BranchsDirectly<>(graph,i);
            //BranchsWithTreeLayers p5 = new BranchsWithTreeLayers<>(graph, i);
            BranchsDirectlyMap p5= new BranchsDirectlyMap(graph,i);

            p5.execute();
            System.out.print(""+(System.currentTimeMillis() - startTime));


            startTime = System.currentTimeMillis();
            BranchsDirectlyMass p6= new BranchsDirectlyMass(graph,i);
            p6.execute();
            System.out.println(";"+(System.currentTimeMillis() - startTime));
            //System.out.println("------------------------------");

        }


    }

}