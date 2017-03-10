package operator;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import net.sf.jsqlparser.statement.select.*;
import project2.ExpressionEvaluator;
import project2.ParserExample;
import project2.Tuple;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

public class SelectOperator extends Operator {
	private Operator baseOperator;
	private Expression condition;
	private Column[] schema;
	public SelectOperator(Operator baseOperator, Column[] schema, Expression condition){
		this.baseOperator = baseOperator;
		this.schema = schema;
		this.condition = condition;
	}
	
	@Override
	public Tuple getNextTuple() throws FileNotFoundException, IOException {

		//iterate through the whole table, find all tuples match the condition
		Tuple tuple = null;
		do{			
			tuple = baseOperator.getNextTuple();
			if(tuple == null) return null;
			ExpressionEvaluator eval = new ExpressionEvaluator(schema, tuple);
			condition.accept(eval);
//			System.out.println("evaluation tuple " + tuple + " reuslt " + eval.result());
			if(!eval.result()){
				tuple = null;
			}
		}
		while(tuple == null);
		
		return tuple;
	}
	@Override
	public void reset() throws FileNotFoundException {
		// TODO Auto-generated method stub
		baseOperator.reset();
	}

	@SuppressWarnings("resource")
	@Override
	public void dump(Operator operator, String outputDir) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		Tuple tuple = getNextTuple();
		PrintWriter writer = new PrintWriter(outputDir+"/query"+(ParserExample.fileCounter++));
		while(tuple != null){
			writer.println(tuple.toString());
			System.out.println("select operator dumping " + tuple.toString());
			tuple = getNextTuple();
		}
		writer.close();
	}

}
