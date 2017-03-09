package operator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import project2.Tuple;

public abstract class Operator {

	/**
	 * Repeatedly to get the next tuple of the operator's output.
	 * @return next Tuple if exists
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */
	public abstract Tuple getNextTuple() throws FileNotFoundException, IOException;
	/**
	 * Tells the operator to reset its state and start returning its output 
	 * again from the beginning;	
	 * @throws FileNotFoundException 
	 */
	public abstract void reset() throws FileNotFoundException;
	
	/**
	 * This method repeatedly calls getNextTuple() until the next tuple 
	 * is null (no more output) and writes each tuple to a suitable PrintStream.
	 * @throws IOException 
	 */
	public abstract void dump(Operator operator) throws IOException;
}