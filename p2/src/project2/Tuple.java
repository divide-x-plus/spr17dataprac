package project2;
import java.util.ArrayList;
import java.util.List;

public class Tuple {
	private String[] tuple;
	public Tuple(String data[]){
		tuple = data;
	}
	
	Tuple(){}
	
	@Override
	public String toString(){
		String result = "";
		for(String str: tuple){
			result += str + " ";
		}
		return result;
	}
	
	public String[] getData(){
		return tuple;
	}
}
