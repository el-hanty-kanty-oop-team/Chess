package GamePackage;
import GamePackage.Cell;
import PiecePackage.*;
public class SingleMove {
    Cell to,from;
    Piece p;

    public SingleMove(Cell to, Cell from, Piece p) {
        this.to = to;
        this.from = from;
        this.p = p;
    }
}
