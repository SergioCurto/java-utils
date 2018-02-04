package scurto.utils.string.algorithms;

import java.util.Collection;
import java.util.Map;

/**
 * 
 * @author Sergio Curto
 *
 */
public class OverlapAlgo {
	/**
	 * Implements Overlap coefficient: Overlap(A,B) = |A and B| / min(|A|, |B|) = |C| / min(|A|,|B|)
	 * 
	 * C - number of words shared between sentence A and B
	 * 
	 * @param wordSetA Sentence A words ngrams
	 * @param wordSetB Sentence B words ngrams
	 * @return AlgorithmResult with score value between 0.00 (completely different) and 1.00 (completely similar)
	 */
	static public AlgorithmResult overlapAlgoDetailed(Collection<String> wordSetA, Collection<String> wordSetB) {
		double aCount = wordSetA.size();
		double bCount = wordSetB.size();
		Collection<String> firstSet = wordSetA;
		Collection<String> secondSet = wordSetB;
		
		// testing similar words through the smallest set
		if(aCount > bCount) {
			firstSet = wordSetB;
			secondSet = wordSetA;
		}
		
		AlgorithmResult result = new AlgorithmResult();
		
		double cCount = 0;
		for(String word : firstSet) {
			if(secondSet.contains(word)) {
				result.addWordScoring(word, AlgorithmResult.DEFAULT_WORD_SCORING);
				cCount++;
			}
		}
		
		result.setScore((cCount) / Math.min(aCount, bCount));
		return result;
	}
	
	static public double overlapAlgo(Collection<String> wordSetA, Collection<String> wordSetB) {
		return overlapAlgoDetailed(wordSetA, wordSetB).getScore();
	}
	
	/**
	 * Implements Overlap coefficient: Overlap(A,B) = |A and B| / min(|A|, |B|) = |C| / min(|A|,|B|)
	 * Applies a scoring factor to the words that matched, giving same or less score then the base algo.
	 * C - number of words shared between sentence A and B
	 * 
	 * @param wordSetA Sentence A words ngrams
	 * @param wordSetB Sentence B words ngrams
	 * @param scoreAlgo Scoring algorithm to be applied
	 * @return AlgorithmResult with score value between 0.00 (completely different) and 1.00 (completely similar)
	 */
	static public AlgorithmResult overlapAlgoDetailed(Collection<String> wordSetA, Collection<String> wordSetB, SentenceScoreAlgorithm scoreAlgo) {
		double aCount = wordSetA.size();
		double bCount = wordSetB.size();
		Collection<String> firstSet = wordSetA;
		Collection<String> secondSet = wordSetB;
		
		// testing similar words through the smallest set
		if(aCount > bCount) {
			firstSet = wordSetB;
			secondSet = wordSetA;
		}
		
		AlgorithmResult result = new AlgorithmResult();
		
		// exception case for when there are no bigrams for one of the sentences
		if(aCount == 0 || bCount == 0) {
			result.setScore(0);
			return result;
		}
		Map<String, Double> firstSetScore = scoreAlgo.scoreNgrams(firstSet); 
		Map<String, Double> secondSetScore = scoreAlgo.scoreNgrams(secondSet);
		
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
		
		result.setScore(maxScoreFromScoreAlgo == 0.0? 0 : (cCount * wordsMatchedScore) / (Math.min(aCount, bCount) * maxScoreFromScoreAlgo));
		return result;
	}
	
	static public double overlapAlgo(Collection<String> wordSetA, Collection<String> wordSetB, SentenceScoreAlgorithm scoreAlgo) {
		return overlapAlgoDetailed(wordSetA, wordSetB, scoreAlgo).getScore();
	}
}
