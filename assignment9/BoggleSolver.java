package assignment9;

import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver
{
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
	private final Queue<String> dict;
	private class GraphAndTreeOfLetters
	{
		private boolean[][] marked;
		private final Set<Integer>[] adj;
		private final int rows;
		private final int cols;
		private final BoggleBoard board;
		private Node root;
		public GraphAndTreeOfLetters(BoggleBoard board, Queue<String> dict)
		{
			this.board = board;
			rows = board.rows();
			cols = board.cols();
			marked = new boolean[board.rows()][board.cols()];
			adj = (HashSet<Integer>[]) new HashSet[rows * cols];
			for (int i = 0; i < rows * cols; i++)
				adj[i] = new HashSet<>();
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < cols; j++)
					addEdge(i, j);
			for (String word: dict)
				put(word, word.length());
		}
		private	class Node
		{
			char c;
			Node left, mid, right;
			int val;
		}
		private boolean[][] nextMarked(boolean[][]mark)
		{
			// 返回一个与给定的mark数组同值但不同引用的二维布尔数组
			int rowsOfMark = mark.length, colsOfMark = mark[0].length;
			boolean[][] nextMarked = new boolean[rowsOfMark][colsOfMark];
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < cols; j++)
					nextMarked[i][j] = mark[i][j];
			return nextMarked;
		}
		public void put(String key, int i)
		{
			root = put(root, key, i, 0);
		}
		private Node put(Node x, String key, int val, int d)
		{
			char c = key.charAt(d);
			if (x == null) { x = new Node(); x.c = c; }
			if (c < x.c) x.left = put(x.left, key, val, d);
			else if (c > x.c) x.right = put(x.right, key, val, d);
			else if (d < key.length() - 1) x.mid = put(x.mid, key, val, d + 1);
			else x.val = val;
			return x;
		}
		public Iterable<String> findValidWords()
		{
			Set<String> validWords = new HashSet<>();
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < cols; j++)
				{
					marked = new boolean[rows][cols];
					dfs(i, j, validWords, "", root, marked);
				}
			for (String word: validWords)
				if (word.length() < 3) validWords.remove(word);
			return validWords;
		}
		private void dfs(int i, int j, Set<String> validWords, String word, Node x, boolean[][] mark)
		{
			if (x == null) return;
			// 如果没有以该单词为前缀的键，则放弃当前路径的搜索
			if (!collect(get(x, board.getLetter(i, j)))) return; 
			if (board.getLetter(i, j) == x.c)
			{
				if (board.getLetter(i, j) != 'Q')
				{
					mark[i][j] = true;
					if (x.val >= 3) validWords.add(word + x.c);
					for (int num: adj[i * cols + j])
					{
						int rowIndex = num / cols;
						int colIndex = num % cols;
						if (!mark[rowIndex][colIndex]) 
						{
							boolean[][] nextMarked = nextMarked(mark);
							dfs(rowIndex, colIndex, validWords, word + x.c, x.mid, nextMarked);
						}
					}
				}
				else
				{
					Node nu = get(x.mid, 'U');
					if (nu == null) return;
					mark[i][j] = true;
					if (nu.val >= 3) validWords.add(word + "QU");
					for (int num: adj[i * cols + j])
					{
						int rowIndex = num / cols;
						int colIndex = num % cols;
						if (!mark[rowIndex][colIndex]) 
						{
							boolean[][] nextMarked = nextMarked(mark);
							dfs(rowIndex, colIndex, validWords, word + "QU", nu.mid, nextMarked);
						}
					}
				}
			}
			else if (board.getLetter(i, j) < x.c) 
			{
				boolean[][] nextMarked = nextMarked(mark);
				dfs(i, j, validWords, word, x.left, nextMarked);
			}
			else
			{
				boolean[][] nextMarked = nextMarked(mark);
				dfs(i, j, validWords, word, x.right, nextMarked);
			}
		}
		private Node get(Node x, char c)
		{
			if (x == null) return null;
			if (x.c == c) return x;
			else if (c < x.c) return get(x.left, c);
			else return get(x.right, c);
		}
		private boolean collect(Node x)
		{
			if (x == null) return false;
			if (x.val != 0) return true;
			return collect(x.left) || collect(x.mid) || collect(x.right);
		}
		void addEdge(int i, int j)
		{
			if (i - 1 >= 0) adj[i * cols + j].add((i - 1) * cols + j);
			if (i + 1 < rows)adj[i * cols + j].add((i + 1) * cols + j);
			if (j - 1 >= 0) adj[i * cols + j].add(i * cols + j - 1);
			if (j + 1 < cols) adj[i * cols + j].add(i * cols + j + 1);
			if (i - 1 >= 0 && j - 1 >= 0) adj[i * cols + j].add((i - 1) * cols + j - 1);
			if (i - 1 >= 0 && j + 1 < cols) adj[i * cols + j].add((i - 1) * cols + j + 1);
			if (i + 1 < rows && j - 1 >= 0) adj[i * cols + j].add((i + 1) * cols + j - 1);
			if (i + 1 < rows && j + 1 < cols) adj[i * cols + j].add((i + 1) * cols + j + 1);
		}
	}
    public BoggleSolver(String[] dictionary)
    {
    	dict = new Queue<>();
    	for (String word: dictionary)
    		dict.enqueue(word);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
    	GraphAndTreeOfLetters g = new GraphAndTreeOfLetters(board, dict);
    	return g.findValidWords();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
    	boolean mark = false;
    	for (String item: dict)
    		if (item.equals(word))
    		{
    			mark = true;
    			break;
    		}
    	if (!mark) return 0;
    	int length = word.length();
    	switch(length)
    	{
    		case(0): return 0;
    		case(1): return 0;
    		case(2): return 0;
	    	case(3): return 1; 
	    	case(4): return 1;
	    	case(5): return 2;
	    	case(6): return 3;
	    	case(7): return 5;
	    	default: return 11;
    	}
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}


