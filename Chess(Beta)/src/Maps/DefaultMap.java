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
        //System.out.println("WE GOT HERE 3");

    }
    
    @Override
    protected void LoadTexture()
    {
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        texture = assetManager.loadTexture("Textures/Maps/defaultMap/Mossy_rock_01_2K_Base_Color.png");       
        
        texture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("ColorMap", texture);      
    }
}
