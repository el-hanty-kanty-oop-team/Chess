package OriginPieces;

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
public class OriginPiece extends AbstractAppState
{
    private AssetManager assetManager;
    private boolean killed[][];
    private float modelScale;
    private Node localNode, rootNode, originPiece, piece[][];
    private Pair dimension[][];
    public OriginPiece(SimpleApplication app) 
    {
        modelScale = 0.15f;
        localNode = new Node();
        assetManager = app.getAssetManager();
        rootNode = app.getRootNode();
        piece = new Node[4][8];
        dimension = new Pair[4][8];
        killed = new boolean[4][8];
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);
        originPiece = (Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o");
        
        for(int j = 0; j < 8; j ++)
            piece[1][j] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("White Pawn"));
        
        for(int j = 0; j < 8; j ++)
            piece[2][j] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Pawn"));
        
        piece[3][0] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Rock"));
        piece[3][7] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Rock"));
        
        piece[3][1] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Knight"));
        piece[3][6] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Knight"));
        
        piece[3][2] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Bishop"));
        piece[3][5] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Bishop"));
        
        piece[3][3] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Queen"));
        piece[3][4] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black King"));
        
        piece[0][0] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Rock"));
        piece[0][7] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Rock"));
        
        piece[0][1] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Knight"));
        piece[0][6] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Knight"));
        
        piece[0][2] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Bishop"));
        piece[0][5] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Bishop"));
        
        piece[0][3] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Queen"));
        piece[0][4] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("King"));
        
       // originPiece.setLocalScale(modelScale);
        for(int i = 0; i < piece.length - 2; i ++)
        {
            float up = 0.0f;
            if(i % 2 == 1)
                up = 0.1f;
            for(int j = 0; j < 8 ; j ++)
            {
                piece[i][j].setLocalScale(modelScale);
                piece[i][j].setLocalTranslation(i, up, j);
                dimension[i][j] = new Pair(i, j);
                localNode.attachChild(piece[i][j]);
            }
        }
        
        piece[0][1].setLocalTranslation(0, 0.1f, 1);
        piece[0][6].setLocalTranslation(0, 0.1f, 6);
        for(int i = 2; i < piece.length; i ++)
        {
            float up = 0.0f;
            if(i % 2 == 0)
                up = 0.1f;
            for(int j = 0; j < 8 ; j ++)
            {
                piece[i][j].setLocalScale(modelScale);
                piece[i][j].setLocalTranslation(i + 4, up, j);
                dimension[i][j] = new Pair(i + 4, j);
                localNode.attachChild(piece[i][j]);
            }
        }
        piece[3][1].setLocalTranslation(7, 0.1f, 1);
        piece[3][6].setLocalTranslation(7, 0.1f, 6);
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
                if(s == piece[i][j])
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
                if(s == piece[i][j])
                    return new Vector3i(i, 0, j);
            }
        }
        return null;
    }
    
    public void Move(Vector3i pieceIndex, Vector3i to)
    {

        int x = pieceIndex.x, z = pieceIndex.z;
        float y = piece[x][z].getLocalTranslation().y;
        Vector3f toF = new Vector3f(to.x, y, to.z);
        dimension[x][z] = new Pair(to.x, to.z);
        System.out.println((int)dimension[2][7].getKey() + " " + (int)dimension[2][7].getValue());
        System.out.println((int)dimension[0][6].getKey() + " " + (int)dimension[0][6].getValue());

        MotionPath path = new MotionPath();
        path.addWayPoint(piece[x][z].getLocalTranslation());
        if((x == 0 || x == 3) && (z == 1 || z == 6))
        {
            Vector3f mid = new Vector3f((piece[x][z].getLocalTranslation().x + toF.x) / 2.0f, 1.0f, (piece[x][z].getLocalTranslation().z + toF.z) / 2.0f);
            path.addWayPoint(mid);
        }
        path.addWayPoint(toF);

        MotionEvent motionControl = new MotionEvent(piece[x][z],path);
        //motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
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
        System.out.println("Kill " + p);
        localNode.detachChild(p);
    }
    private boolean isEqual(float x1, float x2)
    {
        float x3 = x1 - x2;
        x3 = Math.abs(x3);

        return x3 < 0.4;
    }
}