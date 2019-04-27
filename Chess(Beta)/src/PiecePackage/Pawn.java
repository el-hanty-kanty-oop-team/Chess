package PiecePackage;

import GamePackage.Board;
import GamePackage.Cell;
import GamePackage.Color;
import java.util.*;

public class Pawn extends Piece {

    boolean en_passent;

    public Pawn(Cell pos, Color c) {
        super(pos, c);
        en_passent = false;
        last_move_id = 0;
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
        int dx = -1;
        if (board[current.getRow()][current.getColumn()].color == Color.White) {
            dx = 1;
        }
        // checking for the first move
        if (this.steps == 0 && board[current.getRow() + dx][current.getColumn()] == null && board[current.getRow() + 2 * dx][current.getColumn()] == null) {
            boolean ok = check_my_king(current, new Cell(current.getRow() + 2 * dx, current.getColumn()), king, board);
            if (ok) {
                list.add(new Cell(current.getRow() + 2 * dx, current.getColumn()));
            }
        }
        // checking the north cell
        if (this.check_valid_borders(current.getRow() + dx, current.getColumn()) && board[current.getRow() + dx][current.getColumn()] == null) {
            boolean ok = check_my_king(current, new Cell(current.getRow() + dx, current.getColumn()), king, board);
            if (ok) {
                list.add(new Cell(current.getRow() + dx, current.getColumn()));
            }
        }
        // checking the northern-east cell
        if (this.check_valid_borders(current.getRow() + dx, current.getColumn() + 1) && board[current.getRow() + dx][current.getColumn() + 1] != null) {
            boolean ok = check_my_king(current, new Cell(current.getRow() + dx, current.getColumn() + 1), king, board);
            if (ok) {
                list.add(new Cell(current.getRow() + dx, current.getColumn() + 1));
            }
        }
        // checking the northern-west cell
        if (this.check_valid_borders(current.getRow() + dx, current.getColumn() - 1) && board[current.getRow() + dx][current.getColumn() - 1] != null) {
            boolean ok = check_my_king(current, new Cell(current.getRow() + dx, current.getColumn() - 1), king, board);
            if (ok) {
                list.add(new Cell(current.getRow() + dx, current.getColumn() - 1));
            }
        }
        // checking the northern-east en-passant
        if (this.check_valid_borders(current.getRow(), current.getColumn() + 1) && board[current.getRow()][current.getColumn() + 1] instanceof Pawn && board[current.getRow()][current.getColumn() + 1].color != board[current.getRow()][current.getColumn()].color) {
            if (board[current.getRow()][current.getColumn() + 1].steps == 1 && ((Pawn) board[current.getRow()][current.getColumn() + 1]).en_passent) {
                if (b.moveid == board[current.getRow()][current.getColumn() + 1].last_move_id + 1) {
                    Piece p = board[current.getRow()][current.getColumn() + 1];
                    board[current.getRow()][current.getColumn() + 1] = null;
                    boolean ok = check_my_king(current, new Cell(current.getRow() + dx, current.getColumn() + 1, 1), king, board);
                    if (ok) {
                        list.add(new Cell(current.getRow() + dx, current.getColumn() + 1, 1));
                    }
                    board[current.getRow()][current.getColumn() + 1] = p;
                }
            }
        }
        // checking the northern-west en-passant
        if (this.check_valid_borders(current.getRow(), current.getColumn() - 1) && board[current.getRow()][current.getColumn() - 1] instanceof Pawn && board[current.getRow()][current.getColumn() - 1].color != board[current.getRow()][current.getColumn()].color) {
            if (board[current.getRow()][current.getColumn() - 1].steps == 1 && ((Pawn) board[current.getRow()][current.getColumn() - 1]).en_passent) {
                if (b.moveid == board[current.getRow()][current.getColumn() - 1].last_move_id + 1) {
                    Piece p = board[current.getRow()][current.getColumn() - 1];
                    board[current.getRow()][current.getColumn() - 1] = null;
                    boolean ok = check_my_king(current, new Cell(current.getRow() + dx, current.getColumn() - 1, 1), king, board);
                    if (ok) {
                        list.add(new Cell(current.getRow() + dx, current.getColumn() - 1, 1));
                    }
                    board[current.getRow()][current.getColumn() - 1] = p;
                }
            }
        }
        return list;
    }
}
