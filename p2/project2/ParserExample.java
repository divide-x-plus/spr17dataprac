package project2;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.schema.*;

/**
 * Example class for getting started with JSQLParser. Reads SQL statements from
 * a file and prints them to screen; then extracts SelectBody from each query
 * and also prints it to screen.
 * 
 * @author Lucja Kot
 * @Modified Ella Xue, Jim Li
 */

public class ParserExample {

	private static final String queriesFile = "queries.sql";
	public static String inputDir;
	public static String outputDir;
	
	/*
	 * Below is a set of fields that maintain a database catalog.
	 */
	public static int fileCounter = 1; //for naming output files
	//schema maintains tableName and its column attributes that allow column indexing
	public static HashMap<String, HashMap<String, Integer>> schema = new HashMap<String, HashMap<String, Integer>>();
	//schema2 maintains tableName with Column objects (which doesn't have indexing)
	public static HashMap<String, Column[]> schema2 = new HashMap<String, Column[]>();
	
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
				System.out.println("intepreting statement...");
					QueryInterpreter queryInter = new QueryInterpreter(statement);
					QueryPlanner queryPlan = new QueryPlanner(queryInter);
					queryInter.setQueryPlan(queryPlan.getQueryPlan());
					queryInter.printQueryPlan(queryPlan.getRoot());
					queryInter.executeQuery(queryPlan.getRoot());
				
				//static method	
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
			while((line = input.readLine()) != null) {
				
				String items[] = line.split(" ");
				String name = items[0]; //first element is always name of table
				Table table = new Table();
				table.setName(name);
				//add column attributes to table
				HashMap<String, Integer> columnAttrMap = new HashMap<String, Integer>();
				Column[] tableColumns = new Column[items.length - 1];

				//create table columns with all table column info
				for(int i = 1; i < items.length; i++) {
					//Column constructor has the form of (table, columnName)
					tableColumns[i-1] = new Column(table, items[i]);
					columnAttrMap.put(items[i], i-1); //Integer is the index of the columns
				}

				//table name matches its table columns
				schema2.put(name, tableColumns);
				schema.put(name,columnAttrMap);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		printSchema(schema);
	}
	
	public static void printSchema(HashMap<String, HashMap<String,Integer>> map){
		for(Entry<String, HashMap<String,Integer>> entry: map.entrySet()){
			print(entry.getKey() + " ");
			for(Entry<String, Integer> attrMap: entry.getValue().entrySet()){
				print(attrMap.getKey()+ " ");
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

