package assignment4;

import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver 
{
	private class SearchNode implements Comparable<SearchNode>
	{
		Board board;
		int moves;
		SearchNode previous_node;
		@Override
		public int compareTo(SearchNode o) {
			// TODO Auto-generated method stub
			return this.moves + this.board.manhattan() - o.moves- o.board.manhattan();
		}
	}
	private MinPQ<SearchNode> minpq;
	private MinPQ<SearchNode> twin_of_minpq;
	private int move;
	private SearchNode result;
	private boolean is_solvable = true;
	private Stack<Board> stack;
	public Solver(Board initial)
	{
		if(initial == null) throw new IllegalArgumentException();
		
		SearchNode initial_node = new SearchNode();
		SearchNode twin_of_initial_node = new SearchNode();
		
		minpq = new MinPQ<SearchNode>();
		twin_of_minpq = new MinPQ<SearchNode>();
		
		initial_node.moves = 0;
		initial_node.board = initial;
		initial_node.previous_node = null;
		
		twin_of_initial_node.moves = 0;
		twin_of_initial_node.board = initial.twin();
		twin_of_initial_node.previous_node = null;
		minpq.insert(initial_node);
		twin_of_minpq.insert(twin_of_initial_node);
		while(true)
		{
			SearchNode nextstep = minpq.delMin();
			if(nextstep.board.isGoal()) 
			{
				result = nextstep;
				move = result.moves;
				break;
			}
			for(Board neighbor: nextstep.board.neighbors())
			{
				if(nextstep.previous_node != null && neighbor.equals(nextstep.previous_node.board)) continue;
				SearchNode search_neighbor = new SearchNode();
				search_neighbor.board = neighbor;
				search_neighbor.moves = nextstep.moves + 1;
				search_neighbor.previous_node = nextstep;
				minpq.insert(search_neighbor);
			}
			
			SearchNode twin_of_nextstep = twin_of_minpq.delMin();
			if(twin_of_nextstep.board.isGoal())
			{
				is_solvable = false;
				break;
			}
			for(Board neighbour: twin_of_nextstep.board.neighbors())
			{
				if(twin_of_nextstep.previous_node != null && neighbour.equals(twin_of_nextstep.previous_node.board)) continue;
				SearchNode search_neighbor = new SearchNode();
				search_neighbor.board = neighbour;
				search_neighbor.moves = nextstep.moves + 1;
				search_neighbor.previous_node = twin_of_nextstep;
				twin_of_minpq.insert(search_neighbor);
			}
				
		}

	}
	public int moves()
	{
		if(!is_solvable) return -1;
		return move;
	}
	public boolean isSolvable() 
	{
		return is_solvable;
	}
	public Iterable<Board> solution()
	{
		if(!is_solvable) return null;
		if(stack != null) return stack;
		stack = new Stack<>();
		while(result != null)
		{
			stack.push(result.board);
			result = result.previous_node;
		}
		return stack;
	}
	public static void main(String[] args)
	{
		int[][] a = new int[][] {{3, 7, 1}, {8, 4, 5}, {6, 2, 0}};
		Board board = new Board(a);
		Solver solver = new Solver(board);
		if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board1 : solver.solution())
	            StdOut.println(board1);
	    }
	}
}
