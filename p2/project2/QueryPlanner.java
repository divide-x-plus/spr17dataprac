package project2;

import operator.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;


public class QueryPlanner {
	QueryInterpreter queryInterpreter;
	Node<Operator> root;
	Node<Operator> dummyNode;
	HashMap<String, Expression> joinCondition;
	HashMap<String, ArrayList<Expression>> selectCondition;

	public QueryPlanner(QueryInterpreter queryInter){
		queryInterpreter = queryInter;
		root = null;
		dummyNode = new Node<Operator>();
	}
	
	public void setLeftChild(Node<Operator> cur, Operator newOperator, Expression exp){
		cur.leftChild = new Node<Operator>();
		cur.leftChild.operator = newOperator;
		cur.leftChild.expression = exp;
	}
	
	public void setRightChild(Node<Operator> cur, Operator newOperator, Expression exp){
		cur.rightChild = new Node<Operator>();
		cur.rightChild.operator = newOperator;
		cur.rightChild.expression = exp;
	}
	
	/*package*/ Node<Operator> getQueryPlan(){
		Node<Operator> cur = dummyNode;
		
		//DISTINCT to remove duplicates
		if (queryInterpreter.getDistinct() != null){
			setLeftChild(cur, new DistinctOperator(), null);
			cur = cur.leftChild;
		}
		
		List<OrderByElement> orderElementList = queryInterpreter.getOrderByElements();
		if (orderElementList != null){
			setLeftChild(cur, new SortOperator(), null);
			cur = cur.leftChild;
		}
		
		List<SelectItem> itemList = queryInterpreter.getSelectItemList();
		if (!(itemList.get(0) instanceof AllColumns)){
			setLeftChild(cur, new ProjectOperator(itemList), null);
			cur = cur.leftChild;
		}
		
		List<Join> joinList = queryInterpreter.getJoinList();
		if (queryInterpreter.getWhereCondition() != null){
			WhereExpressionEvaluator eval = new WhereExpressionEvaluator();
			queryInterpreter.getWhereCondition().accept(eval);
			joinCondition = eval.getJoinCondition();
			selectCondition = eval.getSelectCondition();
		}
		
		for (Map.Entry<String, ArrayList<Expression>> entry : selectCondition.entrySet()){
			System.out.println(" key " + entry.getKey() + " value " + entry.getValue() + "\n;;;;;;;;;;;;; ");
		}
		
		if (joinList != null){
			Collections.reverse(joinList);
			for(Join join : joinList){
				setLeftChild(cur, new JoinOperator(joinCondition, join.getRightItem()), null);
				cur = cur.leftChild;
				setRightChild(cur, new SelectOperator(join.getRightItem(), selectCondition), null);
			}
		}
		
		if (!(itemList.get(0) instanceof SelectExpressionItem)){
			setLeftChild(cur, new SelectOperator(queryInterpreter.getFromItem(), selectCondition), null);
			cur = cur.leftChild;
		}
		
		root = dummyNode.leftChild;
		return root;
	}
	
	/*
	 * Getter function for the root Node of a Query Plan.
	 */
	public Node<Operator> getRoot(){
		return root;
	}
	
	/*
	 * An inner class Node that contains an Operator, an Expression, and 
	 * its child Node's. Node can contain all null fields
	 */
	@SuppressWarnings("hiding")
	public class Node<Operator>{
		
		private Node<Operator> leftChild;
		private Node<Operator> rightChild;
		private Operator operator;
		private Expression expression;
		
		Node() {
			leftChild = null;
			rightChild = null;
			operator = null;
			expression = null;
		}
		
		public Operator getOperator(){
			return operator;
		}
		
		public boolean hasLeftChild(){
			return leftChild != null;
		}
		
		public boolean hasRightChild(){
			return rightChild != null;
		}
		
		public Node<Operator> getLeftChild(){
			return leftChild;
		}
		
		public Node<Operator> getRightChild(){
			return rightChild;
		}
		
		public Expression getExpression(){
			return expression;
		}
	}
}
