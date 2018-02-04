package scurto.utils.string.algorithms;

import java.util.Set;

/**
 * @author Sergio Curto
 */
public abstract class WeightedBagOfWordsSetAlgorithm extends BagOfWordsSetAlgorithm{
	private SentenceScoreAlgorithm scoringAlgo;

	/**
	 * Sets a new weight algorithm
	 * @param scoringAlgo
	 */
	public void setWeightAlgorithm(SentenceScoreAlgorithm scoringAlgo) {
		this.scoringAlgo = scoringAlgo;
	}
	
	/**
	 * Disables the weight algorithm
	 */
	public void removeWeightAlgorithm() {
		this.scoringAlgo = null;
	}
	
	@Override
	public AlgorithmResult measureSimilarityDetailed(Set<String> wordSetA, Set<String> wordSetB){
		AlgorithmResult result = measureSimilarityWeightedDetailed(wordSetA, wordSetB, this.scoringAlgo);
		return result;
	}
	
	protected final double measureSimilarityWeighted(Set<String> wordSetA,
			Set<String> wordSetB, SentenceScoreAlgorithm scoringAlgo){
		AlgorithmResult result = measureSimilarityWeightedDetailed(wordSetA, wordSetB, scoringAlgo);
		return result.getScore();
	}

	/**
	 * Measures the similarity between two sentences represented by a set of words
	 * 
	 * If scoringAlgo parameter is null, no scoring algorithm will be used.
	 * 
	 * @see BagOfWordsSetAlgorithm#measureSimilarity(Set, Set)
	 * @param wordSetA
	 * @param wordSetB
	 * @param scoringAlgo
	 * @return
	 */
	protected abstract AlgorithmResult measureSimilarityWeightedDetailed(Set<String> wordSetA,
			Set<String> wordSetB, SentenceScoreAlgorithm scoringAlgo);
}
