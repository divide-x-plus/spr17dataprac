package operator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import project2.*;

public class ScanOperator extends Operator{
	private File file;
	BufferedReader bufferReader;
	
	public ScanOperator(File file){
		this.file = file;
		try {
			bufferReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void reset() throws FileNotFoundException {
		// Reading from the beginning of the files
		bufferReader = new BufferedReader(new FileReader(file));
	}

	@Override
	public void dump(Operator operator, String outputDir) throws IOException {
		// TODO Auto-generated method stub
		Tuple tuple = getNextTuple();
		PrintWriter writer = new PrintWriter(outputDir+"/query"+(ParserExample.fileCounter++));
		while(tuple != null){
			writer.println(tuple.toString());
//			System.out.println("scan operator dumping " + tuple.toString());
			tuple = getNextTuple();
		}
		writer.close();
	}
	
	@Override
	public Tuple getNextTuple() throws IOException {
		
		if(file == null) return null;
		String line = bufferReader.readLine();
		if(line != null){
			String data[] = line.split(",");
			Tuple tuple = new Tuple(data);
			return tuple;
		}
		return null;
	}

}
