package scurto.utils.string.algorithms;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import scurto.utils.collections.SoftHashMap;
import scurto.utils.string.Ngrams;

/**
 * @author Sergio Curto
 *
 */
public class TfIdfAlgo extends SentenceScoreAlgorithm {
	private HashMap<String, Integer> wordOnDocumentFrequency = new HashMap<String, Integer>();
	private Integer numberOfDocuments = 0;
	private SoftHashMap<String, Map<String, Double>> cache = new SoftHashMap<String, Map<String,Double>>();
	
	/**
	 * Creates a new instance of the algorithm with an empty training set
	 */
	public TfIdfAlgo() {
		
	}
	
	public void addSentenceToTrainSet(Set<String> words) {
		for(String word : words) {
			Integer currentCount = wordOnDocumentFrequency.get(word);
			
			if(currentCount == null) {
				wordOnDocumentFrequency.put(word, Integer.valueOf(1));
			} else {
				wordOnDocumentFrequency.put(word, currentCount+1);
			}
		}
		
		numberOfDocuments++;
	}
	
	public Map<String, Double> scoreSentence(Collection<String> wordsList) {
		Map<String, Double> results = cache.get(wordsList.toString());
		if(results != null) {
			return results;
		}
		
		int numberOfWords = wordsList.size();
		
		Map<String, Integer> wordFrequency = new HashMap<String, Integer>();
		
		for(String word : wordsList) {
			Integer currentCount = wordFrequency.get(word);
			
			if(currentCount == null) {
				wordFrequency.put(word, Integer.valueOf(1));
			} else {
				wordFrequency.put(word, currentCount+1);
			}
		}
		
		results = new HashMap<String, Double>();
		for(String word : wordFrequency.keySet()) {
			Integer tfValue = wordFrequency.get(word);
			Integer wordOnDocumentValue = wordOnDocumentFrequency.get(word);
			
			if(wordOnDocumentValue == null) {
				// we don't have this word on our training set, giving score 0
				results.put(word, 0.0);
			} else {
				results.put(word, ((double)tfValue / (double)numberOfWords)*(Math.log((double)numberOfDocuments / (double) wordOnDocumentValue)));
			}
		}
		
		cache.put(wordsList.toString(), results);
		//System.out.println("TfIdfAlgo: Score given to sentence components "+results);
		return results;
	}
	
	@Override
	public Map<String, Double> scoreNgrams(Collection<String> ngramCollection) {
		Map<String, Double> results= cache.get(ngramCollection.toString());
		if(results != null) {
			return results;
		}
		
		results = new HashMap<String, Double>();
		int numberOfWords = 0;
		
		Map<String, Integer> wordFrequency = new HashMap<String, Integer>();
		
		for(String ngram : ngramCollection){
			for(String word : ngram.split(Ngrams.ngramSeparator)){
				Integer currentCount = wordFrequency.get(word);
				
				if(currentCount == null) {
					wordFrequency.put(word, Integer.valueOf(1));
				} else {
					wordFrequency.put(word, currentCount+1);
				}
				numberOfWords++;
			}
		}
		
		for(String ngram : ngramCollection){
			double ngramScore = 0.00;
			for(String word : ngram.split(Ngrams.ngramSeparator)){
				Integer tfValue = wordFrequency.get(word);
				Integer wordOnDocumentValue = wordOnDocumentFrequency.get(word);
				
				if(wordOnDocumentValue == null) {
					// we don't have this word on our training set, giving score 0
				} else {
					ngramScore += ((double)tfValue / (double)numberOfWords)*(Math.log((double)numberOfDocuments / (double) wordOnDocumentValue));
				}
			}
			results.put(ngram, Double.valueOf(ngramScore));
		}

		cache.put(ngramCollection.toString(), results);
		//System.out.println("TfIdfAlgo: Score given to ngram components "+results);
		return results;
	}
	
	public void printResults(Map<String, Double> results, int doublePrecision) {
		if(doublePrecision < 0) {
			doublePrecision = 4;
		}
		
		System.out.print("Results: ");
		for(String word : results.keySet()) {
			System.out.format("[%s %."+doublePrecision+"f] ", word, results.get(word));
		}
		System.out.println();
	}
}
