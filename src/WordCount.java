import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
/* WordCount.java
 * Written by: Charles 'Nick' Dix
 * 
 * Description:
 * WordCount reads from a file called input.txt, unless a user supplies the name of an input
 * file on the command line, and reads each line of the file. Each line is split into words
 * delimited by spaces and symbols, such as punctuation, which are stripped from the words.
 * The words are also all set to lowercase, to ensure there are no duplicates based on case.
 * Words are then inserted into a WordMap, which increments word counts and is used for
 * sorting the words and counts in descending order by count.
 * 
 * WordMap contains a HashMap and Arraylist of the words read from the input file.
 * HashMap is used for efficient insertion of words into the structure (worst case: O(n)), 
 * efficient lookup and incrementing counts associated with words (worst case: O(n)), and to 
 * preclude duplicate words from being inserted. ArrayList is used for efficient Keypair 
 * insertion (O(1)) and to take advantage of Java's built-in Collections.sort() method
 * (which I've read has O(nlogn)).
 * 
 * Keypair is a helper class representing a word/word count pair.
 * 
 * The WordMap class could be improved if there was a method to clear list, allowing for
 * further insertions into WordMap that would not result in duplicates when sorting list.
 */

public class WordCount {

	public static void main(String[] args) {
		BufferedReader in;
		String fileName, line;
		String wordArr[];
		WordMap wordMap = new WordMap();
		
		// the user can supply an input file if desired; otherwise input.txt will be used
		if (args.length == 1) {
			fileName = args[0];
		}
		else {
			fileName = "input.txt";
		}
		
		try {
			in = new BufferedReader(new FileReader(fileName));
			
			// each line in the file is read
			while ((line = in.readLine()) != null) {
				// the line is split into an array of words, stripping away extra whitespace
				// and punctuation symbols; words are also set to lowercase
				wordArr = line.trim().toLowerCase().split("[ ,.;:—\"“”{}()?!|`]");
				
				for (String word : wordArr) {
					wordMap.insert(word);
				}
			}
			
			// after all the words in the input file have been inserted to a WordMap,
			// the list of words is sorted by counts and written to an output file
			wordMap.sortList();
			wordMap.printListToFile();
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Word counts written to output.txt...");
	}
}

/**
 * Keypair is a helper class for WordMap, used to represent the word and count key-pair
 * that is used in the WordMap's map data structure.
 */
class Keypair implements Comparable<Keypair>
{
	// key = word, count = number of instances of that word
	public String key;
	public int count;
	
	public Keypair(String k, int c) {
		key = k;
		count = c;
	}
	
	// compareTo is overridden so that Java's Collections.sort method can be utilized
	@Override
	public int compareTo(Keypair other) {
		if (this.count < other.count)
			return -1;
		else if (this.count > other.count)
			return 1;
		else
			return 0;
	}
	
	// toString is overridden to assist in printing Keypairs in histograph format
	@Override
	public String toString() {
		char equalsSigns[] = new char[count];
		Arrays.fill(equalsSigns, '=');
		return key + " | " + String.valueOf(equalsSigns) + " (" + count + ")";
	}
}

/**
 * WordMap contains a map and list of words read from the input file. It facilitates the
 * storage of new words, incrementing word counts, and writing the stored words and counts
 * in sorted order to an output file, output.txt.
 */
class WordMap {
	private HashMap<String, Integer> map;
	private ArrayList<Keypair> list;
	
	public WordMap() {
		 map = new HashMap<String, Integer>();
		 list = new ArrayList<Keypair>();
	}
	
	/**
	 * insert puts the word in map if it does not yet exist and increments the word count
	 * if the word is already in the map.
	 * @param word String containing the word to be inserted
	 */
	public void insert(String word) {
		if (map.containsKey(word)) {
			map.put(word, map.get(word)+1);
		}
		else {
			map.put(word, 1);
		}
	}
	
	/**
	 * sortList iterates through the words in map, adding a Keypair containing the
	 * word and the associated count for the word to list. list is then sorted from
	 * largest to smallest word count.
	 * Assumption: map contains an empty String; list is empty
	 */
	public void sortList() {
		// map tends to contain an empty string because of the split method used in main(),
		// so it needs to be removed before being put into list
		map.remove("");
		for (String key : map.keySet()) {
			list.add(new Keypair(key, map.get(key)));
		}
		Collections.sort(list);
		Collections.reverse(list);
	}
	
	/**
	 * printListToFile prints each Keypair, that is each word and its associated count, to
	 * output.txt.
	 */
	public void printListToFile() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("output.txt"));
			for (Keypair k : list) {
				out.write(k + "\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
