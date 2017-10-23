package weka.classifiers.trees.gsgp.tree;

import java.io.Serializable;

import weka.classifiers.trees.gsgp.node.Node;

/**
 * 
 * @author João Batista, jbatista@di.fc.ul.pt
 *
 */
public interface Tree extends Serializable{
	/**
	 * Returns the Tree head
	 * @return
	 */
	public Node getHead();
	
	/**
	 * Returns the Tree name
	 * @return
	 */
	public double getId();
	
	/**
	 * Sets the Tree head to n
	 * @param n
	 */
	public void setHead(Node n);
	
	/**
	 * Returns the Tree RMSE on the train data
	 * @param data
	 * @param target
	 * @param trainPercentage
	 * @return
	 */
	public double getTrainRMSE(double [][] data, double [] target, double trainPercentage);
	
	/**
	 * Returns the Tree RMSE on the test data
	 * @param data
	 * @param target
	 * @param trainPercentage
	 * @return
	 */
	public double getTestRMSE(double [][] data, double [] target, double trainPercentage);
}
