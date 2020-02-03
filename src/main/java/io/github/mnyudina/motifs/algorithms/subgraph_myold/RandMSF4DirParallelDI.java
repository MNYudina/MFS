package io.github.mnyudina.motifs.algorithms.subgraph_myold;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import io.github.mnyudina.motifs.algorithms.subgraph.EdgeLayerParameters;
import io.github.mnyudina.motifs.algorithms.subgraph.VertexLayerParameters;
import io.github.mnyudina.motifs.exception.GraphStatsException;
import io.github.mnyudina.motifs.exception.UnsupportedEdgeTypeException;

public class RandMSF4DirParallelDI<V, E> {
	Random randomGenerator = new Random();

	public int searchLapka() {
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		VertexLayerParameters<V> selectedVertexLayer = new VertexLayerParameters<>();

		// Choose a layer of vertices taking into account the probabilities of
		// layers selection
		for (Entry<Long, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			borderOfProbability += vertexLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedVertexLayer = vertexLayer.getValue();
				break;
			}
		}

		V v1 = selectedVertexLayer.getVerticies()
				.get(randomGenerator.nextInt(selectedVertexLayer.getVerticies().size()));

		// Choose 3 successors of the vertex randomly
		List<V> v1List = new LinkedList<>(graph.getNeighbors(v1));
		int randomIntValue = randomGenerator.nextInt(v1List.size());
		V v2 = v1List.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(v1List.size());
		V v3 = v1List.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(v1List.size());
		V v4 = v1List.remove(randomIntValue);

