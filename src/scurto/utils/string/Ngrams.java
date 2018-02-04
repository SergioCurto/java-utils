/**
 * 
 */
package scurto.utils.string;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sergio Curto
 *
 */
public class Ngrams {
	public static final String ngramSeparator = ","; 
	/**
	 * Generates an ordered list of bigrams with start and end of sentence tags
	 * @param sentenceWords
	 * @return
	 */
	public static List<String> generateBigramsList(List<String> sentenceWords) {
		ArrayList<String> results = new ArrayList<String>();
		
		if(sentenceWords.size() < 2) {
			// can't form bigrams without at least two elements
			return results;
		}
		
		String previousWord = sentenceWords.get(0);
		
		for(String word : sentenceWords.subList(1, sentenceWords.size())) {
			String wordAux = NormalizerSimple.normPunctLCaseDMarks(word);
			results.add(previousWord+ngramSeparator+wordAux);
			previousWord = wordAux;
		}
		
		results.trimToSize();
		return results;
	}
	
	/**
	 * Generates the unordered set of bigrams with start and end of sentence tags
	 * @param sentenceWords
	 * @return
	 */
	public static Set<String> generateBigramsSet(List<String> sentenceWords) {
		HashSet<String> results = new HashSet<String>();
		
		if(sentenceWords.size() < 2) {
			// can't form bigrams without at least two elements
			return results;
		}
		
		String previousWord = sentenceWords.get(0);
		
		for(String word : sentenceWords.subList(1, sentenceWords.size())) {
			String wordAux = NormalizerSimple.normPunctLCaseDMarks(word);
			results.add(previousWord+ngramSeparator+wordAux);
			previousWord = wordAux;
		}
		
		return results;
	} 
}
