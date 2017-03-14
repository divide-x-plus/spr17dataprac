package operator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.statement.select.FromItem;
import project2.ExpressionEvaluator;
import project2.ParserExample;
import project2.QueryInterpreter;
import project2.QueryPlanner.Node;
import project2.Tuple;

public class SelectOperator extends Operator {
	private ArrayList<Expression> selectConditionList; //list of expressions to be evaluated
	@SuppressWarnings("unused")
	private HashMap<String, HashMap<String, Integer>> schema; //schema name, and its corresponding schema structure
	ArrayList<Tuple> tupleList;
	private FromScanner fromScanner;
	private FromItem fromItem;
	private Expression selectCondition;
	
	public SelectOperator(FromItem fromItem, HashMap<String, ArrayList<Expression>> selectConditions) {
		this.fromItem = fromItem; //register table
		tupleList = new ArrayList<Tuple>(); //instantiate list of queried info
		if (selectConditions != null){
			selectConditionList = selectConditions.get(fromItem.toString());
			if (selectConditionList != null){
				selectCondition = selectConditionList.get(0); //TODO: why is it just the first one?
				for (int i = 1; i < selectConditionList.size(); i++){
					selectCondition = new AndExpression(selectCondition, selectConditionList.get(i));
				}
			}
		}
		schema = ParserExample.schema;
	}

	@Override
	public Tuple getNextTuple(Node<Operator> node, QueryInterpreter interpreter) throws FileNotFoundException, IOException {
		Tuple tuple = null;
		//iterate through the entire table and find tuples that match the condition
		do {
			tuple = fromScanner.scanOperator.getNextTuple(node, null);
			if (tuple == null) return null;
			if (selectCondition != null) {
				ExpressionEvaluator eval = new ExpressionEvaluator(tuple);
				selectCondition.accept(eval);
				if (!eval.result()){
					tuple = null;
				}
			}
		} while (tuple == null);
		return tuple;
	}

	@Override
	public void reset(Node<Operator> node, Operator operator) throws FileNotFoundException {
		node.getOperator().reset(node, null);
	}
	
	public ArrayList<Tuple> dump(Node<Operator> node, QueryInterpreter interpreter) throws FileNotFoundException, IOException{
		fromScanner = new FromScanner();
		fromItem.accept(fromScanner);
		
		Tuple tuple = getNextTuple(node, interpreter);
		while (tuple != null){
			tupleList.add(tuple);
			tuple = getNextTuple(node, interpreter);
		}
		fromScanner.scanOperator.reset(null, null);
		return tupleList;
	}
	
	public FromItem getFromItem(){
		return fromItem;
	}
}
