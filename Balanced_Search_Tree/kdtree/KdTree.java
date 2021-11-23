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
        private boolean splitByX;
        private double x, y;
        private Node left, right;
        private int size;

        public Node(double x, double y, int size, boolean splitByX) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.splitByX = splitByX;
        }
    }

    private Node root;
    private int size = 0;
    final private boolean IS_SPLIT_BY_X = true;
    final private boolean IS_SPLIT_BY_Y = false;

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
        root = insertEntry(root, p.x(), p.y(), IS_SPLIT_BY_X);
    }

    private Node insertEntry(Node currentNode, double x, double y, boolean splitByX) {
        // Adding a node to a empty slot
        if (currentNode == null) {
            return new Node(x, y, 1, splitByX);
        }

        if (currentNode.splitByX) {
            if (x < currentNode.x) {
                currentNode.left = insertEntry(currentNode.left, x, y, IS_SPLIT_BY_Y);
            }
            else {
                if ((x == currentNode.x && y == currentNode.y)) {
                    System.out.printf("Node (%f, %f) is already in the tree.\n",
                                      x, y);
                }
                else {
                    currentNode.right = insertEntry(currentNode.right, x, y, IS_SPLIT_BY_Y);
                }
            }
        }
        else {
            // not splitByX, aka split by y
            if (y < currentNode.y) {
                // go to left branch
                currentNode.left = insertEntry(currentNode.left, x, y, IS_SPLIT_BY_X);
            }
            else {
                // go to right branch
                if ((x == currentNode.x && y == currentNode.y)) {
                    System.out.printf("Node (%f, %f) is already in the tree.\n",
                                      x, y);
                }
                else {
                    currentNode.right = insertEntry(currentNode.right, x, y, IS_SPLIT_BY_X);
                }
            }
        }
        return currentNode;
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
