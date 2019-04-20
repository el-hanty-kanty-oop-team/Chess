/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maps;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;

/**
 *
 * @author shaks
 */
public class DefaultMap extends Map
{

    public DefaultMap(SimpleApplication app)
    {
        super(app);
    }
  
    
    @Override
    protected void LoadMap()
    {
        LoadModel();
        LoadTexture();    
    }
    
    @Override
    protected void LoadTexture()
    {
        mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        texture1 = assetManager.loadTexture("Textures/Maps/defaultMap/White.png");       
        texture2 = assetManager.loadTexture("Textures/Maps/defaultMap/Black.png");
        
        texture1.setWrap(Texture.WrapMode.Repeat);
        texture2.setWrap(Texture.WrapMode.Repeat);
        
        mat1.setTexture("ColorMap", texture1);
        mat2.setTexture("ColorMap", texture2);
    }
}
