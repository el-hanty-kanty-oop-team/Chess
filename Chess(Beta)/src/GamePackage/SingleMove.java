package GamePackage;
import PiecePackage.*;
public class SingleMove {
    Cell to,from;
    Piece piece;

    public SingleMove(Cell to, Cell from, Piece p) {
        this.to = to;
        this.from = from;
        this.piece = p;
    }
    public SingleMove(Cell to, Cell from) {
        this.to = to;
        this.from = from;
    } 

    public Cell getTo() {
        return to;
    }

    public Cell getFrom() {
        return from;
    }

    
}
