/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAndAnimation.OriginPieces;


import PiecesAndAnimation.PiecesBehaviors;
import Tools.Vector3i;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author delll
 */
public abstract class AbstractOriginalPieces extends AbstractAppState implements PiecesBehaviors
{
    
    public Map< Pair<Integer,Integer>,String> map; 
    
    protected AssetManager assetManager;
    protected boolean killed[][], promoted[];
    protected float modelScale;
    protected Node localNode, rootNode, piece[][];
    protected Pair dimension[][];
    protected AppStateManager stateManager;
    protected Pair<Integer,Integer> p;

    private boolean isMoveDone = false;
    
    @Override
    public Vector3i getPieceDimension(Spatial s)
    {
        for(int i = 0; i < piece.length; i ++)
        {
            for(int j = 0; j < piece[i].length; j ++)
            {
                if(s == piece[i][j])
                    return new Vector3i((int)dimension[i][j].getKey(), 0, (int)dimension[i][j].getValue());
            }
        }
        return null;
    }
    
    @Override
    public Vector3i getPieceDimension(int x, int z)
    {
        return new Vector3i((int)dimension[x][z].getKey(), 0, (int)dimension[x][z].getValue());   
    }
    
    @Override
    public Vector3i getPieceIndex(Spatial s)
    {
        for(int i = 0; i < piece.length; i ++)
        {
            for(int j = 0; j < piece[i].length; j ++)
            {
                if(s == piece[i][j])
                    return new Vector3i(i, 0, j);
            }
        }
        return null;
    }
    
    @Override
    public void Move(Vector3i pieceIndex, Vector3i to)
    {
        int x = pieceIndex.x, z = pieceIndex.z;
        float y = piece[x][z].getLocalTranslation().y;
        Vector3f toF = new Vector3f(to.x, y, to.z);
        dimension[x][z] = new Pair(to.x, to.z);
       
        MotionPath path = new MotionPath();
        path.addWayPoint(piece[x][z].getLocalTranslation());
        
        if((x == 0 || x == 3) && (z == 1 || z == 6))
        {
            Vector3f mid = new Vector3f((piece[x][z].getLocalTranslation().x + toF.x) / 2.0f, 1.0f, (piece[x][z].getLocalTranslation().z + toF.z) / 2.0f);
            path.addWayPoint(mid);
        }
        path.addWayPoint(toF);
        
        
        MotionEvent motionControl = new MotionEvent(piece[x][z], path)
        {
            @Override
            public void onStop()
            {
                isMoveDone = true;
            }
        };
       // motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
        motionControl.setInitialDuration(10f);
        motionControl.setSpeed(10f);
        motionControl.play();
        
        check(x, z);
        
        piece[x][z].setLocalTranslation(toF);

    }
    
    @Override
    public boolean isMoveDone()
    {
        boolean done = isMoveDone;
        isMoveDone = false;
        return done;
    }
    
    @Override
    public boolean checkPromotion(int i, int j)
    {
        int r = (int)dimension[i][j].getKey(), c = (int)dimension[i][j].getValue();
        System.out.println(i + " " + j);
        if(i == 1)
        {
            boolean check = !promoted[j] && r == 7;
            System.out.println("pawn check " + promoted[j]  + " " + r);
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
    public void promote(int i, int j, int type)
    {
        String color = "";
        
        if(i == 1)
            color = "";
        else if(i == 2)
            color = "Black ";
        localNode.detachChild(piece[i][j]);
        switch(type)
        {
            case 0:
                piece[i][j] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild(color + "Rock"));
                setMAp(color + "Rock", i, j);
            break;
            
            case 1:
               piece[i][j] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild(color + "Bishop"));
               setMAp(color + "Bishop", i, j);
            break;
            
            case 2:
                piece[i][j] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild(color + "Knight"));
                setMAp(color + "Knight", i, j);
            break;
            
            case 3:
                piece[i][j] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild(color + "Queen"));
                setMAp(color + "Queen", i, j);
            break;
        }
        piece[i][j].setLocalScale(modelScale);
        if(type == 3)
            piece[i][j].setLocalTranslation((int)dimension[i][j].getKey(), 0.1f, (int)dimension[i][j].getValue());
        else
            piece[i][j].setLocalTranslation((int)dimension[i][j].getKey(), 0.0f, (int)dimension[i][j].getValue());
        localNode.attachChild(piece[i][j]);
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
                    kill(i, j);
                    killed[i][j] = true;
                    return;
                }
            }
        }
    }
    
    protected void setMAp (String s ,int i ,int j)
    {
        p = new Pair<>(i,j); 
        map.put(p, s) ;           
    }
    
    private void kill(int i, int j)
    {
        Vector3f diePosition;
        if(map.get(new Pair(i, j)).contains("White"))
        {
            if(map.get(new Pair(i, j)).contains("Pawn"))
                diePosition = new Vector3f(j, 1.0f, 10.0f);
            else
                diePosition = new Vector3f(j, 1.0f, 11.0f);
        }
        else
        {
            if(map.get(new Pair(i, j)).contains("Pawn"))
                diePosition = new Vector3f(j, 1.0f, -4.0f);
            else
                diePosition = new Vector3f(j, 1.0f, -3.0f);
        }
        piece[i][j].lookAt(diePosition, Vector3f.ZERO);
        piece[i][j].setLocalTranslation(diePosition);
    }
   
}
    

