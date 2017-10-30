package weka.classifiers.trees.gsgp.tree;

public class TreeCreationHandler {
	public static Tree create(String [] op, String [] term, double t_rate, int depth, double[][] data, double train_p, double [] target) {
		return new Tree(op,term,t_rate,depth,data,train_p,target);
	}
}
