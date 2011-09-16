package edu.columbia.cs.utils;
import java.util.HashMap;
import java.util.Map;


public class POStoGenericPOSConverter {
	
	private static Map<String,String> mapping;
	
	static{
		mapping = new HashMap<String,String>();
		mapping.put("JJ", "Adjective");
		mapping.put("JJR", "Adjective");
		mapping.put("JJS", "Adjective");
		mapping.put("NN", "Noun");
		mapping.put("NNS", "Noun");
		mapping.put("NNP", "Noun");
		mapping.put("NNPS", "Noun");
		mapping.put("RB","Adverb");
		mapping.put("RBR","Adverb");
		mapping.put("RBS","Adverb");
		mapping.put("VB", "Verb");
		mapping.put("VBD", "Verb");
		mapping.put("VBG", "Verb");
		mapping.put("VBN","Verb");
		mapping.put("VBP","Verb");
		mapping.put("VBZ","Verb");
	}
	
	public static String convertPOS(String tag){
		String t = mapping.get(tag);
		if(t==null){
			return "Other";
		}else{
			return t;
		}
	}
}
