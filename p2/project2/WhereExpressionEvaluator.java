package project2;

import java.util.ArrayList;
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

public class WhereExpressionEvaluator implements ExpressionVisitor {

	HashMap<String, HashMap<String, Integer>> schema;
	Tuple tuple;
	private Column leftColumn;
	private Column rightColumn;
	HashMap<String, Expression> joinCondition;
	HashMap<String, ArrayList<Expression>> selectCondition;
	
	public WhereExpressionEvaluator(HashMap<String, HashMap<String, Integer>> schema, Tuple lTuple, Tuple rTuple){
		this.schema = schema;
	}
	
	public WhereExpressionEvaluator(){
		joinCondition = new HashMap<String, Expression>();
		selectCondition = new HashMap<String, ArrayList<Expression>>();
	}
	
	public HashMap<String, Expression> getJoinCondition(){
		return joinCondition;
	}
	
	public HashMap<String, ArrayList<Expression>> getSelectCondition(){
		return selectCondition;
	}
	
	@Override
	public void visit(NullValue nullValue) {}

	@Override
	public void visit(Function function) {}

	@Override
	public void visit(InverseExpression inverseExpression) {}

	@Override
	public void visit(JdbcParameter jdbcParameter) {}

	@Override
	public void visit(DoubleValue doubleValue) {}

	@Override
	public void visit(LongValue longValue) {}

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
	public void visit(AndExpression andExpression) {
		Expression leftExp = andExpression.getLeftExpression();
		Expression rightExp = andExpression.getRightExpression();
		leftExp.accept(this);
		rightExp.accept(this);
	}

	@Override
	public void visit(OrExpression orExpression) {}

	@Override
	public void visit(Between between) {}

	@Override
	public void visit(EqualsTo equalsTo) {
		Expression leftExp = equalsTo.getLeftExpression();
		Expression rightExp = equalsTo.getRightExpression();
		String tableName = "";
		if (leftExp instanceof Column && rightExp instanceof Column){
			leftColumn = (Column) leftExp;
			rightColumn = (Column) rightExp;
			
			//join table name combined A, B
			joinCondition.put(leftColumn.getTable().getName() + rightColumn.getTable().getName(), equalsTo);
			joinCondition.put(rightColumn.getTable().getName() + leftColumn.getTable().getName(), equalsTo);
		} else{
			if (leftExp instanceof Column){
				tableName = ((Column)leftExp).getTable().getName();
			} else if (rightExp instanceof Column){
				tableName = ((Column)rightExp).getTable().getName();
			}
			
			ArrayList<Expression> list;
			
			if (selectCondition.containsKey(tableName)){
				list = selectCondition.get(tableName);
			} else {
				list = new ArrayList<Expression>();
			}
			
			list.add(equalsTo);
			selectCondition.put(tableName, list);
		}
	}

	@Override
	public void visit(GreaterThan greaterThan) {
		Expression leftExp = greaterThan.getLeftExpression();
		Expression rightExp = greaterThan.getRightExpression();
		String tableName = "";
		if(leftExp instanceof Column && rightExp instanceof Column){
			leftColumn = (Column)leftExp;
			rightColumn = (Column)rightExp;
			
			//join table name combined A+B
			joinCondition.put(leftColumn.getTable().getName()+rightColumn.getTable().getName(),greaterThan);
			joinCondition.put(rightColumn.getTable().getName()+leftColumn.getTable().getName(),greaterThan);
		}
		else{ 
			if(leftExp instanceof Column){
				tableName = ((Column)leftExp).getTable().getName();
			}
			else if(rightExp instanceof Column){
				tableName = ((Column)rightExp).getTable().getName();
			}
			ArrayList<Expression> list;
			if(selectCondition.containsKey(tableName)){
				list = selectCondition.get(tableName);
			}
			else{
				list = new ArrayList<Expression>();
			}
			list.add(greaterThan);
			selectCondition.put( tableName, list);
		}
	}

