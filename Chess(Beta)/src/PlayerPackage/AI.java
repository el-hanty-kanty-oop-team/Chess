package PlayerPackage;
 
import GamePackage.*;
import PiecePackage.*;
import java.util.*;
import java.lang.Math;
 
public class AI extends Player {
 
    public AI(Color c) {
        super("AI", c);
    }
 
    private ArrayList< SingleMove> generate_all_moves_on_all_pieces(Board board, Color mycolor) {
        ArrayList< SingleMove> all_moves = new ArrayList<>();
 
        for (int i = 0; i < 8; i++) {
            for (int y = 0; y < 8; y++) {
                if (board.pieces[i][y] != null && board.pieces[i][y].getColor() == mycolor) {
                    Cell cur = new Cell(i, y);
                    /*if(board.pieces[i][y] == null)
                        System.out.println("A7eeeeeeeeeh");
                    System.out.println(i + " ===== " + y);*/
                    ArrayList<Cell> tmp = board.pieces[i][y].possible_moves(cur, board);
 
                    for (int z = 0; z < tmp.size(); z++) {
                        SingleMove single = new SingleMove( tmp.get(z),cur);
                        all_moves.add(single);
 
                    }
                }
            }
        }
 
        return all_moves;
    }
 
    public SingleMove root(int depth, boolean is_max_player, Board board) {
        ArrayList< SingleMove> moves = generate_all_moves_on_all_pieces(board, this.color);
        /*System.out.println("all moves");
        for(int i=0;i<moves.size();i++)
        {   System.out.println("this color "+this.color);
            System.out.println("from "+moves.get(i).getFrom().getRow()+" "+moves.get(i).getFrom().getColumn()+" to  "+moves.get(i).getTo().getRow()+" "+moves.get(i).getTo().getColumn());
            if(board.pieces[+moves.get(i).getFrom().getRow()][moves.get(i).getFrom().getColumn()] != null)
                System.out.println(board.pieces[+moves.get(i).getFrom().getRow()][moves.get(i).getFrom().getColumn()].getColor() + " " + board.pieces[+moves.get(i).getFrom().getRow()][moves.get(i).getFrom().getColumn()].getClass().toString());
            else
                System.out.println("NUll From ");
            
            if(board.pieces[+moves.get(i).getTo().getRow()][moves.get(i).getTo().getColumn()] != null)
                System.out.println(board.pieces[+moves.get(i).getTo().getRow()][moves.get(i).getTo().getColumn()].getColor() + " " + board.pieces[+moves.get(i).getTo().getRow()][moves.get(i).getTo().getColumn()].getClass().toString());
            else
                System.out.println("NUll To ");
        }*/
        int best_score = Integer.MIN_VALUE;
        Cell best_move_f = new Cell();
        Cell best_move_t=new Cell();
        if (moves.isEmpty()) {System.out.println("noooooooooooooooooooooooooooooooo moves ");
            /*computer loss player win  */
        } else {
            for (int i = 0; i < (int) moves.size(); i++) {
                SingleMove one_move = moves.get(i);
                int row_f = one_move.getFrom().getRow();
                int col_f = one_move.getFrom().getColumn();
                int row_t = one_move.getTo().getRow();
                int col_t = one_move.getTo().getColumn();
                ///make_move
                Piece tmp_piece = board.pieces[row_f][col_f];
                Piece tmp_piece2 = board.pieces[row_t][col_t];
                board.pieces[row_f][col_f] = null;
                board.pieces[row_t][col_t] = tmp_piece;
 
                ///finish_move
                int score = mini_max(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, !is_max_player, board);
                ///undo_move
                board.pieces[row_f][col_f] = tmp_piece;
                board.pieces[row_t][col_t] = tmp_piece2;
                ///finish_undo
                if (score > best_score) {
                    best_score = score;
                    best_move_f=one_move.getFrom();
                    best_move_t = one_move.getTo();
                }
            }
 
        }
        SingleMove ret=new SingleMove(best_move_t,best_move_f);
        return ret;
    }
 
