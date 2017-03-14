package project2;
import java.util.HashMap;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

public class ExpressionEvaluator implements ExpressionVisitor {
	private boolean result;
	private int leftVal;
	private int rightVal;
	private String columnName;
	private long isLongValue;
	private String tableName;
	private int flag;
	HashMap<String, HashMap<String, Integer>> schema = ParserExample.schema;
	Tuple leftTuple;
	Tuple rightTuple;
	String rightTableName;
	String leftTableName;
	Tuple tuple;
	
	public ExpressionEvaluator(Tuple tuple){
		this.tuple = tuple;
		leftVal = 0;
		rightVal = 0;
		columnName = "";
		isLongValue = 0;
		result = true;
		flag = 0;
	}
	
	public ExpressionEvaluator(Tuple A, Tuple B, String leftTableName, String rightTableName){
		leftTuple = A;
		rightTuple = B;
		leftVal = 0;
		rightVal = 0;
		columnName = "";
		isLongValue = 0;
		result = true;
		this.leftTableName = leftTableName;
		this.rightTableName = rightTableName;
		flag = 1;
	}
	
	public boolean result(){
		return result;
	}
	
	public int getNumericVal(String colName){
		//col name can be accessed via indexing for each table
		HashMap<String, Integer> currentTable = schema.get(tableName);
		int index = currentTable.get(colName);
		int columnVal = Integer.parseInt(tuple.getData()[index]);//select a numeric value given a tuple/row
		return columnVal;
	}
	
	public int getNumericVal(){
		String theTableName = "";
		Tuple theTuple = null;
		if(tableName.equals(rightTableName)){
			theTableName = rightTableName;
			theTuple = rightTuple;
		} else {
			theTableName = leftTableName;
			theTuple = leftTuple;
		}
		HashMap<String, Integer> currentTable = schema.get(theTableName);
		int index = currentTable.get(columnName);
		int columnVal = Integer.parseInt(theTuple.getData()[index]);
		return columnVal;
	}
	
	@Override
	public void visit(LongValue longValue){
		isLongValue = longValue.toLong();
	}
	
	@Override
	public void visit(AndExpression andExpression) {
		Expression leftExp = andExpression.getLeftExpression();
		Expression rightExp = andExpression.getRightExpression();
		leftExp.accept(this);
		rightExp.accept(this);		
	}
	
	@Override
	public void visit(EqualsTo equalsTo) {
		Expression leftExp = equalsTo.getLeftExpression();
		Expression rightExp = equalsTo.getRightExpression();
		
		leftExp.accept(this);
		if(leftExp instanceof Column){
			leftVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		} else if(leftExp instanceof LongValue){
			leftVal = (int)isLongValue;
		}
		
		rightExp.accept(this);
		if(rightExp instanceof Column){
			rightVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		} else if(rightExp instanceof LongValue){
			rightVal = (int)isLongValue;
		}
		
		result = (leftVal == rightVal) && (result == true);
	}

	/**
	 * TODO:
	 */
	@Override
	public void visit(GreaterThan greaterThan) {
		Expression leftExp = greaterThan.getLeftExpression();
		Expression rightExp = greaterThan.getRightExpression();
		
		leftExp.accept(this);
		if(leftExp instanceof Column){
			leftVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		} else if(leftExp instanceof LongValue){
			leftVal = (int)isLongValue;
		}
		
		rightExp.accept(this);
		if(rightExp instanceof Column){
			rightVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		} else if(rightExp instanceof LongValue){
			rightVal = (int)isLongValue;
		}
		
		result = (leftVal > rightVal) && (result == true);
	}

	/**
	 * TODO:
	 */
	@Override
	public void visit(GreaterThanEquals greaterThanEquals) {
		Expression leftExp = greaterThanEquals.getLeftExpression();
		Expression rightExp = greaterThanEquals.getRightExpression();
		
		leftExp.accept(this);
		if(leftExp instanceof Column){
			leftVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		} else if(leftExp instanceof LongValue){
			leftVal = (int)isLongValue;
		}
		
		rightExp.accept(this);
		if(rightExp instanceof Column){
			rightVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		} else if(rightExp instanceof LongValue){
			rightVal = (int)isLongValue;
		}
		
		result = ((leftVal > rightVal) || (leftVal == rightVal)) && (result == true);
	}


