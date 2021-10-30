/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private Queue<Board> solution;
    private boolean solvable;
    private int move;

    /**
     * find a solution to the initial board (using the A* algorithm)
     * <p>
     * To check whether a board is solvable, we make a copy of the board with
     * a pair of tiles swapped. Then we perform each step of the A* search on both
     * boards. Only one board will get to the final state so whichever one is
     * solved first, the other one is unsolvable. Then we know whether the original
     * board is solvable.
     */
    //
    public Solver(Board initialBoard) {
        if (initialBoard == null) {
            throw new IllegalArgumentException();
        }
        aStarSearch(initialBoard);
    }

    /**
     * Node class
     */
    private class Node {
        private final Node previous;
        private final Board board;
        private final int move;

        public Node(Board board, Node previous, int move) {
            this.board = board;
            this.previous = previous;
            this.move = move;
        }


        public int getManhattanPriority() {
            return board.manhattan() + move;
        }
    }

    // Comparator to compare boards by there hamming distance
    private static class ByManhatton implements Comparator<Node> {
        public int compare(Node n1, Node n2) {
            return n1.getManhattanPriority() - n2.getManhattanPriority();
        }
    }

    private void aStarSearch(Board initialboard) {
        // Initialise instance variables
        move = -1;
        solution = new Queue<>();
        solvable = false;
        Comparator<Node> BY_MANHATTAN = new ByManhatton();

        // Initialise the min priority queue of the initial board
        Node initialNode = new Node(initialboard, null, 0);
        MinPQ<Node> minPQ = new MinPQ<>(BY_MANHATTAN);
        minPQ.insert(initialNode);

        // Initialise the min priority queue for the swapped board
        Board swappedBoard = initialboard.twin();
        Node initialNodeSwapped = new Node(swappedBoard, null, 0);
        MinPQ<Node> minPQSwapped = new MinPQ<>(BY_MANHATTAN);
        minPQSwapped.insert(initialNodeSwapped);
        boolean isAltBoardSolved = false;

        while (!solvable && !isAltBoardSolved) {
            Node minNode = aStarSearchAddMinNeighbors(minPQ);
            solvable = minNode.board.isGoal();
            Node minNodeSwapped = aStarSearchAddMinNeighbors(minPQSwapped);
            isAltBoardSolved = minNodeSwapped.board.isGoal();

            // Save the Board to the `solution` regardless whether it's solvable
            solution.enqueue(minNode.board);
            move = minNode.move;
        }

        // Reset the related state if the board is unsolvable
        if (!solvable) {
            solution = null;
            move = -1;
        }
    }

    private Node aStarSearchAddMinNeighbors(MinPQ<Node> minPQ) {
        // Remove min from min PQ (priority queue)
        Node minNode = minPQ.delMin();

        // Then add all the neighbors of the removed node back to the PQ.
        // To optimise, we don't want to add the neighbor back that is the
        // same as the previous board.
        for (Board neighbor : minNode.board.neighbors()) {
            try {
                // Throw NullPointerException if the previous board is Null
                // i.e. when the Node represents the initial board
                Board previousBoard = minNode.previous.board;
                if (!neighbor.equals(previousBoard)) {
                    minPQ.insert(new Node(neighbor, minNode, minNode.move + 1));
                }
            }
            catch (NullPointerException e) {
                minPQ.insert(new Node(neighbor, minNode, minNode.move + 1));
            }
        }

        return minNode;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return move;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board testBoard = new Board(tiles);

        Solver solver = new Solver(testBoard);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    }

}
