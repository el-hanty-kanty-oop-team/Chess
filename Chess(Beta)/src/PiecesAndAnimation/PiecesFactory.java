package PiecesAndAnimation;

import PiecesAndAnimation.OriginPieces.OriginalPieces;
import PiecesAndAnimation.PiecesAnimation.ZombieMode.Zombie;
import com.jme3.app.SimpleApplication;

/**
 *
 * @author delll
 */
public class PiecesFactory 
{
    public static PiecesBehaviors GetPiecesType(SimpleApplication app, String selected)
    {
        if (selected.equalsIgnoreCase("WhiteAndBlackOriginal"))
        {
            return new OriginalPieces(app);
        }
        else if (selected.equalsIgnoreCase("ZombiePieces"))
        {
             return new Zombie(app);
        }
        else if (selected.equalsIgnoreCase("MagicalPieces"))
        { 
            // we will do it soon inshallah
            return null  ;
        }

        return null ;
    }
}





