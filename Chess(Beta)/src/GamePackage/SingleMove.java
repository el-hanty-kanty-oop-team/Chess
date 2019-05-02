package GamePackage;
import PiecePackage.*;
public class SingleMove {
    Cell to,from;
    Piece piece;

    public SingleMove(Cell to, Cell from, Piece p) {
        this.to = new Cell(to);
        this.from = new Cell(from);
        this.piece = p.clone();
    }
    public SingleMove(Cell to, Cell from) {
        this.to = new Cell(to);
        this.from = new Cell(from);
    } 

    public SingleMove() {
        to = new Cell(0,0);
        from = new Cell(0,0);
        
    }
    
    public SingleMove(SingleMove sm)
    {
        if(sm == null)
            System.out.println("NUll singlmove");
        else if(sm.to == null)
            System.out.println("to is null");
        this.to = new Cell(sm.to);
        this.from = new Cell(sm.from);
        this.piece = sm.piece.clone();
    }

    public boolean equalTo(SingleMove sm)
    {
        return this.to.isEqual(sm.to) && this.from.isEqual(sm.from) && this.piece.getClass() == sm.piece.getClass();
    }
    public Cell getTo() {
        return to;
    }

    public Cell getFrom() {
        return from;
    }

    
}
