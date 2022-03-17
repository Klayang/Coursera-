package assignment3;

import java.util.ArrayList;
import java.util.Arrays;
public class BruteCollinearPoints 
{
	private final Point[] points;
	//if you aren't clear about the capacity of the array, set it an arraylist. You can turn it to array later.
	private ArrayList<LineSegment> line_segment;
	private int number_of_segments = 0;
	public BruteCollinearPoints(Point[] points)
	{
		if(points == null) throw new IllegalArgumentException();
		for(Point p: points) if(p == null) throw new IllegalArgumentException();
		Arrays.sort(points);
		for(int i = 0; i < points.length - 1; i++) 
			if(points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException();
		//method of copying arrays
		this.points = Arrays.copyOf(points, points.length);
		line_segment = new ArrayList<LineSegment>();
		findLine();
	}
	public int numberOfSegments()
	{
		return number_of_segments;
	}
	private void findLine()
	{
		int i, j, k, t;
		int length = points.length;
		for(i = 0; i < length - 3; i++)
			for(j = i + 1; j < length - 2; j++)
			{
				double k12 = points[i].slopeTo(points[j]);
				for(k = j + 1; k < length - 1; k++)
				{
					double k13 = points[i].slopeTo(points[k]);
					if(k12 != k13) continue;
					for(t = k + 1; t < length; t++)
					{
						double k14 = points[i].slopeTo(points[t]);
						if(k14 != k12) continue;
						line_segment.add(new LineSegment(points[i], points[t]));
						number_of_segments++;
					}
				}
			}
	}
	public LineSegment[] segments()
	{
		return line_segment.toArray(new LineSegment[0]);
	}
}
