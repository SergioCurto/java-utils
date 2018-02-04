package scurto.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import scurto.utils.collections.SoftHashMap;

public class RandomUtils {
	static private Random random = new Random();
	
	static public Random getRandom(){
		return random;
	}
	
	static private SoftHashMap<String, List<List<Integer>>> cache = new SoftHashMap<>();
	
	static public List<List<Integer>> generateRandomPartitionsOfIndexes(int numberOfPartitions, int numberOfIndexes){
		List<List<Integer>> result = null;
		String key = numberOfPartitions+"_"+numberOfIndexes;
		
		if((result = cache.get(key)) != null) {
			return result;
		}

		List<Integer> indexes = new ArrayList<>(numberOfIndexes); 
		for(int i=0; i<numberOfIndexes; i++) {
			indexes.add(i);
		}
		
		Collections.shuffle(indexes, getRandom());
		
		result = new ArrayList<>(numberOfPartitions);

		int indexesPerPartition = (numberOfIndexes/numberOfPartitions)+1;
//		int remaining = numberOfIndexes;
		for(int i=0; i<numberOfPartitions; i++) {
			int fromIndex = i*indexesPerPartition;
			int   toIndex = Math.min((i+1)*indexesPerPartition, numberOfIndexes);
			ArrayList<Integer> currentPartition = new ArrayList<>(indexes.subList(fromIndex, toIndex));
			
//			remaining -= indexesPerPartition;
			
			currentPartition.trimToSize();
			result.add(currentPartition);
		}
		
		if(result instanceof ArrayList<?>){
			((ArrayList<List<Integer>>) result).trimToSize();
		}
		cache.put(key, result);
		return result;
	}
}
