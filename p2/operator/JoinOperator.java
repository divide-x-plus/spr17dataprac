package operator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import project2.ParserExample;
import project2.QueryInterpreter;
import project2.QueryPlanner.Node;
import project2.Tuple;

public class JoinOperator extends Operator {
	ArrayList<Tuple> tupleList;
	FromScanner fromScanner;
	HashMap<String, Expression> joinCondition;
	ArrayList<Tuple> joinTupleList;
	private Table leftFromItem;
	private Table rightFromItem;
	FromItem fromItem;
	HashMap<String, HashMap<String, Integer>> schema = ParserExample.schema;
	HashMap<String, Column[]> schema2 = ParserExample.schema2;
	
	public JoinOperator(HashMap<String, Expression> joinCondition, FromItem fromItem){
		tupleList = null;
		joinTupleList = new ArrayList<Tuple>();
		this.joinCondition = joinCondition;
		this.fromItem = fromItem;
	}

	@Override
	public Tuple getNextTuple(Node<Operator> node, QueryInterpreter interpreter)
			throws FileNotFoundException, IOException {
		return null;
	}

	@Override
	public void reset(Node<Operator> node, Operator operator) throws FileNotFoundException {
		node.getLeftChild().getOperator().reset(node.getLeftChild(), operator);
	}
	
	public String[] joinTwoTuple(Tuple A, Tuple B){
		//instantiate String list with combined length of tuples
		String newTuple[] = new String[A.getData().length + B.getData().length]; 
		
		//copy from Tuple A and Tuple B to new combined Tuple
		int index = 0;
		for (int i = 0; i < A.getData().length; i++){
			newTuple[index++] = A.getData()[i];
		}
		for (int i = 0; i < B.getData().length; i++){
			newTuple[index++] = B.getData()[i];
		}
		return newTuple;
	}
	
	public void printTupleList(ArrayList<Tuple> list){
		for (Tuple rightTuple : list){
			System.out.println(rightTuple.toString());
		}
	}
	
	public String setChildTableName(Node<Operator> node){
		if (node.getLeftChild().getOperator() instanceof SelectOperator){
			leftFromItem = (Table) ((SelectOperator)node.getLeftChild().getOperator()).getFromItem();
		} else if (node.getLeftChild().getOperator() instanceof JoinOperator){
			leftFromItem = (Table)((SelectOperator)node.getLeftChild().getRightChild().getOperator()).getFromItem();
		} //TODO: ProjectOperator instance
		rightFromItem = (Table)((SelectOperator)node.getRightChild().getOperator()).getFromItem();
		return leftFromItem.getName() + rightFromItem.getName();
	}
	
	//TODO: dump method
}
