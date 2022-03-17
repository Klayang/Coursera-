package assignemnt6;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet 
{
	private Map<String, Integer> map;
	private Digraph G;
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms)
	{
		if(synsets == null || hypernyms == null) throw new IllegalArgumentException();
		map = new HashMap<>();
		In in_synsets = new In(synsets);
		In in_hypernyms = new In(hypernyms);
		while(in_synsets.hasNextLine())
		{
			String[] synset_array = in_synsets.readLine().split(",");
			String[] noun_array = synset_array[1].split(" ");
			for(int i = 0; i < noun_array.length; i++)
				map.put(noun_array[i], Integer.parseInt(synset_array[0]));
		}
		G = new Digraph(map.size());
		while(in_hypernyms.hasNextLine())
		{
			String[] hypernyms_array = in_hypernyms.readLine().split(",");
			for(int i = 0; i < hypernyms_array.length; i++)
				G.addEdge(Integer.parseInt(hypernyms_array[0]), Integer.parseInt(hypernyms_array[i]));
		}
	}

	   // returns all WordNet nouns
	   public Iterable<String> nouns()
	   {
		   return map.keySet();
	   }

	   // is the word a WordNet noun?
	   public boolean isNoun(String word)
	   {
		   if(word == null) throw new IllegalArgumentException();
		   return map.containsKey(word);
	   }

	   // distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB)
	{
		if(nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
		SAP sap = new SAP(G);
		return sap.length(map.get(nounA), map.get(nounB));
	}

	   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	   // in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB)
	{
		if(nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
		SAP sap = new SAP(G);
	    int ancestor = sap.ancestor(map.get(nounA), map.get(nounB));
	    String noun = null;
	    for(String a: map.keySet())
	    	if(ancestor == map.get(a))
	    		noun = a;
	    return noun;
	}

	   // do unit testing of this class
	   public static void main(String[] args)
	   {
		   In in = new In("123.txt.txt");
		   Map<Integer, String> map = new HashMap<>();
		   while(in.hasNextLine())
			   System.out.println(in.readLine());
		   in.close();
	   }
}
