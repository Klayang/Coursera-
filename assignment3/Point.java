package assignment3;

import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point>
{
	private final int x; private final int y;
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public int compareTo(Point that) 
	{
		if(this.y != that.y) return this.y - that.y;
		else return this.x - that.x;
	}
	public double slopeTo(Point that)
	{
		if(this.x == that.x && this.y != that.y) return Double.POSITIVE_INFINITY;
		if(this.y == that.y && this.x != that.x) return (this.y - that.y) / Math.abs(this.x - that.x);
		if(this.x == that.x && this.y == that.y) return Double.NEGATIVE_INFINITY;
		return ((double)this.y - (double)that.y) / ((double)this.x - (double)that.x);
	}
	public Comparator<Point> slopeOrder()
	{
		return new Comparator<Point>() 
		{
			@Override
			public int compare(Point o1, Point o2) {
				// TODO Auto-generated method stub
				return Double.compare(slopeTo(o1), slopeTo(o2));
			}
		};
	}
	 public void draw() 
	 {
	        /* DO NOT MODIFY */
	        StdDraw.point(x, y);
	 }
	 public void drawTo(Point that) {
	        /* DO NOT MODIFY */
	        StdDraw.line(this.x, this.y, that.x, that.y);
	    }
	 
	 public String toString() {
	        /* DO NOT MODIFY */
	        return "(" + x + ", " + y + ")";
	    }
	 
	 public static void main(String[] args) 
	 {
		 Point[] points = new Point[5];
		 points[0] = new Point(2000, 24000);
		 points[1] = new Point(9000, 24000);
		 points[2] = new Point(14000, 24000);
		 points[3] = new Point(25000, 24000);
		 points[4] = new Point(1000, 1000);
		 FastCollinearPoints collinear = new FastCollinearPoints(points);
		    for (LineSegment segment : collinear.segments()) {
		        StdOut.println(segment);
		        segment.draw();
		    }
		 StdDraw.show();
	 }
}
