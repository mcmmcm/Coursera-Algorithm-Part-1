/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private int[] results;
    private final int side;
    private final int nTotalSites;
    private final int nTrials;
    private Percolation percolationModel;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        side = n;
        nTotalSites = (int) Math.pow(n, 2);
        nTrials = trials;
        results = new int[trials];

        runMonteCarloSimulation();
    }

    private void runMonteCarloSimulation() {
        for (int t = 0; t < nTrials; t++) {
            percolationModel = new Percolation(side);
            results[t] = runMonteCarloStep();
        }
    }

    private int runMonteCarloStep() {
        int nOpenSites = 0;

        // Open sites until percolates
        while (!percolationModel.percolates()) {
            int[] coord = randomPickClosedSite();
            percolationModel.open(coord[0], coord[1]);
            nOpenSites += 1;
        }

        return nOpenSites;
    }

    private int[] randomPickClosedSite() {
        int[] coord = { 0, 0 };

        boolean isClosedSite = false;
        while (!isClosedSite) {
            int randomIndex = StdRandom.uniform(0, nTotalSites); // [0, n)

            int row = randomIndex / side + 1;
            int col = randomIndex % side + 1;

            coord = new int[] { row, col };
            isClosedSite = !percolationModel.isOpen(row, col);
        }

        // Convert index to row, col
        return coord;
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results) / nTotalSites;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results) / nTotalSites;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean() - CONFIDENCE_95 * stddev() / Math.sqrt(nTrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + CONFIDENCE_95 * stddev() / Math.sqrt(nTrials));
    }

    // test client (see below)
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats percolationStatModel = new PercolationStats(n, t);

        System.out.printf("Mean = %f\n", percolationStatModel.mean());
        System.out.printf("stddev = %f\n", percolationStatModel.stddev());
        System.out.printf("Confidence Level  High = %f, Low = %f \n",
                          percolationStatModel.confidenceHi(),
                          percolationStatModel.confidenceLo());
    }
}
