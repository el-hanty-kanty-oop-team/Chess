package GamePackage;

import PlayerPackage.*;
import PiecePackage.*;
import java.util.*;

public class Game {

    public Board board;
    boolean turn;
    GameMode mode;
    int moves,doublemove_indicator;
    Player firstPlayer, secondPlayer;
    DoubleMove current_move;
    Stack<DoubleMove> move_history;
    public Game(GameMode mode, Player firstPlayer, Player secondPlayer) {
        moves = 0;
        turn = false;
        this.mode = mode;
        board = new Board();
        doublemove_indicator = 0;
        move_history = new Stack<>();
        this.firstPlayer = firstPlayer;
        current_move = new DoubleMove();
        this.secondPlayer = secondPlayer;
        board.whiteKing = board.pieces[0][4];
        board.blackKing = board.pieces[7][4];
    }

    public void run() {
        Color c = Color.White;
        while (!isCheckmated(c)) {
//            //body
//            //ArrayList<Cell> list = board.pieces[3][4].possible_moves(new Cell(3, 4), board);     
//            //turn = !turn;
//            if (c == Color.Black) {
//                c = Color.White;
//            } else {
//                c = Color.Black;
//            }
//            break;
        }
    }

    public boolean isCheckmated(Color c) {
        ArrayList<Cell> list;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.pieces[i][j] != null && board.pieces[i][j].getColor() == c) {
                    list = board.pieces[i][j].possible_moves(new Cell(i, j), board);
                    if (!list.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        boolean ok = true;
        if (c == Color.Black) {
            ok &= board.blackKing.check_my_king(board.blackKing.getPos(), board.blackKing.getPos(), board.blackKing, board.pieces);
        } else {
            ok &= board.whiteKing.check_my_king(board.whiteKing.getPos(), board.whiteKing.getPos(), board.whiteKing, board.pieces);
        }
        if (ok) {
            return false;
        }
        return true;
    }

    private boolean fiftMovesRule() {
        return moves >= 50;
    }

    private boolean threefoldRepetitionRule() {
        if(move_history.size() < 3)
            return false;
        DoubleMove d1,d2,d3;
        d1 = move_history.pop();
        d2 = move_history.pop();
        d3 = move_history.pop();
        move_history.push(d3);
        move_history.push(d2);
        move_history.push(d1);
        if(d1.equalTo(d2) && d2.equalTo(d3))
            return true;
        return false;
    }

    private boolean stalemateRule(Color c) {
        ArrayList<Cell> list;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.pieces[i][j] != null && board.pieces[i][j].getColor() == c) {
                    list = board.pieces[i][j].possible_moves(new Cell(i, j), board);
                    if (!list.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
public boolean draw(Color c) {
        // checking for 50 moves rule
        boolean res = false;
        res |= fiftMovesRule();
        
        // checking for threefold repetition 
        res |= threefoldRepetitionRule();

        // checking for no possible moves for current player
        res |= stalemateRule(c);
        
        return res;
    }

    public void update(Cell from, Cell to){
       
        // updating steps , moveid , 50MovesRule Counter
        board.pieces[from.getRow()][from.getColumn()].steps++;
        board.pieces[from.getRow()][from.getColumn()].last_move_id = board.moveid;
        board.moveid++;
        if(board.pieces[to.getRow()][to.getColumn()] != null || board.pieces[from.getRow()][from.getColumn()] instanceof Pawn)
            this.moves = 0;
        else
            this.moves++;
        board.pieces[from.getRow()][from.getColumn()].setPos(to);
        board.pieces[to.getRow()][to.getColumn()] = board.pieces[from.getRow()][from.getColumn()];
        board.pieces[from.getRow()][from.getColumn()] = null;
        if(to.special_move == 1){
            if(board.pieces[from.getRow()][from.getColumn()] instanceof Pawn){
                if(board.pieces[from.getRow()][from.getColumn()].getColor() == Color.White)
                   board.pieces[to.getRow() - 1][to.getColumn()] = null;
                else
                   board.pieces[to.getRow() + 1][to.getColumn()] = null;
            }
            else if(board.pieces[from.getRow()][from.getColumn()] instanceof King){
                if(to.getColumn() == 2){
                    board.pieces[to.getRow()][to.getColumn() + 1] = board.pieces[to.getRow()][0];
                    board.pieces[to.getRow()][0] = null;
                }
                else{
                    board.pieces[to.getRow()][to.getColumn() - 1] = board.pieces[to.getRow()][7];
                    board.pieces[to.getRow()][7] = null;
                }
            }
        }
        // saving game history
        if(this.doublemove_indicator == 0){
            this.current_move.player1_move = new SingleMove(to, from,  board.pieces[to.getRow()][to.getColumn()]);
            this.doublemove_indicator = 1;
        }
        else{
            this.current_move.player2_move = new SingleMove(to, from,  board.pieces[to.getRow()][to.getColumn()]);
            move_history.push(current_move);
            this.doublemove_indicator = 0;
        }
        System.out.print(board.pieces[to.getRow()][to.getColumn()].getColor().toString() + " piece moved from ");
        System.out.println(from.getRow() + " " + from.getColumn() + " to " + to.getRow() + " " + to.getColumn());
    }
 
    public void  makePromotion(Cell c, int choice){
        Color col = Color.White;
        if(c.getRow() == 0)
            col = Color.Black;
        switch (choice) {
            case 0:
                board.pieces[c.getRow()][c.getColumn()] = new Rook(c, col);
                break;
            case 1:
                board.pieces[c.getRow()][c.getColumn()] = new Bishop(c, col);
                break;
            case 2:
                board.pieces[c.getRow()][c.getColumn()] = new Knight(c, col);
                break;
            case 3:
                board.pieces[c.getRow()][c.getColumn()] = new Queen(c, col);
                break;
            default:
                break;
        }
    }

    public void start_time_of_game() {

    }

    public void end_time_of_game() {

    }

    public void start_game() {

    }

    public void end_game() {

    }
}
