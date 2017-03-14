package operator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import project2.ParserExample;
import project2.QueryInterpreter;
import project2.QueryPlanner.Node;
import project2.Tuple;
import project2.ExpressionEvaluator;

public class ProjectOperator extends Operator {
	ArrayList<Tuple> tupleList;
	private HashMap<String, HashMap<String, Integer>> schema;
	private String tableName;
	private String columnName;
	List<SelectItem> itemList;
	private FromScanner fromScanner;
	private FromItem fromItem;
	
	
	public ProjectOperator(List<SelectItem> itemList) {
		this.itemList = itemList;
	}

	@Override
	public Tuple getNextTuple(Node<Operator> node, QueryInterpreter interpreter)
			throws FileNotFoundException, IOException {
		Tuple tuple = null;
		do {
			HashMap<String, Integer> currentTable = schema.get(tableName);
			int index = currentTable.get(columnName);
			tuple = new Tuple(tuple.getData()[index]); //TODO: bugs
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
}
