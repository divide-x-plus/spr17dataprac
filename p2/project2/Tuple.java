package project2;

/**
 * Tuple encapsulates a list of inputs for a certain relation, associated
 * with the table registered as field. 
 * @author Ella Xue, Jim Li
 */
public class Tuple {
	private String[] tuple;
	private String table;
	
	public Tuple(String[] data){
		tuple = data;
	}
	
	public Tuple(){}
	
	public String[] getData(){
		return tuple;
	}
	
	public String getTableName(){
		return table;
	}
	
	public void setTable(String table){
		this.table = table;
	}
	
	public void setData(String[] data){
		this.tuple = data;
	}
	
	@Override
	public String toString(){
		String result = "";
		for (String str : tuple){
			result += str + " ";
		}
		return result;
	}
}
