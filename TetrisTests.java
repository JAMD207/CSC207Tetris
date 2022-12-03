import model.TetrisModel;
import model.TetrisPiece;
import model.TetrisBoard;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TetrisTests {

    //Piece tests
    @Test
    void testPiece() {

        TetrisPiece piece = new TetrisPiece("1 0  1 1  1 2  0 1");
        int [] output = piece.getLowestYVals();
        int [] target = {1,0};
        for (int i =0; i< output.length; i++) {
            assertEquals(output[i], target[i], "Error when testing lowest Y values");
        }
        TetrisPiece piece2 = new TetrisPiece("1 0  1 3  2 2  0 1");
        int [] output2 = piece.getLowestYVals();
        int [] target2 = {1,0,2};
        for (int i =0; i< output2.length; i++) {
            assertEquals(output2[i], target2[i], "Error when testing lowest Y values");
        }
        /**
        TetrisPiece piece3 = new TetrisPiece("-3 0  1 0  1 1  1 2  0 1  1 -1  50 -4");
        int [] output3 = piece.getLowestYVals();
        int [] target3 = {0,1,0,-4};
        for (int i =0; i< output3.length; i++) {
            assertEquals(output[i], target3[i], "Error when testing lowest Y values");
        }
         */

        TetrisPiece piece4 = new TetrisPiece("0 2  1 1  1 2  1 0");
        int [] output4 = piece4.getLowestYVals();
        int [] target4 = {2, 0};
        for (int i =0; i< output4.length; i++) {
            assertEquals(output4[i], target4[i], "Error when testing lowest Y values");
        }

    }

    @Test
    void testMakeFastRotations() {
        TetrisPiece piece = new TetrisPiece(TetrisPiece.S2_STR);
        piece = TetrisPiece.makeFastRotations(piece);
        String[] target = {"0 0 0 1 1 1 1 2", "0 1 1 0 1 1 2 0", "0 0 0 1 1 1 1 2", "0 1 1 0 1 1 2 0"};
        int counter = 0;
        TetrisPiece[] o = TetrisPiece.getPieces();
        while(counter < 4){
            TetrisPiece np = new TetrisPiece(target[counter]);
            piece = piece.fastRotation();
            assertTrue(np.equals(piece), "Error when testing piece equality");
            counter++;
        }

    }
    @Test
    void computeNextRotation() {
        TetrisPiece piece = new TetrisPiece("0 1  1 1  1 0  2 0");
        System.out.println(piece.toString());
        TetrisPiece piece2 = piece.computeNextRotation();
    }
    @Test
    void testEquals() {

        TetrisPiece pieceA = new TetrisPiece("1 0  1 1  1 2  0 1");
        TetrisPiece pieceB = new TetrisPiece("1 0  1 1  1 2  0 1");
        assertTrue(pieceB.equals(pieceA), "Error when testing piece equality");
        assertTrue(pieceA.equals(pieceB), "Error when testing piece equality");

        TetrisPiece pieceC = new TetrisPiece("0 0  1 0  2 0  1 1  0 1");
        TetrisPiece pieceD = new TetrisPiece("1 0  0 0  2 0  0 1  1 1");
        TetrisPiece pieceE = new TetrisPiece("1 0  0 0  2 0  0 1");
        assertTrue(pieceD.equals(pieceC), "Error when testing piece equality");
        assertTrue(pieceC.equals(pieceD), "Error when testing piece equality");
        assertFalse(pieceD.equals(pieceE), "Error when testing piece equality");
        assertFalse(pieceE.equals(pieceD), "Error when testing piece equality");

    }

    @Test
    void testPlacePiece() {

        TetrisBoard board = new TetrisBoard(10,24);
        TetrisPiece pieceA = new TetrisPiece(TetrisPiece.SQUARE_STR);

        board.commit();
        TetrisPiece piece4 = new TetrisPiece("0 2  1 1  1 2  1 0");
        int retval = board.placePiece(pieceA, 0,0);
        assertEquals(TetrisBoard.ADD_OK,retval);

        board.commit();
        retval = board.placePiece(pieceA, 12,12); //out of bounds
        assertEquals(TetrisBoard.ADD_OUT_BOUNDS,retval);

        board.commit();
        retval = board.placePiece(pieceA, 0,0);
        assertEquals(TetrisBoard.ADD_BAD, retval);

        //fill the entire row
        retval = board.placePiece(pieceA, 2,0); board.commit();
        retval = board.placePiece(pieceA, 4,0); board.commit();
        retval = board.placePiece(pieceA, 6,0); board.commit();
        retval = board.placePiece(pieceA, 8,0);
        assertEquals(TetrisBoard.ADD_ROW_FILLED, retval);
        for (int i = 0; i < board.getWidth(); i++) {
            assertEquals(board.getGrid(i,0), true);
            assertEquals(board.getGrid(i,1), true);
            assertEquals(board.getGrid(i,2), false);
        }

    }

    @Test
    void testPlacementHeight() {
        TetrisPiece pieceA = new TetrisPiece(TetrisPiece.SQUARE_STR);
        TetrisBoard board = new TetrisBoard(10,24); board.commit();
        int retval = board.placePiece(pieceA, 0,0); board.commit();
        int x = board.placementHeight(pieceA, 0);
        System.out.println(board.toString());
        System.out.println(board.placementHeight(pieceA, 0));
        assertEquals(2,x);
        retval = board.placePiece(pieceA, 0,2); board.commit();
        x = board.placementHeight(pieceA, 0);
        assertEquals(4,x);

    }

    @Test
    void testClearRows() {
        TetrisBoard board = new TetrisBoard(10,24); board.commit();
        TetrisPiece pieceA = new TetrisPiece(TetrisPiece.SQUARE_STR);
        TetrisModel mon = new TetrisModel();
        //fill two rows completely
        int retval = board.placePiece(pieceA, 0,0); board.commit();
        retval = board.placePiece(pieceA, 2,0); board.commit();
        retval = board.placePiece(pieceA, 4,0); board.commit();
        retval = board.placePiece(pieceA, 6,0); board.commit();
        retval = board.placePiece(pieceA, 8,0);
        int rcleared = board.clearRows();
        for(int i =0; i < board.getWidth(); i++){
            //System.out.println(board.getColumnHeight(i));
        }
        for(int i =0; i < board.getHeight(); i++){
            System.out.print("");
            for(int j =0; j < board.getWidth(); j++){

            }
        }
        assertEquals(2, rcleared);
    }


}
