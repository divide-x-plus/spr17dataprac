package operator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import project2.QueryInterpreter;
import project2.QueryPlanner.Node;
import project2.Tuple;

public class SortOperator extends Operator {
	public SortOperator(){
		
	}

	@Override
	public Tuple getNextTuple(Node<Operator> node, QueryInterpreter interpreter)
			throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset(Node<Operator> node, Operator operator) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<Tuple> dump(Node<Operator> node, QueryInterpreter interpreter) throws IOException {
		return null;
	}
}
