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
public class Pawn extends PieceAnimation 
{
    public Pawn(SimpleApplication app, int i, int j, boolean good)
    {
        super(app);
        this.good = good;
        playerWalkDirection.set(i, 0, j);
        startPosition.set(playerWalkDirection);
        attackIteration = 1;
        modelScale = 0.8f;
        destination.set(i, 0, j);
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
        
        
        headText.setText("Pawn");             // the text
        
    }
    
    @Override
    protected void loadAnim()
    {
        
        attackNode = (Node)assetManager.loadModel("Models/Animations/Magical/pawnAttack01/pawnAttack.j3o");
        
        deathNode = (Node)assetManager.loadModel("Models/Animations/Magical/pawnDeath01/pawnDeath.j3o");
        
        standNode = (Node)assetManager.loadModel("Models/Animations/Magical/pawnStand01/pawnStand.j3o");
        
        walkNode = (Node)assetManager.loadModel("Models/Animations/Magical/pawnWalk01/pawnWalk.j3o");
    }
    
    @Override
    protected void loadTexture()
    {
        Texture texture = assetManager.loadTexture("Textures/Animations/Magical/pawn/pawn_diffuse.png");
        if(!good)
            texture = assetManager.loadTexture("Textures/Animations/Magical/pawn/pawn_diffuse(evil).png");
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
        attackAnimControl = attackNode.getChild("pawnAttack").getControl(AnimControl.class);
        attackCh = attackAnimControl.createChannel();
        attackCh.setAnim("Attack");
        attackCh.setLoopMode(LoopMode.Loop);
        attackAnimControl.addListener(this);
        
        deathAnimControl = deathNode.getChild("pawnDeath").getControl(AnimControl.class);
        deathCh = deathAnimControl.createChannel();
        deathCh.setAnim("Death");
        deathCh.setLoopMode(LoopMode.DontLoop);
        deathAnimControl.addListener(this);
        
        standAnimControl = standNode.getChild("pawnStand").getControl(AnimControl.class);
        standCh = standAnimControl.createChannel();
        standCh.setAnim("Stand");
        standCh.setLoopMode(LoopMode.Loop);
        standAnimControl.addListener(this);
        
        walkAnimControl = walkNode.getChild("pawnWalk").getControl(AnimControl.class);
        walkCh = walkAnimControl.createChannel();
        walkCh.setAnim("Walk");
        walkCh.setLoopMode(LoopMode.DontLoop);
        walkAnimControl.addListener(this);
    }
    
    @Override
    public boolean isEquale(Spatial selectedObject)
    {
        boolean ok = selectedObject == (Spatial)standNode.getChild("pawnStand");
        for(int i= 0; i < 4; i ++)
            ok |= (selectedObject == (Spatial)standNode.getChild("pawnStand_" + String.valueOf(i)));
        return ok;
    }
    
}
