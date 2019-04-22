/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maps;

import GamePackage.Cell;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.Light;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author shaks
 */
public abstract class MapCell extends AbstractAppState
{
    protected AssetManager assetManager;
    protected Material mat;
    protected Node cellModel, localNode;
    protected Texture texture;
    
    private final Cell cell;
    private final float cellScale;
    private final Node rootNode;
    private final SpotLight moveLight, attackLight;

    public MapCell(SimpleApplication app, Cell cell, float cellScale) 
    {
        this.cell = cell;
        this.cellScale = cellScale;
        this.attackLight = new SpotLight();
        this.moveLight = new SpotLight();
        this.moveLight.setColor(ColorRGBA.Blue);
        this.attackLight.setColor(ColorRGBA.Red);
        this.assetManager = app.getAssetManager();
        this.rootNode = app.getRootNode();
        this.localNode = new Node();
        this.mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        this.cellModel = (Node)assetManager.loadModel("Models/Maps/DefaultMap/DefaultMap.j3o");
        this.cellModel.setLocalTranslation(cell.getRow(), 0.0f, cell.getColumn());
        this.cellModel.setLocalScale(cellScale);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        super.initialize(stateManager, app);
        
        loadCell();
        setCell();
        setLight();
        localNode.attachChild(cellModel);
        rootNode.attachChild(localNode);
    }
    
    private void setLight()
    {
        moveLight.setSpotRange(10);
        moveLight.setSpotInnerAngle(5*FastMath.DEG_TO_RAD);
        moveLight.setSpotOuterAngle(10*FastMath.DEG_TO_RAD);
        moveLight.setColor(ColorRGBA.Blue.mult(16));
        
        attackLight.setSpotRange(10);
        attackLight.setSpotInnerAngle(5*FastMath.DEG_TO_RAD);
        attackLight.setSpotOuterAngle(10*FastMath.DEG_TO_RAD);
        attackLight.setColor(ColorRGBA.Blue.mult(16));

        moveLight.setEnabled(false);
        attackLight.setEnabled(false);
        
        localNode.addLight(moveLight);
        localNode.addLight(attackLight);

    }
    
    public void setAttackLight()
    {
        attackLight.setPosition(new Vector3f(cell.getRow(), 4.0f, cell.getColumn()));
        attackLight.setDirection(new Vector3f(cell.getRow(), 0.0f, cell.getColumn()).subtract(moveLight.getPosition()));     
        
    }
    
    public void setMoveLight()
    {
        moveLight.setEnabled(true);
        moveLight.setPosition(new Vector3f(cell.getRow(), 4.0f, cell.getColumn()));
        moveLight.setDirection(new Vector3f(cell.getRow(), 0.0f, cell.getColumn()).subtract(moveLight.getPosition()));     
    }
    
    public void removLight()
    {
        moveLight.setEnabled(false);
        attackLight.setEnabled(false);
    }
    
    public Cell getCell()
    {
        return new Cell(cell);
    }
    
    public void setCellPiece(String type)
    {
        cell.setType(type);
    }
    
    public boolean isEqual(Spatial obj)
    {
        return obj == (Spatial)cellModel.getChild("Plane");
    }
    
    private void setCell()
    {
        texture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", texture);
        cellModel.setMaterial(mat);
    }
    protected abstract void loadCell();
   
}
