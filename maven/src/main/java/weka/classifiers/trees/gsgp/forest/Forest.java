package weka.classifiers.trees.gsgp.forest;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author João Batista, jbatista@di.fc.ul.pt
 *
 */
public interface Forest extends Serializable {
	
	/**
	 * Trains the forest
	 * @throws IOException 
	 */
	public ArrayList<Double>[] train() throws IOException;
	
	/**
	 * Predicts a result given a data rown
	 * @param data data row
	 * @requires isTrained()
	 */
	public double predict(double [] data);
}
