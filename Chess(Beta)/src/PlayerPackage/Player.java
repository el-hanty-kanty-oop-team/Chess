package PlayerPackage;
import GamePackage.Cell;
import GamePackage.Color;
import PiecePackage.*;
public class Player {
    public String name;
    public Color color;
    public Piece king;

    public Player(String name, Color c) {
        this.name = name;
        this.color = c;
    }
    
    public void move(Piece p ,Cell to){
        
    }
}
