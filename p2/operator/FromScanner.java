package operator;

import java.io.File;
import java.util.HashMap;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import project2.ParserExample;


/**
 * FromScanner contains information being scanned from a table, generated
 * from the instantiation and manipulation of ScannerOperator class.
 * @author Ella Xue, Jim Li
 */	
public class FromScanner implements FromItemVisitor {
	File inputDir;
	HashMap<String, HashMap<String, Integer>> schema;
	Table table;
	String outputDir;
	public ScanOperator scanOperator;
	
	public FromScanner(){}
	
	@Override
	public void visit(Table tableName) {
		scanOperator = new ScanOperator(new File(ParserExample.inputDir + "/data/" + tableName.getName()));
	}
	
	@Override
	public void visit(SubSelect subSelect) {}
	
	@Override
	public void visit(SubJoin subjoin) {}
	
	public void println(String sentence) {
		System.out.println(sentence);
	}
}
