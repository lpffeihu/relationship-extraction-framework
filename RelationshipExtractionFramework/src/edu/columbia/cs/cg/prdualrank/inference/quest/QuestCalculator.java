package edu.columbia.cs.cg.prdualrank.inference.quest;

import java.util.Map;

import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.prdualrank.graph.PRDualRankGraph;
import edu.columbia.cs.cg.relations.Relationship;

public interface QuestCalculator {

	void runQuestP(PRDualRankGraph gs);

	void runQuestR(PRDualRankGraph gs);

	Map<Pattern,Double> getPatternPrecisionMap();

	Map<Relationship,Double> getTuplePrecisionMap();

	Map<Pattern,Double> getPatternRecallMap();

	Map<Relationship,Double> getTupleRecallMap();

}
