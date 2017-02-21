package cs4321.project1;

import static org.junit.Assert.*;
import cs4321.project1.list.*;

import org.junit.Test;

public class PrintListVisitorTest {

    /**
     * added more test cases for complex expression list
     */
    @Test
    public void testComplexExpressionList2(){
        ListNode n1 = new NumberListNode(17.0);
        ListNode n2 = new NumberListNode(6.0);
        ListNode n3 = new AdditionListNode();
        ListNode n4 = new NumberListNode(1.0);
        ListNode n5 = new UnaryMinusListNode();
        ListNode n7 = new MultiplicationListNode();
        ListNode n8 = new NumberListNode(2.0);
        ListNode n9 = new UnaryMinusListNode();
        ListNode n10 = new DivisionListNode();
        
        
        n1.setNext(n2);
        n2.setNext(n3);
        n3.setNext(n4);
        n4.setNext(n5);
        n5.setNext(n7);
        n7.setNext(n8);
        n8.setNext(n9);
        n9.setNext(n10);
        PrintListVisitor pv1 = new PrintListVisitor();
        n1.accept(pv1);
        assertEquals("17.0 6.0 + 1.0 ~ * 2.0 ~ /", pv1.getResult());
    }
    
    /**
     * added more test cases for complex expression list
     */
    @Test
    public void testComplexExpressionList3(){
        ListNode n1 = new NumberListNode(1.0);
        ListNode n2 = new UnaryMinusListNode();
        ListNode n3 = new UnaryMinusListNode();
        ListNode n4 = new NumberListNode(6.0);
        ListNode n5 = new UnaryMinusListNode();
        ListNode n6 = new AdditionListNode();
        ListNode n7 = new NumberListNode(1.0);
        ListNode n8 = new UnaryMinusListNode();
        ListNode n9 = new DivisionListNode();
        
        n1.setNext(n2);
        n2.setNext(n3);
        n3.setNext(n4);
        n4.setNext(n5);
        n5.setNext(n6);
        n6.setNext(n7);
        n7.setNext(n8);
        n8.setNext(n9);
        PrintListVisitor pv1 = new PrintListVisitor(); 
        n1.accept(pv1);
        assertEquals("1.0 ~ ~ 6.0 ~ + 1.0 ~ /", pv1.getResult());
    }

	@Test
	public void testSingleNumberNode() {
		ListNode n1 = new NumberListNode(1.0);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0", pv1.getResult());
	}
	
	@Test
	public void testAdditionSimplePrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n3.setNext(n2);
		n2.setNext(n1);
		PrintListVisitor pv1 = new PrintListVisitor();
		n3.accept(pv1);
		assertEquals("+ 2.0 1.0", pv1.getResult());
	}
	
	@Test
	public void testAdditionSimplePostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 +", pv1.getResult());
	}
}
