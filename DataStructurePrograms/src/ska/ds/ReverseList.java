package ska.ds;
/**
 * 
 */

/**
 * @author Shubham (tech.shubham@gmail.com)
 * Reverse a singly linked List
 * https://leetcode.com/problems/reverse-linked-list/
 */
public class ReverseList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int size = 2;
		Node prev = null;
		Node start = null;
		for(int i=1; i<=size; i++){
			Node node = new Node(i);
			if(start == null ){
				start = node;
				prev = node;
			}else{
				prev.next = node;
				prev = prev.next;
			}

		}

		//Recursive reverse list
		reverseListRec(start);
		
		//iterative reverse list
		reverseListIterative(start, size);

	}

	private static void reverseListIterative(Node start,int size) {
		
		Node trav = start;
		while(trav != null){
			System.out.println(trav.data);
			trav = trav.next;
		}
		Node n1,n2,n3;
		if(size<1){
			System.out.println("Invalid input");
			return;
		}else if(size ==1){
			return;
		}
		else if(size ==2){
			n1 = start;
			n2 = n1.next;
			n1.next = null;
			n2.next = n1;
			start = n2;
			return;
		}
		else{
			n1 = start;
			n2 = start.next;
			n3 = n2.next;
			n1.next = null;
			while(n3.next != null){
				n2.next = n1;
				n1 = n2;
				n2 = n3;
				n3 = n3.next;

			}
			start = n3;
			n3.next = n2;
			n2.next = n1;

		}
		trav = start;
		while(trav != null){
			System.out.println(trav.data);
			trav = trav.next;
		}

		
	}

	// Recursive method
	public static Node reverseListRec(Node head) {

		if(head == null || head.next == null){
			return head;
		}

		Node second = head.next;
		head.next = null;
		Node rest = reverseListRec(second);
		second.next = head;


		return rest;
	}
	
	//Node class
	public static class Node{
		int data;
		Node next;

		public Node(int data){
			this.data = data;
			next = null;
		}
	}

}
