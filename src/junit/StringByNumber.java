package junit;

/* A special string that can be sorted only by a specific word that is an integer, delimeted by spaces. */
public class StringByNumber implements Comparable {
  private String str;
  public int numIndex;
  public StringByNumber(String str, int numIndex) {
    this.str = str;
    this.numIndex = numIndex;
  }
  
  @Override
  public String toString() {
    return str;
  }

  @Override
  public int compareTo(Object o) {
    StringByNumber s = (StringByNumber) o;
    String split[] = str.split(" ");
    String oSplit[] = s.str.split(" ");
    
    int num1 = 0;
    int num2 = 0;
    boolean parseFailed = false;
    try {
      num1 = Integer.parseInt(split[numIndex]);
      num2 = Integer.parseInt(oSplit[numIndex]);
    } catch(NumberFormatException e) {
      parseFailed = true;
    }
    
    if(parseFailed || numIndex > split.length-1 || numIndex > oSplit.length-1) { // Safety check; just compare the whole string in this case...
      return str.compareTo(s.str);
    }
    
    return num1 - num2;
  }
}
