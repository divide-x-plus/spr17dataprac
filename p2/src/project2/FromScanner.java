package project2;
import java.io.File;
import java.util.HashMap;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import operator.Operator;
import operator.ScanOperator;

public class FromScanner implements FromItemVisitor{
	File inputDir;
	HashMap<String, Column[]> schema;
	Table table;
	Column[] tableColumns;
	public Operator scanOperator;
	
	FromScanner(File path, HashMap<String, Column[]> sche){
		inputDir = path;
		schema = sche;
	}
	
	@Override
	public void visit(Table tableName) {
		
		println("visit table name " + tableName);
		//store current table's column attribute name
		tableColumns = schema.get(tableName.getName());
		scanOperator = new ScanOperator(new File(inputDir, "/data/"+tableName.getName()));
	}

	@Override
	public void visit(SubSelect arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SubJoin arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void println(String sentence){
		System.out.println(sentence);
	}
	
				
}
