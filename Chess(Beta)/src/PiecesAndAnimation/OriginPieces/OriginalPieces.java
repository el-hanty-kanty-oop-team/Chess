package PiecesAndAnimation.OriginPieces;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author shaks
 */
public class OriginalPieces extends AbstractOriginalPieces
{
    private Pair<Integer,Integer> p;
    public OriginalPieces(SimpleApplication app) 
    {
        map = new HashMap<>();
        modelScale = 0.15f;
        localNode = new Node();
        assetManager = app.getAssetManager();
        rootNode = app.getRootNode();
        piece = new Node[4][8];
        dimension = new Pair[4][8];
        killed = new boolean[4][8];
    }
   
    private void setMAp (String s ,int i ,int j)
    {
        p = new Pair<>(i,j); 
        map.put(p, s) ;           
    }
    
   
    private void load_Characters_Models()
    {


        for(int j = 0; j < 8; j ++)
        {
            piece[1][j] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("White Pawn"));
            setMAp( "White Pawn", 1, j) ;

        }
        for(int j = 0; j < 8; j ++)
        {
            piece[2][j] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Pawn"));
            setMAp( "Black Pawn", 2, j) ;
        }
        piece[3][0] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Rock"));
        piece[3][7] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Rock"));
        setMAp("Black Rock", 3, 0);
        setMAp("Black Rock", 3, 7);
        
        piece[3][1] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Knight"));
        piece[3][6] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Knight"));
        setMAp("Black Knight", 3, 1);
        setMAp("Black Knight", 3, 6);

        piece[3][2] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Bishop"));
        piece[3][5] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Bishop"));
        setMAp("Black Bishop", 3, 2);
        setMAp("Black Bishop", 3, 5);

        piece[3][3] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black Queen"));
        piece[3][4] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Black King"));
        setMAp("Black Queen", 3, 3);
        setMAp("Black King", 3, 4);

        piece[0][0] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Rock"));
        piece[0][7] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Rock"));
        setMAp("White Rock", 0, 0);
        setMAp("White Rock", 0, 7);

        piece[0][1] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Knight"));
        piece[0][6] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Knight"));
        setMAp("White Knight", 0, 1);
        setMAp("White Knight", 0, 6);

        piece[0][2] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Bishop"));
        piece[0][5] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Bishop"));
        setMAp("White Bishop", 0, 2);
        setMAp("White Bishop", 0, 5);

        piece[0][3] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("Queen"));
        piece[0][4] = (Node)(((Node)assetManager.loadModel("Models/OriginPieces/OriginPieces.j3o")).getChild("King"));
        setMAp("White Queen", 0, 3);
        setMAp("White King", 0, 4);   
    }


    private void SetWhitePiecesPosition()
    {
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
        // white knights
        piece[0][1].setLocalTranslation(0, 0.1f, 1);
        piece[0][6].setLocalTranslation(0, 0.1f, 6);
    }

    private void SetBlackPiecesPosition()
    {
        for(int i = 2; i < piece.length; i ++)
        {
            float up = 0.0f;
            if(i % 2 == 0)
                up = 0.1f;
            for(int j = 0; j < 8 ; j ++)
            {
                piece[i][j].setLocalScale(modelScale);
                piece[i][j].setLocalRotation(new Quaternion().fromAngles(0, -90, 0));
                piece[i][j].setLocalTranslation(i + 4, up, j);
                dimension[i][j] = new Pair(i + 4, j);
                localNode.attachChild(piece[i][j]);
            }
        }
        // black knights
        piece[3][1].setLocalTranslation(7, 0.1f, 1);
        piece[3][6].setLocalTranslation(7, 0.1f, 6);
    }

    private void AddLight()
    {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        localNode.addLight(sun);
        rootNode.attachChild(localNode);
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app)
    { 
        // as a template method :V hahahahaaa ;
        super.initialize(stateManager, app);
        load_Characters_Models() ;
        // originPiece.setLocalScale(modelScale);
        // setting white pieces positions
        SetWhitePiecesPosition() ;
        // setting black pieces positions
        SetBlackPiecesPosition() ;
        // set light and attach with RootNode
        AddLight() ;
    }
}