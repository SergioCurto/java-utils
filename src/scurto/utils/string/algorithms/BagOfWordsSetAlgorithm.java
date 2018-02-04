package scurto.utils.string.algorithms;

import java.util.Set;

/**
 * 
 * @author Sergio Curto
 */
public abstract class BagOfWordsSetAlgorithm {
	/**
	 * Measures the similarity of two sentences represented by each set of words
	 * @param wordSetA Set of words of the first sentence
	 * @param wordSetB Set of words of the second sentence
	 * @return value between 0.00 (completely different) and 1.00 (similar)
	 */
	public double measureSimilarity(Set<String> wordSetA, Set<String> wordSetB){
		AlgorithmResult result = measureSimilarityDetailed(wordSetA, wordSetB);
		return result.getScore();
	}
	
	/**
	 * @see measureSimilarity
	 * @return returns detailed information about how the similarity was calculated if possible
	 */
	public abstract AlgorithmResult measureSimilarityDetailed(Set<String> wordSetA, Set<String> wordSetB);
}
