package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Tetrisqueuetest {

    Tetrisqueue tetrisqueue = new Tetrisqueue();
    TetrisModel model;
    void test_get_next(){
        assertEquals(tetrisqueue.getnext(), model.newPiece);
    }

    void test_show_next(){
        assertEquals(tetrisqueue.shownext(), tetrisqueue.getTetrisPieceIterator().next());
    }

}
