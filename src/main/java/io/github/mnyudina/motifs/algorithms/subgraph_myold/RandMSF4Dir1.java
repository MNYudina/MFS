package io.github.mnyudina.motifs.algorithms.subgraph_myold;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import edu.uci.ics.jung.graph.Graph;
import io.github.mnyudina.motifs.algorithms.Executor;
import io.github.mnyudina.motifs.algorithms.subgraph.VertexLayerParameters;
import io.github.mnyudina.motifs.exception.GraphStatsException;
import io.github.mnyudina.motifs.exception.UnsupportedEdgeTypeException;
import io.github.mnyudina.motifs.graph.MyDirectedSparseGraph;

/**
 * This is parallel version of 4-size directed subgraphs counter which uses
 * random carcasses sampling algorithm.
 * 
 * @author Yudina Maria, Yudin Evgeniy
 */
public class RandMSF4Dir1<V, E> implements Executor {
	private boolean isParallel;
	Map<Integer, VertexLayerParameters<V>> vertexLayers ;
	Map<Integer, EdgeLayerParameters<E>> edgeLayers;
	double[] massKoefL = { 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 2, 1, 1, 2, 2, 4, 6, 6, 0, 0, 1, 1, 1, 1, 1, 1, 2, 1,
			2, 1, 1, 2, 2, 2, 2, 0, 1, 1, 2, 1, 2, 1, 1, 2, 2, 2, 2, 2, 4, 6, 4, 6, 2, 2, 6, 6, 6, 6, 1, 2, 2, 2, 4, 6,
			4, 6, 6, 6, 6, 4, 6, 6, 1, 2, 6, 2, 6, 6, 6, 12, 12, 6, 6, 12, 12, 12, 12, 12, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2,
			2, 2, 2, 2, 2, 2, 2, 6, 4, 6, 4, 6, 6, 6, 6, 6, 6, 2, 0, 1, 2, 2, 2, 1, 2, 2, 4, 6, 4, 6, 6, 6, 6, 4, 6, 6,
			2, 2, 2, 2, 2, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 12, 6, 12, 12, 12,
			12, 12, 6, 6, 6, 6, 12, 12, 6, 12, 12, 12, 12, 12, 12, 12, 6, 12, 12, 6, 12, 12, 12, 12, 12, 12, 6, 6, 6, 6,
			4, 6, 6, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12 };
	double[] massKoefR={0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 2, 1, 2,
			2, 2, 2, 1, 2, 2, 1, 1, 2, 1, 2, 2, 2, 4, 4, 2, 2, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
			1, 1, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 2, 2, 2, 1, 2, 2, 1, 1, 1, 1, 1, 2, 
			2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 4, 2, 4, 4, 4, 4, 4, 2, 2, 2, 2, 4, 4, 
			2, 4, 4, 4, 4, 4, 4, 4, 2, 4, 4, 2, 4, 4, 4, 4, 4, 4, 2, 2, 2, 2, 1, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}; 

	private MyDirectedSparseGraph<V, E> graph;
	//private DirectedSparseGraph<V, E> graph;
	//private Graph<V, E> graph;

	final double motifs[] = new double[218];
	final double motifs_long[] = new double[218];

	final double motifsScobka[] = new double[218];
	final double motifsLapka[] = new double[218];
	final double sigmas[] = new double[218];


	private double numberOfRuns;
	private double numberOfRunsLapka;
	private double numberOfRunsScoba;


	//List<Integer> resultsList;
	//List<Integer> resultsList2;

	double numberOfCarcasScobka = 0;
	double numberOfCarcasLapka = 0;

