package assignment10;

import java.util.Scanner;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class Test 
{
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		int first = scan.nextInt();
    	String t = scan.next();
    	int len = t.length();
    	int next[] = new int[len];
    	char[] fir = t.toCharArray();
    	char[] newFir = new char[len];
    	for(int i = 0; i < len; i++)
    	{
    		int minIndex = 0;
    		for(int j = 0; j < len; j++)
    		{
    			if(fir[minIndex] > fir[j])
    				minIndex = j;
    		}
    		newFir[i] = fir[minIndex];
    		next[i] = minIndex;
    		fir[minIndex] = (char)255;
    	}
    	for(int i = 0; i < len; i++)
    	{
    		System.out.print(newFir[first]);
    		first = next[first];
    	}
	}
}
