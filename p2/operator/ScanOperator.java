package operator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project2.*;
import project2.QueryPlanner.Node;
import net.sf.jsqlparser.schema.Table;

public class ScanOperator extends Operator {
	
	private File file; //input file
	BufferedReader bufferReader;
	ArrayList<Tuple> tupleList; 
	HashMap<String, List<Tuple>> tables; 
	String outputDir;
	
	
	public ScanOperator(File file){
		this.file = file;
		try{
			bufferReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		tupleList = null; 
	}
	
	//TODO: what's the use of the second constructor?
	public ScanOperator(){
		tables = new HashMap<String, List<Tuple>>();
		tupleList = null;
	}
	
	@Override
	public Tuple getNextTuple(Node<Operator> node, QueryInterpreter interpreter) throws IOException{
		if (file == null) return null;
		String line = bufferReader.readLine();
		if (line != null){
			String[] data = line.split(",");
			Tuple tuple = new Tuple(data);
			return tuple;
		}
		return null;
	}
	
	
	@Override
	public void reset(Node<Operator> node, Operator operator) throws FileNotFoundException {
		//instantiate a new BufferedReader off the same file to reset 
		bufferReader = new BufferedReader(new FileReader(file));
	}
	
	@Override
	public ArrayList<Tuple> dump(Node<Operator> node, QueryInterpreter interpreter) throws IOException {
		Table table = (Table) interpreter.getFromItem(); 
		file = new File(ParserExample.inputDir + "/data/" + table.getName());
		bufferReader = new BufferedReader(new FileReader(file));
		Tuple tuple = getNextTuple(node, interpreter);
		while (tuple != null){
			tupleList.add(tuple);
			tuple = getNextTuple(node, interpreter);
		}
		return tupleList;
	}
	
	public String getDirectory(){
		return outputDir;
	}	
}
