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
        private RectHV nodeRect;

        public Node(Point2D p, int size, boolean splitByX, RectHV nodeRect) {
            this.p = p;
            this.size = size;
            this.splitByX = splitByX;
            this.nodeRect = nodeRect;
        }
    }

    private Node root;
    private final boolean isSplitByX = true;
    private final boolean isSplitByY = false;
    private final double minX = 0.0, maxX = 1.0, minY = 0.0, maxY = 1.0;
    private final RectHV boardRect = new RectHV(minX, minY, maxX, maxY);

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
        if (!contains(p)) {
            root = insert(root, p, isSplitByX, boardRect);
        }
    }

    private Node insert(Node currentNode, Point2D p, boolean splitByX, RectHV nodeRect) {
        // Adding a node to an empty slot
        if (currentNode == null) {
            return new Node(p, 1, splitByX, nodeRect);
        }

        if (currentNode.splitByX) {
            if (p.x() < currentNode.p.x()) {
                RectHV childRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), currentNode.p.x(),
                                              nodeRect.ymax());
                currentNode.left = insert(currentNode.left, p, isSplitByY, childRect);
            }
            else {
                RectHV childRect = new RectHV(currentNode.p.x(), nodeRect.ymin(), nodeRect.xmax(),
                                              nodeRect.ymax());
                currentNode.right = insert(currentNode.right, p, isSplitByY, childRect);
            }
        }
        else {
            // not splitByX, aka split by p.y()
            if (p.y() < currentNode.p.y()) {
                RectHV childRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(),
                                              currentNode.p.y());
                currentNode.left = insert(currentNode.left, p, isSplitByX, childRect);
            }
            else {
                RectHV childRect = new RectHV(nodeRect.xmin(), currentNode.p.y(), nodeRect.xmax(),
                                              nodeRect.ymax());
                currentNode.right = insert(currentNode.right, p, isSplitByX, childRect);
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
        draw(root, minX, maxX, minY, maxY);
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
        if (rect.intersects(node.left.nodeRect)) return;
        if (rect.intersects(node.right.nodeRect)) return;

        if (rect.contains(node.p)) {
            inRangePoints.add(node.p);
        }

        range(rect, node.left, inRangePoints);
        range(rect, node.right, inRangePoints);
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
