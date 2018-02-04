package scurto.utils.string.algorithms;

import java.util.Map;
import java.util.Set;

/**
 * Implements Jaccard similarity measure: Jaccard(A,B) = |A and B| / |A or B| = |C| / (|A|+|B|-|C|)
 * 
 * C - number of words shared between sentence A and B
 * 
 * @author Sergio Curto
 *
 */
public class JaccardAlgo extends WeightedBagOfWordsSetAlgorithm{
	
	/**
	 * Implements Jaccard similarity measure: Jaccard(A,B) = |A and B| / |A or B| = |C| / (|A|+|B|-|C|)
	 * 
	 * C - number of words shared between sentence A and B
	 * 
	 * @param wordSetA Sentence A words
	 * @param wordSetB Sentence B words
	 * @return AlgorithmResult with score value between 0.00 (completely different) and 1.00 (completely similar)
	 */
	static public AlgorithmResult jaccardAlgoDetailed(Set<String> wordSetA, Set<String> wordSetB) {
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
		
		result.setScore(cCount / (aCount + bCount - cCount));
		
		return result;
	}
	
	/**
	 * @see jaccardAlgoDetailed without scoring
	 */
	static public double jaccardAlgo(Set<String> wordSetA, Set<String> wordSetB) {
		return jaccardAlgoDetailed(wordSetA, wordSetB).getScore();
	}
	
	/**
	 * Implements Jaccard similarity measure: Jaccard(A,B) = |A and B| / |A or B| = |C| / (|A|+|B|-|C|)
	 * Applies a scoring factor to the words that matched, giving same or less score then the base algo.
	 * C - number of words shared between sentence A and B
	 * 
	 * @param wordSetA Sentence A words
	 * @param wordSetB Sentence B words
	 * @param scoreAlgo Scoring algorithm to be applied
	 * @return AlgorithmResult with score value between 0.00 (completely different) and 1.00 (completely similar)
	 */
	static public AlgorithmResult jaccardAlgoDetailed(Set<String> wordSetA, Set<String> wordSetB, SentenceScoreAlgorithm scoreAlgo) {
		double aCount = wordSetA.size();
		double bCount = wordSetB.size();
		Set<String> firstSet = wordSetA;
		Set<String> secondSet = wordSetB;
		
		// testing similar words through the smallest set
		if(aCount > bCount) {
			firstSet = wordSetB;
			secondSet = wordSetA;
		}
		Map<String, Double> firstSetScore;
		Map<String, Double> secondSetScore;
//	try {	
		firstSetScore = scoreAlgo.scoreSentence(firstSet);
		secondSetScore = scoreAlgo.scoreSentence(secondSet);
//	} catch(NullPointerException npe) {
//		throw npe;
//	}
		
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
		
		result.setScore(maxScoreFromScoreAlgo<=0.0000? 0 : (cCount * wordsMatchedScore) / ((aCount + bCount - cCount) * maxScoreFromScoreAlgo));
		
		if(result.getScore() == Double.NaN){
			System.err.println("JaccardAlgo: Division by 0 detected! InputA: "+wordSetA+" InputB: "+wordSetB+" maxScoreFromScoringAlgo: "+maxScoreFromScoreAlgo);
			return null;
		}
		
		return result;
	}
	
	static public double jaccardAlgo(Set<String> wordSetA, Set<String> wordSetB, SentenceScoreAlgorithm scoreAlgo) {
		return jaccardAlgoDetailed(wordSetA, wordSetB, scoreAlgo).getScore();
	}

	@Override
	protected AlgorithmResult measureSimilarityWeightedDetailed(Set<String> wordSetA,
			Set<String> wordSetB, SentenceScoreAlgorithm scoringAlgo) {
		if(scoringAlgo == null) {
			return jaccardAlgoDetailed(wordSetA, wordSetB);
		} else {
			return jaccardAlgoDetailed(wordSetA, wordSetB, scoringAlgo);
		}
	}
}
