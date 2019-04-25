/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maps;

import GamePackage.Cell;
import Tools.Vector3i;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;

/**
 *
 * @author shaks
 */
public abstract class Map extends AbstractAppState
{
    protected final int numOfRows, numOfColumns;
    protected final float mapScale;
    protected MapCell cell[][];
    
    
    public Map(SimpleApplication app)
    {
        numOfRows = numOfColumns = 8;
        mapScale = 0.062f;
        cell = new MapCell[numOfRows][numOfColumns];
        Node floor = (Node)app.getAssetManager().loadModel("Models/Maps/map.j3o");
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        Texture text = app.getAssetManager().loadTexture("Textures/Maps/desertMap/Mossy_rock_01_2K_Base_Color.png");
        text.setWrap(Texture.WrapMode.Repeat);
        floor.setLocalTranslation(3.5f, 0.0f, 3.5f);
        mat.setTexture("DiffuseMap", text);
        floor.setMaterial(mat);
        floor.setLocalScale(0.62f);
        Node deathFloor1 = floor.clone(true);
        deathFloor1.setLocalTranslation(3.5f, 1.0f, 10.5f);
        deathFloor1.setLocalScale(0.62f, 0.62f, 0.2f);
        Node deathFloor2 = deathFloor1.clone(true);
        deathFloor2.setLocalTranslation(3.5f, 1.0f, -3.5f);

        app.getRootNode().attachChild(deathFloor1);
        app.getRootNode().attachChild(deathFloor2);
        app.getRootNode().attachChild(floor);
    }
    
    public Vector3i getCellIndex(Node cell)
    {
        for(int i = 0; i < numOfRows; i ++)
        {
            for(int j = 0; j < numOfColumns; j ++)
            {
                if(this.cell[i][j].isEqual(cell))
                    return new Vector3i(i, 0 ,j);
            }
        }
        return null;
    }
    
    public void highLightCell(Cell cell, String type)
    {
        int i = cell.getRow(), j = cell.getColumn();
        if(type.equalsIgnoreCase("Move"))
            this.cell[i][j].setMoveLight();
        else
            this.cell[i][j].setAttackLight();           
    }
    
    public boolean isCellHighlighted(Cell cell)
    {
        int i = cell.getRow(), j = cell.getColumn();
        return this.cell[i][j].isHighLighted();
    }
    
    public void removeHighlights()
    {
        for(int i = 0; i < numOfRows; i ++)
        {
            for(int j = 0; j < numOfColumns; j ++)
            {
                cell[i][j].removLight();
            }
        }
    }
    
}