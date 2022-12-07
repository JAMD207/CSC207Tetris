package model;

import java.io.Serializable;
import java.util.*;

/** An immutable representation of a tetris piece in a particular rotation.
 *  Each piece is defined by the blocks that make up its body.
 *
 * Based on the Tetris assignment in the Nifty Assignments Database, authored by Nick Parlante
 */
public class TetrisPiece implements Serializable {

    /*
     Implementation notes:
     -The starter code does a few simple things
     -It stores the piece body as a TetrisPoint[] array
     -The attributes in the TetrisPoint class are x and y coordinates
     -Don't assume there are 4 points in the piece body; there might be less or more!
    */
    private TetrisPoint[] body; // y and x values that make up the body of the piece.
    private int[] lowestYVals; //The lowestYVals array contains the lowest y value for each x in the body.
    private int[] highestYvals;
    private int width;
    private int height;
    private TetrisPiece next; // We'll use this to link each piece to its "next" rotation.
    static private TetrisPiece[] pieces;	// array of rotations for this piece


    // String constants for the standard 7 tetris pieces
    public static final String STICK_STR	= "0 0	0 1	 0 2  0 3";
    public static final String L1_STR		= "0 0	0 1	 0 2  1 0";
    public static final String L2_STR		= "0 0	1 0 1 1	 1 2";
    public static final String S1_STR		= "0 0	1 0	 1 1  2 1";
    public static final String S2_STR		= "0 1	1 1  1 0  2 0";
    public static final String SQUARE_STR	= "0 0  0 1  1 0  1 1";
    public static final String PYRAMID_STR	= "0 0  1 0  1 1  2 0";

    /**
     * Defines a new piece given a TetrisPoint[] array that makes up its body.
     * Makes a copy of the input array and the TetrisPoint inside it.
     * Note that this constructor should define the piece's "lowestYVals". For each x value
     * in the body of a piece, the lowestYVals will contain the lowest y value for that x in the body.
     * This will become useful when computing where the piece will land on the board!!
     */
    public TetrisPiece(TetrisPoint[] points) {
        body = points;
        int asize = body.length;
        int count = 0;
        int hval = 0;
        int yv;
        int wval = 0;
        ArrayList<Integer> xval = new ArrayList<>();
        while (count < asize) {
            if (!(xval.contains(points[count].x))) {
                xval.add(points[count].x);
            }
            if (points[count].x > wval) {
                wval = points[count].x;
            }
            if (points[count].y > hval) {
                hval = points[count].y;
            }
            count = count + 1;
        }
        height = hval + 1;
        width = wval + 1;
        Collections.sort(xval);
        int[] yval = new int[xval.size()];
        int count2 = xval.size();
        while (count2 > 0){
            count = asize;
            yv = -1;
            while (count > 0) {
                if (points[count-1].x == xval.get(count2 - 1)) {
                    if (yv == - 1 || points[count-1].y < yv) {
                        yv = points[count-1].y;
                    }
                }
                count = count - 1;
            }
            yval[count2 - 1] = yv;
            count2 = count2 - 1;
        }
        lowestYVals = yval;
        int[] yval1 = new int[xval.size()];
        int count3 = 0;
        while (count3 < xval.size()){
            count = 0;
            yv = -1;
            while (count < points.length) {
                if (points[count].x == xval.get(count3)) {
                    if (points[count].y > yv) {
                        yv = points[count].y;
                    }
                }
                count = count + 1;
            }
            yval1[count3] = yv;
            count3 = count3 + 1;
        }
        highestYvals = yval1;

    }

    /**
     * Alternate constructor for a piece, takes a String with the x,y body points
     * all separated by spaces, such as "0 0  1 0  2 0  1 1".
     */
    public TetrisPiece(String points) {
        this(parsePoints(points));
    }
    public int[] getHighestYvals() {return highestYvals;}

    /**
     * Returns the width of the piece measured in blocks.
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the piece measured in blocks.
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a pointer to the piece's body. The caller
     * should not modify this array.
     *
     * @return point array that defines piece body
     */
    public TetrisPoint[] getBody() {
        return body;
    }

    /**
     * Returns a pointer to the piece's lowest Y values. For each x value
     * across the piece, this gives the lowest y value in the body.
     * This is useful for computing where the piece will land (if dropped).
     * The caller should not modify the values that are returned
     *
     * @return array of integers that define the lowest Y value for every X value of the piece.
     */
    public int[] getLowestYVals() {
        return lowestYVals;
    }

    /**
     * Returns true if two pieces are the same --
     * their bodies contain the same points.
     * Interestingly, this is not the same as having exactly the
     * same body arrays, since the points may not be
     * in the same order in the bodies. Used internally to detect
     * if two rotations are effectively the same.
     *
     * @param obj the object to compare to this
     *
     * @return true if objects are the same
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TetrisPiece)) {
            return false;
        }
        TetrisPoint[] nbody;
        nbody = ((TetrisPiece) obj).getBody();
        int nsize = nbody.length;
        if (!(nsize == body.length)) {
            return false;
        }
        //ArrayList<TetrisPoint> fpoints = new ArrayList<>();
        ArrayList<TetrisPoint> blist = new ArrayList<>(Arrays.asList(body));
        int count = 0;
        while (count < nsize) {
            if (!(blist.contains(nbody[count]))) {
                return false;
            }
            count = count + 1;
        }
        return true;
    }

    /**
     * This is a static method that will return all rotations of
     * each of the 7 standard tetris pieces:
     * STICK, L1, L2, S1, S2, SQUARE, PYRAMID.
     * The next (counterclockwise) rotation can be obtained
     * from each piece with the fastRotation() method.
     * This method will be called by the model to facilitate
     * selection of random pieces to add to the board.
     * The pieces can be easily rotated because the rotations
     * have been precomputed.
     *
     * @return a list of all the rotations for all the given pieces.
     */
    public static TetrisPiece[] getPieces() {
        // lazy evaluation -- create static array only if needed
        if (TetrisPiece.pieces==null) {
            // use makeFastRotations() to compute all the rotations for each piece
            try {
                TetrisPiece.pieces = new TetrisPiece[]{
                        makeFastRotations(new TetrisPiece(STICK_STR)),
                        makeFastRotations(new TetrisPiece(L1_STR)),
                        makeFastRotations(new TetrisPiece(L2_STR)),
                        makeFastRotations(new TetrisPiece(S1_STR)),
                        makeFastRotations(new TetrisPiece(S2_STR)),
                        makeFastRotations(new TetrisPiece(SQUARE_STR)),
                        makeFastRotations(new TetrisPiece(PYRAMID_STR)),
                };
            } catch (UnsupportedOperationException e) {
                System.out.println("You must implement makeFastRotations!");
                System.exit(1);
            }
        }
        return TetrisPiece.pieces;
    }

