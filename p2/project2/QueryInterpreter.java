package project2;

import java.io.*;
import java.util.*;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.*;
import operator.*;
import project2.QueryPlanner.Node;

/**
 * QueryInterpreter.java contains: 
 * 
 * (0) all information associated with a statement parsed 
 * from the syntax, and 
 * (1) its chosen query plan that intakes input tuples and
 * evaluates accordingly.
 *  
 * @author Ella Xue, Jim Li
 */
public class QueryInterpreter {
	
	private Statement statement = null;
	private PlainSelect plainSelect = null;
	private Expression whereCondition = null;
	private List<Join> joinList = null;
	private List<OrderByElement> orderByElements = null;
	private List<SelectItem> selectItemList = null;
	private Distinct distinct = null;
	private FromItem fromItem = null;
	private Node<Operator> queryPlan;
	ArrayList<Tuple> tupleList; //maintains output rows
	
	@SuppressWarnings("unchecked")
	public QueryInterpreter(Statement statement){
		this.statement = statement;
		System.out.println("SQL: " + statement);
		if (statement instanceof Select){
			plainSelect = (PlainSelect)((Select)statement).getSelectBody();
			whereCondition = plainSelect.getWhere();
			joinList = plainSelect.getJoins();
			orderByElements = plainSelect.getOrderByElements();
			selectItemList = plainSelect.getSelectItems(); //TODO related to project?
			distinct = plainSelect.getDistinct();
			fromItem = plainSelect.getFromItem();
			tupleList = null;
		} 
	}
	
	public void setQueryPlan(Node<Operator> queryPlan){
		this.queryPlan = queryPlan;
	}
	
	public void executeQuery(Node<Operator> root) {
		try {
			System.out.println("root operator" + root.getOperator().getClass()); //print name of root operator
			tupleList = root.getOperator().dump(root, this);
			PrintWriter writer = new PrintWriter(ParserExample.outputDir + "/query" + (ParserExample.fileCounter++));
			System.out.println("Final joined table: \n");
			//printing out the tables in console
			for (Tuple tuple : tupleList){
				System.out.println(tuple.toString() + " ");
				writer.print(tuple.toString() + "\n");
			}
			writer.close(); //close stream
		} catch (IOException e1){
			e1.printStackTrace();
		}
	}
	
	public void printQueryPlan(Node<Operator> root){
		if (root == null) return; //Query Plan doesn't exist
		System.out.println("plan " + root.getOperator().getClass()); //base case
		printQueryPlan(root.getLeftChild());
		printQueryPlan(root.getRightChild()); //recursive case
	}
	
	public FromItem getFromItem(){
		return fromItem;
	}
	public PlainSelect getPlainSelect() {
		return plainSelect;
	}
	public void setPlainSelect(PlainSelect plainSelect) {
		this.plainSelect = plainSelect;
	}
	public Expression getWhereCondition() {
		return whereCondition;
	}
	public void setWhereCondition(Expression whereCondition) {
		this.whereCondition = whereCondition;
	}
	public List<Join> getJoinList() {
		return joinList;
	}
	public void setJoinList(List<Join> joinList) {
		this.joinList = joinList;
	}
	public Statement getStatement() {
		return statement;
	}
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	public List<OrderByElement> getOrderByElements() {
		return orderByElements;
	}
	public void setOrderByElements(List<OrderByElement> orderByElements) {
		this.orderByElements = orderByElements;
	}
	public List<SelectItem> getSelectItemList() {
		return selectItemList;
	}
	public void setSelectItemList(List<SelectItem> selectItemList) {
		this.selectItemList = selectItemList;
	}
	public Distinct getDistinct() {
		return distinct;
	}
	public void setDistinct(Distinct distinct) {
		this.distinct = distinct;
	}
	public Node<Operator> getQueryPlan(){
		return this.queryPlan;
	}
}
