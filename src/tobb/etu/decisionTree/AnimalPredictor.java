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
					System.out.println("Bence tuttugunuz hayvan " + cur.getElement());
					System.out.println("Tahminim dogru mu?");
					String ans = s.nextLine();
					if (ans.equals("evet")) {
						System.out.println("Oley!");
						return;
					}
				else { //have to update DT
					System.out.println("Ne tutmustunuz?");
					String h = s.nextLine();
					System.out.println("Bana " + h + "'yi " + cur.getElement() + "'den ayirmami saglayacak bir evet/hayir sorusu soyler misiniz?");
					System.out.println("(Evet cevabi " + h + " icin olmalidir.)");
					String q = s.nextLine();
					DT.insertYes(cur,h);
					DT.insertNo(cur,cur.getElement());
					cur.setElement(q+"?");
					System.out.println(h + " 'yi ogrendim.");
					return;
				}
			}
			else {
				System.out.println(cur.getElement());
				String ans = s.nextLine();
				if (ans.equals("evet"))
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
		int i = 1 ; // kac�nc� komuttayiz listenin ilk indexi dummy di

		while(i < list.size()) {
			
			 if(list.get(i).equals("evet")) {
			temp = temp.getLeft();
			}else if(list.get(i).equals("hayir")) {
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
		System.out.println("Yuklemek istediginiz bir dosya var mi?");
		String ans = s.nextLine();
		if (ans.equals("evet")) {
			System.out.println("Dosya ismi :");
			if (!DT.load(s.nextLine()))
				System.out.println("Dosya yuklenemedi");
		}
		
		if (DT.size() == 0) {
			System.out.println("Su anda hic hayvan bilmiyorum.");
			System.out.println("Bana bir hayvan ismi soyler misiniz?");
			String h1 = s.nextLine();
			System.out.println("Bir tane daha..");
			String h2 = s.nextLine();
			System.out.println("Bana  " + h1 + "'yi " + h2 + "'den ayirmami saglayacak bir evet/hayir sorusu soyler misiniz?");
			System.out.println("(Evet cevabi " + h1 + " icin olmalidir.)");
			String s1 = s.nextLine();
			Node R = DT.addRoot(s1);
			DT.insertYes(R, h1);
			DT.insertNo(R, h2);
		}
		String c ="";
		while (!c.equals("hayir")) {
			System.out.println("Aklinizdan bir hayvan tutun, ben onu tahmin etmeye calisacagim.");
			System.out.println("Hazir olunca enter'a basin lutfen.");
			s.nextLine();
			guess_and_update(DT);
			System.out.println("Bir daha oynamak ister misiniz?");
			c = s.nextLine();		
		}
		
		System.out.println("Ogrenilen karar agacini kaydetmek ister misiniz?");
		ans = s.nextLine();
		if (ans.equals("evet")) {
			System.out.println("Dosya ismi :");
			DT.save(s.nextLine());
		}			
	}
}
