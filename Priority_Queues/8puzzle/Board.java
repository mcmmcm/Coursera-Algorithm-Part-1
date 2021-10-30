/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class Board {
    private final int[][] board;
    private final int length;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        assert tiles.length == tiles[0].length;
        length = tiles.length;
        board = copyBoard(tiles);
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(length + "\n");
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return length;
    }

    private int currentPosition(int row, int col) {
        // (0, 0) -> 1, (1, 1) -> length + 1 + 1; Notice the 1 offset.
        return row * length + col + 1;
    }

    private boolean isAtLastTile(int row, int col) {
        return ((row == (length - 1)) && (col == (length - 1)));
    }

    // number of tiles out of place
    public int hamming() {
        int score = 0;
        int currPos;
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                if (isAtLastTile(row, col)) {
                    break;
                }
                else {
                    currPos = currentPosition(row, col);
                    score += ((currPos == board[row][col]) ? 0 : 1);
                }
            }
        }

        return score;
    }

    private int manDistToGoalPos(int tile, int row, int col) {
        assert tile > 0;
        int goalRow = (tile - 1) / length;      // 0-based row
        int goalCol = (tile - 1) % length;      // 0-based col

        return Math.abs(goalRow - row) + Math.abs(goalCol - col);
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int score = 0;

        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                if (board[row][col] != 0) {
                    score += manDistToGoalPos(board[row][col], row, col);
                }
            }
        }

        return score;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (hamming() == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;

        boolean isLengthEqual = that.dimension() == length;
        boolean isAllTilesEqual = false;

        if (isLengthEqual) {
            for (int row = 0; row < length; row++) {
                for (int col = 0; col < length; col++) {
                    isAllTilesEqual = (this.board[row][col] == that.board[row][col]);
                    if (!isAllTilesEqual) return false;
                }
            }
        }

        return isLengthEqual && isAllTilesEqual;
    }

    // Deep copy of `inputBoard`
    private int[][] copyBoard(int[][] inputBoard) {
        assert inputBoard.length == length;

        int[][] newBoard = new int[length][length];
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                newBoard[row][col] = inputBoard[row][col];
            }
        }

        return newBoard;
    }

    // Return a new copy of Board that have tiles swapped
    private Board swapTiles(int currRow, int currCol, int thatRow, int thatCol) {
        // Check is neighbor
        assert Math.abs(currRow - thatRow) <= 1;
        assert Math.abs(currCol - thatCol) <= 1;

        int[][] tempTiles = copyBoard(this.board);

        tempTiles[thatRow][thatCol] = board[currRow][currCol];
        tempTiles[currRow][currCol] = board[thatRow][thatCol];

        return new Board(tempTiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighborBoards = new ArrayList<Board>();

        // Find the location of the blank square i.e. tiles[row][col] == 0
        // Note to self: Could make a convenient function to return the neighbor
        // tiles.
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                if (board[row][col] == 0) {
                    // Move blank to the 4 directions

                    // Blank moves up
                    if ((row - 1) >= 0) {
                        Board tempBoard = swapTiles(row, col, row - 1, col);
                        neighborBoards.add(tempBoard);
                    }

                    // Blank moves down
                    if ((row + 1) < length) {
                        Board tempBoard = swapTiles(row, col, row + 1, col);
                        neighborBoards.add(tempBoard);
                    }

                    // Blank moves left
                    if ((col - 1) >= 0) {
                        Board tempBoard = swapTiles(row, col, row, col - 1);
                        neighborBoards.add(tempBoard);
                    }

                    // Blank moves right
                    if ((col + 1) < length) {
                        Board tempBoard = swapTiles(row, col, row, col + 1);
                        neighborBoards.add(tempBoard);
                    }
                    assert ((neighborBoards.size() >= 2) && (neighborBoards.size() <= 4));
                    return neighborBoards;
                }
            }
        }

        throw new RuntimeException("No blank tile found. Board is not valid.");
    }


    // a board that is obtained by exchanging any pair of tiles
    // swap the first two non-zero tiles
    public Board twin() {
        for (int row1 = 0; row1 < length; row1++) {
            for (int col1 = 0; col1 < length; col1++) {
                if (board[row1][col1] != 0) {

                    for (int row2 = row1; row2 < length; row2++) {
                        int col2 = col1 + 1;
                        if (row2 != row1) {
                            col2 = 0;
                        }

                        for (; col2 < length; col2++) {
                            if (board[row2][col2] != 0) {
                                return swapTiles(row1, col1, row2, col2);
                            }
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Cannot swap board");
    }

    // Unit testing
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board testBoard = new Board(tiles);

        System.out.println(testBoard.toString());
        System.out.printf("Hamming distance: %d\n", testBoard.hamming());
        System.out.printf("Manhattan distance: %d\n", testBoard.manhattan());

        // Run it and expect NO exception
        testBoard.neighbors();

        // Print out twin board
        System.out.println(testBoard.twin().toString());

        // Change the tiles and check equals() report False
        tiles[n - 1][n - 1] = 3;
        Board that = new Board(tiles);
        assert !testBoard.equals((that)) : "equals() is broken";
    }
}
