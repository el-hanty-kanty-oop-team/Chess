/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maps;

import GamePackage.Cell;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

/**
 *
 * @author shaks
 */
public class DefaultMap extends Map
{
    Spatial sky;
    private final SimpleApplication app;
    public DefaultMap(SimpleApplication app)
    {
        super(app);
        this.app = app;

        Texture west = app.getAssetManager().loadTexture("Textures/Sky/Lagoon/lagoon_west.jpg"),
        east = app.getAssetManager().loadTexture("Textures/Sky/Lagoon/lagoon_east.jpg"),
        north = app.getAssetManager().loadTexture("Textures/Sky/Lagoon/lagoon_north.jpg"),
        south = app.getAssetManager().loadTexture("Textures/Sky/Lagoon/lagoon_south.jpg"),
        up = app.getAssetManager().loadTexture("Textures/Sky/Lagoon/lagoon_up.jpg"),
        down = app.getAssetManager().loadTexture("Textures/Sky/Lagoon/lagoon_down.jpg");
        
        sky = SkyFactory.createSky(app.getAssetManager(), west, east, north, south, up, down);

        sky.setQueueBucket(Bucket.Sky);

        sky.setLocalScale(500);

        sky.setCullHint(CullHint.Never);
        this.app.getRootNode().attachChild(sky) ;
        
        sky.setQueueBucket(Bucket.Sky);
        sky.setCullHint(Spatial.CullHint.Never);

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {        
        super.initialize(stateManager, app);
        int x = 0;
        for(int i = 0; i < numOfRows; i ++)
        {
            for(int j = 0; j < numOfColumns; j ++)
            {
                if((j + x) % 2 == 0)
                    cell[i][j] = new WhiteCell(this.app, new Cell(i, j, ""), mapScale);
                else
                    cell[i][j] = new BlackCell(this.app, new Cell(i, j, ""), mapScale);
                stateManager.attach(cell[i][j]);
            }
            x ++;
        }
        this.app.getViewPort().setBackgroundColor(ColorRGBA.LightGray);
    }
}
