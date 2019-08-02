package tobb.etu.decisionTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class AnimalPredictor {
	
	//use this as the decision tree (DT) in animal prediction game
	private static DecisionTree DT = new DecisionTree();

	//traverse the decision tree to predict the animal.
	//update decision tree if the guess is wrong.
	public static void guess_and_update(DecisionTree DT) {
		Node cur = DT.getRoot();
		Scanner s = new Scanner(System.in);
		do {
			if (cur.isExternal()) {
					System.out.println("I think  your animal is " + cur.getElement());
					System.out.println("Is it correct ?");
					String ans = s.nextLine();
					if (ans.equals("yes") || ans.equals("evet")) {
						System.out.println("Oley!");
						return;
					}
				else { //have to update DT
					System.out.println("What was it?");
					String h = s.nextLine();
					System.out.println("Can you tell me a yes/no question to decide " + h + "'or " + cur.getElement() + "  ?");
					System.out.println("(Yes answer should be for" + h + " .)");
					String q = s.nextLine();
					DT.insertYes(cur,h);
					DT.insertNo(cur,cur.getElement());
					cur.setElement(q+"?");
					System.out.println( "I learned "+ h  );
					return;
				}
			}
			else {
				System.out.println(cur.getElement());
				String ans = s.nextLine();
				if (ans.equals("evet") || ans.equals("yes"))
					cur = cur.getLeft();
				else
					cur = cur.getRight();				
			}
			
		} while (true);		
	}
	
	//simulates the animal prediction game by reading the interactions from a file
	//returns the final prediction corresponding to the decision tree and query file.
	//if there is any mismatch between DT and query file return empty string.
	//DTFile : Decision Tree File
	//queryFile : list of interactions to reach to a decision.
	public static String guess(String DTFile, String queryFile) {
		
		//IMPLEMENT HERE

		Scanner scanQuery = null;
		
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(""); // ilk indexi dummy yaptim
	
		
		try {
			scanQuery = new Scanner(new File(queryFile));
		
				while(scanQuery.hasNextLine()) {
					list.add(scanQuery.nextLine());
				}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanQuery.close();
		
		DT.load(DTFile);
		
		Node temp = DT.getRoot();
		int i = 1 ; // kacýncý komuttayiz listenin ilk indexi dummy di

		while(i < list.size()) {
			
			 if(list.get(i).equals("evet") || list.get(i).equals("yes")) {
			temp = temp.getLeft();
			}else if(list.get(i).equals("hayir") || list.get(i).equals("no")) {
				temp=temp.getRight();
			}
			else if(!list.get(i).equals(temp.getElement())){	
					return "";
				}
						i++;
		}
				return temp.getElement();
	} 
	
	//the main method
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("Is there any file that you want to upload?");
		String ans = s.nextLine();
		if (ans.equals("evet") || ans.equals("yes")) {
			System.out.println("File name :");
			if (!DT.load(s.nextLine()))
				System.out.println("File could not uploaded");
		}
		
		if (DT.size() == 0) {
			System.out.println("I dont know any animal now.");
			System.out.println("Can you tell me a animal name?");
			String h1 = s.nextLine();
			System.out.println("One more..");
			String h2 = s.nextLine();
			System.out.println("Can you tell me a yes/no question to decide " + h1 + "'or " + h2 + "  ?");
			System.out.println("(Yes answer should be for" + h1 + " .)");
			String s1 = s.nextLine();
			Node R = DT.addRoot(s1);
			DT.insertYes(R, h1);
			DT.insertNo(R, h2);
		}
		String c ="";
		while (!c.equals("hayir") || !c.equals("no")) {
			System.out.println("Pick a animal in your mind, i will try to find it.");
			System.out.println("Press enter when you are ready");
			s.nextLine();
			guess_and_update(DT);
			System.out.println("Do you want to play again ?");
			c = s.nextLine();		
		}
		
		System.out.println("Do you want to save learned decision tree.");
		ans = s.nextLine();
		if (ans.equals("evet") || ans.equals("yes")) {
			System.out.println("File name :");
			DT.save(s.nextLine());
		}			
	}
}
