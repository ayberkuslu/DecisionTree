package tobb.etu.decisionTree;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class DecisionTree {

	//Root of the tree
	private Node Root;
	//number of nodes in decision tree
	private int DTsize;

	public int size() {
		return DTsize;		
	}

	public Node getRoot() {
		return Root;		
	}

	//create and return a new node r storing element e and
	//make r the root of the tree.
	public Node addRoot(String s) {

		//IMPLEMENT HERE

		Node r = new Node(s);
		Root = r;
		DTsize++;
		return Root;
	}

	//create and return a new node w storing element el and 
	//make w the left (yes) child of v. Make sure to increment the size.
	//make sure to update both child's parent link and parent's child link.
	//throws a RuntimeException if the node has already a left child.
	public Node insertYes(Node v, String el) {

		//IMPLEMENT HERE
		Node w = new Node(el);
		w.setParent(v);

		if(v.hasLeft())
			throw new RuntimeException();

		v.setLeft(w);


		DTsize++;

		return w;
	}

	//create and return a new node w storing element el and 
	//make w the right (no) child of v. Make sure to increment the size.
	//make sure to update both child's parent link and parent's child link.
	//throws a RuntimeException if the node has already a right child.
	public Node insertNo(Node v, String el) {

		//IMPLEMENT HERE

		Node w = new Node(el);
		w.setParent(v);

		if(v.hasRight())
			throw new RuntimeException();

		v.setRight(w);

		DTsize++;

		return w;
	}

	//remove node v, replace it with its child, if any, and return 
	//the element stored at v. make sure to decrement the size.
	//make sure to update both child's parent link and parent's child link.
	//throws a RuntimeException if v has two children
	public String remove(Node v) {

		//IMPLEMENT HERE

		//	String s = v.getElement();
		if(v.hasLeft() && v.hasRight()) // 2 cocuk varsa
			throw new RuntimeException();

		Node temp = new Node(v.getElement());
		temp.setLeft(v.getLeft());
		temp.setRight(v.getRight());
		temp.setParent(v.getParent());
		if( (!v.hasLeft()) && !( v.hasRight() ) ){ // hic cocuk yoksa
			if(v.getParent().getLeft().getElement().equals(v.getElement()))
				v.getParent().setLeft(null);
			else 
				v.getParent().setRight(null);
		}

		else if(v.hasLeft()) { // tek cocuk varsa
			v.getLeft().setParent(v.getParent());
			v.getParent().setLeft(temp.getLeft());						
		}else {
			v.getRight().setParent(v.getParent());
			v.getParent().setRight(temp.getRight());
		}
		DTsize--;
		return temp.getElement();
	}

	//write to file in PREORDER traversal order
	//Handle file exceptions as follows: If an exception is thrown, return false.
	//Otherwise, return true.	
	public boolean save(String filename) {

		//IMPLEMENT HERE	
		PrintWriter output = null;

		try {

			output = new PrintWriter(filename);			
			savePreorder(Root,output);		

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}catch (IOException e) {
			e.printStackTrace();
			return false;			
		}finally {
			output.close();
		}
		return true;
	}


	public void savePreorder(Node node,PrintWriter a)
	{
		if (node == null)
			return;

		/* first node */
		// System.out.print(node.getElement() + " ");
		// a.(node.getElement() + " " );

		a.println(node.getElement());

		/* then left child */
		//if(node.hasLeft())
		savePreorder(node.getLeft(),a);
		/* right child */
		//if(node.hasRight())
		savePreorder(node.getRight(),a);
	}


	public void printPreorder(Node node)
	{
		if (node == null)
			return;

		/* first node */
		// System.out.print(node.getElement() + " ");
		// a.(node.getElement() + " " );

		System.out.println(node.getElement());

		/* then left child */
		//if(node.hasLeft())
		printPreorder(node.getLeft());
		/* right child */
		//if(node.hasRight())
		printPreorder(node.getRight());
	}









	//load the DT from file that contains the output of PREORDER traversal
	//Handle file exceptions as follows: If an exception is thrown, return false.
	//Otherwise, return true.
	//You can distinguish a leaf node from an internal node as follows: internal nodes always end with
	//a question mark.	
	public boolean load(String filename) {

		//IMPLEMENT HERE

		Scanner input =null;

		Stack<Node> stackN = new Stack<Node>();
		StringBuilder s= new StringBuilder();

		try {
			input = new Scanner(new File (filename));
			while (input.hasNextLine()) {
				stackN.push(new Node(input.nextLine()));	
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		//System.out.println(s);

		//System.out.println(stackN.size() + " stack n");
		ArrayList<Node> arrayS = new ArrayList<Node>();

		for( ; stackN.size() > 0 ;)
			arrayS.add(stackN.pop());

		for(int i = 0 ;arrayS.size() > i ; i++) {			
			if(!isQuestion(arrayS.get(i).getElement())) {
				Node temp = arrayS.get(i);
				stackN.push(temp);
				DTsize++;
			}
			else  {
				stackN.push(arrayS.get(i));
				Node temp1 = stackN.pop();
				Node tempL = stackN.pop();
				Node tempR = stackN.pop();
				temp1.setLeft(tempL);
				temp1.setRight(tempR);
				tempL.setParent(temp1);
				tempR.setParent(temp1);
				stackN.push(temp1);
				DTsize++;
			}		
		}
		//System.out.println("girmedi");
//		System.out.println("//////");
//		System.out.println(DTsize);
//		System.out.println("//////\\\\");

		Root = stackN.pop();

		//printPreorder(Root);

		return true;
	}

	public static boolean isQuestion(String s) {


		return (s.indexOf('?') >= 0 );

	}

}