    /**
     * Returns a pre-computed piece that is 90 degrees counter-clockwise
     * rotated from the receiver. Fast because the piece is pre-computed.
     * This only works on pieces set up by makeFastRotations(), and otherwise
     * just returns null.
     *
     * @return the next rotation of the given piece
     */
    public TetrisPiece fastRotation() {
        return next;
    }

    public TetrisPiece _fastRotationHelper(TetrisPiece root) {
        int[][] pmatrix = new int[root.getHeight()][root.getWidth()];
        TetrisPoint[] pointlist = root.getBody();
        int bsize = pointlist.length;
        int count = 0;
        int px;
        int py;
        while (count < bsize) {
            px = pointlist[count].x;
            py = pointlist[count].y;
            pmatrix[py][px] = 1;
            count = count + 1;
        }
        int[][] rmatrix = new int[root.getWidth()][root.getHeight()];
        int xcount = 0;
        int ycount = 0;
        while (xcount < root.getHeight()) {
            ycount = 0;
            while (ycount < root.getWidth()) {
                rmatrix[ycount][xcount] = pmatrix[root.getHeight()-1-xcount][ycount];
                ycount = ycount + 1;
            }
            xcount = xcount + 1;
        }
        xcount = 0;
        ycount = 0;
        ArrayList<TetrisPoint> newpoints = new ArrayList<>();
        while (xcount < root.getWidth()) {
            ycount = 0;
            while (ycount < root.getHeight()) {
                if (rmatrix[xcount][ycount] == 1) {
                    newpoints.add(new TetrisPoint(ycount, xcount));
                }
                ycount = ycount + 1;
            }
            xcount = xcount + 1;
        }
        TetrisPoint[] npoints = new TetrisPoint[newpoints.size()];
        npoints = newpoints.toArray(npoints);
        return new TetrisPiece(npoints);
    }
    /**
     * Given the "first" root rotation of a piece, computes all
     * the other rotations and links them all together
     * in a circular list. The list should loop back to the root as soon
     * as possible.
     * Return the root piece. makeFastRotations() will need to relies on the
     * pointer structure setup here. The suggested implementation strategy
     * is to use the subroutine computeNextRotation() to generate 90 degree rotations.
     * Use this to generate a sequence of rotations and link them together.
     * Continue generating rotations until you generate the piece you started with.
     * Use Piece.equals() to detect when the rotations
     * have gotten you back to the first piece.
     *
     * @param root the default rotation for a piece
     *
     * @return a piece that is a linked list containing all rotations for the piece
     */
    public static TetrisPiece makeFastRotations(TetrisPiece root) {
        boolean foundloop = true;
        TetrisPiece[] rotations = new TetrisPiece[4];
        rotations[0] = root;
        int count = 0;
        TetrisPiece npiece;
        npiece = root;
        while (foundloop) {
            npiece = npiece._fastRotationHelper(npiece);
            if (root.equals(npiece)) {
                foundloop = false;
                continue;
            }
            count = count + 1;
            rotations[count] = npiece;
        }
        int count2 = 0;
        while (count2 < count) {
            rotations[count2].next = rotations[count2 + 1];
            count2 = count2 + 1;
        }
        rotations[count2].next = rotations[0];
        return rotations[0];
    }

    /**
     * Returns a new piece that is 90 degrees counter-clockwise
     * rotated from the receiver.
     *
     * @return the next rotation of the given piece
     */
    public TetrisPiece computeNextRotation() {
        throw new UnsupportedOperationException(); //replace this!
    }
    /**
     * Print the points within the piece
     *
     * @return a string representation of the piece
     */
    public String toString() {
        String str = "";
        for (int i = 0; i < body.length; i++) {
            str += body[i].toString();
        }
        return str;
    }

    /**
     * Given a string of x,y pairs (e.g. "0 0 0 1 0 2 1 0"), parses
     * the points into a TPoint[] array.
     *
     * @param string input of x,y pairs
     *
     * @return an array of parsed points
     */
    private static TetrisPoint[] parsePoints(String string) {
        List<TetrisPoint> points = new ArrayList<TetrisPoint>();
        StringTokenizer tok = new StringTokenizer(string);
        try {
            while(tok.hasMoreTokens()) {
                int x = Integer.parseInt(tok.nextToken());
                int y = Integer.parseInt(tok.nextToken());

                points.add(new TetrisPoint(x, y));
            }
        }
        catch (NumberFormatException e) {
            throw new RuntimeException("Could not parse x,y string:" + string);
        }
        // Make an array out of the collection
        TetrisPoint[] array = points.toArray(new TetrisPoint[0]);
        return array;
    }
}
