package PiecePackage;
import GamePackage.Board;
import GamePackage.Cell;
import GamePackage.Color;
import java.util.ArrayList;
public class Knight extends Piece{
    public Knight(Cell pos, Color c) {
        super(pos, c);
        last_move_id = 0;
        steps = 0;
    }
    @Override
    public ArrayList<Cell> possible_moves(Cell current, Board b) {
        ArrayList<Cell> list = new ArrayList<>();
        int []dx = {-1,-1,1,1,-2,-2,2,2};
        int []dy = {-2,2,-2,2,-1,1,-1,1};
        Piece[][] board = b.pieces;
        Piece king;
        if (board[current.getRow()][current.getColumn()].color == Color.White) {
            king = b.whiteKing;
        } else {
            king = b.blackKing;
        }
        for(int i = 0; i < 8; i++){
            int nxtx = current.getRow() + dx[i];
            int nxty = current.getColumn() + dy[i];
            
            if(nxtx > -1 && nxtx < 8 && nxty > -1 && nxty < 8){
                boolean ok = this.check_my_king(current, new Cell(nxtx, nxty), king, board);
                if(!ok)
                    continue;
                if(board[nxtx][nxty] != null){
                    if(board[current.getRow()][current.getColumn()].color != board[nxtx][nxty].color)
                        list.add(new Cell(nxtx, nxty));
                }
                else{
                    list.add(new Cell(nxtx, nxty));
                }
            }
        }
        return list;
    }
}
