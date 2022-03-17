package assignment3;

import java.util.ArrayList;
import java.util.Arrays;
public class FastCollinearPoints 
{
	private Point[] origin_points;
	private Point[] sort_points;
	private ArrayList<LineSegment> line_segment;
	private int number_of_segments = 0;
	public FastCollinearPoints(Point[] points)
	{
		if(points == null) throw new IllegalArgumentException();
		for(Point p: points) if(p == null) throw new IllegalArgumentException();
		Arrays.sort(points);
		for(int i = 0; i < points.length - 1; i++) 
			if(points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException();
		origin_points = Arrays.copyOf(points, points.length);
		sort_points = Arrays.copyOf(points, points.length);
		line_segment = new ArrayList<LineSegment>();
		findline();
	}
	public int numberOfSegments()
	{
		return number_of_segments;
	}
	public LineSegment[] segments()
	{
		return line_segment.toArray(new LineSegment[0]);
	}
	private void findline()
	{
		Arrays.sort(sort_points);
		int length = origin_points.length;
		int current = 0;
		while(current < length)
		{
			Point base = origin_points[current++];
			Arrays.sort(sort_points, base.slopeOrder());
			Point min = base;
			Point max = base;
			int count = 2;
			for(int i = 0; i < length - 1; i++)
			{
				double k1 = base.slopeTo(sort_points[i]);
				double k2 = base.slopeTo(sort_points[i + 1]);
				if(k1 == k2)
				{
					count++;
					if(sort_points[i].compareTo(sort_points[i + 1]) < 0)
					{
						if(max.compareTo(sort_points[i + 1]) < 0) max = sort_points[i+1];
						if(min.compareTo(sort_points[i]) > 0) min = sort_points[i];
					}
					else 
					{
						if(max.compareTo(sort_points[i]) < 0) max = sort_points[i];
						if(min.compareTo(sort_points[i + 1]) > 0) min = sort_points[i + 1];
					}
					if((i == length - 2) && (count >= 4) && (base.compareTo(min) == 0))
					{
						line_segment.add(new LineSegment(base, max));
						number_of_segments++;
					}
				}
				else
				{
					if(count >= 4 && base.compareTo(min) == 0)
					{
						line_segment.add(new LineSegment(min, max));
						number_of_segments++;
					}
					count = 2;
					min = base;
					max = base;
				}
			}
		}
	}
}
