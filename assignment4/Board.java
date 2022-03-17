package assignment4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Board 
{
	private int n;
	private int[][] tiles;
	private int[][] neighbour1_of_tiles;
	private int[][] neighbour2_of_tiles;
	private int[][] neighbour3_of_tiles;
	private int[][] neighbour4_of_tiles;
	private int[][] twin_of_tiles;
	private int row_of_zero;
	private int column_of_zero;
	private ArrayList<Board> neighbours = new ArrayList<Board>();
	private int hamming_distance;
	private int manhattan_distance;
	private Board twin;
	public Board(int[][] tiles)
	{
		if(tiles == null) throw new IllegalArgumentException();
		this.n = tiles[0].length;
		this.tiles = new int[n][n];
		this.neighbour1_of_tiles = new int[n][n];
		this.neighbour2_of_tiles = new int[n][n];
		this.neighbour3_of_tiles = new int[n][n];
		this.neighbour4_of_tiles = new int[n][n];
		this.twin_of_tiles = new int[n][n];
		for(int i = 0; i < n; i++)
		{
			this.tiles[i] = Arrays.copyOf(tiles[i], n);
			this.neighbour1_of_tiles[i] = Arrays.copyOf(tiles[i], n);
			this.neighbour2_of_tiles[i] = Arrays.copyOf(tiles[i], n);
			this.neighbour3_of_tiles[i] = Arrays.copyOf(tiles[i], n);
			this.neighbour4_of_tiles[i] = Arrays.copyOf(tiles[i], n);
			this.twin_of_tiles[i] = Arrays.copyOf(tiles[i], n);
		}
		find_zero();
		hamming();
		manhattan();
	}
	public String toString()
	{
		String name = new String();
		name = Integer.toString(n) + "\n";
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				if(j == n - 1) name += tiles[i][j];
				else name = name + tiles[i][j] + " "; 
			}
			if(i != n - 1) name += "\n";
		}
		return name;
	}
	public int dimension()
	{
		return n;
	}
	public int hamming()
	{
		if(hamming_distance > 0) return hamming_distance;
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
			{
				if(i == n - 1 && j == n - 1) continue;
				if((n * i + j + 1) != tiles[i][j]) hamming_distance++;
			}
		return hamming_distance;
	}
	public int manhattan()
	{
		if(manhattan_distance > 0) return manhattan_distance; 
		int number = 0;
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
			{
				number = tiles[i][j];
				if(number == 0 || number == (n * i + j + 1)) continue;
				else manhattan_distance = manhattan_distance + Math.abs(i - row_of_number(number)) + Math.abs(j - column_of_number(number)); 
			}
		return manhattan_distance;
	}
	public boolean equals(Object y)
	{
		if(y == null) return false;
		return this.toString().equals(y.toString());
	}
	public boolean isGoal()
	{
		return hamming_distance == 0;
	}
	public Board twin()
	{
		if(twin != null) return twin;
		if(row_of_zero == 0 && column_of_zero == 0) exch(twin_of_tiles, 0, 1, 1, 1);
		else if(row_of_zero == 0 && column_of_zero == 1) exch(twin_of_tiles, 0, 0, 1, 0);
		else exch(twin_of_tiles, 0, 0, 0, 1);
		twin = new Board(twin_of_tiles);
		return twin;
	}
	public Iterable<Board> neighbors()
	{
		return new Iterable<Board>() 
		{

			@Override
			public Iterator<Board> iterator() {
				// TODO Auto-generated method stub
				manipulate_neighbours();
				return neighbours.iterator();
			}
			
		};
	}
	private int row_of_number(int number)
	{
		if(number % n != 0) return number / n;
		else return number / n - 1;
	}
	private int column_of_number(int number)
	{
		if(number % n != 0) return number % n - 1;
		else return n - 1;
	}
	private void find_zero()
	{
		label:
			for(int i = 0; i < n; i++)
				for(int j = 0; j < n; j++)
					if(tiles[i][j] == 0) 
					{
						row_of_zero = i; column_of_zero = j;
						break label;
					}
	}
	private void manipulate_neighbours() 
	{
		if(row_of_zero == 0 && column_of_zero == 0)
		{
			exch_right(neighbour1_of_tiles, 0, 0);
			exch_down(neighbour2_of_tiles, 0, 0);
		}
		else if(row_of_zero == 0 && column_of_zero == n - 1)
		{
			exch_left(neighbour1_of_tiles, 0, n - 1);
			exch_down(neighbour2_of_tiles, 0, n - 1);
		}
		else if(row_of_zero == n - 1 && column_of_zero == 0)
		{
			exch_right(neighbour1_of_tiles, n - 1, 0);
			exch_up(neighbour2_of_tiles, n - 1, 0);
		}
		else if(row_of_zero == n - 1 && column_of_zero == n - 1)
		{
			exch_left(neighbour1_of_tiles, n - 1, n - 1);
			exch_up(neighbour2_of_tiles, n - 1, n - 1);
		}
		else if(row_of_zero == 0 && column_of_zero != 0 && column_of_zero != n - 1)
		{
			exch_left(neighbour1_of_tiles, 0, column_of_zero);
			exch_right(neighbour2_of_tiles, 0, column_of_zero);
			exch_down(neighbour3_of_tiles, 0, column_of_zero);
		}
		else if(row_of_zero == n - 1 && column_of_zero != 0 && column_of_zero != n - 1)
		{
			exch_left(neighbour1_of_tiles, n - 1, column_of_zero);
			exch_right(neighbour2_of_tiles, n - 1, column_of_zero);
			exch_up(neighbour3_of_tiles, n - 1, column_of_zero);
		}
		else if(column_of_zero == 0 && row_of_zero != 0 && row_of_zero != n - 1)
		{
			exch_up(neighbour1_of_tiles, row_of_zero, 0);
			exch_down(neighbour2_of_tiles, row_of_zero, 0);
			exch_right(neighbour3_of_tiles, row_of_zero, 0);
		}
		else if(column_of_zero == n - 1 && row_of_zero != 0 && row_of_zero != n - 1)
		{
			exch_up(neighbour1_of_tiles, row_of_zero, n - 1);
			exch_down(neighbour2_of_tiles, row_of_zero, n - 1);
			exch_left(neighbour3_of_tiles, row_of_zero, n - 1);
		}
		else
		{
			exch_up(neighbour1_of_tiles, row_of_zero, column_of_zero);
			exch_down(neighbour2_of_tiles, row_of_zero, column_of_zero);
			exch_left(neighbour3_of_tiles, row_of_zero, column_of_zero);
			exch_right(neighbour4_of_tiles, row_of_zero, column_of_zero);
		}
	}
	private void exch_up(int[][] array, int row, int column)
	{
		array[row][column] = array[row - 1][column];
		array[row - 1][column] = 0;
		neighbours.add(new Board(array));
	}
	private void exch_down(int[][] array, int row, int column)
	{
		array[row][column] = array[row + 1][column];
		array[row + 1][column] = 0;
		neighbours.add(new Board(array));
	}
	private void exch_left(int[][] array, int row, int column)
	{
		array[row][column] = array[row][column - 1];
		array[row][column - 1] = 0;
		neighbours.add(new Board(array));
	}
	private void exch_right(int[][] array, int row, int column)
	{
		array[row][column] = array[row][column + 1];
		array[row][column + 1] = 0;
		neighbours.add(new Board(array));
	}
	private void exch(int[][] array, int row1, int column1, int row2, int column2)
	{
		int middle = array[row1][column1];
		array[row1][column1] = array[row2][column2];
		array[row2][column2] = middle;
	}
	public static void main(String[] args)
	{
		int[][] a = new int[][] {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
		Board board = new Board(a);
		System.out.println(board);
		for(Board bo: board.neighbors())
			System.out.println(bo);
		System.out.println(board.twin());
		System.out.println(board.twin());
	}
}
