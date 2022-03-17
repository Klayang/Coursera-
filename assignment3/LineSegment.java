package assignment3;

import edu.princeton.cs.algs4.StdDraw;

public class LineSegment 
{
	private final Point p; private final Point q;
	public LineSegment(Point p, Point q)
	{
		if (p == null || q == null) throw new NullPointerException("argument is null");
		this.p = p;
		this.q = q;
	}
	public void draw()
	{
		p.drawTo(q);
	}
	public String toString() 
	{
        return p + " -> " + q;
    }
}
