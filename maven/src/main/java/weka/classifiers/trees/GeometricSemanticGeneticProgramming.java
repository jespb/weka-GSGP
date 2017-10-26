package weka.classifiers.trees;

import java.io.Serializable;
import java.util.Collections;
import java.util.Vector;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.trees.gsgp.forest.Forest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

/**
 * 
 * @author Jo�o Batista, jbatista@di.fc.ul.pt
 *
 */
public class GeometricSemanticGeneticProgramming extends AbstractClassifier implements Classifier, Serializable{

	private static final long serialVersionUID = 1L;
	
	private Forest forest;
	
	private int populationSize = 300;
	private int maxDepth = 7;

	@Override
	public void buildClassifier(Instances data) throws Exception {
		int n_cols = data.numAttributes();
		int n_rows = data.numInstances();
		double targetMin = Double.MAX_VALUE;
		double targetMax = -Double.MAX_VALUE;

		double [][] dados = new double [n_rows][n_cols-1];
		double [] target = new double [n_rows];

		for(int y = 0; y < n_rows; y++){
			Instance row = data.get(y);
			for(int x = 0; x < row.numAttributes(); x++){
				if(x < row.numAttributes() - 1){
					dados[y][x] = row.value(x);
				}else{
					target[y] = row.value(x);
					targetMin = Math.min(target[y],targetMin);
					targetMax = Math.max(target[y],targetMax);
				}
			}
		};
		
		String [] op = "+ - * /".split(" ");
		String [] term = new String [n_cols-1];
		for(int i = 0; i < term.length; i++)
			term[i] = "x"+i;

		double ms = -(targetMax-targetMin)/200.0;
		double train_perc = 0.7;
		
		forest = new Forest("",op, term, maxDepth, ms,dados, target, populationSize, train_perc, 6000);
		
		forest.train();

	}
	
	/**
	 * Gets options from this classifier.
	 * 
	 * @return the options for the current setup
	 */
	@Override
	public String[] getOptions() {
		Vector<String> result = new Vector<String>();

		result.add("-populationSize");
		result.add("" + populationSize);

		result.add("-initialMaxDepth");
		result.add("" + maxDepth);

		Collections.addAll(result, super.getOptions());

		return result.toArray(new String[result.size()]);
	}

	/**
	 * Sets options
	 */
	public void setOptions(String[] options) throws Exception {
		String tmpStr;

		tmpStr = Utils.getOption("populationSize", options);
		if (tmpStr.length() != 0) {
			populationSize = Integer.parseInt(tmpStr);
		} else {
			populationSize = 200;
		}

		tmpStr = Utils.getOption("initialMaxDepth", options);
		if (tmpStr.length() != 0) {
			maxDepth = Integer.parseInt(tmpStr);
		} else {
			maxDepth = 7;
		}
	}

	@Override
	public double classifyInstance(Instance instance) throws Exception{
		double ret = -1;
		double [] dados = new double [instance.numAttributes()];
		for(int i = 0; i < dados.length; i++){
			dados[i] = instance.value(i);
		}
		ret = forest.predict(dados);
		return ret;
	}
	
	public String toString(){
		return "This classifier does not support a String representation.";
	}


}