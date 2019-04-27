package GamePackage;

public class DoubleMove {
    SingleMove player1_move,player2_move;
    public DoubleMove(SingleMove player1, SingleMove player2) {
        this.player1_move = player1;
        this.player2_move = player2;
    }

    public DoubleMove() {
    }
    public boolean equalTo(DoubleMove d){
        return (this.player1_move.equals(d.player1_move)
                &&
                this.player2_move.equals(d.player2_move)
                && 
                this.player1_move.piece.getClass() == d.player2_move.piece.getClass());
    }
}
