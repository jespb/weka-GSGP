package weka.classifiers.trees.gsgp.tree;

import java.io.Serializable;
import java.util.ArrayList;

import weka.classifiers.trees.gsgp.node.Node;
import weka.classifiers.trees.gsgp.util.Arrays;

/**
 * 
 * @author João Batista, jbatista@di.fc.ul.pt
 *
 */
public class Tree implements Serializable{
	/**
	 * 
	 */
	private static int idGen = 0;
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Double> geneologicTree;
	
	private double[] parentsId = null;
	private int id;
	
	private Node head;
	
	private double[] train_semantic;
	private double[] test_semantic;
	
	private double train_rmse;
	private double test_rmse;
	
	/**
	 * Constructor
	 * @param name
	 * @param origin
	 * @param parents
	 * @param head
	 * @param train_semantic
	 * @param test_semantic
	 * @param train_rmse
	 * @param test_rmse
	 */
	public Tree(double [] parentsIds, Node head,double [] train_semantic, 
			double [] test_semantic,double train_rmse, double test_rmse){
		this.head = head;
		this.train_semantic = train_semantic;
		this.test_semantic = test_semantic;
		this.train_rmse = train_rmse;
		this.test_rmse = test_rmse;
		this.parentsId = parentsIds;
		this.id = idGen++;
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param op
	 * @param term
	 * @param t_rate
	 * @param depth
	 * @param data
	 * @param train_p
	 * @param target
	 */
	public Tree(String [] op, String [] term, double t_rate, int depth, double[][] data, double train_p, double [] target){
		
		head = new Node(op, term, t_rate,0, depth);
		train_semantic = TreeOperations.semantic(this,data,train_p);
		test_semantic = TreeOperations.semantic(this,data,-train_p);
		
		train_rmse = Arrays.rmse(train_semantic, target,0);
		test_rmse = Arrays.rmse(test_semantic, target,train_semantic.length);
		
		this.id = idGen++;
		
		geneologicTree = new ArrayList<Double>();
	}

	/**
	 * Empty TreeGSGP constructor
	 */
	public Tree() {}

	/**
	 * Returns the TreeGSGP head
	 */
	public Node getHead() {
		return head;
	}
	
	/**
	 * Returns the TreeGSGP train semantic
	 * @return
	 */
	public double[] getTrainSemantic(){
		return train_semantic;
	}

	/**
	 * Returns the TreeGSGP test semantic
	 * @return
	 */
	public double[] getTestSemantic(){
		return test_semantic;
	}
	
	/**
	 * Returns the TreeGSGP test RMSE
	 * @return
	 */
	public double getTestRMSE(double [][] data, double [] target, double trainPercentage){
		return test_rmse;
	}

	/**
	 * Returns the TreeGSGP train RMSE
	 * @return
	 */
	public double getTrainRMSE(double [][] data, double [] target, double trainPercentage){
		return train_rmse;
	}

	/**
	 * Sets the TreeGSGP head
	 * @param n 
	 */
	public void setHead(Node n) {
		head = n;
	}

	/**
	 * Returns the Tree's ID
	 */
	public double getId() {
		return id;
	}

	/**
	 * Returns the Tree's parents's IDs
	 * @return
	 */
	public double[] getParentsId() {
		return parentsId;
	}

	/**
	 * Adds parents to a tree
	 * @param parentsId
	 */
	public void addParents(double[] parentsId) { // pai, tr1, tr2, ms
		geneologicTree.add(parentsId[1]);
		geneologicTree.add(parentsId[2]);
		geneologicTree.add(parentsId[3]);
	}

	/**
	 *Return the geneological tree of this Individual
	 * @return
	 */
	public ArrayList<Double> getGeneologicalTree() {
		return geneologicTree;
	}
	
	/**
	 * Clones a tree
	 */
	public Tree clone(){
		Tree ret = new Tree();
		ret.parentsId = parentsId;
		ret.id = id;
		ret.head = head;
		ret.train_semantic = train_semantic;
		ret.test_semantic = test_semantic;
		ret.train_rmse = train_rmse;
		ret.test_rmse = test_rmse;
		ret.geneologicTree = (ArrayList<Double>) geneologicTree.clone();
		return ret;
	}

	/**
	 * Sets the tree test rmse
	 * @param rmseTest
	 */
	public void setTestRMSE(double rmseTest) {
		test_rmse = rmseTest;
	}

	/**
	 * Sets the tree train rmse
	 * @param rmseTrain
	 */
	public void setTrainRMSE(double rmseTrain) {
		train_rmse = rmseTrain;
	}
}