		if (!graph.getNeighbors(v2).contains(v3) && !graph.getNeighbors(v2).contains(v4)
				&& !graph.getNeighbors(v3).contains(v4)) {

			V[] vert = (V[]) new Object[4];
			vert[0] = v1;
			vert[1] = v2;
			vert[2] = v3;
			vert[3] = v4;
			int code = 0;
			for (int i = 0; i < vert.length - 1; i++) {
				for (int j = i + 1; j < vert.length; j++) {
					E o1 = graph.findEdge(vert[i], vert[j]);
					if (o1 != null)
						code |= RandMSF4Dir1.arr_idx[4 * i + j];
					E o2 = graph.findEdge(vert[j], vert[i]);
					if (o2 != null)
						code |= RandMSF4Dir1.arr_idx[4 * j + i];
				}
			}
			return RandMSF4Dir1.arrcode[code];
		} else {
			return -1;
		}

	}

	public int searchScobka() {
		Random randomGenerator = new Random();
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		EdgeLayerParameters<E> selectedEdgeLayer = null;

		// Choose a layer of edges taking into account the probabilities of
		// layers selection
		for (Entry<Integer, EdgeLayerParameters<E>> edgeLayer : edgeLayers.entrySet()) {
			if (edgeLayer.getValue().getProbability() < 0) {
				System.out.println("ddd");
			}
			borderOfProbability += edgeLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedEdgeLayer = edgeLayer.getValue();
				break;
			}
		}

		// Choose an edge from the layer of edges randomly
		E selectedEdge = selectedEdgeLayer.getEdges().get(randomGenerator.nextInt(selectedEdgeLayer.getEdges().size()));

		// Get endpoints of the edge
		V v1 = graph.getEndpoints(selectedEdge).getFirst();
		V v2 = graph.getEndpoints(selectedEdge).getSecond();

		// Generate a list of successors of the endpoints
		List<V> successors1 = new LinkedList<V>(graph.getNeighbors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getNeighbors(v2));
		successors1.remove(v2);
		successors2.remove(v1);

		// Choose 2 successors of the endpoints randomly
		V v3 = successors1.get(randomGenerator.nextInt(successors1.size()));
		V v4 = successors2.get(randomGenerator.nextInt(successors2.size()));

		V[] vert = (V[]) new Object[4];
		vert[0] = v1;
		vert[1] = v2;
		vert[2] = v3;
		vert[3] = v4;
		int code = 0;
		for (int i = 0; i < vert.length - 1; i++) {
			for (int j = i + 1; j < vert.length; j++) {
				E o1 = graph.findEdge(vert[i], vert[j]);
				if (o1 != null)
					code |= RandMSF4Dir1.arr_idx[4 * i + j];
				E o2 = graph.findEdge(vert[j], vert[i]);
				if (o2 != null)
					code |= RandMSF4Dir1.arr_idx[4 * j + i];
			}
		}
		return RandMSF4Dir1.arrcode[code];
	}

	public void FixLapka(Integer ir) {
		Random randomGenerator = new Random();
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		VertexLayerParameters<V> selectedVertexLayer = new VertexLayerParameters<>();

		// Choose a layer of vertices taking into account the probabilities of
		// layers selection
		for (Entry<Long, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			borderOfProbability += vertexLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedVertexLayer = vertexLayer.getValue();
				break;
			}
		}

		V v1 = selectedVertexLayer.getVerticies()
				.get(randomGenerator.nextInt(selectedVertexLayer.getVerticies().size()));

		List<V> v1List = new LinkedList<>(graph.getNeighbors(v1));

		for (int ii = 0; ii < v1List.size() - 2; ii++) {
			for (int jj = ii + 1; jj < v1List.size() - 1; jj++) {
				for (int kk = jj + 1; kk < v1List.size(); kk++) {
					V v2 = v1List.get(ii);
					V v3 = v1List.get(jj);
					V v4 = v1List.get(kk);

					if (!graph.getNeighbors(v2).contains(v3) && !graph.getNeighbors(v2).contains(v4)
							&& !graph.getNeighbors(v3).contains(v4)) {
						V[] vert = (V[]) new Object[4];
						vert[0] = v1;
						vert[1] = v2;
						vert[2] = v3;
						vert[3] = v4;
						int code = 0;
						for (int i = 0; i < vert.length - 1; i++) {
							for (int j = i + 1; j < vert.length; j++) {
								E o1 = graph.findEdge(vert[i], vert[j]);
								if (o1 != null)
									code |= RandMSF4Dir1.arr_idx[4 * i + j];
								E o2 = graph.findEdge(vert[j], vert[i]);
								if (o2 != null)
									code |= RandMSF4Dir1.arr_idx[4 * j + i];
							}
						}
						int ee = RandMSF4Dir1.arrcode[code];
						synchronized (motifs) {
							motifs[ee] = motifs[ee] + 1;
						}
					}
				}
			}
		}

	}

	double[] massKoef = { 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 2, 1, 1, 2, 2, 4, 6, 6, 0, 0, 1, 1, 1, 1, 1, 1, 2,
			1, 2, 1, 1, 2, 2, 2, 2, 0, 1, 1, 2, 1, 2, 1, 1, 2, 2, 2, 2, 2, 4, 6, 4, 6, 2, 2, 6, 6, 6, 6, 1, 2, 2, 2, 4,
			6, 4, 6, 6, 6, 6, 4, 6, 6, 1, 2, 6, 2, 6, 6, 6, 12, 12, 6, 6, 12, 12, 12, 12, 12, 1, 1, 1, 2, 1, 2, 1, 2, 1,
			2, 2, 2, 2, 2, 2, 2, 2, 6, 4, 6, 4, 6, 6, 6, 6, 6, 6, 2, 0, 1, 2, 2, 2, 1, 2, 2, 4, 6, 4, 6, 6, 6, 6, 4, 6,
			6, 2, 2, 2, 2, 2, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 12, 6, 12, 12,
			12, 12, 12, 6, 6, 6, 6, 12, 12, 6, 12, 12, 12, 12, 12, 12, 12, 6, 12, 12, 6, 12, 12, 12, 12, 12, 12, 6, 6,
			6, 6, 4, 6, 6, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12 };
	private Graph<V, E> graph;

	private int numberOfThreads;

	private long exploredNumberOfSubgraphs4_1, exploredNumberOfSubgraphs4_2, exploredNumberOfSubgraphs4_3,
			exploredNumberOfSubgraphs4_4, exploredNumberOfSubgraphs4_5, exploredNumberOfSubgraphs4_6;
	final double motifs[] = new double[218];
	final long motifsScobka[] = new long[218];
	final long motifsLapka[] = new long[218];
	final double sigmas[] = new double[218];

	private int numberOfRuns;
	private long numberOfRunsLapka;
	private long numberOfRunsScoba;

	private int approximateNumberOfSubgraphs4_1, approximateNumberOfSubgraphs4_2, approximateNumberOfSubgraphs4_3,
			approximateNumberOfSubgraphs4_4, approximateNumberOfSubgraphs4_5, approximateNumberOfSubgraphs4_6;

	List<Integer> resultsList;
	List<Integer> resultsList2;

	long numberOfCarcasScobka = 0;
	long numberOfCarcasLapka = 0;
	Map<Long, VertexLayerParameters<V>> vertexLayers ;
	Map<Integer, EdgeLayerParameters<E>> edgeLayers;
	
	/**
	 * Constructs and initializes the class.
	 *
	 * @author Yudina Maria, Yudin Evgeniy
	 * @param graph
	 *            the graph
	 * @param numberOfRuns
	 *            number of runs of sampling algorithm
	 * @param numberOfThreads
	 *            number of parallel threads
	 */
	public RandMSF4DirParallelDI(Hypergraph<V, E> graph, int numberOfRuns, int numberOfThreads) {
		this.graph = (Graph<V, E>) graph;
		this.numberOfRuns = numberOfRuns;
		this.numberOfThreads = numberOfThreads;
	}

	/**
	 * Saves number of explored <code>graph</code>'s subgraphs4_1 into
	 * <code>exploredNumberOfSubgraphs4_1</code> variable.<br>
	 * Saves results of runs of the algorithm into <code>resultsList</code>.<br>
	 * <p>
	 * The method uses Function and Parallel Stream features of Java 1.8 and
	 * custom ForkJoinPool for parallel execution.
	 * 
	 * @author Yudina Maria, Yudin Evgeniy
	 * @throws UnsupportedEdgeTypeException
	 */
	public void execute() throws GraphStatsException {
		Collection<V> vertices = graph.getVertices();
		vertexLayers = new HashMap<>();
		long neibours;

		/*
		 * Bind each vertex of the graph to one layer of the vertices defined by
		 * number of successors of the vertex.
		 */
		for (V vertex : vertices) {
			neibours = graph.getNeighborCount(vertex);
			if (vertexLayers.get(neibours) == null) {
				vertexLayers.put(neibours, new VertexLayerParameters<>());
			}
			vertexLayers.get(neibours).vertices.add(vertex);
		}

		// Calculate exact number of lapka
		for (Entry<Long, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			numberOfCarcasLapka += vertexLayer.getValue().vertices.size() * vertexLayer.getKey()
					* (vertexLayer.getKey() - 1l) * (vertexLayer.getKey() - 2l) / 6l;
		}

		// Calculate probability of selection for each layer of the vertices
		for (Entry<Long, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			vertexLayer.getValue().probability = (vertexLayer.getValue().vertices.size() * vertexLayer.getKey()
					* (vertexLayer.getKey() - 1l) * (vertexLayer.getKey() - 2l) / (6.0 * numberOfCarcasLapka));
		}

		Collection<E> edges = graph.getEdges();
		edgeLayers = new HashMap<>();
		int numberOfPathsOfLengthThree;

		/*
		 * Bind each edge of the graph to one layer of the edges defined by
		 * number of path of length 3 including the edge.
		 */
		for (E edge : edges) {
			V v1 = graph.getEndpoints(edge).getFirst();
			V v2 = graph.getEndpoints(edge).getSecond();
			numberOfPathsOfLengthThree = (graph.getNeighborCount(v1) - 1) * (graph.getNeighborCount(v2) - 1);
			if (edgeLayers.get(numberOfPathsOfLengthThree) == null) {
				edgeLayers.put(numberOfPathsOfLengthThree, new EdgeLayerParameters<>());
			}
			edgeLayers.get(numberOfPathsOfLengthThree).edges.add(edge);
		}

		// Calculate exact number of the graph's path of length three
		for (Entry<Integer, EdgeLayerParameters<E>> edgeLayer : edgeLayers.entrySet()) {
			numberOfCarcasScobka += edgeLayer.getValue().edges.size() * edgeLayer.getKey();
		}

		// Calculate probability of selection for each layer of the edges
		for (Entry<Integer, EdgeLayerParameters<E>> edgeLayer : edgeLayers.entrySet()) {
			edgeLayer.getValue().probability = (edgeLayer.getValue().edges.size() / (double) numberOfCarcasScobka
					* edgeLayer.getKey());
		}

		// RandMSF4Dir<V, E> counter = new RandMSF4Dir(graph, vertexLayers,
		// edgeLayers);

		// numberOfRunsLapka =
		// (int)(numberOfRuns*(exactNumberOfPathsOfLengthThree)/((double)(exactNumberOfSubgraphs4_1+exactNumberOfPathsOfLengthThree)));
		// numberOfRunsScoba =numberOfRuns-numberOfRunsLapka;
		numberOfRunsLapka = numberOfRuns;
		numberOfRunsScoba = numberOfRuns;

		List<Integer> resultsOfRunsScobka = new ArrayList<>((int) numberOfRunsScoba);
		List<Integer> resultsOfRunsLapka = new ArrayList<>((int) numberOfRunsLapka);

		for (int i = 0; i < numberOfRunsScoba; i++) {
			resultsOfRunsScobka.add(0);
		}

		for (int i = 0; i < numberOfRunsLapka; i++) {
			resultsOfRunsLapka.add(0);
		}

		resultsList2 = resultsOfRunsLapka.stream().map(resultOfRun2 -> this.searchLapka()).collect(ArrayList::new,
				ArrayList::add, ArrayList::addAll);
		resultsList = resultsOfRunsScobka.stream().map(resultOfRun3 -> this.searchScobka()).collect(ArrayList::new,
				ArrayList::add, ArrayList::addAll);
		// resultsList.addAll(resultsList2);

		for (int i = 0; i < motifsScobka.length; i++) {
			{
				final int num = i;
				{
					motifsScobka[num] = (resultsList.stream().filter(x -> x == num).count());
					// if (num > 4)
					// sumScoba = sumScoba + motifsScobka[num];
				}
			}
		}

		for (int i = 0; i < motifsLapka.length; i++) {
			{
				final int num = i;
				{
					motifsLapka[num] = (resultsList2.stream().filter(x -> x == num).count());
					// if (num > 4)
					// sumScoba = sumScoba + motifsLapka[num];
				}
			}
		}
		double D1, D2, lyamda, D, Sigma;

		for (int i = 0; i < motifs.length; i++) {
			if (massKoef[i] == 0)
				continue;
			D1 = numberOfCarcasScobka * numberOfCarcasScobka / ((double) (numberOfRunsScoba * numberOfRunsScoba))
					* motifsScobka[i] / (massKoef[i])
					* (1 - motifsScobka[i] / (massKoef[i]) / ((double) numberOfRunsScoba));
			D2 = numberOfCarcasLapka * (double) numberOfCarcasLapka / ((double) numberOfRunsLapka * numberOfRunsLapka)
					* motifsLapka[i] / (massKoef[i])
					* (1 - motifsLapka[i] / (massKoef[i]) / ((double) numberOfRunsLapka));

			double n1 = motifsScobka[i] * numberOfCarcasScobka / (massKoef[i]) / (double) numberOfRunsScoba;
			double n2 = motifsLapka[i] * numberOfCarcasLapka / (massKoef[i]) / (double) numberOfRunsLapka;

			lyamda = 0;
			if (n1 * n2 > 0) {
				lyamda = D1 * n2 / (D1 * n2 + D2 * n1);
				D = (1 - lyamda) * (1 - lyamda) * D1 + lyamda * lyamda * D2;
				Sigma = Math.sqrt(D);
				sigmas[i] = Sigma;
				motifs[i] = (n1 + lyamda * (n2 - n1));
			} else if (n1 == 0 && n2 != 0) {
				D = D2;
				Sigma = Math.sqrt(D);
				sigmas[i] = Sigma;
				motifs[i] = n2;

			}

			else if (n1 != 0 && n2 == 0) {
				// System.out.println("����� ���� �� ������");
				D = D1;
				Sigma = Math.sqrt(D);
				sigmas[i] = Sigma;
				motifs[i] = n1;
			}
		
		}
	}

	public String toStringWithSigma() {
		/*
		 * int arr_lapkis[] = { 3, 7, 24, 8, 25, 26, 76, 92, 93, 94 };
		 * 
		 * HashSet<Integer> lapkaSet = new HashSet(); for (int i = 0; i <
		 * arr_lapkis.length; i++) { lapkaSet.add(arr_lapkis[i]); }
		 * 
		 * String str = ""; for (int i = 0; i < motifs.length; i++) {
		 * 
		 * str = str + (!lapkaSet.contains(i) ? (double) motifs[i] /
		 * numberOfRunsScoba * numberOfCarcasScobka / massKoef[i] : (double)
		 * motifs[i] / numberOfRunsLapka * numberOfCarcasLapka) + "\n"; }
		 */

		String str = "";

		for (int i = 0; i < motifs.length; i++) {
			if (motifs[i] != 0)
				str = str + (motifs[i] - 3. * sigmas[i]) + " " + motifs[i] + " " + (motifs[i] + 3. * sigmas[i]) + "\n";

		}

		return str;
	}

	@Override
	public String toString() {
		/*
		 * int arr_lapkis[] = { 3, 7, 24, 8, 25, 26, 76, 92, 93, 94 };
		 * 
		 * HashSet<Integer> lapkaSet = new HashSet(); for (int i = 0; i <
		 * arr_lapkis.length; i++) { lapkaSet.add(arr_lapkis[i]); }
		 * 
		 * String str = ""; for (int i = 0; i < motifs.length; i++) {
		 * 
		 * str = str + (!lapkaSet.contains(i) ? (double) motifs[i] /
		 * numberOfRunsScoba * numberOfCarcasScobka / massKoef[i] : (double)
		 * motifs[i] / numberOfRunsLapka * numberOfCarcasLapka) + "\n"; }
		 */

		String str = "";

		for (int i = 0; i < motifs.length; i++) {
			str = str + motifs[i] + "\n";

		}
		str=str+"=======================================\n";
		for (int i = 0; i < motifs.length; i++) {
			str = str + 3*sigmas[i] + "\n";

		}
		
		/*
		 * str = str +"------3 �����(�����������)----------\n"; for (int i = 0;
		 * i < motifs.length; i++) { if(motifs[i]!=0) str = str + 3*sigmas[i] +
		 * "\n"; else str = str +"0\n";
		 * 
		 * }
		 */

		return str;
	}

}