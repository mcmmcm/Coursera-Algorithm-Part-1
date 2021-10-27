import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // private static final int N_NEARBY_SITES = 4;

    private boolean[][] sites; // False means blocked
    private final int side;
    private int nOpenSites = 0;
    private final int topElementIndex;
    private final int bottomElementIndex;

    private final WeightedQuickUnionUF wquf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0.");
        }

        side = n;
        final int nElements = (int) Math.pow(side, 2) + 2;  // +2 for top and bottom nodes
        topElementIndex = nElements - 2;
        bottomElementIndex = nElements - 1;
        sites = new boolean[n][n];

        wquf = new WeightedQuickUnionUF(nElements);
    }

    // Check argument is within 1 and n inclusive
    private boolean isInputWithinRange(int n) {
        return (n >= 1) && (n <= side);
    }

    // Assert one input is within 1 and side
    private void assertInputIsWithinRange(int n) {
        if (!isInputWithinRange(n)) {
            String errMsg = String.format(
                    "The index %d is not within range %d", n, side);
            throw new IllegalArgumentException(errMsg);
        }
    }

    // Helper function to check both row and col inputs
    private boolean assertRowColIsValid(int row, int col) {
        assertInputIsWithinRange(row);
        assertInputIsWithinRange(col);
        return true;
    }

    // Helper function to convert 2D row, col to 1D index in the WQUF array
    private int convert2dTo1D(int row, int col) {
        /*
        Input (1, 1) => 0
        Input (2, 4) => side + 4 - 1
         */
        assertRowColIsValid(row, col);
        return ((row - 1) * side) + col - 1;
    }

    // Return the nearby elements (up, down, left, right)
    private int[][] getAllNearbySites(int row, int col) {
        int[][] nearbySites = new int[4][2];

        nearbySites[0] = new int[] { row - 1, col }; // up
        nearbySites[1] = new int[] { row + 1, col }; // down
        nearbySites[2] = new int[] { row, col - 1 }; // left
        nearbySites[3] = new int[] { row, col + 1 }; // right

        return nearbySites;
    }

    // union with nearby opened site
    private void unionNearbyOpenedSites(int row, int col) {
        /* Find nearby open sites, then union with the target element specified
        by row and col
         */
        int[][] nearbySites = getAllNearbySites(row, col);

        int targetSite1dIndex = 0;

        for (int i = 0; i < 4; i++) {
            int nearbyRow = nearbySites[i][0];
            int nearbyCol = nearbySites[i][1];

            if (isInputWithinRange(nearbyRow) && isInputWithinRange(nearbyCol)) {
                targetSite1dIndex = convert2dTo1D(row, col);

                if (isOpen(nearbyRow, nearbyCol)) {
                    int nearbyIndex = convert2dTo1D(nearbyRow, nearbyCol);
                    wquf.union(targetSite1dIndex, nearbyIndex);
                }
            }
        }


        // Deal with top and bottom row => union with first and last element
        // Those two elements are always open
        if (row == 1) {
            wquf.union(targetSite1dIndex, topElementIndex);
        }
        else if (row == side) {
            wquf.union(targetSite1dIndex, bottomElementIndex);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        assertRowColIsValid(row, col);
        sites[row - 1][col - 1] = true;
        nOpenSites += 1;

        unionNearbyOpenedSites(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        assertRowColIsValid(row, col);
        return sites[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int target1dIndex = convert2dTo1D(row, col);
        return wquf.find(topElementIndex) == wquf.find(target1dIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nOpenSites;
    }

    // does the system percolate?
    // Check the nodes at the top and connect to the bottom
    public boolean percolates() {
        return wquf.find(topElementIndex) == wquf.find(bottomElementIndex);
    }

    // test client (optional)
    public static void main(String[] args) {
        // Optional. Won't do. Run PercolationVisualizer instead.
    }
}
