/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]); // Number of strings to print
        RandomizedQueue<String> model = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String inputString = StdIn.readString();
            model.enqueue(inputString);
        }
        
        // Print k outpus
        for (int i = 0; i < k; i++) {
            StdOut.println(model.dequeue());
        }

    }
}
