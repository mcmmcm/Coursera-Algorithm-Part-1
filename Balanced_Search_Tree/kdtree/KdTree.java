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
        private Point2D p;
        private Node left, right;
        private int size;

        public Node(Point2D p, int size, boolean splitByX) {
            this.p = p;
            this.size = size;
            this.splitByX = splitByX;
        }
    }

    private Node root;
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
        return root.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (contains(p)) {
            System.out.printf("Node (%f, %f) is already in the tree.\n", p.x(), p.y());
        }
        else {
            root = insert(root, p, IS_SPLIT_BY_X);
        }
    }

    private Node insert(Node currentNode, Point2D p, boolean splitByX) {
        // Adding a node to an empty slot
        if (currentNode == null) {
            return new Node(p, 1, splitByX);
        }

        if (currentNode.splitByX) {
            if (p.x() < currentNode.p.x()) {
                currentNode.left = insert(currentNode.left, p, IS_SPLIT_BY_Y);
            }
            else {
                currentNode.right = insert(currentNode.right, p, IS_SPLIT_BY_Y);
            }
        }
        else {
            // not splitByX, aka split by p.y()
            if (p.y() < currentNode.p.y()) {
                // go to left branch
                currentNode.left = insert(currentNode.left, p, IS_SPLIT_BY_X);
            }
            else {
                currentNode.right = insert(currentNode.right, p, IS_SPLIT_BY_X);
            }
        }
        
        currentNode.size++;
        return currentNode;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        // There's no point matching the queried point
        if (node == null) return false;

        if (node.p.x() == p.x() && node.p.y() == p.y()) return true;

        if (node.splitByX) {
            if (p.x() < node.p.x()) return contains(node.left, p);
            else return contains(node.right, p);
        }
        else {
            // splitByY
            if (p.y() < node.p.y()) return contains(node.left, p);
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

                StdDraw.line(node.p.x(), minY, node.p.x(), maxY);

                draw(node.left, minX, node.p.x(), minY, maxY);
                draw(node.right, node.p.x(), maxX, minY, maxY);
            }
            else {
                // splitByY, draw a horizontal partition line
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(minX, node.p.y(), maxX, node.p.y());

                draw(node.left, minX, maxX, minY, node.p.y());
                draw(node.right, minX, maxX, node.p.y(), maxY);
            }

            // Draw the point last to overwrite the line
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(node.p.x(), node.p.y());
        }
        // else, the node is empty so return to previous stack
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> inRangePoints = new ArrayList<>();

        // check if the rectangle contains the node
        range(rect, root, inRangePoints);
        return inRangePoints;
    }

    private void range(RectHV rect, Node node, ArrayList<Point2D> inRangePoints) {
        if (node == null) return;

        if (rect.contains(node.p)) {
            inRangePoints.add(node.p);
        }

        if (node.splitByX) {
            if (rect.xmax() < node.p.x()) {
                range(rect, node.left, inRangePoints);
            }
            else if (rect.xmin() < node.p.x() && rect.xmax() >= node.p.x()) {
                range(rect, node.left, inRangePoints);
                range(rect, node.right, inRangePoints);
            }
            else if (rect.xmin() >= node.p.x()) {
                range(rect, node.right, inRangePoints);
            }
        }
        else {
            // split by Y
            if (rect.ymax() < node.p.y()) {
                range(rect, node.left, inRangePoints);
            }
            else if (rect.ymin() < node.p.y() && rect.ymax() >= node.p.y()) {
                range(rect, node.left, inRangePoints);
                range(rect, node.right, inRangePoints);
            }
            else if (rect.ymin() >= node.p.y()) {
                range(rect, node.right, inRangePoints);
            }
        }
    }

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