	@Override
	public void visit(GreaterThanEquals greaterThanEquals) {
		Expression leftExp = greaterThanEquals.getLeftExpression();
		Expression rightExp = greaterThanEquals.getRightExpression();
		String tableName = "";
		if(leftExp instanceof Column && rightExp instanceof Column){
			leftColumn = (Column)leftExp;
			rightColumn = (Column)rightExp;
			
			//join table name combined A+B
			joinCondition.put(leftColumn.getTable().getName()+rightColumn.getTable().getName(),greaterThanEquals);
			joinCondition.put(rightColumn.getTable().getName()+leftColumn.getTable().getName(),greaterThanEquals);
		}
		else{
				if(leftExp instanceof Column){
					tableName = ((Column)leftExp).getTable().getName();
				}
				else if(rightExp instanceof Column){
					tableName = ((Column)rightExp).getTable().getName();
				}
				ArrayList<Expression> list;
				if(selectCondition.containsKey(tableName)){
					list = selectCondition.get(tableName);
				}
				else{
					list = new ArrayList<Expression>();
				}
				list.add(greaterThanEquals);
				selectCondition.put( tableName, list);
			}
	}

	@Override
	public void visit(InExpression inExpression) {}

	@Override
	public void visit(IsNullExpression isNullExpression) {}

	@Override
	public void visit(LikeExpression likeExpression) {}

	@Override
	public void visit(MinorThan minorThan) {
		Expression leftExp = minorThan.getLeftExpression();
		Expression rightExp = minorThan.getRightExpression();
		String tableName = "";
		if(leftExp instanceof Column && rightExp instanceof Column){
			leftColumn = (Column)leftExp;
			rightColumn = (Column)rightExp;
			
			//join table name combined A+B = AB
			joinCondition.put(leftColumn.getTable().getName()+rightColumn.getTable().getName(),minorThan);
			joinCondition.put(rightColumn.getTable().getName()+leftColumn.getTable().getName(),minorThan);
		}
		else{
			if(leftExp instanceof Column){
				tableName = ((Column)leftExp).getTable().getName();
			}
			else if(rightExp instanceof Column){
				tableName = ((Column)rightExp).getTable().getName();
			}
			ArrayList<Expression> list;
			if(selectCondition.containsKey(tableName)){
				list = selectCondition.get(tableName);
			}
			else{
				list = new ArrayList<Expression>();
			}
			list.add(minorThan);
			selectCondition.put( tableName, list);
		}
	}

	@Override
	public void visit(MinorThanEquals minorThanEquals) {
		Expression leftExp = minorThanEquals.getLeftExpression();
		Expression rightExp = minorThanEquals.getRightExpression();
		String tableName = "";
		if(leftExp instanceof Column && rightExp instanceof Column){
			leftColumn = (Column)leftExp;
			rightColumn = (Column)rightExp;
			
			//join table name combined A+B
			joinCondition.put(leftColumn.getTable().getName()+rightColumn.getTable().getName(),minorThanEquals);
			joinCondition.put(rightColumn.getTable().getName()+leftColumn.getTable().getName(),minorThanEquals);
		}
		else{
			if(leftExp instanceof Column){
				tableName = ((Column)leftExp).getTable().getName();
			}
			else if(rightExp instanceof Column){
				tableName = ((Column)rightExp).getTable().getName();
			}
			ArrayList<Expression> list;
			if(selectCondition.containsKey(tableName)){
				list = selectCondition.get(tableName);
			}
			else{
				list = new ArrayList<Expression>();
			}
			list.add(minorThanEquals);
			selectCondition.put( tableName, list);
		}
	}

	@Override
	public void visit(NotEqualsTo notEqualsTo) {
		Expression leftExp = notEqualsTo.getLeftExpression();
		Expression rightExp = notEqualsTo.getRightExpression();
		String tableName = "";
		if(leftExp instanceof Column && rightExp instanceof Column){
			leftColumn = (Column)leftExp;
			rightColumn = (Column)rightExp;
			
			//join table name combined A+B
			joinCondition.put(leftColumn.getTable().getName()+rightColumn.getTable().getName(),notEqualsTo);
			joinCondition.put(rightColumn.getTable().getName()+leftColumn.getTable().getName(),notEqualsTo);
		}
		else{
			if(leftExp instanceof Column){
				tableName = ((Column)leftExp).getTable().getName();
			}
			else if(rightExp instanceof Column){
				tableName = ((Column)rightExp).getTable().getName();
			}
			ArrayList<Expression> list;
			if(selectCondition.containsKey(tableName)){
				list = selectCondition.get(tableName);
			}
			else{
				list = new ArrayList<Expression>();
			}
			list.add(notEqualsTo);
			selectCondition.put( tableName, list);
		}
	}

	@Override
	public void visit(Column tableColumn) {}

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
}