	/**
	 * TODO:
	 */
	@Override
	public void visit(MinorThan minorThan) {
		
		Expression leftExp = minorThan.getLeftExpression();
		Expression rightExp = minorThan.getRightExpression();
		
		leftExp.accept(this);
		if(leftExp instanceof Column){
			leftVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		}
		else if(leftExp instanceof LongValue){
			leftVal = (int)isLongValue;
		}
		rightExp.accept(this);
		if(rightExp instanceof Column){
			rightVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		}
		else if(rightExp instanceof LongValue){
			rightVal = (int)isLongValue;
		}
		result = (leftVal < rightVal) && (result == true);
	}
	
	/**
	 * TODO:
	 */
	@Override
	public void visit(MinorThanEquals minorThanEquals) {
		// TODO Auto-generated method stub
		Expression leftExp = minorThanEquals.getLeftExpression();
		Expression rightExp = minorThanEquals.getRightExpression();
		
		leftExp.accept(this);
		if(leftExp instanceof Column){
			leftVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		}
		else if(leftExp instanceof LongValue){
			leftVal = (int)isLongValue;
		}
		rightExp.accept(this);
		if(rightExp instanceof Column){
			rightVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		}
		else if(rightExp instanceof LongValue){
			rightVal = (int)isLongValue;
		}
		result = ((leftVal < rightVal) || (leftVal == rightVal)) && (result == true);
	}

	/**
	 * TODO:
	 */
	@Override
	public void visit(NotEqualsTo notEqualsTo) {
		// TODO Auto-generated method stub
		Expression leftExp = notEqualsTo.getLeftExpression();
		Expression rightExp = notEqualsTo.getRightExpression();
		
		leftExp.accept(this);
		if(leftExp instanceof Column){
			leftVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		}
		else if(leftExp instanceof LongValue){
			leftVal = (int)isLongValue;
		}
		rightExp.accept(this);
		if(rightExp instanceof Column){
			rightVal = (flag == 0) ? getNumericVal(columnName) :getNumericVal();
		}
		else if(rightExp instanceof LongValue){
			rightVal = (int)isLongValue;
		}
		result = (leftVal != rightVal) && (result == true);		
	}

	/**
	 * TODO:
	 */
	@Override
	public void visit(Column tableColumn) {
		columnName = tableColumn.getColumnName();
		tableName = tableColumn.getTable().getName();
	}

	@Override
	public void visit(SubSelect subSelect) {}

	@Override
	public void visit(CaseExpression caseExpression) {}

	@Override
	public void visit(WhenClause whenClause) {}

	@Override
	public void visit(ExistsExpression existsExpression) {}

	@Override
	public void visit(AllComparisonExpression allComparisonExpression) {}

	@Override
	public void visit(AnyComparisonExpression anyComparisonExpression) {}

	@Override
	public void visit(Concat concat) {}

	@Override
	public void visit(Matches matches) {}

	@Override
	public void visit(BitwiseAnd bitwiseAnd) {}

	@Override
	public void visit(BitwiseOr bitwiseOr) {}

	@Override
	public void visit(BitwiseXor bitwiseXor) {}

	@Override
	public void visit(InExpression inExpression) {}

	@Override
	public void visit(IsNullExpression isNullExpression) {}

	@Override
	public void visit(LikeExpression likeExpression) {}
	
	@Override
	public void visit(OrExpression orExpression) {}

	@Override
	public void visit(Between between) {}

	@Override
	public void visit(DateValue dateValue) {}

	@Override
	public void visit(TimeValue timeValue) {}

	@Override
	public void visit(TimestampValue timestampValue) {}

	@Override
	public void visit(Parenthesis parenthesis) {}

	@Override
	public void visit(StringValue stringValue) {}

	@Override
	public void visit(Addition addition) {}

	@Override
	public void visit(Division division) {}

	@Override
	public void visit(Multiplication multiplication) {}

	@Override
	public void visit(Subtraction subtraction) {}

	@Override
	public void visit(NullValue nullValue) {}

	@Override
	public void visit(Function function) {}

	@Override
	public void visit(InverseExpression arg0) {}

	@Override
	public void visit(JdbcParameter jdbcParameter) {}

	@Override
	public void visit(DoubleValue doubleValue) {}	
}
