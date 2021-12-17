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
        private final boolean splitByX;
        private final Point2D p;
        private final RectHV rect;
        private Node left, right;
        private int size;

        public Node(Point2D p, int size, boolean splitByX, RectHV rect) {
            this.p = p;
            this.size = size;
            this.splitByX = splitByX;
            this.rect = rect;
        }
    }

    private static final boolean IS_SPLIT_BY_X = true;
    private static final boolean IS_SPLIT_BY_Y = false;
    private static final double MIN_X = 0.0, MAX_X = 1.0, MIN_Y = 0.0, MAX_Y = 1.0;
    private static final RectHV BOARD_RECT = new RectHV(MIN_X, MIN_Y, MAX_X, MAX_Y);
    private Node root;
    private Point2D closestPoint = new Point2D(2, 2);   // To be used in nearest() only

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        if (root == null) return 0;
        else return root.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (!contains(p)) {
            root = insert(root, p, IS_SPLIT_BY_X, BOARD_RECT);
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
                currentNode.left = insert(currentNode.left, p, IS_SPLIT_BY_Y, childRect);
            }
            else {
                RectHV childRect = new RectHV(currentNode.p.x(), nodeRect.ymin(), nodeRect.xmax(),
                                              nodeRect.ymax());
                currentNode.right = insert(currentNode.right, p, IS_SPLIT_BY_Y, childRect);
            }
        }
        else {
            // not splitByX, aka split by p.y()
            if (p.y() < currentNode.p.y()) {
                RectHV childRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(),
                                              currentNode.p.y());
                currentNode.left = insert(currentNode.left, p, IS_SPLIT_BY_X, childRect);
            }
            else {
                RectHV childRect = new RectHV(nodeRect.xmin(), currentNode.p.y(), nodeRect.xmax(),
                                              nodeRect.ymax());
                currentNode.right = insert(currentNode.right, p, IS_SPLIT_BY_X, childRect);
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

    private void draw(Node node, double rectXMin, double rectXMax, double rectYMin,
                      double rectYMax) {
        if (node != null) {
            // Draw the partition line
            if (node.splitByX) {
                // we want to draw a vertical partition line
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), rectYMin, node.p.x(), rectYMax);

                draw(node.left, rectXMin, node.p.x(), rectYMin, rectYMax);
                draw(node.right, node.p.x(), rectXMax, rectYMin, rectYMax);
            }
            else {
                // splitByY, draw a horizontal partition line
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rectXMin, node.p.y(), rectXMax, node.p.y());

                draw(node.left, rectXMin, rectXMax, rectYMin, node.p.y());
                draw(node.right, rectXMin, rectXMax, node.p.y(), rectYMax);
            }

            // Draw the point last to overwrite the line
            StdDraw.setPenColor(StdDraw.BLACK);
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
        if (!rect.intersects(node.rect)) return;

        if (rect.contains(node.p)) {
            inRangePoints.add(node.p);
        }

        range(rect, node.left, inRangePoints);
        range(rect, node.right, inRangePoints);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return nearest(root, p);
    }


    private Point2D nearest(Node node, Point2D p) {
        if (node == null) return null;

        double distToNode = p.distanceSquaredTo(node.p);
        double distToClosest = p.distanceSquaredTo(closestPoint);

        if (distToNode < distToClosest) {
            closestPoint = node.p;
        }

        if (node.splitByX) {
            if (p.x() < node.p.x()) {
                nearest(node.left, p);
                if (node.right != null && closestPoint.distanceSquaredTo(p) > node.right.rect
                        .distanceSquaredTo(p)) {
                    nearest(node.right, p);
                }
            }
            else {
                nearest(node.right, p);
                if (node.left != null && closestPoint.distanceSquaredTo(p) > node.left.rect
                        .distanceSquaredTo(p)) {
                    nearest(node.left, p);
                }
            }
        }
        else {
            if (p.y() < node.p.y()) {
                nearest(node.left, p);
                if (node.right != null && closestPoint.distanceSquaredTo(p) > node.right.rect
                        .distanceSquaredTo(p)) {
                    nearest(node.right, p);
                }
            }
            else {
                nearest(node.right, p);
                if (node.left != null && closestPoint.distanceSquaredTo(p) > node.left.rect
                        .distanceSquaredTo(p)) {
                    nearest(node.left, p);
                }
            }
        }

        return closestPoint;
    }

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
            System.out.printf("Size %d\n", kdtree.size());
            System.out.printf("Is empty? %b\n", kdtree.isEmpty());
        }

        // draw the points
        kdtree.draw();
        StdDraw.show();

        // Unit Test: Range search, make a txt file with the following points
        // 1.0 0.625
        // 0.25 0.875
        // 0.125 0.375
        // 0.375 1.0
        // 0.5 0.125
        // Then query rectangle = x[0.0, 0.625],  y[0.25, 0.5]
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.point(0.125, 0.375);
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.point(0.0, 0.25);
        StdDraw.point(0.625, 0.5);
        StdDraw.show();
        Iterable<Point2D> temp = kdtree.range(new RectHV(0.0, 0.25, 0.626, 0.5));

        System.out.println("Running unit test on KdTree... Finish");
    }
}
