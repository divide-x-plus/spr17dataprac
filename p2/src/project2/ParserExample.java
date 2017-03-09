package project2;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.PlainSelect;
import operator.*;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.schema.*;

/**
 * Example class for getting started with JSQLParser. Reads SQL statements from
 * a file and prints them to screen; then extracts SelectBody from each query
 * and also prints it to screen.
 * 
 * @author Lucja Kot
 */
public class ParserExample {

	private static final String queriesFile = "queries.sql";
	private static String inputDir;
	
	static HashMap<String, Column[]> schema = new HashMap<String, Column[]>();
	
	public static void main(String[] args) {
		inputDir = args[0];
		initSchema();
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				if (statement instanceof Select){
					Select select = ((Select)statement);
					PlainSelect plainSelect = (PlainSelect)select.getSelectBody();
					System.out.println("\nSQL: "  + plainSelect) ;
					
					FromScanner fromScanner = new FromScanner(new File(inputDir), schema);
					plainSelect.getFromItem().accept(fromScanner);
					
					//print whole table
					Operator operator = fromScanner.scanOperator;
					operator.dump(operator);
					operator.reset();
					println("get from item " + plainSelect.getFromItem());
					//print selected tuples
					Expression condition = plainSelect.getWhere();
					println("condition class: " + condition.getClass());
					println("with condition where: " + condition);
					println("schema " + schema.get(plainSelect.getFromItem()));
					operator = new SelectOperator(operator, schema.get(plainSelect.getFromItem().toString()), condition);
					operator.dump(operator);
				}
				
				println("----------------------Next Statement-------------------------");
				
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}
	public static void println(String sentence){
		System.out.println(sentence);
	}
	public static void print(String sentence){
		System.out.print(sentence);
	}


public static void initSchema(){
	BufferedReader input;
	try {
		input  = new BufferedReader(new FileReader(new File(inputDir+"/schema.txt")));
		String line;
		while((line = input.readLine()) != null)
		{
			String items[] = line.split(" ");
			String name = items[0];
			Table table = new Table();
			table.setName(name);
			Column[] tableColumns = new Column[items.length - 1];
			
			//create table columns with all table column info
			for(int i = 1; i < items.length; i++)
			{
				tableColumns[i-1] = new Column(table, items[i]);
			}
			
			//table name matches it's table columns
			schema.put(name, tableColumns);
		}
	} 
	catch (FileNotFoundException e) 
	{
		e.printStackTrace();
	} 
	catch (IOException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	printSchema(schema);
	
}
	public static void printSchema(HashMap<String, Column[]> map){
		for(Entry<String, Column[]> entry: map.entrySet()){
			print(entry.getKey() + " ");
			for(Column col: entry.getValue()){
				print(col.getColumnName() + " ");
			}
			println("");
		}
	}
}
//if (statement instanceof CreateTable){
//CreateTable table = (CreateTable)statement;
//List<ColumnDefinition> colDef = table.getColumnDefinitions();
//println("table : " + table);
//println("colDef : " + colDef);
//println("col name " + colDef.get(0).getColumnName());
//}


////System.out.println("Read statement: " + statement);
//Select select = (Select) statement;
//printf("Select body: " + select.getSelectBody());
//PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
//printf("Selection item: " + plainSelect.getSelectItems());
////FromItem fromItem = (SubSelect) plainSelect.getFromItem();
//printf("From item: "  + plainSelect.getFromItem());
//printf("From join: "  + plainSelect.getJoins());
//printf("Distinct: " + plainSelect.getDistinct());
//printf("Where: " + plainSelect.getWhere());
//printf("order by: " + plainSelect.getOrderByElements());

//Operator operator = fromScanner;
//SelectOperator selectOp = new SelectOperator(plainSelect);

//List<SelectItem> list = plainSelect.getSelectItems() ;
//Expression condition = plainSelect.getWhere();
//println("condition " + list.get(0).getClass());
//System.out.println("Select item:  "  + list);
////printf("From item: "  + 
//FromItem fromItem = plainSelect.getFromItem();
////FromItemVisitor 
////fromItem.accept(fromItemVisitor);
//println("From Item: " + fromItem.getClass());
//for(SelectItem item: list){
//	if (item instanceof AllColumns){
//		String filePath = inputDir+"/"+fromItem;
//		ScanOperator scanOp = new ScanOperator(new File(filePath));
//		scanOp.dump();
//	}
//	else{
//		println("oops dont know how to handle this yet = =||");
//	}
//}