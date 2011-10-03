package edu.columbia.cs.cg.prdualrank.inference.quest;

import java.util.Map;

import edu.columbia.cs.cg.document.Document;
import edu.columbia.cs.cg.pattern.Pattern;
import edu.columbia.cs.cg.pattern.matchable.Matchable;
import edu.columbia.cs.cg.prdualrank.graph.PRDualRankGraph;
import edu.columbia.cs.cg.relations.Relationship;

public interface QuestCalculator<T extends Matchable,D extends Document> {

	void runQuestP(PRDualRankGraph<T,D> gs);

	void runQuestR(PRDualRankGraph<T,D> gs);

	Map<Pattern<T,D>,Double> getPatternPrecisionMap();

	Map<Relationship,Double> getTuplePrecisionMap();

	Map<Pattern<T,D>,Double> getPatternRecallMap();

	Map<Relationship,Double> getTupleRecallMap();

}
