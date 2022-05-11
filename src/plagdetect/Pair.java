package plagdetect;

import java.util.HashSet;

// Represents a pair of items. Essentially a set of two, but cleaner.
// Order does not matter in comparisons (except in getFirst() and getSecond()).
public class Pair<T> {
  private T item1;
  private T item2;
  public Pair(T item1, T item2) {
    this.item1 = item1;
    this.item2 = item2;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object o) {
    if(!(o instanceof Pair))
      return false;
    Pair<T> p = (Pair<T>) o;
    return (item1.equals(p.item1) || item1.equals(p.item2)) && (item2.equals(p.item2) || item2.equals(p.item1));
  }
  
  // Return a hash, regardless of the order of elements.
  @Override
  public int hashCode() {
    HashSet<T> h = new HashSet<T>();
    h.add(item1);
    h.add(item2);
    return h.hashCode();
  }
  
  
  // Return a string of items separated by a space, in alphabetical order.
  public String getOrderedPair() {
    String str1 = ""+item1;
    String str2 = ""+item2;
    if(str1.compareTo(str2) > 0)
      return str2 + " " + str1;
    else
      return str1 + " " + str2;
  }
  
  @Override
  public String toString() {
    return "[Pair:(" + getOrderedPair() + ")]";
  }
  
  public T getFirst() {
    return item1;
  }
  
  public T getSecond() {
    return item2;
  }
  
  // Given an item, get the other item in the pair.
  public T getOther(T item) {
    if(item.equals(item1))
      return item2;
    else
      return item1;
  }
  
  public boolean contains(T obj) {
    return obj.equals(item1) || obj.equals(item2);
  }
}
