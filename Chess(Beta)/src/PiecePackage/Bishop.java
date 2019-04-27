package PiecePackage;

import GamePackage.*;
import java.util.*;

public class Bishop extends Piece {

    public Bishop(Cell pos, Color c) {
        super(pos, c);
        last_move_id = 0;
        steps = 0;
    }

    @Override
    public ArrayList<Cell> possible_moves(Cell current, Board b) {
        ArrayList<Cell> list = new ArrayList<>();
        Piece[][] board = b.pieces;
        Piece king;
        if (board[current.getRow()][current.getColumn()].color == Color.White) {
            king = b.whiteKing;
        } else {
            king = b.blackKing;
        }

        //Checking topright moves
        for (int row = current.getRow() - 1, column = current.getColumn() + 1; row >= 0 && column < 8; row--, column++) {
            // chcek if the king is in danger
            boolean ok = this.check_my_king(current, new Cell(row, column), king, board);
            if (board[row][column] != null) {
                if (board[row][column].color != board[current.getRow()][current.getColumn()].color) {
                    if (ok) {
                        list.add(new Cell(row, column));
                    }
                }
                break;
            } else {
                if (ok) {
                    list.add(new Cell(row, column));
                }
            }
        }

        //Checking the top left
        for (int row = current.getRow() - 1, column = current.getColumn() - 1; row >= 0 && column >= 0; row--, column--) {
            // chcek if the king is in danger
            boolean ok = this.check_my_king(current, new Cell(row, column), king, board);
            if (board[row][column] != null) {
                if (board[row][column].color != board[current.getRow()][current.getColumn()].color) {
                    if (ok) {
                        list.add(new Cell(row, column));
                    }
                }
                break;
            } else {
                if (ok) {
                    list.add(new Cell(row, column));
                }
            }
        }
        //Checking down Right
        for (int row = current.getRow() + 1, column = current.getColumn() + 1; row < 8 && column < 8; row++, column++) {
            // chcek if the king is in danger
            boolean ok = this.check_my_king(current, new Cell(row, column), king, board);
            if (board[row][column] != null) {
                if (board[row][column].color != board[current.getRow()][current.getColumn()].color) {
                    if (ok) {
                        list.add(new Cell(row, column));
                    }
                }
                break;
            } else {
                if (ok) {
                    list.add(new Cell(row, column));
                }
            }
        }
        //Check the colimn to up
        for (int row = current.getRow() + 1, column = current.getColumn() - 1; row < 8 && column >= 0; row++, column--) {
            // chcek if the king is in danger
            boolean ok = this.check_my_king(current, new Cell(row, column), king, board);
            if (board[row][column] != null) {
                if (board[row][column].color != board[current.getRow()][current.getColumn()].color) {
                    if (ok) {
                        list.add(new Cell(row, column));
                    }
                }
                break;
            } else {
                if (ok) {
                    list.add(new Cell(row, column));
                }
            }
        }
        return list;
    }
}
