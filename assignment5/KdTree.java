package assignment5;

import java.util.ArrayList;


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree
{
	private static final boolean VERTICAL = true;
	private static final boolean HORIZONTAL = false;
	private static final RectHV INITIAL_RECTANGLE = new RectHV(0.0, 0.0, 1.0, 1.0);
	private Node root;
	private int size;
	private class Node
	{
		Point2D point;
		boolean direction;
		Node left;
		Node right;
		Node(Point2D point, boolean direction)
		{
			this.point = point;
			this.direction = direction;
		}
	}
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
		if(root == null)
		{
			root = new Node(p, VERTICAL);
			size++;
			return;
		}
		root = insert(root, p, root.direction);
		size++;
	}
	private Node insert(Node h, Point2D p, boolean direction)
	{
		if(h == null) return new Node(p, direction);  
		double cmp;
		if(direction == VERTICAL) cmp = p.x() - h.point.x();
		else cmp = p.y() - h.point.y();
		if(cmp < 0) h.left = insert(h.left, p, !direction);
		else h.right = insert(h.right, p, !direction);
		return h;
	}
	public boolean contains(Point2D p)
	{
		if(p == null) throw new IllegalArgumentException();
		if(root == null) return false;
		return contains(root, p, root.direction);
	}
	private boolean contains(Node h, Point2D p, boolean direction)
	{
		if(h == null) return false;
		if(h.point.x() == p.x() && h.point.y() == p.y()) return true;
		double cmp;
		if(direction == VERTICAL) cmp = p.x() - h.point.x();
		else cmp = p.y() - h.point.y();
		if(cmp < 0) return contains(h.left, p, !direction);
		else return contains(h.right, p, !direction);
	}
	public void draw()
	{
		if(root == null) return;
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.BLACK);
		root.point.draw();
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.line(root.point.x(), 0.0, root.point.x(), 1.0);
		StdDraw.show();
		drawLeft(root, root.left, !root.direction);
		drawRight(root, root.right, !root.direction);
	}
	private void drawLeft(Node previous, Node present, boolean direction)
	{
		if(present == null) return;
		StdDraw.setPenColor(StdDraw.BLACK);
		present.point.draw();
		if(direction == VERTICAL)
		{
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(present.point.x(), 0.0, present.point.x(), previous.point.y());
		}
		else
		{
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(0.0, present.point.y(), previous.point.x(), present.point.y());
		}
		drawLeft(present, present.left, !direction);
		drawRight(present, present.right, !direction);
	}
	private void drawRight(Node previous, Node present, boolean direction)
	{
		if(present == null) 
		{
			return;
		}
		StdDraw.setPenColor(StdDraw.BLACK);
		present.point.draw();
		if(direction == VERTICAL)
		{
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(present.point.x(), previous.point.y(), present.point.x(), 1.0);
		}
		else
		{
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(previous.point.x(), present.point.y(), 1.0, present.point.y());
		}
		drawRight(present, present.right, !direction);
		drawLeft(present, present.left, !direction);
	}
	public Iterable<Point2D> range(RectHV rect)
	{
		if(rect == null) throw new IllegalArgumentException();
		ArrayList<Point2D> points = new ArrayList<>();
		range(root, rect, points, VERTICAL);
		return points;
	}
	private void range(Node h, RectHV rect, ArrayList<Point2D> points, boolean direction)
	{
		if(h == null) return;
		if(rect.contains(h.point)) points.add(h.point);
		if(direction == VERTICAL) 
		{
			if(h.point.x() > rect.xmax()) range(h.left, rect, points, HORIZONTAL);
			else if(h.point.x() < rect.xmin()) range(h.right, rect, points, HORIZONTAL);
			else
			{
				range(h.left, rect, points, HORIZONTAL);
				range(h.right, rect, points, HORIZONTAL);
			}
		}
		else
		{
			if(h.point.y() > rect.ymax()) range(h.left, rect, points, VERTICAL);
			else if(h.point.y() < rect.ymin()) range(h.right, rect, points, VERTICAL);
			else
			{
				range(h.left, rect, points, VERTICAL);
				range(h.right, rect, points, VERTICAL);
			}
		}
	}
	public Point2D nearest(Point2D p)
	{
		if(p == null) throw new IllegalArgumentException();
		if(root == null) return null;
		return nearest(root, p, INITIAL_RECTANGLE, root.point);
	}
	private Point2D nearest(Node h, Point2D query, RectHV rect, Point2D closest)
	{
		if(h == null) return closest;
		RectHV left = null;
		RectHV right = null;
		double distance_between_points = closest.distanceSquaredTo(query);
		double distance_between_point_and_rectangle = rect.distanceSquaredTo(query);
		if(distance_between_points < distance_between_point_and_rectangle) return closest;
		if(h.direction == VERTICAL)
		{
			if(distance_between_points > query.distanceSquaredTo(h.point)) closest = h.point;
			left = new RectHV(rect.xmin(), rect.ymin(), h.point.x(), rect.ymax());
			right = new RectHV(h.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
			if(query.x() < h.point.x())
			{
				closest = nearest(h.left, query, left, closest);
				closest = nearest(h.right, query, right, closest);
			}
			else
			{
				closest = nearest(h.right, query, right, closest);
				closest = nearest(h.left, query, left, closest);
			}
		}
		else
		{
			if(distance_between_points > query.distanceSquaredTo(h.point)) closest = h.point;
			left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), h.point.y());
			right = new RectHV(rect.xmin(), h.point.y(), rect.xmax(), rect.ymax());
			if(query.y() < h.point.y())
			{
				closest = nearest(h.left, query, left, closest);
				closest = nearest(h.right, query, right, closest);
			}
			else
			{
				closest = nearest(h.right, query, right, closest);
				closest = nearest(h.left, query, left, closest);
			}
		}
		return closest;
	}
	public static void main(String[] args)
	{
		KdTree kd = new KdTree();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
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
