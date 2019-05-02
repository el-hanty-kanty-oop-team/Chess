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
   
    private boolean killed[][], promoted[], isMoveDone;
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
        promoted = new boolean[8];
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
    public String getSelectedPieceType(int i, int j)
    {
        if(i == 1 || i == 2)
            return "Pawn";
        else if(j == 4)
            return "King";
        else
            return "Else";
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
                if(piece[i][j].isEquale(s) && !killed[i][j])
                    return new Vector3i(i, 0, j);
            }
        }
        return null;
    }
    
    @Override
    public Vector3i getPieceIndex(int r, int c)
    {
        for(int i = 0; i < piece.length; i ++)
        {
            for(int j = 0; j < piece[i].length; j ++)
            {
                if((int)dimension[i][j].getKey() == r && (int)dimension[i][j].getValue() == c && !killed[i][j])
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
    public void enPassant(Vector3i from, Vector3i to)
    {
        Move(from, to);
        int i = getPieceIndex(to.x - 1, to.z).x, j = getPieceIndex(to.x - 1, to.z).z;
        kill(i, j);
        killed[i][j] = true;
    }
    
    @Override
    public void castling(Vector3i from, Vector3i to)
    {
        Move(from, to);
        int i = from.getX(), j = from.getZ();
        int r = to.x, c = to.z;
        
        if(c == 6)
        {
            piece[i][c + 1].getLocalNode().setLocalTranslation(r, 0, c - 1);
            piece[i][c + 1].getPlayerWalkDirection().set(r, 0, c - 1);
            dimension[i][c + 1] = new Pair(r, c - 1);
        }
        else // if c == 2
        {
            piece[i][c - 2].getLocalNode().setLocalTranslation(r, 0, c + 1);
            piece[i][c - 2].getPlayerWalkDirection().set(r, 0, c + 1);
            dimension[i][c - 2] = new Pair(r, c + 1);
        }
    }
    
    
    @Override
    public void update(float tpf)
    {
        super.update(tpf);
        
        if(x != -1 && z != -1)
        {   
            if(piece[x][z].attackIterationStarted())
                check(x, z, true);
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
    
    @Override
    public boolean checkPromotion(int i, int j)
    {
        int r = (int)dimension[i][j].getKey(), c = (int)dimension[i][j].getValue();
        if(i == 1)
        {
            boolean check = !promoted[j] && r == 7;
            
            if(check)
                promoted[j] = true;
            
            return check;
        }
        else if(i == 2)
        {
            boolean check = !promoted[j] && r == 0;
            
            if(check)
                promoted[j] = true;
            
            return check;
        }
        
        return false;
    }
    
    @Override
    public void promote(int r, int c, int type)
    {
        rootNode.detachChild(piece[r][c].getLocalNode());
        stateManager.detach(piece[r][c]);
        
        switch(type)
        {
            case 0:
                
                if(piece[r][c] instanceof PiecesAndAnimation.PiecesAnimation.ZombieMode.Pawn)
                    piece[r][c] = new PiecesAndAnimation.PiecesAnimation.ZombieMode.Castle(app, (int)dimension[r][c].getKey(), (int)dimension[r][c].getValue(), r == 1);
                else
                    piece[r][c] = new PiecesAndAnimation.PiecesAnimation.MagicalMode.Castle(app, (int)dimension[r][c].getKey(), (int)dimension[r][c].getValue(), r == 1);
            
                break;
            
            case 1:
                
                if(piece[r][c] instanceof PiecesAndAnimation.PiecesAnimation.ZombieMode.Pawn)
                    piece[r][c] = new PiecesAndAnimation.PiecesAnimation.ZombieMode.Bishop(app, (int)dimension[r][c].getKey(), (int)dimension[r][c].getValue(), r == 1);
                else
                    piece[r][c] = new PiecesAndAnimation.PiecesAnimation.MagicalMode.Bishop(app, (int)dimension[r][c].getKey(), (int)dimension[r][c].getValue(), r == 1);            break;
            
            case 2:
                
                if(piece[r][c] instanceof PiecesAndAnimation.PiecesAnimation.ZombieMode.Pawn)
                    piece[r][c] = new PiecesAndAnimation.PiecesAnimation.ZombieMode.Knight(app, (int)dimension[r][c].getKey(), (int)dimension[r][c].getValue(), r == 1);
                else
                    piece[r][c] = new PiecesAndAnimation.PiecesAnimation.MagicalMode.Knight(app, (int)dimension[r][c].getKey(), (int)dimension[r][c].getValue(), r == 1);
                break;
            
            case 3:
                
                if(piece[r][c] instanceof PiecesAndAnimation.PiecesAnimation.ZombieMode.Pawn)
                    piece[r][c] = new PiecesAndAnimation.PiecesAnimation.ZombieMode.Queen(app, (int)dimension[r][c].getKey(), (int)dimension[r][c].getValue(), r == 1);
                else
                    piece[r][c] = new PiecesAndAnimation.PiecesAnimation.MagicalMode.Queen(app, (int)dimension[r][c].getKey(), (int)dimension[r][c].getValue(), r == 1);
                break;
        }
        
        stateManager.attach(piece[r][c]);
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
                    System.out.println("PiecesAndAnimation.PiecesAnimation.AbstractAnimationPieces.check()");
                    System.out.println("Piece " + i + " " + j);
                    if(kill)
                    {
                        System.out.println("Kill piece " + i + " " + j);
                        kill(i, j);
                        killed[i][j] = true;
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    private void kill(int i, int j)
    {
        piece[i][j].die();
    }

}
