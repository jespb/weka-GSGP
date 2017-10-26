package weka.classifiers.trees.gsgp.tree;

import weka.classifiers.trees.gsgp.node.Node;
import weka.classifiers.trees.gsgp.util.Arrays;

/**
 * 
 * @author João Batista, jbatista@di.fc.ul.pt
 *
 */
public class TreeOperations {
	/**
	 * Alerts t applying the sigmoid function in its base
	 * @param t tree
	 */
	public static void sigmoid(Tree t){
		Node n = t.getHead();
		Node neg = new Node(new Node("-1"),n, "*");
		Node e = new Node(new Node("e"), neg, "^");
		Node div = new Node(new Node("1"), e, "+");
		t.setHead(new Node(new Node("1"), div, "/"));

		double [] tr = t.getTrainSemantic();
		double [] neg_tr = Arrays.operation(Arrays.array(-1, tr.length), tr , "*");
		double [] e_tr = Arrays.operation(Arrays.array(Math.E, tr.length), neg_tr , "^");
		double [] div_tr = Arrays.operation(Arrays.array(1, tr.length), e_tr , "+");
		double [] n_tr = Arrays.operation(Arrays.array(1, tr.length), div_tr , "/");

		double [] te = t.getTestSemantic();
		double [] neg_te = Arrays.operation(Arrays.array(-1, te.length), te , "*");
		double [] e_te = Arrays.operation(Arrays.array(Math.E, te.length), neg_te , "^");
		double [] div_te = Arrays.operation(Arrays.array(1, te.length), e_te , "+");
		double [] n_te = Arrays.operation(Arrays.array(1, te.length), div_te , "/");

		Arrays.copy(n_tr, tr);
		Arrays.copy(n_te, te);
	}
	
	/**
	 * Creates the semantic of a tree
	 */
	public static double[] semantic(Tree t, double [][] data, double perc){
		Node n = t.getHead();
		int size = (int) (perc > 0 ? data.length*perc : data.length + (int)(data.length*perc));
		double [] d = new double[size];
		for(int i = 0; i < d.length; i++){
			d[i] = n.calculate(data[(int) (i - (perc < 0?data.length*perc:0))]);
		}
		return d;
	}
}