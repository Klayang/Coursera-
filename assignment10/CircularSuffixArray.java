package assignment10;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray 
{
	private String sa;
	private Integer[] indexArray;
	// circular suffix array of s
	
    public CircularSuffixArray(String s)
    {	
    	if(s == null) throw new IllegalArgumentException();
    	
    	this.sa = s;
    	indexArray = new Integer[sa.length()];
    	for(int i = 0; i < sa.length(); i++)
    		indexArray[i] = i;
		Arrays.sort(indexArray, new Comparator<Integer>() {
			@Override
			public int compare(Integer n1, Integer n2) {
				for(int i = 0; i < sa.length(); i++)
				{
					int index1 = (n1 + i) % sa.length();
					int index2 = (n2 + i) % sa.length();
					if(sa.charAt(index1) < sa.charAt(index2)) return -1;
					else if(sa.charAt(index1) > sa.charAt(index2)) return 1;
				}
				return 0;
		}});
    }
    // length of s
    public int length()
    {
    	return sa.length();
    }

    // returns index of ith sorted suffix
    public int index(int i)
    {
    	if(i < 0 || i >= sa.length()) throw new IllegalArgumentException();
    	return indexArray[i];
    }

    // unit testing (required)
    public static void main(String[] args)
    {
    	CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
    	System.out.println(csa.index(11));
    }

}