/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAndAnimation.PiecesAnimation;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author shaks
 */
public abstract class PieceAnimation extends AbstractAppState  implements AnimEventListener 
{
    protected final AssetManager assetManager;
    protected final Node rootNode, localNode;
    protected final Vector3f playerWalkDirection, destination, upVector; 
    protected AnimChannel channel, attackCh, deathCh, standCh, walkCh;
    protected AnimControl control, attackAnimControl, deathAnimControl, standAnimControl, walkAnimControl;
    protected boolean good;
    protected BitmapText headText;
    protected BitmapFont font;
    protected float modelScale;
    protected int attackIteration;
    protected Material mat;
    protected Node attackNode, deathNode, standNode, walkNode;
    protected Vector3f startPosition;
   
    private final AppStateManager stateManager;
    private final Camera cam;
    private final float animSpeed;
    private boolean attack, attackUpdate, death, walk, attackIterationStarted, isMoveDone;
    private float x, z;
    private int numOfIterations;
    
    public PieceAnimation(SimpleApplication app)
    {
        
        animSpeed = 2.0f;
        attack = attackIterationStarted = death = isMoveDone = walk =  false;
        modelScale = 0.5f;
        
        
        assetManager = app.getAssetManager();
        cam = app.getCamera();
        rootNode = app.getRootNode();    
        stateManager = app.getStateManager();
                
        font = assetManager.loadFont("Interface/Fonts/Console.fnt");

        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        attackAnimControl = new AnimControl();
        deathAnimControl = new AnimControl();
        standAnimControl = new AnimControl();
        walkAnimControl = new AnimControl();
        
        localNode = new Node();
        
        destination = new Vector3f(Vector3f.ZERO);
        playerWalkDirection = new Vector3f(Vector3f.ZERO);
        startPosition = new Vector3f(Vector3f.ZERO);
        upVector = new Vector3f(Vector3f.ZERO);
        
        headText = new BitmapText(font, false);

    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);
        
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        
        LoadModel();
        
