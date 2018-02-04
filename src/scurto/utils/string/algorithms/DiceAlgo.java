package scurto.utils.string.algorithms;

import java.util.Map;
import java.util.Set;

/**
 * @author Sergio Curto
 *
 */
public class DiceAlgo extends WeightedBagOfWordsSetAlgorithm{

	/**
	 * Implements Dice similarity measure: Dice(A,B) = 2 * |A and B| / (|A| + |B|) = 2 * |C| / (|A|+|B|)
	 * 
	 * C - number of words shared between sentence A and B
	 * 
	 * @param wordSetA Sentence A words
	 * @param wordSetB Sentence B words
	 * @return AlgorithmResult with score value between 0.00 (completely different) and 1.00 (completely similar)
	 */
	static public AlgorithmResult diceAlgoDetailed(Set<String> wordSetA, Set<String> wordSetB) {
		double aCount = wordSetA.size();
		double bCount = wordSetB.size();
		Set<String> firstSet = wordSetA;
		Set<String> secondSet = wordSetB;
		
		// testing similar words through the smallest set
		if(aCount > bCount) {
			firstSet = wordSetB;
			secondSet = wordSetA;
		}
		
		AlgorithmResult result = new AlgorithmResult();
		
		double cCount = 0;
		for(String word : firstSet) {
			if(secondSet.contains(word)) {
				cCount++;
				result.addWordScoring(word, AlgorithmResult.DEFAULT_WORD_SCORING);
			}
		}
		
		result.setScore((2*cCount) / (aCount + bCount));
		return result;
	}
	
	/**
	 * @see diceAlgoDetailed without scoring
	 */
	static public double diceAlgo(Set<String> wordSetA, Set<String> wordSetB) {
		return diceAlgoDetailed(wordSetA, wordSetB).getScore();
	}
	
	/**
	 * Implements Dice similarity measure: Dice(A,B) = 2 * |A and B| / (|A| + |B|) = 2 * |C| / (|A|+|B|)
	 * Applies a scoring factor to the words that matched, giving same or less score then the base algo.
	 * C - number of words shared between sentence A and B
	 * 
	 * @param wordSetA Sentence A words
	 * @param wordSetB Sentence B words
	 * @param scoreAlgo Scoring algorithm to be applied
	 * @return AlgorithmResult with score value between 0.00 (completely different) and 1.00 (completely similar)
	 */
	static public AlgorithmResult diceAlgoDetailed(Set<String> wordSetA, Set<String> wordSetB, SentenceScoreAlgorithm scoreAlgo) {
		double aCount = wordSetA.size();
		double bCount = wordSetB.size();
		Set<String> firstSet = wordSetA;
		Set<String> secondSet = wordSetB;
		
		// testing similar words through the smallest set
		if(aCount > bCount) {
			firstSet = wordSetB;
			secondSet = wordSetA;
		}
		
		Map<String, Double> firstSetScore = scoreAlgo.scoreSentence(firstSet);
		Map<String, Double> secondSetScore = scoreAlgo.scoreSentence(firstSet);
		
		AlgorithmResult result = new AlgorithmResult();
		
		double maxScoreFromScoreAlgo = SentenceScoreAlgorithm.mapScoreTotals(firstSetScore) + SentenceScoreAlgorithm.mapScoreTotals(secondSetScore);
		double wordsMatchedScore = 0.0000000;
		double cCount = 0;
		for(String word : firstSet) {
			if(secondSet.contains(word)) {
				double wordScoreWeightFactor = firstSetScore.get(word) + secondSetScore.get(word);
				wordsMatchedScore += wordScoreWeightFactor;
				cCount++;
				
				result.addWordScoring(word, wordScoreWeightFactor);
			}
		}
		
		result.setScore((2*cCount*wordsMatchedScore) / ((aCount + bCount) * maxScoreFromScoreAlgo));
		return result;
	}
	
	/**
	 * @see diceAlgoDetailed with scoring
	 */
	static public double diceAlgo(Set<String> wordSetA, Set<String> wordSetB, SentenceScoreAlgorithm scoreAlgo) {
		return diceAlgoDetailed(wordSetA, wordSetB, scoreAlgo).getScore();
	}

	@Override
	protected AlgorithmResult measureSimilarityWeightedDetailed(
			Set<String> wordSetA, Set<String> wordSetB,
			SentenceScoreAlgorithm scoringAlgo) {
		if(scoringAlgo == null) {
			return diceAlgoDetailed(wordSetA, wordSetB);
		} else {
			return diceAlgoDetailed(wordSetA, wordSetB, scoringAlgo);
		}
	}
}
