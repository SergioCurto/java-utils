package scurto.utils.string.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergio Curto
 *
 */
public class AlgorithmResult {
	public class WordScoring {
		/**
		 * {@link AlgorithmResult} associated to this word scoring
		 */
		private AlgorithmResult parent;
		/**
		 * Word involved on the scoring
		 */
		private String word;
		/**
		 * Weight >0.00 and <=1.00 that relates the impact of this word on the final score (1.00 = maximum impact)
		 */
		private double wordScoreWeightFactor;
		
		public WordScoring(AlgorithmResult algorithmResult, String word, double wordScoreWeightFactor) {
			this.parent = algorithmResult;
			this.word = word;
			this.wordScoreWeightFactor = wordScoreWeightFactor;
		}
		
		public String getWord() {
			return word;
		}
		public double getWordScoreWeightFactor() {
			return wordScoreWeightFactor;
		}
		public double getWordScoreWeightFactorNormalized(AlgorithmResult ar) {
			return wordScoreWeightFactor / parent.getTotalWordScoreWeights();
		}
	}

	public static final double DEFAULT_WORD_SCORING = 1.0000;
	
	/**
	 * Score result for the algorithm call
	 */
	private double score;
	
	/**
	 * Detailed information for each word with impact on the score
	 */
	private List<WordScoring> wordsWithScore;
	private double totalWordScoreWeights;
	
	public AlgorithmResult(){
		this.score = 0;
		this.wordsWithScore = new ArrayList<WordScoring>();
	}

	public double getTotalWordScoreWeights() {
		return this.totalWordScoreWeights;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public List<WordScoring> getWordsWithScore() {
		return wordsWithScore;
	}
	
	public void addWordScoring(String word, double wordScoreWeightFactor) {
		this.wordsWithScore.add(new WordScoring(this, word, wordScoreWeightFactor));
		this.totalWordScoreWeights += wordScoreWeightFactor;
	}
}