        headText.setSize(font.getCharSet().getRenderedSize());      // font size
        headText.setColor(ColorRGBA.Blue);                             // font color
        headText.setLocalTranslation(playerWalkDirection); // position
        //localNode.attachChild(headText);
        localNode.setLocalTranslation(playerWalkDirection);
        localNode.addLight(dl);
        rootNode.attachChild(localNode);
                
    }
    
    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) 
    {
        if(animName.equalsIgnoreCase("Walk"))
        {
            if(numOfIterations == 0)
                isMoveDone = true;
            if(attack && numOfIterations <= attackIteration)
            {
                dimensionChanging();
                playerWalkDirection.addLocal(x, 0.0f, z);
                localNode.setLocalTranslation(playerWalkDirection);
                attack();
            }
            else if(numOfIterations == 0)
            {
                stand();
                isMoveDone = true;
            }
            else
            {
                dimensionChanging();
                playerWalkDirection.addLocal(x, 0.0f, z);
                localNode.setLocalTranslation(playerWalkDirection);
                walk();
            }
            x = z = 0.0f;
        }
        else if(animName.equalsIgnoreCase("Attack"))
        {
            attackIterationStarted = false;
            if(numOfIterations == 0)
            {
                stand();
                isMoveDone = true;
            }
            else
                walk();
        }
        else if(animName.equals("Death"))
        {
            if(good)
            {
                startPosition.set(startPosition.z, 1.0f, startPosition.x + 12);
                localNode.lookAt(startPosition, upVector);
                localNode.setLocalTranslation(startPosition);
            }
            else
            {
                startPosition.set(startPosition.z , 1.0f, startPosition.x - 12);
                localNode.lookAt(startPosition, upVector);
                localNode.setLocalTranslation(startPosition);
            }
        }
    }
    
    @Override   
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) 
    {
      // unused
    }

  /** Custom Keybinding: Map named actions to inputs. */
    
    
    public void update(Vector3f newDirection, String str)
    {
        destination.set(newDirection);
        numOfIterations = (int) max(abs(playerWalkDirection.x - destination.x), abs(playerWalkDirection.z - destination.z));
        if(numOfIterations > 0)
        {
            if(str.equalsIgnoreCase("attack"))
                attack = attackUpdate = true;
            else if(str.equalsIgnoreCase("death"))
                death = true;
            else if(str.equalsIgnoreCase("walk"))
                walk = true; 
        }
    }
    
    
    public float max(float x, float y)
    {
        if(x > y)
            return x;
        return y;
    }
    
    public float abs(float x)
    {
        if(x < 0)
            return -1 * x;
        return x;
    }
  
    @Override
    public void update(float tpf)
    {
        if(walk)
            walk();
        if(attack && attackUpdate)
        {
            if(numOfIterations > attackIteration)
                walk();
            else if(numOfIterations == attackIteration)
                attack();
            attackUpdate = false;
        }
    }
  
    
    public boolean attackIterationStarted()
    {
        return attackIterationStarted;
    }
    
    
    public boolean isMoveDone()
    {
        boolean done = isMoveDone;
        isMoveDone = false;
        return done;
    }
    
    public Vector3f getLocalTranslation()
    {
        return localNode.getLocalTranslation();
    }
    
    public Node getLocalNode()
    {
        return localNode;
    }
    
    public void setLocalTranslation(Vector3f v)
    {
        destination.set(v);
        localNode.setLocalTranslation(v);
    }
            
    public void die()
    {
        death();
    }
    
    private void attack()
    {
        attackIterationStarted = true;
        attack = false;
        if(!localNode.hasChild(attackNode))
        {
            localNode.detachAllChildren();
            localNode.attachChild(attackNode);
        }
        localNode.lookAt(destination, upVector);
        attackCh.reset(true);
        attackCh.setAnim("Attack");
        attackCh.setLoopMode(LoopMode.DontLoop);
        attackCh.setSpeed(animSpeed);
    }
    
    private void death()
    {
        if(!localNode.hasChild(deathNode))
        {
            localNode.detachAllChildren();
            localNode.attachChild(deathNode);
        }
        deathCh.reset(true);
        deathCh.setAnim("Death");
        deathCh.setLoopMode(LoopMode.DontLoop);
        deathCh.setSpeed(animSpeed);
    }
    
    private void stand()
    {
        dimensionChanging();
        localNode.lookAt(destination, upVector);
        playerWalkDirection.addLocal(x, 0.0f, z);
        localNode.setLocalTranslation(playerWalkDirection);
        localNode.detachAllChildren();
        localNode.attachChild(standNode);
        standCh.setLoopMode(LoopMode.Loop);
        standCh.setSpeed(1.0f * animSpeed);
        x = z = 0.0f;
    }
    
    private void walk()
    {
        walk = false;
        numOfIterations--;
        if(!localNode.hasChild(walkNode))
        {
            localNode.detachAllChildren();
            localNode.attachChild(walkNode);
        }
        localNode.lookAt(destination, upVector);
        walkCh.reset(true);
        walkCh.setAnim("Walk");
        walkCh.setLoopMode(LoopMode.DontLoop);
        walkCh.setSpeed(animSpeed);
    }
    
    private void dimensionChanging()
    {
        float scale = 1.0f;
        
        if(destination.x > playerWalkDirection.x)
            x = 1.0f * scale;
        else if(destination.x < playerWalkDirection.x)
            x = -1.0f* scale;
        else
            x = 0.0f * scale;

        if(destination.z > playerWalkDirection.z)
            z = 1.0f * scale;
        else if(destination.z < playerWalkDirection.z)
            z = -1.0f * scale;
        else
            z = 0.0f * scale;   
    }
   
    public abstract boolean isEquale(Spatial selectedObject);
    protected abstract void LoadModel();
    protected abstract void loadAnim();
    protected abstract void loadTexture();
    protected abstract void setChannelsAndControls();
    
}
