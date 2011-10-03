package edu.columbia.cs.cg.prdualrank.inference.quest;

import java.util.Map;

import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.graph.PRDualRankGraph;
import edu.columbia.cs.cg.relations.Relationship;

public interface QuestCalculator<T> {

	void runQuestP(PRDualRankGraph<T> gs);

	void runQuestR(PRDualRankGraph<T> gs);

	Map<Pattern<T>,Double> getPatternPrecisionMap();

	Map<Relationship,Double> getTuplePrecisionMap();

	Map<Pattern<T>,Double> getPatternRecallMap();

	Map<Relationship,Double> getTupleRecallMap();

}
