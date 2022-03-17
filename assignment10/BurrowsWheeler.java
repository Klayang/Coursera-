package assignment10;


public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        // input
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdInputStream.isEmpty()) {
            sb.append(BinaryStdInputStream.readChar());
        }
        int length = sb.length();
        CircularSuffixArray csa = new CircularSuffixArray(sb.toString());
        // find "first" and output
        for (int i = 0; i < length; i++) {
            if (csa.index(i) == 0) {
                // write a 32-bit int
                BinaryStdOutputStream.write(i);
                break;
            }
        }
        // find the string "t"
        for (int i = 0; i < length; i++) {
            // the i-th original suffix string
            int index = csa.index(i);
            // get the index of its last character
            int lastIndex = (length - 1 + index) % length;
            // append these characters, and then we get "t"
            BinaryStdOutputStream.write(sb.charAt(lastIndex));
        }
        BinaryStdOutputStream.close();
    }
    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        // input
        int first = BinaryStdInputStream.readInt();
        StringBuilder t = new StringBuilder();
        while (!BinaryStdInputStream.isEmpty()) {
            t.append(BinaryStdInputStream.readChar());
        }
        int length = t.length();
        // compute frequency counts
        final int EXTENDED_ASCII_INDEX_MAX = 256;
        int[] count = new int[EXTENDED_ASCII_INDEX_MAX + 1];
        //key-index
        for (int i = 0; i < length; i++) {
            count[t.charAt(i) + 1]++;
        }
        // transform counts to indices
        for (int i = 0; i < EXTENDED_ASCII_INDEX_MAX; i++) {
            count[i + 1] += count[i];
        }
        // generate next array
        int[] next = new int[length];
        for (int i = 0; i < length; i++) {
            char c = t.charAt(i);
            next[count[c]] = i;
            count[c]++;
        }
        // output
        for (int i = 0; i < length; i++) {
            BinaryStdOutputStream.write(t.charAt(next[first]));
            first = next[first];
        }
        BinaryStdOutputStream.close();
    }
    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            transform();
        } else {
            inverseTransform();
        }
    }
}