	static int[] arrcode={
			  0,  1,  1,  2,  1,  2,  2,  3,  1,  4,  5,  6,  5,  6,  7,  8,  1,  5,  9, 10,
			  11, 12, 13, 14,  2,  6, 10, 15, 12, 16, 17, 18,  1,  5, 11, 12,  9, 10, 13, 14,
			   2,  6, 12, 16, 10, 15, 17, 18,  2,  7, 13, 17, 13, 17, 19, 20,  3,  8, 14, 18,
			  14, 18, 20, 21,  1,  5,  4,  6,  5,  7,  6,  8,  9, 22, 22, 23, 24, 25, 25, 26,
			   5, 27, 22, 28, 29, 30, 31, 32, 10, 28, 33, 34, 35, 36, 37, 38, 11, 29, 39, 40,
			  41, 42, 43, 44, 13, 31, 45, 46, 47, 48, 49, 50, 12, 30, 45, 51, 52, 53, 54, 55,
			  14, 32, 56, 57, 58, 59, 60, 61,  1,  9,  5, 10, 11, 13, 12, 14,  5, 22, 27, 28,
			  29, 31, 30, 32,  4, 22, 22, 33, 39, 45, 45, 56,  6, 23, 28, 34, 40, 46, 51, 57,
			   5, 24, 29, 35, 41, 47, 52, 58,  7, 25, 30, 36, 42, 48, 53, 59,  6, 25, 31, 37,
			  43, 49, 54, 60,  8, 26, 32, 38, 44, 50, 55, 61,  2, 10,  6, 15, 12, 17, 16, 18,
			  10, 33, 28, 34, 35, 37, 36, 38,  6, 28, 23, 34, 40, 51, 46, 57, 15, 34, 34, 62,
			  63, 64, 64, 65, 12, 35, 40, 63, 66, 67, 68, 69, 17, 37, 51, 64, 67, 70, 71, 72,
			  16, 36, 46, 64, 68, 71, 73, 74, 18, 38, 57, 65, 69, 72, 74, 75,  1, 11,  5, 12,
			   9, 13, 10, 14, 11, 39, 29, 40, 41, 43, 42, 44,  5, 29, 24, 35, 41, 52, 47, 58,
			  12, 40, 35, 63, 66, 68, 67, 69,  9, 41, 41, 66, 76, 77, 77, 78, 13, 43, 52, 68,
			  77, 79, 80, 81, 10, 42, 47, 67, 77, 80, 82, 83, 14, 44, 58, 69, 78, 81, 83, 84,
			   2, 12,  6, 16, 10, 17, 15, 18, 13, 45, 31, 46, 47, 49, 48, 50,  7, 30, 25, 36,
			  42, 53, 48, 59, 17, 51, 37, 64, 67, 71, 70, 72, 13, 52, 43, 68, 77, 80, 79, 81,
			  19, 54, 54, 73, 82, 85, 85, 86, 17, 53, 49, 71, 80, 87, 85, 88, 20, 55, 60, 74,
			  83, 88, 89, 90,  2, 13,  7, 17, 13, 19, 17, 20, 12, 45, 30, 51, 52, 54, 53, 55,
			   6, 31, 25, 37, 43, 54, 49, 60, 16, 46, 36, 64, 68, 73, 71, 74, 10, 47, 42, 67,
			  77, 82, 80, 83, 17, 49, 53, 71, 80, 85, 87, 88, 15, 48, 48, 70, 79, 85, 85, 89,
			  18, 50, 59, 72, 81, 86, 88, 90,  3, 14,  8, 18, 14, 20, 18, 21, 14, 56, 32, 57,
			  58, 60, 59, 61,  8, 32, 26, 38, 44, 55, 50, 61, 18, 57, 38, 65, 69, 74, 72, 75,
			  14, 58, 44, 69, 78, 83, 81, 84, 20, 60, 55, 74, 83, 89, 88, 90, 18, 59, 50, 72,
			  81, 88, 86, 90, 21, 61, 61, 75, 84, 90, 90, 91,  1,  5,  5,  7,  4,  6,  6,  8,
			   9, 22, 24, 25, 22, 23, 25, 26, 11, 29, 41, 42, 39, 40, 43, 44, 13, 31, 47, 48,
			  45, 46, 49, 50,  5, 27, 29, 30, 22, 28, 31, 32, 10, 28, 35, 36, 33, 34, 37, 38,
			  12, 30, 52, 53, 45, 51, 54, 55, 14, 32, 58, 59, 56, 57, 60, 61,  9, 24, 22, 25,
			  22, 25, 23, 26, 76, 92, 92, 93, 92, 93, 93, 94, 41, 95, 96, 97, 98, 99,100,101,
			  77,102,103,104,105,106,107,108, 41, 95, 98, 99, 96, 97,100,101, 77,102,105,106,
			 103,104,107,108, 66,109,110,111,110,111,112,113, 78,114,115,116,115,116,117,118,
			  11, 41, 29, 42, 39, 43, 40, 44, 41, 96, 95, 97, 98,100, 99,101, 39, 98, 98,119,
			 120,121,121,122, 43,100,123,124,121,125,126,127, 29, 95,128,129, 98,123,130,131,
			  42, 97,129,132,119,124,133,134, 40, 99,130,133,121,126,135,136, 44,101,131,134,
			 122,127,136,137, 13, 47, 31, 48, 45, 49, 46, 50, 77,103,102,104,105,107,106,108,
			  43,123,100,124,121,126,125,127, 79,138,138,139,140,141,141,142, 52,143,130,144,
			 110,145,146,147, 80,148,149,150,151,152,153,154, 68,155,146,156,157,158,159,160,
			  81,161,162,163,164,165,166,167,  5, 29, 27, 30, 22, 31, 28, 32, 41, 98, 95, 99,
			  96,100, 97,101, 29,128, 95,129, 98,130,123,131, 52,130,143,144,110,146,145,147,
			  24, 95, 95,109, 92,102,102,114, 47,123,143,155,103,138,148,161, 35,129,143,168,
			 105,149,169,170, 58,131,171,172,115,162,173,174, 10, 35, 28, 36, 33, 37, 34, 38,
			  77,105,102,106,103,107,104,108, 42,129, 97,132,119,133,124,134, 80,149,148,150,
			 151,153,152,154, 47,143,123,155,103,148,138,161, 82,169,169,175,176,177,177,178,
			  67,168,145,179,151,180,181,182, 83,170,173,183,184,185,186,187, 12, 52, 30, 53,
			  45, 54, 51, 55, 66,110,109,111,110,112,111,113, 40,130, 99,133,121,135,126,136,
			  68,146,155,156,157,159,158,160, 35,143,129,168,105,169,149,170, 67,145,168,179,
			 151,181,180,182, 63,144,144,188,140,189,189,190, 69,147,172,191,164,192,193,194,
			  14, 58, 32, 59, 56, 60, 57, 61, 78,115,114,116,115,117,116,118, 44,131,101,134,
			 122,136,127,137, 81,162,161,163,164,166,165,167, 58,171,131,172,115,173,162,174,
			  83,173,170,183,184,186,185,187, 69,172,147,191,164,193,192,194, 84,174,174,195,
			 196,197,197,198,  1,  9, 11, 13,  5, 10, 12, 14,  5, 22, 29, 31, 27, 28, 30, 32,
			   5, 24, 41, 47, 29, 35, 52, 58,  7, 25, 42, 48, 30, 36, 53, 59,  4, 22, 39, 45,
			  22, 33, 45, 56,  6, 23, 40, 46, 28, 34, 51, 57,  6, 25, 43, 49, 31, 37, 54, 60,
			   8, 26, 44, 50, 32, 38, 55, 61, 11, 41, 39, 43, 29, 42, 40, 44, 41, 96, 98,100,
			  95, 97, 99,101, 29, 95, 98,123,128,129,130,131, 42, 97,119,124,129,132,133,134,
			  39, 98,120,121, 98,119,121,122, 43,100,121,125,123,124,126,127, 40, 99,121,126,
			 130,133,135,136, 44,101,122,127,131,134,136,137,  9, 76, 41, 77, 41, 77, 66, 78,
			  24, 92, 95,102, 95,102,109,114, 22, 92, 96,103, 98,105,110,115, 25, 93, 97,104,
			  99,106,111,116, 22, 92, 98,105, 96,103,110,115, 25, 93, 99,106, 97,104,111,116,
			  23, 93,100,107,100,107,112,117, 26, 94,101,108,101,108,113,118, 13, 77, 43, 79,
			  52, 80, 68, 81, 47,103,123,138,143,148,155,161, 31,102,100,138,130,149,146,162,
			  48,104,124,139,144,150,156,163, 45,105,121,140,110,151,157,164, 49,107,126,141,
			 145,152,158,165, 46,106,125,141,146,153,159,166, 50,108,127,142,147,154,160,167,
			   5, 41, 29, 52, 24, 47, 35, 58, 29, 98,128,130, 95,123,129,131, 27, 95, 95,143,
			  95,143,143,171, 30, 99,129,144,109,155,168,172, 22, 96, 98,110, 92,103,105,115,
			  31,100,130,146,102,138,149,162, 28, 97,123,145,102,148,169,173, 32,101,131,147,
			 114,161,170,174, 12, 66, 40, 68, 35, 67, 63, 69, 52,110,130,146,143,145,144,147,
			  30,109, 99,155,129,168,144,172, 53,111,133,156,168,179,188,191, 45,110,121,157,
			 105,151,140,164, 54,112,135,159,169,181,189,192, 51,111,126,158,149,180,189,193,
			  55,113,136,160,170,182,190,194, 10, 77, 42, 80, 47, 82, 67, 83, 35,105,129,149,
			 143,169,168,170, 28,102, 97,148,123,169,145,173, 36,106,132,150,155,175,179,183,
			  33,103,119,151,103,176,151,184, 37,107,133,153,148,177,180,185, 34,104,124,152,
			 138,177,181,186, 38,108,134,154,161,178,182,187, 14, 78, 44, 81, 58, 83, 69, 84,
			  58,115,131,162,171,173,172,174, 32,114,101,161,131,170,147,174, 59,116,134,163,
			 172,183,191,195, 56,115,122,164,115,184,164,196, 60,117,136,166,173,186,193,197,
			  57,116,127,165,162,185,192,197, 61,118,137,167,174,187,194,198,  2, 10, 12, 17,
			   6, 15, 16, 18, 10, 33, 35, 37, 28, 34, 36, 38, 12, 35, 66, 67, 40, 63, 68, 69,
			  17, 37, 67, 70, 51, 64, 71, 72,  6, 28, 40, 51, 23, 34, 46, 57, 15, 34, 63, 64,
			  34, 62, 64, 65, 16, 36, 68, 71, 46, 64, 73, 74, 18, 38, 69, 72, 57, 65, 74, 75,
			  13, 47, 45, 49, 31, 48, 46, 50, 77,103,105,107,102,104,106,108, 52,143,110,145,
			 130,144,146,147, 80,148,151,152,149,150,153,154, 43,123,121,126,100,124,125,127,
			  79,138,140,141,138,139,141,142, 68,155,157,158,146,156,159,160, 81,161,164,165,
			 162,163,166,167, 13, 77, 52, 80, 43, 79, 68, 81, 47,103,143,148,123,138,155,161,
			  45,105,110,151,121,140,157,164, 49,107,145,152,126,141,158,165, 31,102,130,149,
			 100,138,146,162, 48,104,144,150,124,139,156,163, 46,106,146,153,125,141,159,166,
			  50,108,147,154,127,142,160,167, 19, 82, 54, 85, 54, 85, 73, 86, 82,176,169,177,
			 169,177,175,178, 54,169,112,181,135,189,159,192, 85,177,181,199,189,200,201,202,
			  54,169,135,189,112,181,159,192, 85,177,189,200,181,199,201,202, 73,175,159,201,
			 159,201,203,204, 86,178,192,202,192,202,204,205,  7, 42, 30, 53, 25, 48, 36, 59,
			  42,119,129,133, 97,124,132,134, 30,129,109,168, 99,144,155,172, 53,133,168,188,
			 111,156,179,191, 25, 97, 99,111, 93,104,106,116, 48,124,144,156,104,139,150,163,
			  36,132,155,179,106,150,175,183, 59,134,172,191,116,163,183,195, 17, 67, 51, 71,
			  37, 70, 64, 72, 80,151,149,153,148,152,150,154, 53,168,111,179,133,188,156,191,
			  87,180,180,206,180,206,206,207, 49,145,126,158,107,152,141,165, 85,181,189,201,
			 177,199,200,202, 71,179,158,208,153,206,201,209, 88,182,193,209,185,210,211,212,
			  17, 80, 53, 87, 49, 85, 71, 88, 67,151,168,180,145,181,179,182, 51,149,111,180,
			 126,189,158,193, 71,153,179,206,158,201,208,209, 37,148,133,180,107,177,153,185,
			  70,152,188,206,152,199,206,210, 64,150,156,206,141,200,201,211, 72,154,191,207,
			 165,202,209,212, 20, 83, 55, 88, 60, 89, 74, 90, 83,184,170,185,173,186,183,187,
			  55,170,113,182,136,190,160,194, 88,185,182,210,193,211,209,212, 60,173,136,193,
			 117,186,166,197, 89,186,190,211,186,213,211,214, 74,183,160,209,166,211,204,215,
			  90,187,194,212,197,214,215,216,  1, 11,  9, 13,  5, 12, 10, 14, 11, 39, 41, 43,
			  29, 40, 42, 44,  9, 41, 76, 77, 41, 66, 77, 78, 13, 43, 77, 79, 52, 68, 80, 81,
			   5, 29, 41, 52, 24, 35, 47, 58, 12, 40, 66, 68, 35, 63, 67, 69, 10, 42, 77, 80,
			  47, 67, 82, 83, 14, 44, 78, 81, 58, 69, 83, 84,  5, 29, 22, 31, 27, 30, 28, 32,
			  41, 98, 96,100, 95, 99, 97,101, 24, 95, 92,102, 95,109,102,114, 47,123,103,138,
			 143,155,148,161, 29,128, 98,130, 95,129,123,131, 52,130,110,146,143,144,145,147,
			  35,129,105,149,143,168,169,170, 58,131,115,162,171,172,173,174,  5, 41, 24, 47,
			  29, 52, 35, 58, 29, 98, 95,123,128,130,129,131, 22, 96, 92,103, 98,110,105,115,
			  31,100,102,138,130,146,149,162, 27, 95, 95,143, 95,143,143,171, 30, 99,109,155,
			 129,144,168,172, 28, 97,102,148,123,145,169,173, 32,101,114,161,131,147,170,174,
			   7, 42, 25, 48, 30, 53, 36, 59, 42,119, 97,124,129,133,132,134, 25, 97, 93,104,
			  99,111,106,116, 48,124,104,139,144,156,150,163, 30,129, 99,144,109,168,155,172,
			  53,133,111,156,168,188,179,191, 36,132,106,150,155,179,175,183, 59,134,116,163,
			 172,191,183,195,  4, 39, 22, 45, 22, 45, 33, 56, 39,120, 98,121, 98,121,119,122,
			  22, 98, 92,105, 96,110,103,115, 45,121,105,140,110,157,151,164, 22, 98, 96,110,
			  92,105,103,115, 45,121,110,157,105,140,151,164, 33,119,103,151,103,151,176,184,
			  56,122,115,164,115,164,184,196,  6, 40, 23, 46, 28, 51, 34, 57, 43,121,100,125,
			 123,126,124,127, 25, 99, 93,106, 97,111,104,116, 49,126,107,141,145,158,152,165,
			  31,130,100,146,102,149,138,162, 54,135,112,159,169,189,181,192, 37,133,107,153,
			 148,180,177,185, 60,136,117,166,173,193,186,197,  6, 43, 25, 49, 31, 54, 37, 60,
			  40,121, 99,126,130,135,133,136, 23,100, 93,107,100,112,107,117, 46,125,106,141,
			 146,159,153,166, 28,123, 97,145,102,169,148,173, 51,126,111,158,149,189,180,193,
			  34,124,104,152,138,181,177,186, 57,127,116,165,162,192,185,197,  8, 44, 26, 50,
			  32, 55, 38, 61, 44,122,101,127,131,136,134,137, 26,101, 94,108,101,113,108,118,
			  50,127,108,142,147,160,154,167, 32,131,101,147,114,170,161,174, 55,136,113,160,
			 170,190,182,194, 38,134,108,154,161,182,178,187, 61,137,118,167,174,194,187,198,
			   2, 12, 10, 17,  6, 16, 15, 18, 13, 45, 47, 49, 31, 46, 48, 50, 13, 52, 77, 80,
			  43, 68, 79, 81, 19, 54, 82, 85, 54, 73, 85, 86,  7, 30, 42, 53, 25, 36, 48, 59,
			  17, 51, 67, 71, 37, 64, 70, 72, 17, 53, 80, 87, 49, 71, 85, 88, 20, 55, 83, 88,
			  60, 74, 89, 90, 10, 35, 33, 37, 28, 36, 34, 38, 77,105,103,107,102,106,104,108,
			  47,143,103,148,123,155,138,161, 82,169,176,177,169,175,177,178, 42,129,119,133,
			  97,132,124,134, 80,149,151,153,148,150,152,154, 67,168,151,180,145,179,181,182,
			  83,170,184,185,173,183,186,187, 12, 66, 35, 67, 40, 68, 63, 69, 52,110,143,145,
			 130,146,144,147, 45,110,105,151,121,157,140,164, 54,112,169,181,135,159,189,192,
			  30,109,129,168, 99,155,144,172, 53,111,168,179,133,156,188,191, 51,111,149,180,
			 126,158,189,193, 55,113,170,182,136,160,190,194, 17, 67, 37, 70, 51, 71, 64, 72,
			  80,151,148,152,149,153,150,154, 49,145,107,152,126,158,141,165, 85,181,177,199,
			 189,201,200,202, 53,168,133,188,111,179,156,191, 87,180,180,206,180,206,206,207,
			  71,179,153,206,158,208,201,209, 88,182,185,210,193,209,211,212,  6, 40, 28, 51,
			  23, 46, 34, 57, 43,121,123,126,100,125,124,127, 31,130,102,149,100,146,138,162,
			  54,135,169,189,112,159,181,192, 25, 99, 97,111, 93,106,104,116, 49,126,145,158,
			 107,141,152,165, 37,133,148,180,107,153,177,185, 60,136,173,193,117,166,186,197,
			  15, 63, 34, 64, 34, 64, 62, 65, 79,140,138,141,138,141,139,142, 48,144,104,150,
			 124,156,139,163, 85,189,177,200,181,201,199,202, 48,144,124,156,104,150,139,163,
			  85,189,181,201,177,200,199,202, 70,188,152,206,152,206,199,210, 89,190,186,211,
			 186,211,213,214, 16, 68, 36, 71, 46, 73, 64, 74, 68,157,155,158,146,159,156,160,
			  46,146,106,153,125,159,141,166, 73,159,175,201,159,203,201,204, 36,155,132,179,
			 106,175,150,183, 71,158,179,208,153,201,206,209, 64,156,150,206,141,201,200,211,
			  74,160,183,209,166,204,211,215, 18, 69, 38, 72, 57, 74, 65, 75, 81,164,161,165,
			 162,166,163,167, 50,147,108,154,127,160,142,167, 86,192,178,202,192,204,202,205,
			  59,172,134,191,116,183,163,195, 88,193,182,209,185,211,210,212, 72,191,154,207,
			 165,209,202,212, 90,194,187,212,197,215,214,216,  2, 13, 13, 19,  7, 17, 17, 20,
			  12, 45, 52, 54, 30, 51, 53, 55, 10, 47, 77, 82, 42, 67, 80, 83, 17, 49, 80, 85,
			  53, 71, 87, 88,  6, 31, 43, 54, 25, 37, 49, 60, 16, 46, 68, 73, 36, 64, 71, 74,
			  15, 48, 79, 85, 48, 70, 85, 89, 18, 50, 81, 86, 59, 72, 88, 90, 12, 52, 45, 54,
			  30, 53, 51, 55, 66,110,110,112,109,111,111,113, 35,143,105,169,129,168,149,170,
			  67,145,151,181,168,179,180,182, 40,130,121,135, 99,133,126,136, 68,146,157,159,
			 155,156,158,160, 63,144,140,189,144,188,189,190, 69,147,164,192,172,191,193,194,
			  10, 77, 47, 82, 42, 80, 67, 83, 35,105,143,169,129,149,168,170, 33,103,103,176,
			 119,151,151,184, 37,107,148,177,133,153,180,185, 28,102,123,169, 97,148,145,173,
			  36,106,155,175,132,150,179,183, 34,104,138,177,124,152,181,186, 38,108,161,178,
			 134,154,182,187, 17, 80, 49, 85, 53, 87, 71, 88, 67,151,145,181,168,180,179,182,
			  37,148,107,177,133,180,153,185, 70,152,152,199,188,206,206,210, 51,149,126,189,
			 111,180,158,193, 71,153,158,201,179,206,208,209, 64,150,141,200,156,206,201,211,
			  72,154,165,202,191,207,209,212,  6, 43, 31, 54, 25, 49, 37, 60, 40,121,130,135,
			  99,126,133,136, 28,123,102,169, 97,145,148,173, 51,126,149,189,111,158,180,193,
			  23,100,100,112, 93,107,107,117, 46,125,146,159,106,141,153,166, 34,124,138,181,
			 104,152,177,186, 57,127,162,192,116,165,185,197, 16, 68, 46, 73, 36, 71, 64, 74,
			  68,157,146,159,155,158,156,160, 36,155,106,175,132,179,150,183, 71,158,153,201,
			 179,208,206,209, 46,146,125,159,106,153,141,166, 73,159,159,203,175,201,201,204,
			  64,156,141,201,150,206,200,211, 74,160,166,204,183,209,211,215, 15, 79, 48, 85,
			  48, 85, 70, 89, 63,140,144,189,144,189,188,190, 34,138,104,177,124,181,152,186,
			  64,141,150,200,156,201,206,211, 34,138,124,181,104,177,152,186, 64,141,156,201,
			 150,200,206,211, 62,139,139,199,139,199,199,213, 65,142,163,202,163,202,210,214,
			  18, 81, 50, 86, 59, 88, 72, 90, 69,164,147,192,172,193,191,194, 38,161,108,178,
			 134,182,154,187, 72,165,154,202,191,209,207,212, 57,162,127,192,116,185,165,197,
			  74,166,160,204,183,211,209,215, 65,163,142,202,163,210,202,214, 75,167,167,205,
			 195,212,212,216,  3, 14, 14, 20,  8, 18, 18, 21, 14, 56, 58, 60, 32, 57, 59, 61,
			  14, 58, 78, 83, 44, 69, 81, 84, 20, 60, 83, 89, 55, 74, 88, 90,  8, 32, 44, 55,
			  26, 38, 50, 61, 18, 57, 69, 74, 38, 65, 72, 75, 18, 59, 81, 88, 50, 72, 86, 90,
			  21, 61, 84, 90, 61, 75, 90, 91, 14, 58, 56, 60, 32, 59, 57, 61, 78,115,115,117,
			 114,116,116,118, 58,171,115,173,131,172,162,174, 83,173,184,186,170,183,185,187,
			  44,131,122,136,101,134,127,137, 81,162,164,166,161,163,165,167, 69,172,164,193,
			 147,191,192,194, 84,174,196,197,174,195,197,198, 14, 78, 58, 83, 44, 81, 69, 84,
			  58,115,171,173,131,162,172,174, 56,115,115,184,122,164,164,196, 60,117,173,186,
			 136,166,193,197, 32,114,131,170,101,161,147,174, 59,116,172,183,134,163,191,195,
			  57,116,162,185,127,165,192,197, 61,118,174,187,137,167,194,198, 20, 83, 60, 89,
			  55, 88, 74, 90, 83,184,173,186,170,185,183,187, 60,173,117,186,136,193,166,197,
			  89,186,186,213,190,211,211,214, 55,170,136,190,113,182,160,194, 88,185,193,211,
			 182,210,209,212, 74,183,166,211,160,209,204,215, 90,187,197,214,194,212,215,216,
			   8, 44, 32, 55, 26, 50, 38, 61, 44,122,131,136,101,127,134,137, 32,131,114,170,
			 101,147,161,174, 55,136,170,190,113,160,182,194, 26,101,101,113, 94,108,108,118,
			  50,127,147,160,108,142,154,167, 38,134,161,182,108,154,178,187, 61,137,174,194,
			 118,167,187,198, 18, 69, 57, 74, 38, 72, 65, 75, 81,164,162,166,161,165,163,167,
			  59,172,116,183,134,191,163,195, 88,193,185,211,182,209,210,212, 50,147,127,160,
			 108,154,142,167, 86,192,192,204,178,202,202,205, 72,191,165,209,154,207,202,212,
			  90,194,197,215,187,212,214,216, 18, 81, 59, 88, 50, 86, 72, 90, 69,164,172,193,
			 147,192,191,194, 57,162,116,185,127,192,165,197, 74,166,183,211,160,204,209,215,
			  38,161,134,182,108,178,154,187, 72,165,191,209,154,202,207,212, 65,163,163,210,
			 142,202,202,214, 75,167,195,212,167,205,212,216, 21, 84, 61, 90, 61, 90, 75, 91,
			  84,196,174,197,174,197,195,198, 61,174,118,187,137,194,167,198, 90,197,187,214,
			 194,215,212,216, 61,174,137,194,118,187,167,198, 90,197,194,215,187,214,212,216,
			  75,195,167,212,167,212,205,216, 91,198,198,216,198,216,216,217
			 };
	static int arr_idx[] = {
			  0, 8, 64, 512, 1, 0, 128, 1024, 2, 16, 0, 2048, 4, 32, 256, 0 };

	
	Random randomGenerator = new Random();

