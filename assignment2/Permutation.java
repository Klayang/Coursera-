package assignment2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation 
{
	public static void main(String[] argv)
	{
		Deque<String> deque = new Deque<>();
		int number = Integer.parseInt(argv[0]);
		for(int i = 0; i < number; i++)
		{
			String line = StdIn.readString();
			deque.addFirst(line);
			StdOut.println(line);
		}
	}
}