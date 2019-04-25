package PiecePackage;


import GamePackage.Board;
import GamePackage.Cell;
import GamePackage.Color;
import java.util.*;

public class Rook extends Piece {

    public Rook(Cell pos, Color c) {
        super(pos, c);
    }

    
    
    @Override
   public ArrayList<Cell> possible_moves(Cell current, Board b) {
        ArrayList<Cell> list = new ArrayList<>();
        Piece [][] board = b.pieces;
        Piece king = null;
        if(board[current.getRow()][current.getColumn()].color == Color.White)
            king = b.whiteKing;
        else
            king = b.blackKing;
        //Checking left direction
            for (int column = current.getColumn() - 1; column >= 0; column--) {
                // chcek if the king is in danger
                boolean ok = this.check_my_king(current, new Cell(current.getRow(), column), king,board);
                 if (board[current.getRow()][column] != null) {
                    if (board[current.getRow()][column].color != board[current.getRow()][current.getColumn()].color) {
                        if (ok) {
                            list.add(new Cell(current.getRow(), column));
                        }
                    }
                    break;
                } 
                else {
                    if (ok) {
                        list.add(new Cell(current.getRow(), column));
                    }
                }
            }

            //Checking Right direction
            for (int column = current.getColumn() + 1; column < 8; column++) {
                // chcek if the king is in danger 
               boolean ok = this.check_my_king(current, new Cell(current.getRow(), column), king,board);
                 if (board[current.getRow()][column] != null) {
                    if (board[current.getRow()][column].color != board[current.getRow()][current.getColumn()].color) {
                        if (ok) {
                            list.add(new Cell(current.getRow(), column));
                        }
                    }
                    break;
                } 
                else {
                    if (ok) {
                        list.add(new Cell(current.getRow(), column));
                    }
                }
            }
            //Checking down direction
            for (int row = current.getRow() + 1; row < 8; row++) {
                // chcek if the king is in danger 
                boolean ok = this.check_my_king(current, new Cell(row, current.getColumn()), king,board);
                 if (board[row][current.getColumn()] != null) {
                    if (board[row][current.getColumn()].color != board[current.getRow()][current.getColumn()].color) {
                        if (ok) {
                            list.add(new Cell(row, current.getColumn()));
                        }
                    }
                    break;
                } 
                else {
                    if (ok) {
                        list.add(new Cell(row, current.getColumn()));
                    }
                }
            }
            //Checking up direction
            for (int row = current.getRow() - 1; row >= 0; row--) {
                // chcek if the king is in danger 
                boolean ok = this.check_my_king(current, new Cell(row, current.getColumn()), king,board);
                 if (board[row][current.getColumn()] != null) {
                    if (board[row][current.getColumn()].color != board[current.getRow()][current.getColumn()].color) {
                        if (ok) {
                            list.add(new Cell(row, current.getColumn()));
                        }
                    }
                    break;
                } 
                else {
                    if (ok) {
                        list.add(new Cell(row, current.getColumn()));
                    }
                }
            }
        return list;
    }
}
