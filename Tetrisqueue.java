package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Tetrisqueue {
    public TetrisModel model;
    protected Random random;
    private ArrayList<TetrisPiece> queue;
    private Iterator<TetrisPiece> tetrisPieceIterator = queue.iterator();
    private Iterator<TetrisPiece> last = queue.iterator();
    public Tetrisqueue(){
        this.queue = new ArrayList<>();
        int pieceNum;
        pieceNum = (int) (5 + random.nextDouble());
        TetrisPiece piece = model.pieces[pieceNum];
        queue.add(piece);
        queue.add(piece.fastRotation());
        TetrisPiece piece1 = tetrisPieceIterator.next();
    }

    public TetrisPiece getnext(){
        TetrisPiece piece = tetrisPieceIterator.next();
        queue.add(model.pieces[(int) (queue.size() + random.nextDouble())]);
        return piece;
    }

    public TetrisPiece shownext(){
        return last.next();
    }

    public Iterator<TetrisPiece> getTetrisPieceIterator(){
        return this.last;
    }
}
