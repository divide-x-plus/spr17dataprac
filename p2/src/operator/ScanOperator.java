package operator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import project2.*;

public class ScanOperator extends Operator{
	private File file;
	BufferedReader bufferReader;
	
	public ScanOperator(File file) throws FileNotFoundException{
		this.file = file;
		bufferReader = new BufferedReader(new FileReader(file));
	}

	@Override
	public void reset() throws FileNotFoundException {
		// Reading from the beginning of the files
		bufferReader = new BufferedReader(new FileReader(file));
	}

	@Override
	public void dump(Operator operator) throws IOException {
		// TODO Auto-generated method stub
		Tuple tuple = getNextTuple();
		while(tuple != null){
			System.out.println("scan operator dumping " + tuple.toString());
			tuple = getNextTuple();
		}
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
