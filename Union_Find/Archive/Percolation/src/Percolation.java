import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    boolean[][] sites; // False means blocked
    int side;
    int n_open_sites = 0;
    WeightedQuickUnionUF wquf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0.");
        }

        side = n;
        sites = new boolean[n][n];
        wquf = new WeightedQuickUnionUF(n ^ 2 + 2); // +2 for top and bottom nodes
    }

    // Make sure the input index is within 1 and n
    private boolean check_input_within_range(int n) {
        if ((n < 1) || (n > side)) {
            throw new IllegalArgumentException("The index is not within range");
        }
        return true;
    }

    // Helper function to check both row and col inputs
    private boolean check_both_input_within_range(int row, int col) {
        return (check_input_within_range(row) && check_input_within_range(col));
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        check_both_input_within_range(row, col);
        sites[row][col] = true;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        check_both_input_within_range(row, col);
        return sites[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return !isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return n_open_sites;
    }

    // does the system percolate?
    // Check the nodes at the top and connect to the bottom
    public boolean percolates() {
        p = 0;
        q = 1;
        return wquf.find(p) == wquf.find(q);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
