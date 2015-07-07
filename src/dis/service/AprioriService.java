package dis.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import dis.materialien.ItemSet;

public final class AprioriService {
	
	private static final AprioriService apriori;
	private ArrayList<HashSet<String>> Transactions;
	private double minsub = 0.01;
	private ArrayList<HashSet<ItemSet>> L = new ArrayList<HashSet<ItemSet>>();
	
	
	static {
		try {
			apriori = new AprioriService();
		}
		catch(Throwable e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public AprioriService() {
		
	}
	
	static public AprioriService getInstance(){
		return apriori;
	}
	
	public void apriori(String filename){
		readTransactions(filename);
		L.add(findFrequentSingleItemsets());
		ArrayList<HashSet<ItemSet>> C = new ArrayList<HashSet<ItemSet>>();
		C.add(new HashSet<ItemSet>());
		//System.out.print("Single ItemSets found");
		for(int k = 1; !L.get(k-1).isEmpty(); k++){
			C.add(k, generateCandidates(L.get(k-1)));
			//System.out.println("Candidates generated");
			int count = 0;
			for(HashSet<String> t: Transactions){
				int count2 = 0;
				for(ItemSet candidate: C.get(k)){
					if(t.containsAll(candidate.getArray())){
						candidate.count++;
						//System.out.print("Count increased");
					}
				}
			}
			//System.out.print("Transactions through"); 
			ArrayList<ItemSet> items = new ArrayList<ItemSet>(C.get(k));
			for(int x = 0; x < items.size(); x++){
				if(minsub > ((double)items.get(x).count/((double)Transactions.size()))){
					items.remove(x);
					x--;
				}
			}
			C.set(k, new HashSet<ItemSet>(items));
			//System.out.print("Removal finished");
			L.add(C.get(k));
			//System.out.print(k + "");
		}
		for(int x = 0; x < L.size(); x++){
			String output = ("L" + (x+1) + ": ");
			for(ItemSet i: L.get(x)){
				output += "(";
				for(String s: i.getArray()){
					output += s + " ";
				}
				output += ") ";
			}
			System.out.printf(output + "%n");
		}
		clear();
	}
	
	private void clear(){
		L = new ArrayList<HashSet<ItemSet>>();
		Transactions = null;
	}
	
	private HashSet<ItemSet> generateCandidates(HashSet<ItemSet> l){
		HashSet<ItemSet> c = new HashSet<ItemSet>();
		for(ItemSet i1: l){
			for(ItemSet i2: l){
				if(!i1.equals(i2) && i1.checkCompatability(i2)){
					ItemSet c1 = i1.combine(i2);
					if(!prune(c1, l)){
						c.add(c1);
					}
				}
			}
		}			
		return c;
	}
	
	private boolean prune(ItemSet c, HashSet<ItemSet> l){
		ArrayList<ItemSet> list = new ArrayList<ItemSet>(l);
		for(ItemSet i: c.getSubsets()){
			if(!l.contains(i)){
				return true;
			}
		}
		return false;
	}
	
	private HashSet<ItemSet> findFrequentSingleItemsets(){
		ArrayList<String> aStrings = new ArrayList<String>();
		ArrayList<ItemSet> fItemSet = new ArrayList<ItemSet>();
		ArrayList<Integer> support = new ArrayList<Integer>();
		for(HashSet<String> transaction:Transactions){
			for(String s: transaction){
				ItemSet i = new ItemSet(s);
				if(!aStrings.contains(s)){
					fItemSet.add(i);
					aStrings.add(s);
					support.add(0);
				} 
				int index = aStrings.indexOf(s);
				fItemSet.get(fItemSet.indexOf(i)).count++;
			}
		}
		int size = Transactions.size();
//		ItemSet remove = null;
//		for(ItemSet i: fItemSet){
//			fItemSet.remove(remove);
//			if(minsub > i.count/(Transactions.size())){
//				remove = i;
//			}
//		}
		for(int x = 0; x < aStrings.size(); x++){
			if(minsub > (double)fItemSet.get(x).count/size){
				support.remove(x);
				fItemSet.remove(x);
				aStrings.remove(x);
			}
		}
		return new HashSet<ItemSet>(fItemSet);
	}
	
//	for(HashSet<String> t: Transactions){
//				for(ItemSet candidate: C.get(k)){
//					boolean contains = true;
//					for(String s: candidate.getArray()){
//						if(!t.contains(s)){
//							contains = false;
//						}
//						t.contains(C.get(k));
//					}
//					if(contains){
//						candidate.count++;
//					}
//				}
//			}
	
	
	private void readTransactions(String filename){
		Transactions = new ArrayList<HashSet<String>>();
		try{
			Scanner scan = new Scanner(new FileInputStream(new File(filename)), "Windows-1252");
			//scan.nextLine();
			int counter = 0;
			while(scan.hasNextLine()){
				String[] transaction = scan.nextLine().split(" ");
				Transactions.add(new HashSet<String>(Arrays.asList(transaction)));
				
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
