package GamePackage;
import PiecePackage.*;
public class SingleMove {
    Cell to,from;
    Piece p;

    public SingleMove(Cell to, Cell from, Piece p) {
        this.to = to;
        this.from = from;
        this.p = p;
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
