package weka.classifiers.trees.gsgp.forest;

import java.io.IOException;
import java.util.ArrayList;

import weka.classifiers.trees.gsgp.client.ClientWekaSim;
import weka.classifiers.trees.gsgp.tree.Tree;
import weka.classifiers.trees.gsgp.tree.TreeGSGP;
import weka.classifiers.trees.gsgp.tree.TreeGSGPMutationHandler;
import weka.classifiers.trees.gsgp.tree.TreeGSGPOperations;
import weka.classifiers.trees.gsgp.util.Mat;

/**
 * 
 * @author Jo�o Batista, jbatista@di.fc.ul.pt
 *
 */
public class ForestGSGP implements Forest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean firstGen = true;
	
	private int idGen=0;
	
	private boolean messages = true;

	TreeGSGP tree;
	TreeGSGP oldTree;
	TreeGSGP ancient;
	TreeGSGP globalBest;
	TreeGSGP [] trn;
	TreeGSGP [] descendentes;

	double[] target;
	double ms;
	double train_perc;

	int generation = 0;
	int maxGeneration = 2000;

	/**
	 * Construtor
	 * @param filename
	 * @param op
	 * @param term
	 * @param max_depth
	 * @param ms
	 * @param t_rate
	 * @param data
	 * @param target
	 * @param pop_size
	 * @param train_perc
	 * @param classification
	 * @throws IOException
	 */
	public ForestGSGP(String filename, String [] op, String [] term, int max_depth, double ms, 
			double [][] data, double [] target, int pop_size, double train_perc,
			int maxGeneration) throws IOException{
		message("Creating forest...");
		this.target = target;
		this.ms = ms;
		this.train_perc = train_perc;
		this.maxGeneration = maxGeneration;

		trn = new TreeGSGP[pop_size];
		for(int i = 0; i < pop_size; i++){
			trn[i] = new TreeGSGP(op, term, 0.1,max_depth, data,train_perc,target,idGen++);
			TreeGSGPOperations.sigmoid(trn[i]);
		}
		
		tree = new TreeGSGP(op, term, 0.1,max_depth, data,train_perc,target, idGen++);
		TreeGSGP candidate;
		for(int i = 0; i < pop_size; i++){
			candidate = new TreeGSGP(op, term, 0.1,max_depth, data,train_perc,target, idGen++);
			if(rmseTrain(candidate)< rmseTrain(tree))
				tree = candidate;
		}
		ancient = tree;
	}
	
	/**
	 * Makes a new generation
	 * @return
	 */
	public double[] nextGeneration(){
		descendentes = new TreeGSGP [trn.length];
		
		TreeGSGP tr1,tr2, best = null;
		for(int i = 0; i < descendentes.length; i++){
			tr1 = trn[Mat.random(trn.length)];
			tr2 = trn[Mat.random(trn.length)];
			descendentes[i] = TreeGSGPMutationHandler.mutation(tree,tr1,tr2,ms,target, idGen++);
			if(best == null || rmseTrain(descendentes[i]) < rmseTrain(best)){
				best = descendentes[i];
			}
		}
		
		oldTree = tree;
		tree = best;
		ancient.addParents(tree.getParentsId());
		
		if(globalBest == null || rmseTest(best)< rmseTest(globalBest)){
			globalBest = ancient.clone();
			globalBest.setTestRMSE(rmseTest(best));
			globalBest.setTrainRMSE(rmseTrain(best));
		}
		
		return new double[] {rmseTrain(best), rmseTest(best)};
	}
	
	/**
	 * returns the train rmse of t
	 * @param t
	 * @return
	 */
	private double rmseTrain(Tree t){
		return t.getTrainRMSE(null,null,0);
	}
	
	/**
	 * returns the test rmse of t
	 * @param t
	 * @return
	 */
	private double rmseTest(Tree t){
		return t.getTestRMSE(null,null,0);
	}

	/**
	 * Return true if the classifier is improving
	 * It's considered to be improving if at least 5% of
	 * the descendents have a better rmse than the parent
	 * @return
	 */
	private boolean improving() {
		if(firstGen){
			firstGen = false;
			return true;
		}
		if(generation > maxGeneration) return false;
		
		int count = 0;
		for(int i = 0; i < descendentes.length; i++){
			if(rmseTrain(descendentes[i])< rmseTrain(oldTree))
				count++;
		}
		double result = (count*100.0/descendentes.length);
		
		return result > 5;
	}

	/**
	 * Trains the classifier
	 */
	public ArrayList<Double>[] train(){
		message("Starting train...");
		
		generation = 0;
		while(improving()){
			double [] result = nextGeneration();
			//TODO delete
			//ClientWekaSim.results[generation][0]+=result[0];
			//ClientWekaSim.results[generation][1]+=result[1];
			//ClientWekaSim.results[generation][2]++;
			generation ++;
		}
		
		System.out.println("Done in " + generation + " generations.");
		return null;
	}

	/**
	 * Makes a prediction
	 */
	public double predict(double[] data) {
		double acc = globalBest.getHead().calculate(data);
		ArrayList<Double> trace = globalBest.getGeneologicalTree();
		ArrayList<Double> randoms = new ArrayList<Double>();
		
		for(int i = 0; i<trn.length; i++){
			randoms.add(trn[i].getHead().calculate(data));
		}
		
		for(int i = 0; i+2 < trace.size(); i+=3){
			acc += trace.get(i+2) * (randoms.get(trace.get(i).intValue())-randoms.get(trace.get(i+1).intValue()));
		}

		return acc;
	}

	/**
	 * Prints a message if messages==true
	 * @param s
	 */
	private void message(String s){
		if(messages)
			System.out.println(s);
	}
	
	/**
	 * Returns the string representation of the classifier
	 */
	public String toString(){
		return "This class has no String representation.";
	}
}