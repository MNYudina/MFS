package io.github.mnyudina.motifs.algorithms;

import io.github.mnyudina.motifs.exception.GraphStatsException;

/**
 * The interface specifies methods that each operation working with a graph
 * must implement.
 * 
 * @author Gleepa
 */
public interface GraphStatsOperation {
	
	/**
	 * @author Gleepa
	 */
	void execute() throws GraphStatsException;

}