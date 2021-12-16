/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

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
    final private double MIN_X = 0.0, MAX_X = 1.0, MIN_Y = 0.0, MAX_Y = 1.0;

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
        if (contains(p)) {
            System.out.printf("Node (%f, %f) is already in the tree.\n", p.x(), p.y());
        }
        else {
            root = insert(root, p.x(), p.y(), IS_SPLIT_BY_X);
        }
    }

    private Node insert(Node currentNode, double x, double y, boolean splitByX) {
        // Adding a node to an empty slot
        if (currentNode == null) {
            return new Node(x, y, 1, splitByX);
        }

        if (currentNode.splitByX) {
            if (x < currentNode.x) {
                currentNode.left = insert(currentNode.left, x, y, IS_SPLIT_BY_Y);
            }
            else {
                currentNode.right = insert(currentNode.right, x, y, IS_SPLIT_BY_Y);
            }
        }
        else {
            // not splitByX, aka split by y
            if (y < currentNode.y) {
                // go to left branch
                currentNode.left = insert(currentNode.left, x, y, IS_SPLIT_BY_X);
            }
            else {
                currentNode.right = insert(currentNode.right, x, y, IS_SPLIT_BY_X);
            }
        }
        return currentNode;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        // There's no point matching the queried point
        if (node == null) return false;
        if (node.x == p.x() && node.y == p.y()) return true;

        if (node.splitByX) {
            if (p.x() < node.x) return contains(node.left, p);
            else return contains(node.right, p);
        }
        else {
            // splitByY
            if (p.y() < node.y) return contains(node.left, p);
            else return contains(node.right, p);
        }
    }


    // draw all points to standard draw
    public void draw() {
        draw(root, MIN_X, MAX_X, MIN_Y, MAX_Y);
    }

    private void draw(Node node, double minX, double maxX, double minY, double maxY) {
        if (node != null) {
            // Draw the partition line
            StdDraw.setPenRadius(0.002);
            if (node.splitByX) {
                // we want to draw a vertical partition line
                StdDraw.setPenColor(StdDraw.RED);

                StdDraw.line(node.x, minY, node.x, maxY);

                draw(node.left, minX, node.x, minY, maxY);
                draw(node.right, node.x, maxX, minY, maxY);
            }
            else {
                // splitByY, draw a horizontal partition line
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(minX, node.y, maxX, node.y);

                draw(node.left, minX, maxX, minY, node.y);
                draw(node.right, minX, maxX, node.y, maxY);
            }

            // Draw the point last to overwrite the line
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(node.x, node.y);
        }
        // else, the node is empty so return to previous stack
    }

    // all points that are inside the rectangle (or on the boundary)
    // public Iterable<Point2D> range(RectHV rect) {
    //
    // }

    // // a nearest neighbor in the set to point p; null if the set is empty
    // public Point2D nearest(Point2D p) {
    //     double maxDist = -1;
    //     Point2D closestPoint = null;
    //
    //     for (Point2D eachPoint : bst) {
    //         double distToP = eachPoint.distanceTo(p);
    //         if (distToP > maxDist) {
    //             closestPoint = eachPoint;
    //         }
    //     }
    //
    //     return closestPoint;
    // }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        System.out.println("Running unit test on KdTree... ");
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            System.out.printf("%f %f\n", x, y);
            kdtree.insert(p);
        }

        // draw the points
        kdtree.draw();
        StdDraw.show();

        // contains() test, N.B. requires -ea option
        assert kdtree.contains(new Point2D(0.206107, 0.904508));
        assert !kdtree.contains(new Point2D(0.3, 0.3));

        System.out.println("Running unit test on KdTree... Finish");
    }
}
