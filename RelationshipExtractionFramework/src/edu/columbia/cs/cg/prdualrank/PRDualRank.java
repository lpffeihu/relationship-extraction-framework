package edu.columbia.cs.cg.prdualrank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import edu.columbia.cs.api.PatternBasedRelationshipExtractor;
import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.graph.PRDualRankGraph;
import edu.columbia.cs.cg.prdualrank.graph.generator.ExtractionGraphGenerator;
import edu.columbia.cs.cg.prdualrank.graph.generator.SearchGraphGenerator;
import edu.columbia.cs.cg.prdualrank.inference.InferencePRDualRank;
import edu.columbia.cs.cg.prdualrank.inference.convergence.NumberOfIterationsConvergence;
import edu.columbia.cs.cg.prdualrank.inference.quest.MapBasedQuestCalculator;
import edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction;
import edu.columbia.cs.cg.prdualrank.model.PRDualRankModel;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.PatternExtractor;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.impl.ExtractionPatternExtractor;
import edu.columbia.cs.cg.prdualrank.pattern.extractor.impl.SearchPatternExtractor;
import edu.columbia.cs.cg.prdualrank.searchengine.QueryGenerator;
import edu.columbia.cs.cg.prdualrank.searchengine.SearchEngine;
import edu.columbia.cs.cg.relations.Entity;
import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.constraints.roles.RoleConstraint;
import edu.columbia.cs.cg.relations.entity.matcher.EntityMatcher;
import edu.columbia.cs.engine.Engine;
import edu.columbia.cs.model.Model;
import edu.columbia.cs.og.structure.OperableStructure;
import edu.columbia.cs.og.structure.impl.RelationOperableStructure;
import edu.columbia.cs.utils.MegaCartesianProduct;

public class PRDualRank implements Engine{

	private SearchEngine se;
	private QueryGenerator qg;
	private int k_seed;
	private int span;
	private int ngram;
	private int window;
	private int searchdepth;
	private int minsupport;
	private int k_nolabel;
	private int iterations;
	private RankFunction<Pattern> patternRankFunction;
	private RankFunction<Relationship> tupleRankFunction;
	
	public PRDualRank(SearchEngine se, QueryGenerator qg, int k_seed, int ngram, int window, int searchdepth, int minsupport, int k_nolabel, int iterations, RankFunction<Pattern> patternRankFunction, RankFunction<Relationship> tupleRankFunction){
		this.se = se;
		this.qg = qg;
		this.k_seed = k_seed;
		this.ngram = ngram;
		this.window = window;
		this.searchdepth = searchdepth;
		this.minsupport = minsupport;
		this.k_nolabel = k_nolabel;
		this.iterations = iterations;
		this.patternRankFunction = patternRankFunction;
		this.tupleRankFunction = tupleRankFunction;
		//span is for the relationship type. that comes in the List<OperableStructure>
	}
	
	@Override
	public Model train(List<OperableStructure> list) {
		
		PatternExtractor spe = new SearchPatternExtractor(window, ngram, searchdepth);
		
		PatternExtractor epe = new ExtractionPatternExtractor(window);
		
		HashMap<Pattern, Integer> Ps = new HashMap<Pattern, Integer>();
		
		HashMap<Pattern, Integer> Pe = new HashMap<Pattern, Integer>();
		
		Set<Relationship> seeds = new HashSet<Relationship>();
		
		Set<Relationship> initial = new HashSet<Relationship>();
		
		for (OperableStructure operableStructure : list) {
			
			seeds.add(((RelationOperableStructure)operableStructure).getRelation());
			
			initial.add(((RelationOperableStructure)operableStructure).getRelation());
			
		}
		
		for (Relationship relationship : seeds) {
			
			List<Document> documents = se.search(qg.generateQuery(relationship), k_seed);

			enrichDocuments(documents,relationship);
			
			updateMap(Ps,spe.extractPatterns(documents,relationship));
			
			updateMap(Pe,epe.extractPatterns(documents,relationship));
			
		}
		
		Set<Pattern> searchPatterns = filter(Ps,minsupport);
		
		Set<Pattern> extractPatterns = filter(Pe,minsupport);
		
		PatternBasedRelationshipExtractor pbre = new PatternBasedRelationshipExtractor(extractPatterns);
		
		HashMap<Relationship,Integer> extractedTuples = new HashMap<Relationship,Integer>();
		
		for (Relationship relationship : seeds) {
			
			for (String role : relationship.getRoles()) {
				
				List<Document> documents = se.search(qg.generateQuery(relationship.getRole(role)), k_seed);
				
				for (Document document : documents) {
					
					updateMap(extractedTuples,filterByRole(role,relationship.getRole(role),pbre.extractTuples(document)));
					
				}
								
			}
			
		}
		
		Set<Relationship> topTuples = filterTopK(extractedTuples,k_nolabel,initial);
		
		Set<Document> documents = new HashSet<Document>();
		
		for (Relationship relationship : topTuples) {
			
			documents.addAll(se.search(qg.generateQuery(relationship), k_seed));
			
		}
		
		PRDualRankGraph Gs = new SearchGraphGenerator().generateGraph(topTuples,searchPatterns,documents);
		
		PRDualRankGraph Ge = new ExtractionGraphGenerator().generateGraph(topTuples,extractPatterns,documents);
				
		InferencePRDualRank search = new InferencePRDualRank();
		
		search.rank(Gs, patternRankFunction, tupleRankFunction, new MapBasedQuestCalculator(seeds,new NumberOfIterationsConvergence(iterations)));
		
		InferencePRDualRank extract = new InferencePRDualRank();

		extract.rank(Ge, patternRankFunction, tupleRankFunction, new MapBasedQuestCalculator(seeds,new NumberOfIterationsConvergence(iterations)));
		
		return new PRDualRankModel(search.getRankedPatterns(),extract.getRankedPatterns(),search.getRankedTuples(),extract.getRankedTuples());
		
	}

