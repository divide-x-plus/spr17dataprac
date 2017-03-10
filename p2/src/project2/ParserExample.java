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
	public static int fileCounter = 1;
	private static String outputDir;
	static HashMap<String, Column[]> schema = new HashMap<String, Column[]>();

	public static void main(String[] args) {
		if(args.length < 2){
			System.out.println("Usage: inputDir outputDir");
			System.exit(0);
		}

		inputDir = args[0];
		outputDir = args[1];
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
					//print selected tuples
					Expression condition = plainSelect.getWhere();
					println("with condition where: " + condition);
					println("schema " + plainSelect.getFromItem().toString());
					operator = new SelectOperator(operator, schema.get(plainSelect.getFromItem().toString()), condition);
					operator.dump(operator, outputDir);
				}

				println("----------------------Next Statement-------------------------");

			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
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
	public static void println(String sentence){
		System.out.println(sentence);
	}
	public static void print(String sentence){
		System.out.print(sentence);
	}
}
