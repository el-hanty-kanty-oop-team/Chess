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
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        
/*
        Texture west=null,east=null,north=null,south=null,up=null,down=null ;
        
        sky = SkyFactory.createSky(assetManager, west, east, north, south, up, down);

        sky.setQueueBucket(Bucket.Sky);

        sky.setLocalScale(500);

        sky.setCullHint(CullHint.Never);
        this.app.getRootNode().attachChild(sky) ;
        */
        //sky.setQueueBucket(Bucket.Sky);
        //sky.setCullHint(Spatial.CullHint.Never);

        
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