	private void enrichDocuments(List<Document> documents,
			Relationship relationship) {
		
		for (Document document : documents) {
			
			List<Relationship> mathchingRelationships = getMatchingRelationships(document,relationship);
			
			for (Relationship matchingRelationship : mathchingRelationships) {
				
				document.addRelationship(matchingRelationship);
				
			}
			
		}
	
	}

	private List<Relationship> getMatchingRelationships(Document document,
			Relationship relationship) {
		
		Set<Entity> entities = new HashSet<Entity>(document.getEntities());
		
		Collection<String> roles = relationship.getRoles();
		
		Map<String,Set<Entity>> candidateEntitiesForRole = new HashMap<String,Set<Entity>>();
		
		for(String role : roles){
			
			RoleConstraint roleConstraint = relationship.getRelationshipType().getConstraint(role);
			
			Set<Entity> entitiesForRole = roleConstraint.getCompatibleEntities(entities);
			
			EntityMatcher entityMatcher = relationship.getRelationshipType().getMatchers(role);
			
			Set<Entity> filteredEntitiesForRole = new HashSet<Entity>();
			
			for (Entity entity : entitiesForRole) {
				
				if (entityMatcher.match(relationship.getRole(role), entity)){
					filteredEntitiesForRole.add(entity);
				}
				
			}
			
			candidateEntitiesForRole.put(role, filteredEntitiesForRole);
		
		}

		List<Relationship> matchingTuples = new ArrayList<Relationship>();
		
		for(Map<String,Entity> candidate : MegaCartesianProduct.generateAllPossibilities(candidateEntitiesForRole)){
			
			Relationship newRelationship = new Relationship(relationship.getRelationshipType());
			
			for(Entry<String,Entity> entry : candidate.entrySet()){

				newRelationship.setRole(entry.getKey(), entry.getValue());
			
			}

			if (relationship.getRelationshipType().getRelationshipConstraint().checkConstraint(newRelationship)){
				
				matchingTuples.add(newRelationship);
				
			}
			
		}
		
		return matchingTuples;

		
	}

	private class ValueComparator<T> implements Comparator<T>{

		private Map<T, Integer> frequencymap;

		private ValueComparator(Map<T,Integer> frequencymap){
			this.frequencymap = frequencymap;
		}
		
		@Override
		public int compare(T obj1, T obj2) {
			
			return frequencymap.get(obj2).compareTo(frequencymap.get(obj1));
			
		}
		
	}
	
	private <T> Set<T> filterTopK(
			Map<T, Integer> toSelect, int k, Set<T> initial) {
		
		int realLimit = k + initial.size();
		
		SortedMap<T,Integer> sorted = new TreeMap<T, Integer>(new ValueComparator<T>(toSelect));
		
		for (T element : toSelect.keySet()) {
			
			sorted.put(element, toSelect.get(element));
			
		}
		
		for (T element : sorted.keySet()) {
			
			initial.add(element);
			
			if (initial.size() == realLimit)
				break;
		}
		
		return initial;
	}

	private Map<Relationship,Integer> filterByRole(String role,
			Entity value, List<Relationship> extractTuples) {
		
		//TODO I have to use the matcher...
		
		Map<Relationship, Integer> ret = new HashMap<Relationship, Integer>();
		
		for (Relationship relationship : extractTuples) {
			
			if (relationship.getRole(role).equals(value)){
				
				Integer freq = ret.get(relationship);
				
				if (freq == null){
					freq = 0;
				}
				
				ret.put(relationship, freq+1);
			}
			
		}
		
		return ret;
	}

	private <T> Set<T> filter(Map<T, Integer> toFilter, int minsupport) {
		
		Set<T> ret = new HashSet<T>();
		
		for (T pattern : toFilter.keySet()) {

			Integer freq = toFilter.get(pattern);
			
			if (freq >= minsupport){
				ret.add(pattern);
			}
			
		}

		return ret;
		
	}

	private <T> void updateMap(Map<T, Integer> acc,
			Map<T, Integer> actual) {
		
		for (T pattern : actual.keySet()) {
			
			Integer freq = acc.get(pattern);
			
			if (freq == null){
				
				freq = 0;
				
			}
			
			acc.put(pattern, freq + actual.get(pattern));
			
		}
		
	}
	
}
