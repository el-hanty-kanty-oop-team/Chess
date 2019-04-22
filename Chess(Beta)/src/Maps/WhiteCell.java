/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maps;

import GamePackage.Cell;
import com.jme3.app.SimpleApplication;

/**
 *
 * @author shaks
 */
public class WhiteCell extends MapCell
{
    private final float cellScale;

    
    public WhiteCell(SimpleApplication app, Cell cell, float cellScale) 
    {
        super(app, cell, cellScale);
        this.cellScale = cellScale;
    }
    
    @Override
    protected void loadCell() 
    {
        texture = assetManager.loadTexture("Textures/Maps/defaultMap/White.png");
    }    
}
