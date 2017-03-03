import java.io.FileReader;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * Example class for getting started with JSQLParser. Reads SQL statements from
 * a file and prints them to screen; then extracts SelectBody from each query
 * and also prints it to screen.
 * 
 * @author Lucja Kot
 */
public class ParserExample {

	private static final String queriesFile = "queries.sql";

	public static void main(String[] args) {
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
//				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				printf("Select body: " + select.getSelectBody());
				PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
				printf("Selection item: " + plainSelect.getSelectItems());
//				FromItem fromItem = (SubSelect) plainSelect.getFromItem();
				printf("From item: "  + plainSelect.getFromItem());
				printf("From join: "  + plainSelect.getJoins());
				printf("Distinct: " + plainSelect.getDistinct());
				printf("Where: " + plainSelect.getWhere());
				printf("order by: " + plainSelect.getOrderByElements());
				
				
				
				printf("----------------------Next Statement-------------------------");
				
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}
	public static void printf(String sentence){
		System.out.println(sentence);
	}
}