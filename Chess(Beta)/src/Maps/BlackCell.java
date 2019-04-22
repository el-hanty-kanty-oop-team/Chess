package Maps;

import GamePackage.Cell;
import com.jme3.app.SimpleApplication;

/**
 *
 * @author shaks
 */
public class BlackCell extends MapCell
{
    private final float cellScale;
    
    public BlackCell(SimpleApplication app, Cell cell, float cellScale) 
    {
        super(app, cell, cellScale);
        this.cellScale = cellScale;
    }
    
    
    @Override
    protected void loadCell() 
    {
        texture = assetManager.loadTexture("Textures/Maps/defaultMap/Black.png");
    }    
}
