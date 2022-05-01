package junit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import plagdetect.IPlagiarismDetector;
import plagdetect.PlagiarismDetector;

class TestAllDocs {
	
	static final String DOCS = "docs/alldocs";

	
	private IPlagiarismDetector detector;
	    
    public void makeDetector(int n) throws Exception {
        // n is the size of an n-gram
        detector = new PlagiarismDetector(n);
        detector.readFilesInDirectory(new File(DOCS));
    }
    
	@Test
	void test() throws Exception {
		// how long does it take to create a detector?
		long start = System.currentTimeMillis();
		makeDetector(3);
		long total = System.currentTimeMillis() - start;
		System.out.printf("It took %.1f seconds to read the documents\n", total/1000.0);
		
		// how long does it take to search for suspicious pairs?
		start = System.currentTimeMillis();
		// TODO: 500 is too big here! These are not long enough essays to
		// have 500 ngrams in common!
		// The average essay is less than 500 words long.
		// What is a good number to put here? Try some different values to
		// figure that out!
		Collection<String> pairs = detector.getSuspiciousPairs(500);
		for (String pair : pairs) {
			System.out.println(pair);
		}
		total = System.currentTimeMillis() - start;
		System.out.printf("It took %.1f seconds to check for suspicious pairs in the documents\n", total/1000.0);
	}
	
	

}
