/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> allSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        allSegments = new ArrayList<LineSegment>();

        if (points == null) {
            throw new IllegalArgumentException("Argument to BruteCollinearPoints is null");
        }

        int nPoints = points.length;
        Arrays.sort(points);
        for (int i = 1; i < nPoints; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                throw new IllegalArgumentException("Repeated points found in arguments");
            }
        }

        for (int i = 0; i < nPoints; i++) {
            for (int j = (i + 1); j < nPoints; j++) {
                for (int k = (j + 1); k < nPoints; k++) {
                    for (int n = (k + 1); n < nPoints; n++) {
                        double temp1 = points[i].slopeTo(points[j]);
                        double temp2 = points[i].slopeTo(points[k]);
                        double temp3 = points[i].slopeTo(points[n]);

                        if ((temp1 == temp2) && (temp1 == temp3)) {
                            allSegments.add(new LineSegment(points[i], points[n]));
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return allSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return allSegments.toArray(new LineSegment[allSegments.size()]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
