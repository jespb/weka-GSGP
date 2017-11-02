package weka.classifiers.trees.gsgp.forest;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import weka.classifiers.trees.gsgp.tree.Tree;
import weka.classifiers.trees.gsgp.forest.ForestFunctions;
import weka.classifiers.trees.gsgp.tree.TreeMutationHandler;
import weka.classifiers.trees.gsgp.tree.TreeCreationHandler;
import weka.classifiers.trees.gsgp.tree.TreeOperations;
import weka.classifiers.trees.gsgp.util.Mat;

/**
 * 
 * @author João Batista, jbatista@di.fc.ul.pt
 *
 */
public class Forest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean firstGen = true;
	
	private boolean messages = true;

	private Tree tree;
	private Tree oldTree;
	private Tree ancient;
	private Tree globalBest;
	private Tree [] trn;
	private Tree [] descendentes;

	private double[] target;
	private double ms;

	private int generation = 0;

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
	public Forest(String [] op, String [] term, int max_depth, double ms, 
			double [][] data, double [] target, int pop_size, double train_perc
			) throws IOException{
		message("Creating forest...");
		this.target = target;
		this.ms = ms;

		trn = new Tree[pop_size];
		for(int i = 0; i < pop_size; i++){
			trn[i] = TreeCreationHandler.create(op, term, 0.1,max_depth, data,train_perc,target);
			TreeOperations.sigmoid(trn[i]);
		}
		
		tree = TreeCreationHandler.create(op, term, 0.1,max_depth, data,train_perc,target);
		Tree candidate;
		for(int i = 0; i < pop_size; i++){
			candidate = TreeCreationHandler.create(op, term, 0.1,max_depth, data,train_perc,target);
			if(fitnessTrain(candidate)< fitnessTrain(tree))
				tree = candidate;
		}
		ancient = tree;
	}

	/**
	 * Makes a new generation
	 * @return
	 */
	public double[] nextGeneration(){
		descendentes = new Tree [trn.length];
		
		Tree tr1,tr2, best = null;
		for(int i = 0; i < descendentes.length; i++){
			tr1 = trn[Mat.random(trn.length)];
			tr2 = trn[Mat.random(trn.length)];
			descendentes[i] = TreeMutationHandler.mutation(tree,tr1,tr2,ms,target);
			if(best == null || fitnessTrain(descendentes[i]) < fitnessTrain(best)){
				best = descendentes[i];
			}
		}
		
		oldTree = tree;
		tree = best;
		ancient.addParents(tree.getParentsId());
		
		if(globalBest == null || fitnessTest(best)< fitnessTest(globalBest)){
			globalBest = ancient.clone();
			globalBest.setTestRMSE(fitnessTest(best));
			globalBest.setTrainRMSE(fitnessTrain(best));
		}
		
		return new double[] {fitnessTrain(best), fitnessTest(best)};
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
	 * Return true if the classifier is improving
	 * It's considered to be improving if at least 5% of
	 * the descendents have a better rmse than the parent
	 * @return
	 */
	private boolean improving() {
		if(firstGen) {
			firstGen=false;
			return true;
		}
		boolean improving = ForestFunctions.improving(descendentes, oldTree);
		return improving;
	}
	
	private double fitnessTrain(Tree t) {
		return ForestFunctions.fitnessTrain(t);
	}
	
	private double fitnessTest(Tree t) {
		return ForestFunctions.fitnessTest(t);
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