	public Integer searchLapka() {
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		VertexLayerParameters<V> selectedVertexLayer = new VertexLayerParameters<>();
				
		// Choose a layer of vertices taking into account the probabilities of layers selection
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			borderOfProbability += vertexLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedVertexLayer = vertexLayer.getValue();
				break;
			}
		}
		
		V v1 = selectedVertexLayer.getVerticies().get(randomGenerator.nextInt(selectedVertexLayer.getVerticies().size()));
	V v2,v3,v4;	
	//do
	{
		// Choose 3 successors of the vertex randomly
		List<V> v1List = new LinkedList<>(graph.getNeighbors(v1));
		int randomIntValue = randomGenerator.nextInt(v1List.size());
		v2 = v1List.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(v1List.size());
		v3 = v1List.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(v1List.size());
		v4 = v1List.remove(randomIntValue);
	}
		/*while (!graph.isNeighbor(v2,v3) &&
			!graph.isNeighbor(v2,v4) &&
			!graph.isNeighbor(v3,v4));*/
		{

			V[] vert=(V[])new Object[4];
			vert[0]=v1;	vert[1]=v2;vert[2]=v3;vert[3]=v4;
			int code=0;
			for (int i = 0; i < vert.length-1; i++) {
				for (int j = i+1; j < vert.length; j++) {
					E o1 = graph.findEdge(vert[i], vert[j]);
					if(o1!=null)code|=arr_idx[4*i+j];
					E o2 = graph.findEdge(vert[j], vert[i]);
					if(o2!=null)code|=arr_idx[4*j+i];
				}
			}
			return arrcode[code];		} 
			
		
	}
	
	public void searchLapka2(Boolean x) {
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		VertexLayerParameters<V> selectedVertexLayer = new VertexLayerParameters<>();
				
		// Choose a layer of vertices taking into account the probabilities of layers selection
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			borderOfProbability += vertexLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedVertexLayer = vertexLayer.getValue();
				break;
			}
		}
		
		V v1 = selectedVertexLayer.getVerticies().get(randomGenerator.nextInt(selectedVertexLayer.getVerticies().size()));
		
		// Choose 3 successors of the vertex randomly
		List<V> v1List = graph.getModifiedNeighbors(v1);// graph.getModifiedNeighbors(v1);

		//int num = v1List.getNeighborCount(v1);	
		
		int i1=randomGenerator.nextInt(v1List.size());
		int i2=i1;
		do{
			i2=randomGenerator.nextInt(v1List.size());

		}
		while(i2==i1);
		int i3=i1;
		do{
			i3=randomGenerator.nextInt(v1List.size());
		}
		while(i3==i1||i3==i2);
		
		
		V v2 = v1List.get(i1);
		V v3 = v1List.get(i2);
		V v4 = v1List.get(i3);
		

		
		/*if (!graph.isNeighbor(v2, v3)&&
			!graph.isNeighbor(v2, v4) &&
			!graph.isNeighbor(v3, v4)) */{

			V[] vert=(V[])new Object[4];
			vert[0]=v1;	vert[1]=v2;vert[2]=v3;vert[3]=v4;
			int code=0;
			for (int i = 0; i < vert.length-1; i++) {
				for (int j = i+1; j < vert.length; j++) {
					E o1 = graph.findEdge(vert[i], vert[j]);
					if(o1!=null)code|=arr_idx[4*i+j];
					E o2 = graph.findEdge(vert[j], vert[i]);
					if(o2!=null)code|=arr_idx[4*j+i];
				}
			}
			int num =arrcode[code];
			synchronized (motifsLapka) {
				motifsLapka[num]=motifsLapka[num]+1.;
				
			}
			return ;		} 
			/*else {
				return ;
		}*/
		
	}
	public int searchScobka() {
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		EdgeLayerParameters<E> selectedEdgeLayer = null;
		
		// Choose a layer of edges taking into account the probabilities of layers selection
		for (Entry<Integer, EdgeLayerParameters<E>> edgeLayer : edgeLayers.entrySet()) {
			if(edgeLayer.getValue().getProbability()<0)
				{
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
		List<V> neigbours1 = new LinkedList<V>(graph.getNeighbors(v1));
		List<V> neigbours2 = new LinkedList<V>(graph.getNeighbors(v2));
		neigbours1.remove(v2);
		neigbours2.remove(v1);
		
		// Choose 2 successors of the endpoints randomly
		V v3 = neigbours1.get(randomGenerator.nextInt(neigbours1.size()));
		V v4 = neigbours2.get(randomGenerator.nextInt(neigbours2.size()));
		
		V[] vert=(V[])new Object[4];
		vert[0]=v1;	vert[1]=v2;vert[2]=v3;vert[3]=v4;
		int code=0;
		for (int i = 0; i < vert.length-1; i++) {
			for (int j = i+1; j < vert.length; j++) {
				E o1 = graph.findEdge(vert[i], vert[j]);
				if(o1!=null)code|=arr_idx[4*i+j];
				E o2 = graph.findEdge(vert[j], vert[i]);
				if(o2!=null)code|=arr_idx[4*j+i];
			}
		}
		return arrcode[code];
	}
	
	public void searchScobka2(Boolean c) {
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		EdgeLayerParameters<E> selectedEdgeLayer = null;
		
		// Choose a layer of edges taking into account the probabilities of layers selection
		for (Entry<Integer, EdgeLayerParameters<E>> edgeLayer : edgeLayers.entrySet()) {
			if(edgeLayer.getValue().getProbability()<0)
				{
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
		List<V> neigbours1 = new LinkedList<V>(graph.getNeighbors(v1));
		List<V> neigbours2 = new LinkedList<V>(graph.getNeighbors(v2));
		neigbours1.remove(v2);
		neigbours2.remove(v1);
		
		// Choose 2 successors of the endpoints randomly
		V v3 = neigbours1.get(randomGenerator.nextInt(neigbours1.size()));
		V v4 = neigbours2.get(randomGenerator.nextInt(neigbours2.size()));
		
		V[] vert=(V[])new Object[4];
		vert[0]=v1;	vert[1]=v2;vert[2]=v3;vert[3]=v4;
		int code=0;
		for (int i = 0; i < vert.length-1; i++) {
			for (int j = i+1; j < vert.length; j++) {
				E o1 = graph.findEdge(vert[i], vert[j]);
				if(o1!=null)code|=arr_idx[4*i+j];
				E o2 = graph.findEdge(vert[j], vert[i]);
				if(o2!=null)code|=arr_idx[4*j+i];
			}
		}
		int num =arrcode[code];
		synchronized (this) {
			motifsScobka[num]=motifsScobka[num]+1.;

		}

		return;
	}
	/**
	 * Constructs and initializes the class.
	 *
	 * @author Yudina Maria, Yudin Evgeniy
	 * @param graph
	 *            the graph
	 * @param numberOfRuns
	 *            number of runs of sampling algorithm
	 */
	public RandMSF4Dir1(Graph<V, E> graph, int numberOfRuns, boolean isParallel) {
		this.graph =  (MyDirectedSparseGraph)graph;
		this.numberOfRuns = numberOfRuns;
		this.isParallel =isParallel;

	}

	/**
	 * 
	 * @author Yudina Maria, Yudin Evgeniy
	 * @throws UnsupportedEdgeTypeException
	 */
	public void execute() throws GraphStatsException {
		Collection<V> vertices = graph.getVertices();
		vertexLayers = new HashMap<>();
		int neibours;
		long t=System.currentTimeMillis();
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
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			numberOfCarcasLapka += vertexLayer.getValue().vertices.size() * vertexLayer.getKey()
					* (vertexLayer.getKey() - 1l) * (vertexLayer.getKey() - 2l) / 6l;
		}

		// Calculate probability of selection for each layer of the vertices
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
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

		System.out.println("vertexLayers.size():"+vertexLayers.size());
		System.out.println("edgeLayers.size():"+edgeLayers.size());
		
		t=System.currentTimeMillis();

		
		
		numberOfRunsLapka = numberOfRuns;
		numberOfRunsScoba = numberOfRuns;


		//System.out.println("after preparing:"+(t-System.currentTimeMillis())/1000);
		List<Integer> resultsOfRunsLapka =null;
		//Map<Object, Long> countsLapka = Stream.iterate(0, i -> i).limit((int)numberOfRunsLapka).parallel().map(x -> this.searchLapka()).collect(Collectors.groupingBy( e ->  e, Collectors.counting()));

		
		//System.out.println("after  Lapka:"+(t-System.currentTimeMillis())/1000);
		List<Integer> resultsOfRunsScobka = null;
		//System.out.println("after Scobka:"+(t-System.currentTimeMillis())/1000);

		
		if(isParallel){
			resultsOfRunsLapka= Stream.iterate(0, i -> i).limit((int)numberOfRunsLapka).parallel().map(x -> this.searchLapka()).collect(Collectors.toList());
			resultsOfRunsScobka=Stream.iterate(0, i -> i).limit((int)numberOfRunsScoba).parallel().map(x -> this.searchScobka()).collect(Collectors.toList());
			}
		else
		{
			resultsOfRunsLapka= Stream.iterate(0, i -> i).limit((int)numberOfRunsLapka).sequential().map(x -> this.searchLapka()).collect(Collectors.toList());		
			resultsOfRunsScobka=  Stream.iterate(0, i -> i).limit((int)numberOfRunsScoba).sequential().map(x -> this.searchScobka()).collect(Collectors.toList());

		}
		
		
		for (int i = 0; i < motifsScobka.length; i++) {
			{
				final int num = i;
				{
					if(isParallel){
						motifsScobka[num] = (resultsOfRunsScobka.stream().parallel().filter(x -> x == num).count());
					}
					else{
						motifsScobka[num] = (resultsOfRunsScobka.stream().filter(x -> x == num).count());
					}
				}
			}
		}

		for (int i = 0; i < motifsLapka.length; i++) {
			{
				final int num = i;
				{
					if(isParallel){
						motifsLapka[num] = (resultsOfRunsLapka.stream().parallel().filter(x -> x == num).count());
					}else{
						motifsLapka[num] = (resultsOfRunsLapka.stream().filter(x -> x == num).count());
					}
				}
			}
		}

		
		for (int i = 0; i < motifsLapka.length; i++) 
			{
			motifs_long[i]=motifsLapka[i]+motifsScobka[i];

			}
				
		double D1,D2,lyamda,D,Sigma;
		//System.out.println("numberOfCarcasScobka:"+numberOfCarcasScobka);
		//System.out.println("numberOfCarcasLapka:"+numberOfCarcasLapka);

		for (int i = 0; i < motifs.length; i++) {
			if(massKoefL[i]==0) continue;
			//if(massKoefR[i]==0) continue;

			D1 = (numberOfCarcasScobka/numberOfRunsScoba) * (numberOfCarcasScobka / numberOfRunsScoba)
					* motifsScobka[i] / massKoefL[i]
					* (1. - motifsScobka[i] / (massKoefL[i]) / numberOfRunsScoba);
			D2 = (numberOfCarcasLapka/numberOfRunsLapka) * (numberOfCarcasLapka / numberOfRunsLapka)
					* (motifsLapka[i] / massKoefR[i])
					* (1 - motifsLapka[i] / (massKoefR[i]) / numberOfRunsLapka);

			double n1 = motifsScobka[i] * (numberOfCarcasScobka / massKoefL[i]/ numberOfRunsScoba);
			double n2 = motifsLapka[i] * (numberOfCarcasLapka / massKoefR[i] /numberOfRunsLapka);

			lyamda = 0;
			if(n1<-0.1||n2<-0.1) throw new ArithmeticException();
			
			if (n2>= 0.1 && n1 >=0.1) {
				lyamda = D1 * n2 / (D1 * n2 + D2 * n1);
				D = (1 - lyamda) * (1 - lyamda) * D1 + lyamda * lyamda * D2;
				Sigma = Math.sqrt(D);
				sigmas[i] = Sigma;
				motifs[i] = (n1 + lyamda * (n2 - n1));
			} 
			else if (n1< 0.1 && n2 >=0.1) {
				D = D2;
				Sigma = Math.sqrt(D);
				sigmas[i] = Sigma;
				motifs[i] = n2;
			}
			else if(n1>= 0.1 && n2 <=0.1) {
				// System.out.println("����� ���� �� ������");
				D = D1;
				Sigma = Math.sqrt(D);
				sigmas[i] = Sigma;
				motifs[i] = n1;
			}
		}
		//System.out.println("after Real All:"+(t-System.currentTimeMillis())/1000);


	}

	public String toStringWithSigma() {
		String str = "";
		for (int i = 0; i < motifs.length; i++) {
			//if(motifs[i]!=0)
			str = str+ motifs[i]+ "\n";
		}
		str =str+ "3 sigma \n";
		for (int i = 0; i < motifs.length; i++) {
			//if(motifs[i]!=0)
			str =str+3*sigmas[i]+ "\n";
		}
		
		return str;
		
	}
	
	
	@Override
	public String toString() {
		/*String str = ""+this.getClass().getSimpleName()+"\n";
		str = str+"V:"+graph.getVertexCount()+" E:"+ graph.getEdgeCount()+"\n";

		for (int i = 0; i < motifs.length; i++) {
			str = str + motifs[i] +"\n";
			
		}*/
		
		String str = "";

		/*for (int i = 0; i < motifs.length; i++) {
		//	if(motifs[i]>0.10)
		//	str = str+ (motifs[i]-3.*sigmas[i])+ " " + motifs[i]+" " + (motifs[i]+3.*sigmas[i])+ "\n";
				str = str+ motifs[i]+" " + 3.*sigmas[i]+ "\n";
		}
		str=str+"======================= i =====================================\n";*/
		for (int i = 0; i < motifs.length; i++) {
			if(motifs[i]>0.10)
		//	str = str+ (motifs[i]-3.*sigmas[i])+ " " + motifs[i]+" " + (motifs[i]+3.*sigmas[i])+ "\n";
				str = str+ motifs[i]+" \n";
		}


		System.out.println("---------jjjjjjjjjjjjjj----------------------------------");
		for (int i = 0; i < motifsLapka.length; i++) 
			{
			System.out.println(""+(motifs_long[i]));

			}
		
		return str;
	}

}