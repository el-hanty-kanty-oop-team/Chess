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
public class Knight extends PieceAnimation
{
    
    private boolean good;
    public Knight(SimpleApplication app, int i, int j, boolean good)
    {
        super(app);
        this.good = good;
        playerWalkDirection.set(i, 0, j);
        startPosition.set(playerWalkDirection);
        if(good)
            startPosition.x -= 2.0f;
        else
            startPosition.x += 2.0f;
        modelScale = 1.5f;
        destination.set(i, 0, j);
        attackIteration = 1;
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
        
        
        headText.setText("Knight");             // the text
        
    }
    
    @Override
    protected void loadAnim()
    {
        
        attackNode = (Node)assetManager.loadModel("Models/Animations/Magical/knightAttack01/knightAttack.j3o");
        
        deathNode = (Node)assetManager.loadModel("Models/Animations/Magical/knightDeath01/knightDeath.j3o");
        
        standNode = (Node)assetManager.loadModel("Models/Animations/Magical/knightStand01/knightStand.j3o");
        
        walkNode = (Node)assetManager.loadModel("Models/Animations/Magical/knightWalk01/knightWalk.j3o");
    }
    
    @Override
    protected void loadTexture()
    {
        Texture texture = assetManager.loadTexture("Textures/Animations/knight/knight_diffuse.png");
        if(!good)
            texture = assetManager.loadTexture("Textures/Animations/knight/knight_diffuse(evil).png");
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
        attackAnimControl = attackNode.getChild("knightAttack").getControl(AnimControl.class);
        attackCh = attackAnimControl.createChannel();
        attackCh.setAnim("Attack");
        attackCh.setLoopMode(LoopMode.DontLoop);
        attackAnimControl.addListener(this);
        
        deathAnimControl = deathNode.getChild("knightDeath").getControl(AnimControl.class);
        deathCh = deathAnimControl.createChannel();
        deathCh.setAnim("Death");
        deathCh.setLoopMode(LoopMode.DontLoop);
        deathAnimControl.addListener(this);
        
        standAnimControl = standNode.getChild("knightStand").getControl(AnimControl.class);
        standCh = standAnimControl.createChannel();
        standCh.setAnim("Stand");
        standCh.setLoopMode(LoopMode.Loop);
        standAnimControl.addListener(this);
        
        walkAnimControl = walkNode.getChild("knightWalk").getControl(AnimControl.class);
        walkCh = walkAnimControl.createChannel();
        walkCh.setAnim("Walk");
        walkCh.setLoopMode(LoopMode.DontLoop);
        walkAnimControl.addListener(this);
    }
    
    @Override
    public boolean isEquale(Spatial selectedObject)
    {
        return selectedObject == (Spatial)standNode.getChild("knightStand");
    }
}
