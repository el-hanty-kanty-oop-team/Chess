package PiecesAndAnimation;

import PiecesAndAnimation.OriginPieces.OriginalPieces;
import PiecesAndAnimation.PiecesAnimation.MagicalMode.Magical;
import PiecesAndAnimation.PiecesAnimation.MagicalVsZombie.MagicalVsZombie;
import PiecesAndAnimation.PiecesAnimation.ZombieMode.Zombie;
import PiecesAndAnimation.PiecesAnimation.ZombieVsMagical.ZombieVsMagical;
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
            return new Magical(app)  ;
        }
        else if(selected.equalsIgnoreCase("ZombieVsMagical"))
        {
            return new ZombieVsMagical(app);
        }
        else if(selected.equalsIgnoreCase("MagicalVsZombie"))
        {
            return new MagicalVsZombie(app);
        }
        return null ;
    }
}





