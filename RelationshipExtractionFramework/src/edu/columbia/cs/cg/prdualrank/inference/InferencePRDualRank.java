/**
 * Defines the inference procedure to rank patterns as described in PRDualRank paper.
 *
 * @author      Pablo Barrio
 * @author		Goncalo Simoes
 * @version     0.1
 * @since       2011-10-07
 */
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

	/**
	 * Ranks the patterns and tuples in the graph based on the specified ranking function, using the passed questCalculator.
	 *
	 * @param gs the graph connecting tuples and patterns.
	 * @param patternRankFunction the pattern rank function used to rank patterns.
	 * @param tupleRankFunction the tuple rank function used to rank tuples.
	 * @param questCalculator the quest calculator used to calculate the required metrics (precision or recall in this case)
	 */
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

	/**
	 * Gets the ranked tuples.
	 *
	 * @return the ranked tuples according to the tuple ranking function.
	 */
	public SortedSet<Relationship> getRankedTuples() {
		return rankedTuples;
	}

	/**
	 * Gets the ranked patterns according to the pattern ranking function.
	 *
	 * @return the ranked patterns
	 */
	public SortedSet<Pattern<T,D>> getRankedPatterns() {
		return rankedPatterns;
	}

}