package GamePackage;
import PiecePackage.*;

public class Board {

    public Piece[][] pieces;
    public Piece whiteKing,blackKing;
    public int moveid;
    public Board() {
        pieces = new Piece[10][10];
        this.set_board();
        whiteKing = blackKing = null;
        moveid = 0;
    }

    private void set_board() {
        for (int i = 0; i < 8; i++) {
            // setting Black pawns
            pieces[1][i] = new Pawn(new Cell(1, i), Color.White);
            // setting White pawns
            pieces[6][i] = new Pawn(new Cell(6, i), Color.Black);
        }
        // setting Black pieces
        pieces[0][0] = new Rook(new Cell(0, 0), Color.White);
        pieces[0][7] = new Rook(new Cell(0, 7), Color.White);
        pieces[0][1] = new Knight(new Cell(0, 1), Color.White);
        pieces[0][6] = new Knight(new Cell(0, 6), Color.White);
        pieces[0][2] = new Bishop(new Cell(0, 2), Color.White);
        pieces[0][5] = new Bishop(new Cell(0, 5), Color.White);
        pieces[0][3] = new Queen(new Cell(0, 3), Color.White);
        pieces[0][4] = new King(new Cell(0, 4), Color.White);

        // setting White pieces
        pieces[7][0] = new Rook(new Cell(7, 0), Color.Black);
        pieces[7][7] = new Rook(new Cell(7, 7), Color.Black);
        pieces[7][1] = new Knight(new Cell(7, 1), Color.Black);
        pieces[7][6] = new Knight(new Cell(7, 6), Color.Black);
        pieces[7][2] = new Bishop(new Cell(7, 2), Color.Black);
        pieces[7][5] = new Bishop(new Cell(7, 5), Color.Black);
        pieces[7][3] = new Queen(new Cell(7, 3), Color.Black);
        pieces[7][4] = new King(new Cell(7, 4), Color.Black);
    }

    /**
     * // setting White pieces
        pieces[0][0] = new Rook(new Cell(0, 0), Color.Black);
        pieces[0][7] = new Rook(new Cell(0, 7), Color.Black);
        pieces[0][1] = new Knight(new Cell(0, 1), Color.Black);
        pieces[0][6] = new Knight(new Cell(0, 6), Color.Black);
        pieces[0][2] = new Bishop(new Cell(0, 2), Color.Black);
        pieces[0][5] = new Bishop(new Cell(0, 5), Color.Black);
        pieces[0][3] = new Queen(new Cell(0, 3), Color.Black);
        pieces[0][4] = new King(new Cell(0, 4), Color.Black);

        // setting Black pieces
        pieces[7][0] = new Rook(new Cell(7, 0), Color.White);
        pieces[7][7] = new Rook(new Cell(7, 7), Color.White);
        pieces[7][1] = new Knight(new Cell(7, 1), Color.White);
        pieces[7][6] = new Knight(new Cell(7, 6), Color.White);
        pieces[7][2] = new Bishop(new Cell(7, 2), Color.White);
        pieces[7][5] = new Bishop(new Cell(7, 5), Color.White);
        pieces[7][3] = new Queen(new Cell(7, 3), Color.White);
        pieces[7][4] = new King(new Cell(7, 4), Color.White);
     * @return 
     */
    public Piece[][] getPieces() {
        return pieces;
    }

    public void create_board() {

    }

}
