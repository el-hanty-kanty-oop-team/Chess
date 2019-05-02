/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAndAnimation.PiecesAnimation.MagicalMode;

import PiecesAndAnimation.PiecesAnimation.PieceAnimation;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author shaks
 */
public class Castle extends PieceAnimation 
{
    public Castle(SimpleApplication app, int i, int j, boolean good)
    {
        super(app);
        this.good = good;
        playerWalkDirection.set(i, 0, j);
        startPosition.set(playerWalkDirection);
        if(good)
            startPosition.x -= 2.0f;
        else
            startPosition.x += 2.0f;
        
        destination.set(i, 0, j);
        attackIteration = 1;
        rangeAttack = true;
    }
    @Override
    protected void LoadModel()
    {   
        loadAnim();
        loadTexture();
        setChannelsAndControls();
        
        localNode.attachChild(standNode);
        
        attackNode.setLocalScale(modelScale);
        deathNode.setLocalScale(modelScale);
        standNode.setLocalScale(modelScale);
        walkNode.setLocalScale(modelScale);  
        
        
        headText.setText("castle");             // the text
        
    }
    
    @Override
    protected void loadAnim()
    {
        
        attackNode = (Node)assetManager.loadModel("Models/Animations/Magical/castleAttack01/castleAttack.j3o");
        
        deathNode = (Node)assetManager.loadModel("Models/Animations/Magical/castleDeath01/castleDeath.j3o");
        
        standNode = (Node)assetManager.loadModel("Models/Animations/Magical/castleStand01/castleStand.j3o");
        
        walkNode = (Node)assetManager.loadModel("Models/Animations/Magical/castleWalk01/castleWalk.j3o");
    }
    
    @Override
    protected void loadTexture()
    {
        Texture texture = assetManager.loadTexture("Textures/Animations/Magical/castle/castle_diffuse.png");
        if(!good)
            texture = assetManager.loadTexture("Textures/Animations/Magical/castle/castle_diffuse(evil).png");
        texture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("ColorMap", texture);
        
        attackNode.setMaterial(mat);
        deathNode.setMaterial(mat);
        standNode.setMaterial(mat);
        walkNode.setMaterial(mat);
    }
    
    
    
    // G  ---- ++++ ))) 
    
    
    @Override
    protected void setChannelsAndControls()
    {
        attackAnimControl = attackNode.getChild("castleAttack").getControl(AnimControl.class);
        attackCh = attackAnimControl.createChannel();
        attackCh.setAnim("Attack");
        attackCh.setLoopMode(LoopMode.DontLoop);
        attackAnimControl.addListener(this);
        
        deathAnimControl = deathNode.getChild("castleDeath").getControl(AnimControl.class);
        deathCh = deathAnimControl.createChannel();
        deathCh.setAnim("Death");
        deathCh.setLoopMode(LoopMode.DontLoop);
        deathAnimControl.addListener(this);
        
        standAnimControl = standNode.getChild("castleStand").getControl(AnimControl.class);
        standCh = standAnimControl.createChannel();
        standCh.setAnim("Stand");
        standCh.setLoopMode(LoopMode.Loop);
        standAnimControl.addListener(this);
        
        walkAnimControl = walkNode.getChild("castleWalk").getControl(AnimControl.class);
        walkCh = walkAnimControl.createChannel();
        walkCh.setAnim("Walk");
        walkCh.setLoopMode(LoopMode.DontLoop);
        walkAnimControl.addListener(this);
    }
    
    @Override
    public boolean isEquale(Spatial selectedObject)
    {
        return selectedObject == (Spatial)standNode.getChild("castleStand") || selectedObject == (Spatial)standNode.getChild("castleStand_");
    }
    
}
