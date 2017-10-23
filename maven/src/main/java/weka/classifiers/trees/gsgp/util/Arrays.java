package weka.classifiers.trees.gsgp.util;

/**
 * 
 * @author João Batista, jbatista@di.fc.ul.pt
 *
 */
public class Arrays {
	/**
	 * Returns a double[] which is the result of d1 and d2 under op
	 * @param d1
	 * @param d2
	 * @param op
	 * @return
	 */
	public static double[] operation(double [] d1, double [] d2, String op){
		double [] d = new double [d1.length];
		switch(op){
		case "+":
			for(int i = 0; i < d1.length; i++){
				d[i] = d1[i] + d2[i];
			}
			break;
		case "-":
			for(int i = 0; i < d1.length; i++){
				d[i] = d1[i] - d2[i];
			}
			break;
		case "*":
			for(int i = 0; i < d1.length; i++){
				d[i] = d1[i] * d2[i];
			}
			break;
		case "^":
			for(int i = 0; i < d1.length; i++){
				d[i] = Math.pow(d1[i],d2[i]);
			}
			break;
		case "/":
			for(int i = 0; i < d1.length; i++){
				double div = (d2[i]==0 ? 1 : d2[i]);
				d[i] = d1[i] / div;
			}
			break;
		}
		return d;
	}

	/**
	 * Created an Array with all positions with value value
	 * @param value
	 * @param size
	 * @return
	 */
	public static double[] array(double value, int size){
		double [] d = new double [size];
		for( int i = 0; i < d.length; i++)
			d[i] = value;
		return d;
	}

	/**
	 * Copy an array into another
	 * @param origin
	 * @param dest
	 */
	public static void copy(double [] origin, double [] dest){
		for(int i = 0; i < dest.length;i++)
			dest[i]=origin[i];
	}

	/**
	 * Shuffle two arrays in the same way
	 * @param data
	 * @param target
	 */
	public static void shuffle(double[][] data, double[] target) {
		int n = data.length;
		for (int i = 0; i < data.length; i++) {
			// Get a random index of the array past i.
			int random = i + (int) (Math.random() * (n - i));
			// Swap the random element with the present element.
			double[] randomElement = data[random];
			data[random] = data[i];
			data[i] = randomElement;

			double randomEl = target[random];
			target[random] = target[i];
			target[i] = randomEl;
		}
	}

	/**
	 * Returns the RMSE between two arrays, starting the second with an offset
	 * @param d1
	 * @param target
	 * @param offset
	 * @return
	 */
	public static double rmse(double [] d1, double [] target,int offset){
		double sum = 0;
		for(int i = 0; i < d1.length; i++){
			sum += Math.pow(d1[i]-target[i+offset], 2);
		}
		sum /= d1.length;
		return Math.sqrt(sum);
	}

	/**
	 * Return the minimum and max value of the array between the indexed i and j
	 * @param target array
	 * @param i begin index
	 * @param j end index
	 * @return
	 */
	public static double[] minmax(double[] target, int i, int j) {
		double min = target[i];
		double max = target[i];
		
		for(; i<j;i++) {
			min = Math.min(min, target[i]);
			max = Math.max(max, target[i]);
		}
		
		return new double[] {min,max};
	}
}