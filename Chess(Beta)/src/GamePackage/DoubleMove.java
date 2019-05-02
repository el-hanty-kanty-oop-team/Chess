package GamePackage;

public class DoubleMove {
    SingleMove player1_move,player2_move;
    public DoubleMove(SingleMove player1, SingleMove player2) {
        this.player1_move = player1;
        this.player2_move = player2;
    }

    public DoubleMove()
    {
        player1_move = new SingleMove();
        player2_move = new SingleMove();
    }
    public DoubleMove(DoubleMove dm) 
    {
        this.player1_move = new SingleMove(dm.player1_move);
        this.player2_move = new SingleMove(dm.player2_move);
    }
    public boolean equalTo(DoubleMove d){
        return (this.player1_move.equals(d.player1_move)
                &&
                this.player2_move.equals(d.player2_move)
                && 
                this.player1_move.piece.getClass() == d.player2_move.piece.getClass());
    }
}
