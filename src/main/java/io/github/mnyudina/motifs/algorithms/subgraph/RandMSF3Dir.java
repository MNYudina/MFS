package io.github.mnyudina.motifs.algorithms.subgraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;


import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import io.github.mnyudina.motifs.algorithms.GraphStatsOperation;
import io.github.mnyudina.motifs.exception.GraphStatsException;
import io.github.mnyudina.motifs.exception.UnsupportedEdgeTypeException;

/**
 * This is parallel version of 3-size undirected subgraphs counter which uses
 * random carcasses sampling algorithm.
 * 
 * @author Yudin Evgeniy
 */
public class RandMSF3Dir<V, E>  implements GraphStatsOperation {
	private boolean isParallel;

	Map<Integer, VertexLayerParameters<V>> vertexLayers;
	private Graph<V, E> graph;

    private int numberOfRuns;
    
	//private int[] motifs;
	double coef[]={1,1,1,1,1,1,1,3,3,1,1,3,3,3,3,3};
    
	Random randomGenerator = new Random();
	int[] arrcode = CreateKoefMass.igraph_i_isoclass2_3;
	int[] arr_idx = CreateKoefMass.igraph_i_isoclass_3_idx;
	public final long motifs[] = new long[16];


	public void FixMass(Integer w) {
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		VertexLayerParameters<V> selectedVertexLayer = null;

		// Choose a layer of vertices taking into account the probabilities of
		// layers selection
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			borderOfProbability += vertexLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedVertexLayer = vertexLayer.getValue();
				break;
			}
		}

		// Choose a vertex from the layer of vertices randomly
		V v1 = selectedVertexLayer.getVerticies()
				.get(randomGenerator.nextInt(selectedVertexLayer.getVerticies().size()));

		List<E> edges = new ArrayList(graph.getIncidentEdges(v1));
		List<E> tempL = new LinkedList(edges);
		E e1 = tempL.remove(randomGenerator.nextInt(tempL.size()));
		E e2 = tempL.remove(randomGenerator.nextInt(tempL.size()));
		V v2 = graph.getOpposite(v1, e1);
		V v3 = graph.getOpposite(v1, e2);

		
		V[] vert = (V[]) new Object[3];
		vert[0] = v1;

		vert[1] = v2;
		vert[2] = v3;

		int code = 0;

		for (int i = 0; i < vert.length - 1; i++) {
			for (int j = i + 1; j < vert.length; j++) {

				if (graph.findEdge(vert[i], vert[j]) != null)
					code |= arr_idx[3 * i + j];
				if (graph.findEdge(vert[j], vert[i]) != null)
					code |= arr_idx[3 * j + i];

			}
		}
		int ee = arrcode[code];

		synchronized (this) {
			motifs[ee] = motifs[ee] + 1l;
		}
	}
    /**
     * Constructs and initializes the class.
     *
     * @author Gleepa
     * @param graph the graph
     * @param numberOfRuns number of runs of sampling algorithm 
     */
	public RandMSF3Dir(Graph<V, E> graph, int numberOfRuns, boolean isParallel) {
        this.graph = graph;
        this.numberOfRuns = numberOfRuns;
		this.isParallel =isParallel;

    }
	
	/**
	 * 
	 * @author Yudin Evgeniy
	 * 
	 */
	public void execute() throws GraphStatsException {
		if (graph.getDefaultEdgeType() == EdgeType.UNDIRECTED) {
			throw new UnsupportedEdgeTypeException("The parallel version of 3-size subgraphs counter which uses full enumeration algorithm does not work with " + graph.getDefaultEdgeType() + " graph.");
		}



		Collection<V> vertices = graph.getVertices();
		vertexLayers = new HashMap<>();
		int numberOfVertexNeubor;
	    long t1=System.currentTimeMillis();

		/* 
		 * Bind each vertex of the graph to one layer of the vertices
		 * defined by number of successors of the vertex.
		 */
		for (V vertex : vertices) {
    		numberOfVertexNeubor = graph.getNeighbors(vertex).size();
    		if ( vertexLayers.get(numberOfVertexNeubor) == null) {
    			vertexLayers.put(numberOfVertexNeubor, new VertexLayerParameters<>());
    		}
    		vertexLayers.get(numberOfVertexNeubor).vertices.add(vertex);
		}
		
		long exactNumberOfForks = 0l;
		// Calculate exact number of the graph's "forks"
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			exactNumberOfForks += vertexLayer.getValue().vertices.size() * vertexLayer.getKey() * (vertexLayer.getKey() - 1l) / 2l;
			if(exactNumberOfForks<0)
				System.out.println(exactNumberOfForks);
		}
		System.out.println("forks = "+exactNumberOfForks);
		// Calculate probability of selection for each layer of the vertices
    	for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
    		double d=(vertexLayer.getValue().vertices.size() * vertexLayer.getKey() * (vertexLayer.getKey() - 1)) / (2.0)/ exactNumberOfForks;
    		if(d<0)
    			System.out.println(d);
    		vertexLayer.getValue().probability = (vertexLayer.getValue().vertices.size() * vertexLayer.getKey() * (vertexLayer.getKey() - 1.)) / 2.0 / exactNumberOfForks;
    	}
    	
    	List<Integer> resultsOfRuns = new ArrayList<>((int)numberOfRuns);
    	for (int i = 0; i < numberOfRuns; i++) {
    		resultsOfRuns.add(0);
		}
    	//System.out.println("���������� ������"+	    (System.currentTimeMillis()-t1));
    	if(isParallel){
    		resultsOfRuns.stream().parallel().forEach(this::FixMass);
    	}else{
    		resultsOfRuns.stream().forEach(this::FixMass);

    	}
		}
	
    List<Integer> resultsList;
	/**
	 * @author Gleepa
	 */
	@Override
    public String toString() {
		String ret = ""+this.getClass().getSimpleName()+"\n";

		for (int i = 0; i < motifs.length; i++) {
			ret=ret+motifs[i]/coef[i]+"\n";
		}



		return ret;    	
    }

}
