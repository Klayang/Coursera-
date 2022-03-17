package assignemnt6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class SAP 
{
	private final Digraph G;
	private BreadthFirstDirectedPaths bfdpv;
	private BreadthFirstDirectedPaths bfdpw;
	private Map<Integer, Integer> map;
	private int length;
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) 
	{
		if(G == null) throw new IllegalArgumentException();
		this.G = G;
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) 
	{
		if(v >= G.V() || v < 0 || w>= G.V() || w < 0) throw new IllegalArgumentException();
		int ancestor = ancestor(v, w);
		return length;
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) 
	{
		test(v); test(w);
		bfdpv = new BreadthFirstDirectedPaths(G, v);
		bfdpw = new BreadthFirstDirectedPaths(G, w);
		map = new HashMap<>();
		for(int i = 0; i < G.V(); i++)
			if(bfdpv.hasPathTo(i) && bfdpw.hasPathTo(i))
				map.put(i, bfdpv.distTo(i) + bfdpw.distTo(i));
		if(map.isEmpty()) 
		{
			this.length = -1;
			return -1;
		}
		int length = Integer.MAX_VALUE;
		int ancestor = 0;
		for(int i: map.keySet())
			if(map.get(i) < length)
			{
				ancestor = i;
				length = map.get(i);
			}
		this.length = length;
		return ancestor;
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) 
	{
		test(v); test(w);
		int ancestor = ancestor(v, w);
		return this.length;
	}
	
	private void test(Iterable<Integer> s)
	{
		if(s == null) throw new IllegalArgumentException();
		Iterator<Integer> it = s.iterator();
		if(!it.hasNext()) throw new IllegalArgumentException();
		while(it.hasNext())
			if(it.next() == null) throw new IllegalArgumentException();
		for(int i: s) test(i);
	}
	
	private void test(int v)
	{
		if(v < 0 || v > G.V()) throw new IllegalArgumentException();
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
	{
		test(v); test(w);
		bfdpv = new BreadthFirstDirectedPaths(G, v);
		bfdpw = new BreadthFirstDirectedPaths(G, w);
		map = new HashMap<>();
		for(int i = 0; i < G.V(); i++)
			if(bfdpv.hasPathTo(i) && bfdpw.hasPathTo(i))
				map.put(i, bfdpv.distTo(i) + bfdpw.distTo(i));
		if(map.isEmpty()) return -1;
		int ancestor = 0;
		int length = Integer.MAX_VALUE;
		for(int i: map.keySet())
			if(map.get(i) < length)
			{
				ancestor = i; 
				length = map.get(i);
			}
		this.length = length;
		return ancestor;
	}

	 // do unit testing of this class
	 public static void main(String[] args) 
	 {
		 In in = new In();
		 Digraph G = new Digraph(in);
		 SAP sap = new SAP(G);
		 ArrayList<Integer> list1 = new ArrayList<>();
		 ArrayList<Integer> list2 = new ArrayList<>();
		 list1.add(13); list1.add(23); list1.add(24);
		 list2.add(6); list2.add(16); list2.add(17);
		 System.out.println(sap.length(1, 5));
	 }
}
