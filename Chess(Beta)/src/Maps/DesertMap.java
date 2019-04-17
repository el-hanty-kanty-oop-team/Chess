/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maps;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.texture.Texture;

/**
 *
 * @author shaks
 */
public class DesertMap extends Map
{

    public DesertMap(SimpleApplication app)
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
        mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        texture1 = assetManager.loadTexture("Textures/Maps/DesertMap/Mossy_rock_01_2K_Base_Color.png");       
        texture2 = assetManager.loadTexture("Textures/Maps/DesertMap/Mossy_rock_01_2K_Base_Color.png");       
        
        texture1.setWrap(Texture.WrapMode.Repeat);
        texture2.setWrap(Texture.WrapMode.Repeat);
       
        mat1.setTexture("ColorMap", texture1);
        mat2.setTexture("ColorMap", texture2);
    }
    
    
}