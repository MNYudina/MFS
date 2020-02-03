package io.github.mnyudina.motifs.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gleepa
 */
public class ProgramParameters {

    private Map<String, Boolean> operations;
    
    private String graphFile;

    private int numberOfRuns;
    
    public ProgramParameters() {
    	operations = new HashMap<String, Boolean>();
    	operations.put("isThreeSizeSubgraphsCountFullEnumerationRequested", false);
    	operations.put("isThreeSizeSubgraphsCountSamplingRequested", false);
    	operations.put("isFourSizeSubgraphsCountFullEnumerationRequested", false);
    	operations.put("isFourSizeSubgraphsCountSamplingRequested", false);
	}
   

    public boolean getIsThreeSizeSubgraphsCountFullEnumerationRequestedFlag() {
    	return operations.get("isThreeSizeSubgraphsCountFullEnumerationRequested");
    }
    
    public void setIsThreeSizeSubgraphsCountFullEnumerationRequestedFlag() {
    	operations.put("isThreeSizeSubgraphsCountFullEnumerationRequested", true);
    }
    
    public boolean getIsThreeSizeSubgraphsCountSamplingRequestedFlag() {
    	return operations.get("isThreeSizeSubgraphsCountSamplingRequested");
    }
    
    public void setIsThreeSizeSubgraphsCountSamplingRequestedFlag() {
    	operations.put("isThreeSizeSubgraphsCountSamplingRequested", true);
    }
    
    public boolean getIsFourSizeSubgraphsCountFullEnumerationRequestedFlag() {
    	return operations.get("isFourSizeSubgraphsCountFullEnumerationRequested");
    }
    
    public void setIsFourSizeSubgraphsCountFullEnumerationRequestedFlag() {
    	operations.put("isFourSizeSubgraphsCountFullEnumerationRequested", true);
    }
    
    public boolean getIsFourSizeSubgraphsCountSamplingRequestedFlag() {
    	return operations.get("isFourSizeSubgraphsCountSamplingRequested");
    }
    
    public void setIsFourSizeSubgraphsCountSamplingRequestedFlag() {
    	operations.put("isFourSizeSubgraphsCountSamplingRequested", true);
    }
	
	public String getGraphFile() {
        return graphFile;
    }

    public void setGraphFile(String graphFile) {
        this.graphFile = graphFile;
    }
    
    public int getNumberOfRuns() {
		return numberOfRuns;
	}
    
    public void setNumberOfRuns(int numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
    }


}