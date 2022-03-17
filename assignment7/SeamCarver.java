package assignment7;

import java.awt.Color;
import edu.princeton.cs.algs4.Picture;


public class SeamCarver 
{
	private static final boolean VERTICAL = true;
	private static final boolean HORIZONTAL = false;
	private Picture p;
	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture)
	{
		if (picture == null) throw new IllegalArgumentException();
		this.p = picture;
	}

	   // current picture
	public Picture picture()
	{
		return p;
	}
	   // width of current picture
	public int width()
	{
		return p.width();
	}
	   // height of current picture
	public int height()
	{
		return p.height();
	}
	   // energy of pixel at column x and row y
	public double energy(int x, int y)
	{	// 计算像素点的能量值
		if (x < 0 || x >= p.width() || y < 0 || y >= p.height()) throw new IllegalArgumentException();
		if (x == 0 || y == 0 || x == p.width() - 1 || y == p.height() - 1) return 1000.0;
		double verticalEnergySquare = 0.0; double horizontalEnergySquare = 0.0;
		Color left = new Color(p.getRGB(x - 1, y)); Color right = new Color(p.getRGB(x + 1, y));
		Color up = new Color(p.getRGB(x, y - 1)); Color down = new Color(p.getRGB(x, y + 1));
		horizontalEnergySquare = Math.pow(right.getBlue() - left.getBlue(), 2.0) + Math.pow(right.getGreen() - left.getGreen(), 2.0) + Math.pow(right.getRed() - left.getRed(), 2.0);
		verticalEnergySquare = Math.pow(down.getBlue() - up.getBlue(), 2.0) + Math.pow(down.getGreen() - up.getGreen(), 2.0) + Math.pow(down.getRed() - up.getRed(), 2.0);
		return Math.sqrt(horizontalEnergySquare + verticalEnergySquare);
	}
	   // sequence of indices for horizontal seam
	public int[] findHorizontalSeam()
	{
		return findseam(HORIZONTAL);
	}
	   // sequence of indices for vertical seam
	public int[] findVerticalSeam()
	{
		return findseam(VERTICAL);
	}
	   // remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam)
	{
		if (seam == null) throw new IllegalArgumentException();
		if (seam.length != p.width()) throw new IllegalArgumentException();
		for (int i = 0; i < seam.length; i++)
		{
			if (seam[i] < 0 || seam[i] >= p.height()) throw new IllegalArgumentException();
			if (i >= 1 && Math.abs(seam[i] - seam[i - 1]) > 1) throw new IllegalArgumentException();
		}
		Picture currentPict = new Picture(p.width(), p.height() - 1);
		for (int i = 0; i < p.width(); i++)
			for (int j = 0, k = 0; j < p.height(); j++)
			{
				if (seam[i] == j) continue;
				currentPict.setRGB(i, k++, p.getRGB(i, j));
			}
		p = currentPict;
	}
	   // remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam)
	{
		if (seam == null) throw new IllegalArgumentException();
		if (seam.length != p.height()) throw new IllegalArgumentException();
		for (int i = 0; i < seam.length; i++)
		{
			if (seam[i] < 0 || seam[i] >= p.width()) throw new IllegalArgumentException();
			if (i >= 1 && Math.abs(seam[i] - seam[i - 1]) > 1) throw new IllegalArgumentException();
		}
		Picture currentPict = new Picture(p.width() - 1, p.height());
		for (int i = 0; i < p.height(); i++)
			for (int j = 0, k = 0; j < p.width(); j++)
			{
				if (seam[i] == j) continue;
				currentPict.setRGB(k++, i, p.getRGB(j, i));
			}
		p = currentPict;
	}
	private int[] findseam(boolean mode)
	{
		// judge on 2 specific situations
		int firstLayer, secondLayer;
		if (mode)
		{
			firstLayer = p.height();
			secondLayer = p.width();
		}
		else
		{
			firstLayer = p.width();
			secondLayer = p.height();
		}

		// define two significant arrays
		int[][] edgeTo = new int[firstLayer + 2][];
		double[][] distTo = new double[firstLayer + 2][];
		for (int i = 0; i < firstLayer + 2; i++)
		{
			if (i == 0 || i == firstLayer + 1)
			{
				edgeTo[i] = new int[1];
				distTo[i] = new double[1];
			}
			else 
			{
				edgeTo[i] = new int[secondLayer];
				distTo[i] = new double[secondLayer];
			}
		}
		for (int i = 0; i < distTo.length; i++)
			for (int j = 0; j < distTo[i].length; j++)
				distTo[i][j] = Double.POSITIVE_INFINITY;
		
		// compute the value of each pixel
		double[][] energy = new double[firstLayer][secondLayer];
		for (int i = 0; i < firstLayer; i++)
			for (int j = 0; j < secondLayer; j++)
				if (mode == VERTICAL)
					energy[i][j] = this.energy(j, i);
				else energy[i][j] = this.energy(i, j);
		
		// link the first layer to the starting point
		for (int i = 0; i < secondLayer; i++)
		{
			distTo[1][i] = 0.0;
			edgeTo[1][i] = 0;
		}
		
		// relax in topological order
		for (int i = 1; i <= firstLayer; i++)
			for (int j = 0; j < secondLayer; j++)
			{
				double newEnergy = energy[i - 1][j] + distTo[i][j];
				if (i < firstLayer)
				{
					if (j - 1 >= 0 && distTo[i + 1][j - 1] > newEnergy) 
					{
						distTo[i + 1][j - 1] = newEnergy;
						edgeTo[i + 1][j - 1] = j;
					}
					if (j >= 0 && j < secondLayer && distTo[i + 1][j] > newEnergy) 
					{
						distTo[i + 1][j] = newEnergy;
						edgeTo[i + 1][j] = j;
					}
					if (j + 1 < secondLayer && distTo[i + 1][j + 1] > newEnergy)
					{
						distTo[i + 1][j + 1] = newEnergy;
						edgeTo[i + 1][j + 1] = j;
					}
				}
				else
				{
					if (distTo[i + 1][0] > newEnergy)
					{
						distTo[i + 1][0] = newEnergy;
						edgeTo[i + 1][0] = j;
					}
				}
			}
		
		// output the result in seam
		int[] seam = new int[firstLayer];
		int i = firstLayer + 1;
		int node = edgeTo[i][0];
		while (true)
		{
			seam[i - 2] = node;
			i--;
			if (i < 2) break;
			node = edgeTo[i][node];
		}
		return seam;
	}
	   //  unit testing (optional)
	public static void main(String[] args)
	{
		
	}
}
