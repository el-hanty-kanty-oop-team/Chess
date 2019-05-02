/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecePackage;

import GamePackage.Board;
import GamePackage.Cell;
import GamePackage.Color;
import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(Cell pos, Color c) {
        super(pos, c);
        last_move_id = 0;
        steps = 0;
    }
    
    @Override
    public Piece clone()
    {
        return new Queen(new Cell(this.pos), this.color);
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
        // cheking my left
        for (int column = current.getColumn() - 1; column >= 0; column--) {
            // chcek if the king is in danger
            boolean ok = this.check_my_king(current, new Cell(current.getRow(), column), king, board);
            if (board[current.getRow()][column] != null) {
                if (board[current.getRow()][column].color != board[current.getRow()][current.getColumn()].color) {
                    if (ok) {
                        list.add(new Cell(current.getRow(), column));
                    }
                }
                break;
            } else {
                if (ok) {
                    list.add(new Cell(current.getRow(), column));
                }
            }
        }

        //Checking Right direction
        for (int column = current.getColumn() + 1; column < 8; column++) {
            // chcek if the king is in danger 
            boolean ok = this.check_my_king(current, new Cell(current.getRow(), column), king, board);
            if (board[current.getRow()][column] != null) {
                if (board[current.getRow()][column].color != board[current.getRow()][current.getColumn()].color) {
                    if (ok) {
                        list.add(new Cell(current.getRow(), column));
                    }
                }
                break;
            } else {
                if (ok) {
                    list.add(new Cell(current.getRow(), column));
                }
            }
        }
        //Checking down direction
        for (int row = current.getRow() + 1; row < 8; row++) {
            // chcek if the king is in danger 
            boolean ok = this.check_my_king(current, new Cell(row, current.getColumn()), king, board);
            if (board[row][current.getColumn()] != null) {
                if (board[row][current.getColumn()].color != board[current.getRow()][current.getColumn()].color) {
                    if (ok) {
                        list.add(new Cell(row, current.getColumn()));
                    }
                }
                break;
            } else {
                if (ok) {
                    list.add(new Cell(row, current.getColumn()));
                }
            }
        }
        //Checking up direction
        for (int row = current.getRow() - 1; row >= 0; row--) {
            // chcek if the king is in danger 
            boolean ok = this.check_my_king(current, new Cell(row, current.getColumn()), king, board);
            if (board[row][current.getColumn()] != null) {
                if (board[row][current.getColumn()].color != board[current.getRow()][current.getColumn()].color) {
                    if (ok) {
                        list.add(new Cell(row, current.getColumn()));
                    }
                }
                break;
            } else {
                if (ok) {
                    list.add(new Cell(row, current.getColumn()));
                }
            }
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
