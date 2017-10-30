package weka.classifiers.trees.gsgp.tree;

import weka.classifiers.trees.gsgp.util.Arrays;

/**
 * 
 * @author João Batista, jbatista@di.fc.ul.pt
 *
 */
public class TreeMutationHandler {
	
	/**
	 * This method creates a tree using mutation
	 * @param original original tree
	 * @param tr1 normalized tree
	 * @param tr2 normalized tree
	 * @param ms mutation step
	 * @param target target values of the dataset
	 * @param id new tree id
	 * @return descendent of original through mutation
	 */
	public static Tree mutation(Tree original, Tree tr1, Tree tr2,double ms,double [] target){
		if(ms < 0) ms = Math.random()*-ms;

		double [] parentsIds = new double[] {original.getId(), tr1.getId(), tr2.getId(), ms};
		
		//semantica treino
		double [] d_tr = mutationStep(original.getTrainSemantic(), tr1.getTrainSemantic(),
				tr2.getTrainSemantic(), ms);

		//semantica teste
		double [] d_te = mutationStep(original.getTestSemantic(), tr1.getTestSemantic(),
				tr2.getTestSemantic(), ms);
		
		double train_rmse = Arrays.rmse(d_tr, target,0);
		double test_rmse = Arrays.rmse(d_te, target,d_tr.length);

		return new Tree(parentsIds, null, d_tr, d_te, train_rmse, test_rmse);
	}
	
	/**
	 * Creates an array using the mutation operations
	 * @param original semantic of the original tree
	 * @param r1 semantic of the normalized tree
	 * @param r2 semantic of the normalized tree
	 * @param ms mutation step
	 * @return return the calculated semantic for the new tree
	 */
	private static double[] mutationStep(double [] original, double [] r1, double [] r2, double ms) {
		double [] rrd_tr = Arrays.operation(r1, r2, "-");
		double [] rd_tr = Arrays.operation(Arrays.array(ms, rrd_tr.length), rrd_tr, "*");
		return Arrays.operation(original, rd_tr, "+");	
	}
}