    private Color get_color_now(boolean is_max_player) {
        if (is_max_player == true) {
            return this.color;
        } else if (this.color == Color.Black) {
            return Color.White;
        } else {
            return Color.Black;
        }
 
    }
 
    /////////
    private int mini_max(int depth, int alpha, int bita, boolean is_max_player, Board board) {
 
        if (depth == 0) {
            return get_rating_of_board(board);
        }
        int best_val;
        ArrayList< SingleMove> moves = generate_all_moves_on_all_pieces(board, get_color_now(is_max_player));
 
        if (is_max_player == true) {
            best_val = Integer.MIN_VALUE;
            for (int i = 0; i < (int) moves.size(); i++) {
                ///do
                SingleMove one_move = moves.get(i);
                int row_f = one_move.getFrom().getRow();
                int col_f = one_move.getFrom().getColumn();
                int row_t = one_move.getTo().getRow();
                int col_t = one_move.getTo().getColumn();
                ///make_move
                Piece tmp_piece = board.pieces[row_f][col_f];
                Piece tmp_piece2 = board.pieces[row_t][col_t];
                board.pieces[row_t][col_t] = tmp_piece;
                board.pieces[row_f][col_f] = null;
                ///finish_move
                best_val = Math.max(best_val, mini_max(depth - 1, alpha, bita, !is_max_player, board));
                alpha = Math.max(alpha, best_val);
                ///undo_move
                board.pieces[row_f][col_f] = tmp_piece;
                board.pieces[row_t][col_t] = tmp_piece2;
                if (alpha >= bita) {
                    return best_val;
                }
            }
        } else {
            best_val = Integer.MAX_VALUE;
            for (int i = 0; i < (int) moves.size(); i++) {
                ///do
                SingleMove one_move = moves.get(i);
                int row_f = one_move.getFrom().getRow();
                int col_f = one_move.getFrom().getColumn();
                int row_t = one_move.getTo().getRow();
                int col_t = one_move.getTo().getColumn();
                ///make_move
                Piece tmp_piece = board.pieces[row_f][col_f];
                Piece tmp_piece2 = board.pieces[row_t][col_t];
                board.pieces[row_t][col_t] = tmp_piece;
                board.pieces[row_f][col_f] = null;
                ///finish_move
                best_val = Math.min(best_val, mini_max(depth - 1, alpha, bita, !is_max_player, board));
                bita = Math.min(bita, best_val);
                ///undo_move
                board.pieces[row_f][col_f] = tmp_piece;
                board.pieces[row_t][col_t] = tmp_piece2;
                if (alpha >= bita) {
                    return best_val;
                }
            }
        }
 
        /*never reach to this case*/
        return best_val;
    }
 
    /////////
    private int get_rating_of_board(Board board) {
        int total_score = 0;
 
        for (int i = 0; i < 8; i++) {
            for (int y = 0; y < 8; y++) {
                if(board.pieces[i][y]!=null)
                 total_score += get_piece_score(board.pieces[i][y]);
            }
        }
 
        return total_score;
    }
 
    private int get_piece_score(Piece piece) {
        int posORneg=0;
 
        if (piece!=null &&piece.getColor() == this.color) {
            posORneg = 1;
        } else if(piece!=null) {
            posORneg = -1;
        }
 
        if (piece instanceof Pawn) {
            return 10 * posORneg;
        } else if (piece instanceof Knight) {
            return 30 * posORneg;
        } else if (piece instanceof Bishop) {
            return 40 * posORneg;
        } else if (piece instanceof Rook) {
            return 50 * posORneg;
        } else if (piece instanceof Queen) {
            return 100 * posORneg;
        } else if (piece instanceof King) {
            return 1000 * posORneg;
        } else {
            return 0;
        }
 
    }
}