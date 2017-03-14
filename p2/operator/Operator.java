package operator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import project2.QueryInterpreter;
import project2.QueryPlanner.Node;
import project2.Tuple;

public abstract class Operator {
	//Operator includes all fields with inherited subclasses
	ScanOperator scanOperator;
	SelectOperator selectOperator;
	ProjectOperator projectOperator;
	JoinOperator 	joinOperator;
	FromScanner 	fromScanner;
	String outputDir;
	
	/**
	 * Repeatedly get to the next tuple of operator's output.
	 * @return next Tuple if exists
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public abstract Tuple getNextTuple(Node<Operator> node, QueryInterpreter interpreter) throws FileNotFoundException, IOException;
	
	/**
	 * Reset operator to the start of the output.
	 * @throws FileNotFoundException
	 */
	public abstract void reset(Node<Operator> node, Operator operator) throws FileNotFoundException;
	
	/**
	 * Repeatedly calls getNextTuple() until the next Tuple is null (no more output)
	 * and writes each Tuple to a suitable PrintStream.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ArrayList<Tuple> dump(Node<Operator> node, QueryInterpreter interpreter) throws FileNotFoundException, IOException{
		return null;
	}
}
