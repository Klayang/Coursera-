package assignment1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats 
{
	private final int n;
	private final int trails;
	private final Percolation[] per;
	private double[] thre;
	private double mean;
	private double stddev;
	public PercolationStats(int n, int trails)
	{
		if(n <= 0 || trails <= 0) throw new IllegalArgumentException();
		this.n = n;
		this.trails = trails;
		per = new Percolation[trails];
		for(int i = 0; i < trails; i++)
			per[i] = new Percolation(n);
		thre = new double[trails];
		simulate();
	}
	public double mean()
	{
		mean = StdStats.mean(thre);
		return mean;
	}
	public double stddev()
	{
		if(trails == 1) return Double.NaN;
		stddev = StdStats.stddev(thre);
		return stddev;
	}
	public double confidenceLo()
	{
		double confidencelo = mean - 1.96 * stddev / Math.sqrt(trails);
		return confidencelo;
	}
	public double confidenceHi()
	{
		double confidencehi = mean + 1.96 * stddev / Math.sqrt(trails);
		return confidencehi;
	}
	private void simulate()
	{
		int times = 0;
		for(times = 0; times < this.trails; times++)
		{
			while(true)
			{
				int row = StdRandom.uniform(1, n + 1);
				int col = StdRandom.uniform(1, n + 1);
				per[times].open(row, col);
				if(per[times].percolates()) 
				{
					double numberofOpenSites = (double)per[times].numberOfOpenSites();
					thre[times] = numberofOpenSites / (n*n);
					break;
				}
			}
		}
		mean();
		stddev(); 
		confidenceLo(); confidenceHi();
	}
	public static void main(String[] args)
	{
		PercolationStats per = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		StdOut.printf("mean = %f\n", per.mean());
		StdOut.printf("stddev = %f\n", per.stddev()); 
		StdOut.printf("%s confidence interval = [%f, %f]\n", "95%", per.confidenceLo(), per.confidenceHi());
	}
}
