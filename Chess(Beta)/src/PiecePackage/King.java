package PiecePackage;

import GamePackage.*;
import java.util.ArrayList;

public class King extends Piece {
    public King(Cell pos, Color c) {
        super(pos, c);
    }
    @Override
    public ArrayList<Cell> possible_moves(Cell current, Board b) {
        ArrayList<Cell> list = new ArrayList<>();
        Piece[][] board = b.pieces;
        Piece king = board[current.getRow()][current.getColumn()];
        int dx[] = {1, 1, 1, 0, 0, -1, -1, -1};
        int dy[] = {-1, 1, 0, -1, 1, -1, 0, 1};
        int x = king.pos.getRow(), y = king.pos.getColumn();
        // checking king normal moves
        for (int i = 0; i < 8; i++) {
            if (check_valid_borders(x + dx[i], y + dy[i])) {
                king.pos.setRow(x + dx[i]);
                king.pos.setColumn(y + dy[i]);
                boolean ok = check_my_king(current, new Cell(x + dx[i], y + dy[i]), king, board);
                king.pos.setRow(x);
                king.pos.setColumn(y);
                if(ok){
                    list.add(new Cell(x + dx[i], y + dy[i]));
                }
            }
        }
        // checking castling
        int row = 7;
        if(king.color == Color.White)
            row = 0;
        if(king.steps == 0){
            //checking right castling
            if(board[row][7] instanceof Rook && board[row][7].steps == 0 && board[row][5] == null && board[row][6] == null){
                // checking cells (row,4) , (row,5), (row,6) is not in danger
                boolean ok = true;
                king.pos.setRow(row);
                for(int i = 4;  i < 7; i++){
                    king.pos.setColumn(i);
                    ok &= check_my_king(current, new Cell(row, i), king, board);
                }
                if(ok){
                    list.add(new Cell(row, y + 2, 1));
                }
                king.pos.setRow(x);
                king.pos.setColumn(y);
            }
            // checking left castling
            if(board[row][0] instanceof Rook && board[row][0].steps == 0 && board[row][1] == null && board[row][2] == null && board[row][3] == null){
                // checking cells (row,2) , (row,3),(row, 4) is not in danger
                boolean ok = true;
                king.pos.setRow(row);
                for(int i = 2;  i < 5; i++){
                    king.pos.setColumn(i);
                    ok &= check_my_king(current, new Cell(row, i), king, board);
                }
                if(ok){
                    list.add(new Cell(row, y - 2, 1));
                }
                king.pos.setRow(x);
                king.pos.setColumn(y);
            }
        }
        return list;
    }
}
