/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAndAnimation.PiecesAnimation.ZombieMode;

import PiecesAndAnimation.PiecesAnimation.AbstractAnimationPieces;
import PiecesAndAnimation.PiecesAnimation.PieceAnimation;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import javafx.util.Pair;

/**
 *
 * @author shaks
 */
public class Zombie extends AbstractAnimationPieces
{

    public Zombie(SimpleApplication app) 
    {
        super(app);
    }
    
    private void loadCharactersModels()
    {
        for(int j = 0; j < 8; j ++)
            piece[1][j] = new Pawn(app, 1, j, true);
        
        for(int j = 0; j < 8; j ++)
            piece[2][j] = new Pawn(app, 6, j, false);
        
        piece[3][0] = new Castle(app, 7, 0, false);
        piece[3][7] = new Castle(app, 7, 7, false);
        
        piece[3][1] = new Knight(app, 7, 1, false);
        piece[3][6] = new Knight(app, 7, 6, false);
        
        piece[3][2] = new Bishop(app, 7, 2, false);
        piece[3][5] = new Bishop(app, 7, 5, false);
        
        piece[3][3] = new Queen(app, 7, 3, false);
        piece[3][4] = new King(app, 7, 4, false);
        
        piece[0][0] = new Castle(app, 0, 0, true);
        piece[0][7] = new Castle(app, 0, 7, true);
        
        piece[0][1] = new Knight(app, 0, 1, true);
        piece[0][6] = new Knight(app, 0, 6, true);
        
        piece[0][2] = new Bishop(app, 0, 2, true);
        piece[0][5] = new Bishop(app, 0, 5, true);
        
        piece[0][3] = new Queen(app, 0, 3, true);
        piece[0][4] = new King(app, 0, 4, true);
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application application)
    {
        super.initialize(stateManager, application);
        loadCharactersModels();
        setPiecesDimensions();
        addLight();
    }
    
}
