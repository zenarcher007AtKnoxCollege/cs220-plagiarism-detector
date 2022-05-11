package junit;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import plagdetect.IPlagiarismDetector;
import plagdetect.PlagiarismDetector;

class TestAllDocs {
	
	static final String DOCS = "docs/alldocs";
	//static final String DOCS = "docs/smallDocs";

    public static IPlagiarismDetector makeDetector(int n) throws Exception {
        // n is the size of an n-gram
        IPlagiarismDetector detector = new PlagiarismDetector(n);
        detector.readFilesInDirectory(new File(DOCS));
        return detector;
    }
    
	public static void main(String[] args) throws Exception {
		//
		// TODO you probably want to add more code or more tests
		// This just gets things started
		//
    
		
		// how long does it take to create a detector?
		long start = System.currentTimeMillis();
		IPlagiarismDetector detector = makeDetector(3);
		long total = System.currentTimeMillis() - start;
		System.out.printf("It took %.1f seconds to read the documents\n", total/1000.0);
		
		// how long does it take to search for suspicious pairs?
		start = System.currentTimeMillis();
		// TODO: 500 is too big here! These are not long enough essays to
		// have 500 ngrams in common!
		// The average essay is less than 500 words long.
		// What is a good number to put here? Try some different values to
		// figure that out!
		System.out.println("\nSuspicious pairs:");
		Collection<String> pairs = detector.getSuspiciousPairs(15);
		
		total = System.currentTimeMillis() - start;
    System.out.printf("It took %.1f seconds to check for suspicious pairs in the documents\n", total/1000.0);
		
    
    // Print results, sorted by their number of matches...
    System.out.println("\nSorting results...");
		// Sort results by their number of matches
	  StringByNumber wordStrings[] = new StringByNumber[pairs.size()];
	  int i1 = 0;
	  for(String s : pairs) {
	    StringByNumber w = new StringByNumber(s, 2);
	    wordStrings[i1] = w;
	    ++i1;
	  }
	  Arrays.sort(wordStrings); // This will sort them by the 3rd word: the number of matches.
		
	  for (Object pair : wordStrings) {
			System.out.println(pair);
		}
		
	}
	
	

}
