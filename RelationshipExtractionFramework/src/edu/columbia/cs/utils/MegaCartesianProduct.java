package edu.columbia.cs.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;



public class MegaCartesianProduct {
	public static <A,B> List<Map<A,B>> generateAllPossibilities(Map<A,Set<B>> input){
		List<Pair<A,Set<B>>> in = new ArrayList<Pair<A,Set<B>>>();
		
		for(Entry<A,Set<B>> entry : input.entrySet()){
			in.add(new Pair<A,Set<B>>(entry.getKey(),entry.getValue()));
		}
		
		return generateAllPossibilities(in);
	}
	
	private static <A,B> List<Map<A,B>> generateAllPossibilities(List<Pair<A,Set<B>>> input){
		if(input.size()==0){
			List<Map<A,B>> l = new ArrayList<Map<A,B>>();
			l.add(new HashMap<A,B>());
			return l;
		} else {
			Pair<A, Set<B>> entry = input.get(0);
			
			List<Pair<A,Set<B>>> newList = new ArrayList<Pair<A,Set<B>>>();
			for(int i=1;i<input.size();i++){
				newList.add(input.get(i));
			}
			
			List<Map<A,B>> results = generateAllPossibilities(newList);
			List<Map<A,B>> newResults = new ArrayList<Map<A,B>>();
			
			for(B newEntry : entry.b()){
				for(Map<A,B> previousMap : results){
					Map<A,B> newMap = new HashMap<A,B>();
					for(Entry<A,B> e : previousMap.entrySet()){
						newMap.put(e.getKey(), e.getValue());
					}
					newMap.put(entry.a(), newEntry);
					newResults.add(newMap);
				}
			}
			return newResults;
		}
	}
	
	public static <A,B> List<Map<B,A>> invert(List<Map<A,B>> input){
		List<Map<B,A>> newList = new ArrayList<Map<B,A>>();
		
		for(Map<A,B> i : input){
			Map<B,A> newMap = new HashMap<B,A>();
			for(Entry<A,B> entry : i.entrySet()){
				newMap.put(entry.getValue(), entry.getKey());
			}
			newList.add(newMap);
		}
		
		return newList;
	}
	
	public static void main(String[] args){
		Map<Integer,Set<String>> input = new HashMap<Integer,Set<String>>();
		Set<String> list1 = new HashSet<String>();
		list1.add("A");
		list1.add("B");
		list1.add("C");
		Set<String> list2 = new HashSet<String>();
		list2.add("D");
		list2.add("E");
		Set<String> list3 = new HashSet<String>();
		list3.add("F");
		list3.add("G");
		input.put(1, list1);
		input.put(2, list2);
		input.put(3, list3);
		System.out.println(input);
		System.out.println(generateAllPossibilities(input));
	}
}
