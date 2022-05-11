package plagdetect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;


public class PlagiarismDetector implements IPlagiarismDetector {
  private int minNGrams; // The threshold of matching ngrams to where it will consider two documents
  // as containing plaigerism.
  private Map<String,Set<String>> files; // HashMap of file paths to a set of ngrams
  private Map<Pair<String>,Integer> comparisons; // Maps Pairs of Strings to Integers
  private int nSize;
  private String lastDir; // Last directory read by the readFilesInDirectory method
  public PlagiarismDetector(int n) {
    files = new HashMap<String,Set<String>>();
    nSize = n;
    // Filename1 => Filename2 => number of ngrams in common between filename1 and filename2
    comparisons = new HashMap<Pair<String>,Integer>();
    lastDir = "";
  }
  @Override
  public int getN() {
    return nSize;
  }

  @Override
  public Collection<String> getFilenames() {
    return files.keySet();
  }
  
  /* If the string is an absolute path, it will keep it the same. If it is a filename,
   * it will add on the directory path of the most recently directory that was read.
   */
  public String estimateAbsPath(String path) {
    if(path.contains("/")) {
      return path;
    } else {
      return lastDir + "/" + path;
    }
  }

  /* Returns all ngrams in the file by the file path */
  @Override
  public Collection<String> getNgramsInFile(String filename) {
    Scanner sc;
    //System.out.println(filename);
    try {
      sc = new Scanner(new File(estimateAbsPath(filename)));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      //System.out.println("NULLLL");
      return null;
    }
    HashSet<String> grams = new HashSet<String>();
    while(sc.hasNextLine()) {
      String line = sc.nextLine(); // Get line
      String split[] = line.split(" "); // Split by spaces
      for(int i = 0; i <= split.length - nSize; ++i) {
        String gram[] = Arrays.copyOfRange(split, i, i+nSize); // Get portion of array
        grams.add(String.join(" ", gram));
      }
    }
    return grams;
  }

  /* Looks up the ngrams from the file path, and returns the number of items. */
  @Override
  public int getNumNgramsInFile(String filename) {
    HashSet<String> grams = (HashSet<String>) files.get(filename);
    if(grams == null)
      return 0;
    else
      return grams.size();
  }

  @Override
  public Map<String, Map<String, Integer>> getResults() { // TODO...
    return null; //comparisons.get(new Pair<String>)
  }

  
  // Cross-compares two sets, and returns the number of elements in common.
  public int getNumElementsInCommon(Set<String> set1, Set<String> set2) {
    int matchCount = 0;
    for(String s: set1) {
      if(set2.contains(s)) {
        ++matchCount;
      }
    }
    return matchCount;
  }
  
  /* Populates a map of file paths to sets of ngrams */
  // TODO: "Map of maps of ngrams in common"
  @Override
  public void readFile(File file) throws IOException {
    String absPath = file.getAbsolutePath();
    String path = file.getName();

    HashSet<String> thisGrams = (HashSet<String>) getNgramsInFile(absPath);

    for(Entry<String, Set<String>> e : files.entrySet()) {
      // The weird double map thing was driving me crazy, so I made a Pair class, which
      // also doesn't allow it to contain duplicates.
      Pair<String> p = new Pair<String>(path, e.getKey()); // Add this to the comparisons map
      if(! comparisons.containsKey(p)) { // If the pair doesn't already exist
        int matchCount = getNumElementsInCommon(thisGrams, e.getValue());
        //System.out.println(thisGrams.size() + " " + matchCount + " " + p);
        comparisons.put(p, matchCount);
      }
    }
    files.put(path, thisGrams); // Map file path to ngrams
  }

  /* Looks up the number of ngrams in common between two files */
  @Override
  public int getNumNGramsInCommon(String file1, String file2) {
    Pair<String> p = new Pair<String>(file1,file2);
    Integer i = comparisons.get(p);
    if(i == null) {
      return 0;
    } else {
      return i;
    }
  }
  
  /* Gets pairs that match at least minNgrams times. */
  @Override
  public Collection<String> getSuspiciousPairs(int minNgrams) {
    HashSet<String> pairs = new HashSet<String>();
    for(Entry<Pair<String>,Integer> e : comparisons.entrySet()) {
      int matches = e.getValue();
      if(matches >= minNgrams) {
        pairs.add(e.getKey().getOrderedPair() + " " + matches); // Look in "Pair"
      }
    }
    return pairs;
  }

  @Override
  public void readFilesInDirectory(File dir) throws IOException {
    // Set last read dir
    lastDir = dir.getAbsolutePath();
    // delegation!
    // just go through each file in the directory, and delegate
    // to the method for reading a file
    for (File f : dir.listFiles()) {
      readFile(f);
    }
  }
}
