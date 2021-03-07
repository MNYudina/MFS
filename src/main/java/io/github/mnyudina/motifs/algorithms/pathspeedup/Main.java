package io.github.mnyudina.motifs.algorithms.pathspeedup;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.PajekNetReader;
import io.github.mnyudina.motifs.algorithms.branchspeedup.PAGenerator;
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
    //_DEBUG
    public static void main2(String[] args) throws IOException, GraphStatsException { // for DEBUG
        Graph<Integer, Integer> graph;
        int N=100000;
        for (int i = 10000; i < 200000; i=i+10000) {
        GraphGenerator<Integer, Integer> gn = new PAGenerator(graphFactory, createIntegerFactory(),
                createIntegerFactory(), new double[]{0,0,1}, k->(int)k*1., N);
        graph= gn.create();
        long startTime = System.currentTimeMillis();
        PathNonTree p4= new PathNonTree<>(graph,i);
        p4.execute();
        System.out.print(";"+(System.currentTimeMillis() - startTime)+"; ;");
        startTime = System.currentTimeMillis();
        PathTree p5= new PathTree<>(graph,i);
        p5.execute();
        System.out.println("; "+(System.currentTimeMillis() - startTime));

        }
    }
    //_DEBUG
    public static void main(String[] args) throws IOException, GraphStatsException { // for DEBUG
        Graph<Integer, Integer> graph;
       // String path="graphs\\PathwayCommons.net";
        // String path="graphs\\my_polBlog.net";
        //String path="graphs\\GenReg.net";
        //   String path="graphs\\MyAs.net";

        String[] files = new String[]{"graphs\\PathwayCommons.net","graphs\\GenReg.net",
                "graphs\\MyAs.net","graphs\\Twitter.net"};
        for (String path:files
        ) {




// graph=new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new MySparseGraph<>());//AdjacencyListGraph

            graph= new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new MySparseGraph<>());//AdjacencyListGraph
            //  graph=new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new MySparseGraph<>());//AdjacencyListGraph
            // graph = initGraphEdgelistLong("C:\\1\\G+.txt");

            //System.out.println(graph.getEdgeCount());
            //System.out.println(graph.getVertexCount());
            System.out.println(graph);
        for (int i = 0; i < 10; i++) {
            long startTime = System.currentTimeMillis();
            PathTree p4= new PathTree<>(graph,100000);
            p4.execute();
            System.out.print(";"+(System.currentTimeMillis() - startTime)+"; ;");

            startTime = System.currentTimeMillis();
            PathNonTree p5= new PathNonTree<>(graph,100000);
            p5.execute();
            System.out.println("; "+(System.currentTimeMillis() - startTime));
        }
    }}
}