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

public class KdTree {
    private static class Node {
        boolean splitByX;
        double x;
        double y;
        Node left = null;
        Node right = null;

        public Node(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private Node root = null;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        Node target = new Node(p.x(), p.y());
        insertStep(root, p, true);
    }

    private void insertStep(Node parent, Node point, boolean splitX) {
        // Adding a node to a empty slot
        if (parent == null) {
            parent = point;
            return;
        }

        if (parent.splitByX) {

            if (point.x < parent.x) {
                insertStep(parent.left, point);
            }
            else {
                if ((point.x == parent.x && point.y == parent.y)) {
                    // Same point
                    return;
                }
                else {

                }
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D eachPoint : bst) {
            StdDraw.point(eachPoint.x(), eachPoint.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
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
        double maxDist = -1;
        Point2D closestPoint = null;

        for (Point2D eachPoint : bst) {
            double distToP = eachPoint.distanceTo(p);
            if (distToP > maxDist) {
                closestPoint = eachPoint;
            }
        }

        return closestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree brute = new KdTree();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            System.out.printf("%f %f\n", x, y);
            brute.insert(p);
        }

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
        StdDraw.show();

    }
}
