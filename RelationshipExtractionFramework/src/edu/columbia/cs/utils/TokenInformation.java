package edu.columbia.cs.utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TokenInformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2805314937535969201L;
	private List<String> features;
	private boolean isInShortestPath = false;
	private boolean isEntity1=false;
	private boolean isEntity2=false;

	public TokenInformation(){
		features=new ArrayList<String>();
	}
	
	public void add(String feature){
		features.add(feature);
	}
	
	public double getSimilarity(TokenInformation other){
		if(isInShortestPath!=other.isInShortestPath){
			return 0;
		}
		
		if(isEntity1()!=other.isEntity1()){
			return 0;
		}
		
		if(isEntity2()!=other.isEntity2()){
			return 0;
		}
		
		if(!isEntity1() && !isEntity2()){
			int sizeFeats=features.size();
			int inner=0;
			for (int i = 0; i < sizeFeats; i++){
				if(features.get(i).equals(other.features.get(i))){
					inner++;
				}
			}
			
			return inner/Math.sqrt(sizeFeats*sizeFeats);
		}
		
		int numFeats1=0;
		int numFeats2=0;
		double inner=0;
		int sizeFeats=features.size();		
		for(int i=0; i<sizeFeats; i++){
			if(features.get(i).equals(other.features.get(i))){
				inner++;
			}
		}
			
		
		numFeats1+=features.size();
		numFeats2+=other.features.size();
		
		return inner/Math.sqrt(numFeats1*numFeats2);
	}
	
	public List<String> getOwnFeatures(){
		return features;
	}
	
	public String toString(){
		return features + "|" + isEntity1 + "|" + isEntity2;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof TokenInformation){
			TokenInformation other =(TokenInformation) o;

			if(isInShortestPath!=other.isInShortestPath){
				return false;
			}
			
			if(isEntity1()!=other.isEntity1()){
				return false;
			}
			
			if(isEntity2()!=other.isEntity2()){
				return false;
			}
			
			for(int i=0; i<features.size(); i++){
				if(!features.get(i).equals(other.features.get(i))){
					return false;
				}
			}

			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return features.get(0).hashCode();
	}

	public void setInShortestPath(boolean isInShortestPath) {
		this.isInShortestPath = isInShortestPath;
	}

	public boolean isInShortestPath() {
		return isInShortestPath;
	}
	
	public void setIsEntity1(boolean v, String type){
		isEntity1=v;
	}
	
	public void setIsEntity2(boolean v, String type){
		isEntity2=v;
		
	}
	
	public boolean isEntity1(){
		return isEntity1;
	}
	public boolean isEntity2(){
		return isEntity2;
	}
	
	public TokenInformation copyNoShortestPath(){
		TokenInformation result = new TokenInformation();
		result.features=features;
		result.isEntity1=isEntity1;
		result.isEntity2=isEntity2;
		return result;
	}
}
