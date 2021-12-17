/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> bst;

    // construct an empty set of points
    public PointSET() {
        bst = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return bst.isEmpty();
    }

    // number of points in the set
    public int size() {
        return bst.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        bst.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return bst.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D eachPoint : bst) {
            StdDraw.point(eachPoint.x(), eachPoint.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Point2D> pointsInRange = new ArrayList<>();
        for (Point2D eachPoint : bst) {
            if (rect.contains(eachPoint)) {
                pointsInRange.add(eachPoint);
            }
        }
        return pointsInRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (bst.isEmpty()) return null;

        Point2D closestPoint = bst.first();
        double maxDist = closestPoint.distanceSquaredTo(p);

        for (Point2D eachPoint : bst) {
            double distToP = eachPoint.distanceSquaredTo(p);
            if (distToP < maxDist) {
                closestPoint = eachPoint;
                maxDist = distToP;
            }
        }

        return closestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        int nPoints = 0;

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            System.out.printf("%f %f\n", x, y);
            brute.insert(p);
            nPoints++;
        }

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
        StdDraw.show();

        ArrayList<Point2D> results = (ArrayList<Point2D>) brute.range(new RectHV(0, 0, 1, 1));
        assert results.size() == nPoints;
    }
}
