/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAndAnimation.PiecesAnimation;

import PiecesAndAnimation.PiecesBehaviors;
import Tools.Vector3i;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import javafx.util.Pair;

/**
 *
 * @author shaks
 */
public abstract class AbstractAnimationPieces extends AbstractAppState implements PiecesBehaviors
{
    
    protected final AssetManager assetManager;
    protected final Node localNode, rootNode;
    protected final PieceAnimation piece[][];
    protected final AppStateManager stateManager;
    protected final SimpleApplication app;
    protected Pair dimension[][];
    protected float modelScale;
   
    private boolean killed[][], isMoveDone;
    private int x, z;
    
    public AbstractAnimationPieces(SimpleApplication app) 
    {
        this.app = app;
        modelScale = 0.15f;
        localNode = new Node();
        assetManager = app.getAssetManager();
        rootNode = app.getRootNode();
        stateManager = app.getStateManager();
        piece = new PieceAnimation[4][8];
        dimension = new Pair[4][8];
        killed = new boolean[4][8];
        x = z = -1;
    }

    @Override
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
    
    @Override
    public Vector3i getPieceDimension(int i, int j)
    {
        return new Vector3i((int)dimension[i][j].getKey(), 0, (int)dimension[i][j].getValue());   
    }
    
    @Override
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
    
    @Override
    public void Move(Vector3i pieceIndex, Vector3i to)
    {
        x = pieceIndex.x;
        z = pieceIndex.z;
        Vector3f toF = new Vector3f(to.x, 0, to.z);
        dimension[x][z] = new Pair(to.x, to.z);
        if(check(x, z, false))
            piece[x][z].update(toF, "Attack");
        else
            piece[x][z].update(toF, "Walk");
    }
    
    @Override
    public void update(float tpf)
    {
        super.update(tpf);
        if(x != -1 && z != -1)
        {   
            if(piece[x][z].attackIterationStarted())
                check(x, z, true);
            if((piece[x][z] instanceof PiecesAndAnimation.PiecesAnimation.ZombieMode.Pawn || piece[x][z] instanceof PiecesAndAnimation.PiecesAnimation.MagicalMode.Pawn ) && piece[x][z].isMoveDone())
                checkPawnToQueen(x, z);
        }
    }
    
    @Override
    public boolean isMoveDone()
    {
        if(x != -1 && z != -1)
            return piece[x][z].isMoveDone();
        return false;
    }
    
    protected void setPiecesDimensions()
    {
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
    }
    
    protected void addLight()
    {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        localNode.addLight(sun);
        rootNode.attachChild(localNode);
    }
    
    
    private boolean check(int r, int c, boolean kill)
    {
        for(int i = 0; i < piece.length; i ++)
        {
            for(int j = 0; j < piece[i].length; j ++)
            {
                if(i == r && j == c)
                    continue;
                
                if(dimension[r][c].equals(dimension[i][j]) && !killed[i][j])
                {
                    if(kill)
                    {
                        kill(i, j);
                        killed[i][j] = true;
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    private void checkPawnToQueen(int r, int c)
    {
        if((int)dimension[r][c].getKey() == 7 || (int)dimension[r][c].getKey() == 0)
        {
            rootNode.detachChild(piece[r][c].getLocalNode());
            stateManager.detach(piece[r][c]);
            if(piece[r][c] instanceof PiecesAndAnimation.PiecesAnimation.ZombieMode.Pawn)
                piece[r][c] = new PiecesAndAnimation.PiecesAnimation.ZombieMode.Queen(app, (int)dimension[r][c].getKey(), (int)dimension[r][c].getValue(), r == 1);
            else
                piece[r][c] = new PiecesAndAnimation.PiecesAnimation.MagicalMode.Queen(app, (int)dimension[r][c].getKey(), (int)dimension[r][c].getValue(), r == 1);
            stateManager.attach(piece[r][c]);
        }   
    }
    
    private void kill(int i, int j)
    {
        piece[i][j].die();
    }

}
