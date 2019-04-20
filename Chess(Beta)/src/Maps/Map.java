/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maps;

import Tools.Vector3i;
import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author shaks
 */
public abstract class Map extends AbstractAppState
{
    protected final AssetManager assetManager;
    protected final DirectionalLight dl;
    protected final Node rootNode;
    protected final Node localNode;
    protected final int numOfRows, numOfColumns;
    protected final float mapScale;
    protected Material mat1, mat2;
    protected Node mapModel[][];
    protected Texture texture1, texture2;
    
    private final ViewPort viewPort;
    
    public Map(SimpleApplication app)
    {
        numOfRows = numOfColumns = 8;
        mapScale = 0.062f;
        assetManager = app.getAssetManager();
        rootNode = app.getRootNode();
        viewPort = app.getViewPort();
        
        localNode = new Node();
        mapModel = new Node[numOfRows][numOfColumns];
        dl = new DirectionalLight();
    }
    /*public Vector3f getCellIndex()
    {
        
    }*/
    // arr[8][8] ;  map  arr[0][0] =3skry press 3la el button di [0][0] 
    
    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        //System.out.println("WE GOT HERE 1");
        super.initialize(stateManager, app);
        LoadMap();
        setModel();
        
        viewPort.setBackgroundColor(ColorRGBA.LightGray);
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        
        localNode.addLight(dl);
        rootNode.attachChild(localNode);
  //      System.out.println(mapModel[0][0].getLocalTransform());
    }
    
    private void setModel()
    {
        int x = 0;
        for(int i = 0; i < numOfRows; i ++)
        {
            for(int j = 0; j < numOfColumns; j ++)
            {
                if((j + x) % 2 == 1)
                    mapModel[i][j].setMaterial(mat1);
                else
                    mapModel[i][j].setMaterial(mat2);
            }
            x ++;
        }
        // Cell X = new whiteCell() ;  
        for(int i = 0; i < numOfRows; i ++)
        {
            for(int j = 0; j < numOfColumns; j ++)
            {
                // if (>i && <j ) return vector = (i,0,j)
                mapModel[i][j].setLocalScale(mapScale);
                mapModel[i][j].setLocalTranslation(i, 0, j);
                localNode.attachChild(mapModel[i][j]);
            }
        }
    }
    
    protected void LoadModel()
    {
        for(int i = 0; i < numOfRows; i ++)
            for(int j = 0; j < numOfColumns; j ++)
                mapModel[i][j] = (Node)assetManager.loadModel("Models/Maps/DefaultMap/DefaultMap.j3o");
    }
    
    public Vector3i getCellIndex(Spatial cell)
    {
        for(int i = 0; i < numOfRows; i ++)
        {
            for(int j = 0; j < numOfColumns; j ++)
            {
                if(cell == (Spatial)mapModel[i][j].getChild("Plane"))
                    return new Vector3i(i, 0 ,j);
            }
        }
        return null;
    }
    
    protected abstract void LoadMap();
    protected abstract void LoadTexture();
}