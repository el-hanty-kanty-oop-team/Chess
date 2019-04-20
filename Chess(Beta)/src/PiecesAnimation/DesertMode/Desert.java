/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAnimation.DesertMode;

import PiecesAndAnimation.PiecesAnimation.PieceAnimation;
import Tools.Vector3i;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import javafx.util.Pair;

/**
 *
 * @author shaks
 */
public class Desert extends AbstractAppState
{
    
    private final SimpleApplication app;
    private AssetManager assetManager;
    private boolean killed[][];
    private float modelScale;
    private Node localNode, rootNode;
    private PieceAnimation piece[][];
    private Pair dimension[][];
    public Desert(SimpleApplication app) 
    {
        this.app = app;
        modelScale = 0.15f;
        localNode = new Node();
        assetManager = app.getAssetManager();
        rootNode = app.getRootNode();
        piece = new PieceAnimation[4][8];
        dimension = new Pair[4][8];
        killed = new boolean[4][8];
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application application)
    {
        super.initialize(stateManager, application);
        
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
        
        for(int i = 0; i < piece.length - 2; i ++)
        {
            for(int j = 0; j < 8 ; j ++)
            {
                stateManager.attach(piece[i][j]);    
                dimension[i][j] = new Pair(i, j);
            }
        }
        
        for(int i = 2; i < piece.length; i ++)
        {
            for(int j = 0; j < 8 ; j ++)
            {
                stateManager.attach(piece[i][j]);
                dimension[i][j] = new Pair(i + 4, j);
            }
        }
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        localNode.addLight(sun);
        rootNode.attachChild(localNode);
    }

    public Vector3i getPieceDimension(Spatial s)
    {
        for(int i = 0; i < piece.length; i ++)
        {
            for(int j = 0; j < piece[i].length; j ++)
            {
                if(piece[i][j].isEquale(s))
                    return new Vector3i((int)dimension[i][j].getKey(), 0, (int)dimension[i][j].getValue());
            }
        }
        return null;
    }
 
    public Vector3i getPieceIndex(Spatial s)
    {
        for(int i = 0; i < piece.length; i ++)
        {
            for(int j = 0; j < piece[i].length; j ++)
            {
                if(piece[i][j].isEquale(s))
                    return new Vector3i(i, 0, j);
            }
        }
        return null;
    }
    
    public void Move(Vector3i pieceIndex, Vector3i to)
    {
        int x = pieceIndex.x, z = pieceIndex.z;
        Vector3f toF = new Vector3f(to.x, 0, to.z);
        dimension[x][z] = new Pair(to.x, to.z);
        
        piece[x][z].update(toF, "Walk");
        
        check(x, z);
        

    }
    
    private void check(int x, int z)
    {
        for(int i = 0; i < piece.length; i ++)
        {
            for(int j = 0; j < piece[i].length; j ++)
            {
                if(i == x && j == z)
                    continue;
                
                if(dimension[x][z].equals(dimension[i][j]) && !killed[i][j])
                {
                    kill(piece[i][j].getLocalNode());
                    killed[i][j] = true;
                    return;
                }
            }
        }
    }
    
    private void kill(Node p)
    {
       
    }

}
