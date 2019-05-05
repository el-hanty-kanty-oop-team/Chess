package mygame;

import GamePackage.Chess;
import com.jme3.app.SimpleApplication;


/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication
{
    private Chess chess;
    public static void main(String[] args) 
    {
        Main app = new Main();      
        app.start();
    }

    @Override
    public void simpleInitApp() 
    {
        //@2nd parm pieceseType
        //@3rd parm AI
        //@4 depth of engine (easy(2), mid(3), hard(5), HIQ(6))
        chess = new Chess(this, "MagicalVsZombie", true, 3);
        stateManager.attach(chess);
    }

    @Override
    public void simpleUpdate(float tpf)
    {
        if(chess != null && chess.isGameDone() != 0)
        {
            if(chess.isGameDone() == 1) // white wins
            {
                
            }
            else // black wins
            {
                
            }
            rootNode.detachAllChildren();
            chess.detach();
            stateManager.detach(chess);
            chess = null;
            System.gc();
            //TODO: load gui again
        }
    }
    
}
