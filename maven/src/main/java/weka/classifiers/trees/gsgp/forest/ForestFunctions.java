package weka.classifiers.trees.gsgp.forest;

import weka.classifiers.trees.gsgp.tree.Tree;

public class ForestFunctions {
	public static int improvingThreshold = 5; // percentage
	
	/**
	 * Return true if the classifier is improving
	 * It's considered to be improving if at least 5% of
	 * the descendents have a better rmse than the parent
	 * @return
	 */
	static boolean improving(Tree [] descendentes, Tree parent) {		
		int count = 0;
		for(int i = 0; i < descendentes.length; i++){
			if(fitnessTrain(descendentes[i])< fitnessTrain(parent))
				count++;
		}
		double result = (count*100.0/descendentes.length);
		
		return result > improvingThreshold;
	}
	
	/**
	 * returns the train rmse of t
	 * @param t
	 * @return
	 */
	static double fitnessTrain(Tree t){
		return t.getTrainRMSE(null,null,0);
	}
	
	/**
	 * returns the test rmse of t
	 * @param t
	 * @return
	 */
	static double fitnessTest(Tree t){
		return t.getTestRMSE(null,null,0);
	}
}
