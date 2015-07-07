package dis.materialien;

import java.util.ArrayList;
import java.util.Collections;

public class ItemSet {
	
	private ArrayList<String> items;
	public int count;
	
	public ItemSet(){
		items = new ArrayList<String>();
		count = 0;
	}
	
	public ItemSet(String s){
		items = new ArrayList<String>();
		items.add(s);
		count = 0;
	}
	
	public ArrayList<String> getArray(){
		return items;
	}
	
	public void add(String s){
		items.add(s);
	}
	
	public boolean checkCompatability(ItemSet i){
		int counter = 0;
		for(String s: i.getArray()){
			if(items.contains(s)){
				counter++;
			}
		}
		if(counter == i.getArray().size()-1){
			return true;
		}
		return false;
	}
	
	public ItemSet combine(ItemSet i){
		ItemSet result = new ItemSet();
		for(int x = 0; x < items.size(); x++){
			if(!result.contains(items.get(x))){
				result.add(items.get(x));
			}
			if(!result.contains(i.get(x))){
				result.add(i.get(x));
			}
		}
		return result;
	}
	
	public String get(int index){
		return items.get(index);
	}
	
	public boolean contains(String s){
		return items.contains(s);
	}
	
	public ArrayList<ItemSet> getSubsets(){
		ArrayList<ItemSet> iList = new ArrayList<ItemSet>();
		for(String s1: items){
			ItemSet i = new ItemSet();
			for(String s2: items){
				if(!s2.equals(s1)){
					i.add(s2);
				}
			}
			iList.add(i);
		}
		return iList;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof ItemSet){
			ArrayList<String> h1 = items;
			ArrayList<String> h2 = ((ItemSet) obj).getArray();
			Collections.sort(h1);
			Collections.sort(h2);
			ItemSet i = (ItemSet)obj;
			return h1.equals(h2);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		ArrayList<String> hashlist = items;
		Collections.sort(hashlist);
		return hashlist.hashCode();
	}
}
