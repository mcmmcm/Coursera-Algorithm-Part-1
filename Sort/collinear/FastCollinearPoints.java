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

public class FastCollinearPoints {
    private ArrayList<LineSegment> allSegments;

    public FastCollinearPoints(Point[] points) {
        allSegments = new ArrayList<LineSegment>();

        Arrays.sort(points);

        // Go through the originalPoints array and find segments
        for (int i = 0; i < (points.length); i++) {
            Point currOrigin = points[i];    // Alias to current point

            /// Create second array exclude the current origins
            Point[] slopeOrderedPoints = new Point[points.length - 1];
            int pointsIndex = 0;
            for (int a = 0; a < slopeOrderedPoints.length; a++) {
                if (points[pointsIndex].compareTo(currOrigin) == 0) {
                    pointsIndex++;
                }
                slopeOrderedPoints[a] = points[pointsIndex];
                pointsIndex++;
            }
            Arrays.sort(slopeOrderedPoints, currOrigin.slopeOrder());

            /// Find line segment
            int segmentStartIndex = 0;
            int segmentEndIndex = 0;
            // ArrayList<Point> pointsInSameSegment = new ArrayList<Point>();

            // Go through the sorted points and check 4 consecutive line segments
            /**
             * Gradient = [0, 0, 0, 0, 0]   <- an array of straight line
             * Gradient = [-1, -1, 0, 1, 1, 2, 2, 2, 2, 3, 3]
             * Gradient = [-1, -1, 0, 1, 1, 2, 2, 3, 3] <- no line segment
             */
            boolean lessThanOrigin = currOrigin.compareTo(slopeOrderedPoints[0]) > 0;
            for (int j = 1; j < slopeOrderedPoints.length; j++) {
                if (currOrigin.slopeTo(slopeOrderedPoints[j]) ==
                        currOrigin.slopeTo(slopeOrderedPoints[j - 1])) {
                    // pointsInSameSegment.add(points[j]);
                    segmentEndIndex = j;

                    if (currOrigin.compareTo(slopeOrderedPoints[j]) > 0) {
                        lessThanOrigin = true;
                    }
                }
                else {
                    if ((segmentEndIndex - segmentStartIndex) >= 2) {
                        // Any three points + currOrigin form a line segment.
                        // No more points will be added to the line segment because
                        // the points array is sorted by gradient and
                        // if point[j]'s gradient increases from previous,
                        // then any points after will have different gradient
                        break;
                    }
                    else {
                        // reset the cache
                        // pointsInSameSegment = new ArrayList<Point>();
                        lessThanOrigin = currOrigin.compareTo(slopeOrderedPoints[j]) > 0;
                        segmentStartIndex = j;
                    }
                }

            }

            if (((segmentEndIndex - segmentStartIndex) >= 2) && !lessThanOrigin) {
                // Point[] temp = new Point[pointsInSameSegment.size()];
                // pointsInSameSegment.toArray(temp);
                // Arrays.sort(temp);
                // allSegments.add(new LineSegment(temp[0], temp[temp.length - 1]));
                allSegments.add(new LineSegment(currOrigin, slopeOrderedPoints[segmentEndIndex]));
            }

        }
    }


    public int numberOfSegments() {
        return allSegments.size();
    }

    public LineSegment[] segments() {
        return allSegments.toArray(new LineSegment[numberOfSegments()]);
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
