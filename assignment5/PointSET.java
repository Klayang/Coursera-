package assignment5;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET 
{
	private Set<Point2D> set = new TreeSet<>();
	private int size;
	public boolean isEmpty()
	{
		return size == 0;
	}
	public int size()
	{
		return size;
	}
	public void insert(Point2D p)
	{
		if(p == null) throw new IllegalArgumentException();
		if(contains(p)) return;
		set.add(p);
		size++;
	}
	public boolean contains(Point2D p)
	{
		if(p == null) throw new IllegalArgumentException();
		return set.contains(p);
	}
	public void draw()
	{
		for(Point2D p: set)
			p.draw();
	}
	public Iterable<Point2D> range(RectHV rect)
	{
		if(rect == null) throw new IllegalArgumentException();
		ArrayList<Point2D> points = new ArrayList<>();
		for(Point2D p: set)
			if(p.x() >= rect.xmin() && p.x() <= rect.xmax() && p.y() >= rect.ymin() && p.y() <= rect.ymax())
				points.add(p);
		return points;
	}
	public Point2D nearest(Point2D p)
	{
		if(p == null) throw new IllegalArgumentException();
		Point2D nearest_point = null;
		double distance = 0.0;
		int count = 0;
		for(Point2D point: set)
		{
			if(count == 0)
			{
				distance = point.distanceTo(p);
				nearest_point = point;
				count++;
			}
			else if(point.distanceTo(p) < distance)
			{
				distance = point.distanceTo(p);
				nearest_point = point;
			}
			else continue;
		}
		return nearest_point;
	}
	public static void main(String[] args)
	{
		PointSET kd = new PointSET();
		Point2D p1 = new Point2D(0.7, 0.2);
		Point2D p2 = new Point2D(0.5, 0.4);
		Point2D p3 = new Point2D(0.2, 0.3);
		Point2D p4 = new Point2D(0.4, 0.7);
		Point2D p5 = new Point2D(0.9, 0.6);
		kd.insert(p1);
		kd.insert(p2);
		kd.insert(p3);
		kd.insert(p4);
		kd.insert(p5);
		kd.draw();;
		StdDraw.setPenColor(StdDraw.BLACK);
		p1.draw();
		p2.draw();
		p3.draw();
		p4.draw();
		p5.draw();
		StdDraw.show();
		System.out.println(kd.nearest(new Point2D(0.896, 0.385)));
	}
}
