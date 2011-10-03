package edu.columbia.cs.cg.prdualrank.inference;

import java.util.SortedSet;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.matchable.Matchable;
import edu.columbia.cs.cg.prdualrank.graph.PRDualRankGraph;
import edu.columbia.cs.cg.prdualrank.inference.quest.QuestCalculator;
import edu.columbia.cs.cg.prdualrank.inference.ranking.RankFunction;
import edu.columbia.cs.cg.relations.Relationship;

public class InferencePRDualRank<T extends Matchable,D extends Document> {

	private SortedSet<Relationship> rankedTuples;
	private SortedSet<Pattern<T,D>> rankedPatterns;

	public InferencePRDualRank(){
	}
	
	public void rank(PRDualRankGraph<T,D> gs, RankFunction<Pattern<T,D>> patternRankFunction, RankFunction<Relationship> tupleRankFunction, QuestCalculator<T,D> questCalculator) {
	
		if (patternRankFunction.requiresPrecision() || tupleRankFunction.requiresPrecision()){
			questCalculator.runQuestP(gs);
			if (patternRankFunction.requiresPrecision()){
				patternRankFunction.setPrecision(questCalculator.getPatternPrecisionMap());
			}
			if (tupleRankFunction.requiresPrecision()){
				tupleRankFunction.setPrecision(questCalculator.getTuplePrecisionMap());
			}
		}
		
		if (patternRankFunction.requiresRecall() || tupleRankFunction.requiresRecall()){
			questCalculator.runQuestR(gs);
			if (patternRankFunction.requiresRecall()){
				patternRankFunction.setRecall(questCalculator.getPatternRecallMap());
			}
			if (tupleRankFunction.requiresRecall()){
				tupleRankFunction.setRecall(questCalculator.getTupleRecallMap());
			}
		}
		
		rankedPatterns = patternRankFunction.rank();
		
		rankedTuples = tupleRankFunction.rank();
		
	}

	public SortedSet<Relationship> getRankedTuples() {
		return rankedTuples;
	}

	public SortedSet<Pattern<T,D>> getRankedPatterns() {
		return rankedPatterns;
	}

}
