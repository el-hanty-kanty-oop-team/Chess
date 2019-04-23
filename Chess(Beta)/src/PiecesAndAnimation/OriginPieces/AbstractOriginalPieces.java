/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAndAnimation.OriginPieces;


import PiecesAndAnimation.PiecesBehaviors;
import Tools.Vector3i;
import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import javafx.util.Pair;

/**
 *
 * @author delll
 */
public abstract class AbstractOriginalPieces extends AbstractAppState implements PiecesBehaviors
{
    
    
    protected AssetManager assetManager;
    protected boolean killed[][];
    protected float modelScale;
    protected Node localNode, rootNode, piece[][];
    protected Pair dimension[][];
    
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
             // todo checkPawnToQueen()   
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
                    kill(piece[i][j]);
                    killed[i][j] = true;
                    return;
                }
            }
        }
    }
    
    private void kill(Node p)
    {
        localNode.detachChild(p);
    }
   
}
    

