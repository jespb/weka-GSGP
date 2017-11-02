package weka.classifiers.trees.gsgp.node;

import java.io.Serializable;

import weka.classifiers.trees.gsgp.util.Mat;

/**
 * 
 * @author João Batista, jbatista@di.fc.ul.pt
 *
 */
public class Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String v = null;
	Node l = null;
	Node r = null;
	
	/**
	 * Basic constructor
	 * @param value
	 */
	public Node(String value){
		v = value;
	}

	/**
	 * Basic constructor
	 * @param left
	 * @param right
	 * @param op
	 */
	public Node(Node left, Node right, String op){
		l = left;
		r = right;
		v = op;
	}

	/**
	 * Constructor
	 * @param op
	 * @param term
	 * @param t_rate
	 * @param depth
	 */
	public Node(String [] op, String [] term, double t_rate, int depth){
		if(Math.random() < t_rate || depth < 0){
			v = term[Mat.random(term.length)];
		}else{
			v = op[Mat.random(op.length)];
			l = new Node(op, term, t_rate, depth-1);
			r = new Node(op, term, t_rate, depth-1);
		}
	}

	/**
	 * Used the node to calculate
	 * @param vals
	 * @return
	 */
	public double calculate(double [] vals){
		if(isLeaf()){
			if(v.startsWith("x")){
				int index = Integer.parseInt(v.substring(1));
				return vals[index];
			}else{
				if(v.equals("e"))
					return Math.E;
				else
					return Double.parseDouble(v);
			}
		}else{
			double d = 0;
			switch(v){
			case "+":
				d = l.calculate(vals)+r.calculate(vals);
				break;
			case "-":
				d = l.calculate(vals)-r.calculate(vals);
				break;
			case "*":
				d = l.calculate(vals)*r.calculate(vals);
				break;
			case "^":
				d = Math.pow(l.calculate(vals),r.calculate(vals));
				break;
			case "/":
				double div = r.calculate(vals);
				d = l.calculate(vals)/(div != 0 ? div : 1);
				break;
			}
			return d;
		}
	}

	/**
	 * Returns the node under the String format
	 */
	public String toString(){
		if(isLeaf()){
			return v;
		}else{
			return "( " + l + " " + v + " " + r + " )";
		}
	}

	/**
	 * Returns true if the node has no children
	 * This assumes that either the node has two children or zero
	 * @return
	 */
	private boolean isLeaf(){
		return r==null;
	}
}