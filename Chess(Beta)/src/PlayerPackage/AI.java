package PlayerPackage;
 
import GamePackage.*;
import PiecePackage.*;
import java.util.*;
import java.lang.Math;
 
public class AI extends Player {
        double[][] pawnrate;
    double[][] rookrate;
    double[][] bishobrate;
    double[][] knightrate;
    double[][] queenrate;
    double[][] kingrate;
 
    final void fillpawn() {
        this.pawnrate = new double[][]{
            {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, 0.0},
            {5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0, 5.0},
            {1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0, 1.0},
            {0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5, 0.5},
            {0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0, 0.0},
            {0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5, 0.5},
            {0.5,  1.0,  1.0, -2.0, -2.0,  1.0,  1.0, 0.5},
            {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, 0.0}
        };
    }
 
    final void fillrook() {
        this.rookrate = new double[][]{
            { 0.0, 0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
            { 0.5, 1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5},
            {-0.5, 0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            {-0.5, 0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            {-0.5, 0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            {-0.5, 0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            {-0.5, 0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            { 0.0, 0.0,  0.0,  0.5,  0.5,  0.0,  0.0,  0.0}         
     };
    }
    final void fillbishob() {
        this.bishobrate=new double[][]{
            { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
            { -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
            { -1.0,  0.0,  0.5,  1.0,  1.0,  0.5,  0.0, -1.0},
            { -1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5, -1.0},
            { -1.0,  0.0,  1.0,  1.0,  1.0,  1.0,  0.0, -1.0},
            { -1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0, -1.0},
            { -1.0,  0.5,  0.0,  0.0,  0.0,  0.0,  0.5, -1.0},
            { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}  
        };
    }
 
    final void fillknight() {
        this.knightrate=new double[][]{
            {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
            {-4.0, -2.0,  0.0,  0.0,  0.0,  0.0, -2.0, -4.0},
            {-3.0,  0.0,  1.0,  1.5,  1.5,  1.0,  0.0, -3.0},
            {-3.0,  0.5,  1.5,  2.0,  2.0,  1.5,  0.5, -3.0},
            {-3.0,  0.0,  1.5,  2.0,  2.0,  1.5,  0.0, -3.0},
            {-3.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -3.0},
            {-4.0, -2.0,  0.0,  0.5,  0.5,  0.0, -2.0, -4.0},
            {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}
        };
    }
 
    final void fillqueen() {
        this.queenrate=new double[][]{
            { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
            { -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
            { -1.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
            { -0.5,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
            {  0.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
            { -1.0,  0.5,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
            { -1.0,  0.0,  0.5,  0.0,  0.0,  0.0,  0.0, -1.0},
            { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}
        };
    }
 
    final void fillking() {
        this.kingrate=new double[][]{
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            { -2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
            { -1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
            {  2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0},
            {  2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0}
       };
    }
 
    public AI(Color c) {
        super("AI", c);
        fillbishob();
        fillking();
        fillknight();
        fillpawn();
        fillqueen();
        fillrook();
    }
 
    private ArrayList< SingleMove> generate_all_moves_on_all_pieces(Board board, Color mycolor) {
        ArrayList< SingleMove> all_moves = new ArrayList<>();
 
        for (int i = 0; i < 8; i++) {
            for (int y = 0; y < 8; y++) {
                if (board.pieces[i][y] != null && board.pieces[i][y].getColor() == mycolor) {
                    Cell cur = new Cell(i, y);
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
        double best_score = Double.MIN_VALUE;
        Cell best_move_f = new Cell();
        Cell best_move_t=new Cell();
        if (moves.isEmpty()){
            /*computer loss player win  */
        } else {
            for (int i = 0; i < (int) moves.size(); i++) {
                SingleMove one_move = moves.get(i);
                int row_f = one_move.getFrom().getRow();
                int col_f = one_move.getFrom().getColumn();
                int row_t = one_move.getTo().getRow();
                int col_t = one_move.getTo().getColumn();
                ///make_move
                Piece cur = board.pieces[row_f][col_f];
                Piece nxt = board.pieces[row_t][col_t];
                board.pieces[row_f][col_f] = null;
                board.pieces[row_t][col_t] = cur;
                board.pieces[row_t][col_t].steps++;
                board.pieces[row_t][col_t].setPos(new Cell(row_t, col_t));
                ///finish_move
                Double score = mini_max(depth - 1, Double.MIN_VALUE, Double.MAX_VALUE, !is_max_player, board);                ///undo_move
                board.pieces[row_f][col_f] = cur;
                board.pieces[row_t][col_t] = nxt;
                board.pieces[row_f][col_f].steps--;
                board.pieces[row_f][col_f].setPos(new Cell(row_f, col_f));
 
                ///finish_undo
                if (score >= best_score) {
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
    private double mini_max(int depth, double alpha, double bita, boolean is_max_player, Board board) {
 
        if (depth == 0) {
            return get_rating_of_board(board);
        }
        double best_val;
        ArrayList< SingleMove> moves = generate_all_moves_on_all_pieces(board, get_color_now(is_max_player));
 
        if (is_max_player == true) {
            best_val = Double.MIN_VALUE;
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
                board.pieces[row_t][col_t].steps++;
                board.pieces[row_t][col_t].setPos(new Cell(row_t, col_t));
                ///finish_move
                best_val = Math.max(best_val, mini_max(depth - 1, alpha, bita, !is_max_player, board));
                alpha = Math.max(alpha, best_val);
                ///undo_move
                board.pieces[row_f][col_f] = tmp_piece;
                board.pieces[row_t][col_t] = tmp_piece2;
                board.pieces[row_f][col_f].steps--;
                board.pieces[row_f][col_f].setPos(new Cell(row_f, col_f));
                if (alpha >= bita) {
                    return best_val;
                }
            }
        } else {
            best_val = Double.MAX_VALUE;
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
                board.pieces[row_t][col_t].steps++;
                board.pieces[row_t][col_t].setPos(new Cell(row_t, col_t));
                ///finish_move
                best_val = Math.min(best_val, mini_max(depth - 1, alpha, bita, !is_max_player, board));
                bita = Math.min(bita, best_val);
                ///undo_move
                board.pieces[row_f][col_f] = tmp_piece;
                board.pieces[row_t][col_t] = tmp_piece2;
                board.pieces[row_f][col_f].steps--;
                board.pieces[row_f][col_f].setPos(new Cell(row_f, col_f));
 
                if (alpha >= bita) {
                    return best_val;
                }
            }
        }
 
        return best_val;
    }
 
    /////////
    private double get_rating_of_board(Board board) {
        double total_score = 0;
 
        for (int i = 0; i < 8; i++) {
            for (int y = 0; y < 8; y++) {
                if(board.pieces[i][y]!=null)
                 total_score += get_piece_score(board.pieces[i][y],i,y);
            }
        }
 
        return total_score;
    }
 
    private double get_piece_score(Piece piece,int r,int c) {
        int posORneg=0;
 
        if (piece!=null &&piece.getColor() == this.color) {
            posORneg = 1;
        } else if(piece!=null) {
            posORneg = -1;
        }
 
        if (piece instanceof Pawn) {
            return (10 * posORneg)+pawnrate[r][c];
        } else if (piece instanceof Knight) {
            return (30 * posORneg)+knightrate[r][c];
        } else if (piece instanceof Bishop) {
            return (40 * posORneg)+bishobrate[r][c];
        } else if (piece instanceof Rook) {
            return (50 * posORneg)+rookrate[r][c];
        } else if (piece instanceof Queen) {
            return (100 *posORneg)+queenrate[r][c];
        } else if (piece instanceof King) {
            return (1000 * posORneg)+kingrate[r][c];
        } else {
            return 0;
        }
 
    }
}