/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAndAnimation.PiecesAnimation;

import PiecesAndAnimation.Skills.Skill;
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
import com.jme3.font.Rectangle;
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
    protected boolean good, rangeAttack, stopMovingAfterAttack;
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
        
        // font color
        if(good)
            headText.setColor(ColorRGBA.Blue);
        else
            headText.setColor(ColorRGBA.Red);

        headText.setSize(0.2f);      // font size
        headText.setLocalTranslation(0f, headText.getHeight() + 1.2f ,-0.01f); // position
        //Rectangle rect = new Rectangle(0, 0, 1, 1);
       // headText.setBox(rect);
       // headText.setAlignment(BitmapFont.Align.Center);
        
        localNode.attachChild(headText);
        localNode.setLocalTranslation(playerWalkDirection);
        localNode.addLight(dl);
        rootNode.attachChild(localNode);
                
    }
    
    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) 
    {
        if(animName.equalsIgnoreCase("Walk") || animName.equalsIgnoreCase("WalkDig"))
        {
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
            if(numOfIterations == 0 || stopMovingAfterAttack)
            {
                stand();
            }
            else
                walk();
        }
        else if(animName.equals("Death"))
        {
            if(good)
            {
                startPosition.set(startPosition.z, 1.0f, startPosition.x + 10);
                localNode.lookAt(startPosition, upVector);
                localNode.setLocalTranslation(startPosition);
            }
            else
            {
                startPosition.set(startPosition.z , 1.0f, startPosition.x - 10);
                localNode.lookAt(startPosition, upVector);
                localNode.setLocalTranslation(startPosition);
            }
        }
    }
    
    @Override   
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) 
    {
      // unused
        if(animName.equalsIgnoreCase("Stand"))
        {
            isMoveDone = true;
            localNode.attachChild(headText);
        }
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
        
        headText.lookAt(new Vector3f(cam.getLocation().x, 0, cam.getLocation().z), upVector);
        if(walk)
            walk();
        if(attack && attackUpdate)
        {
            attackUpdate = false;
            if(numOfIterations == attackIteration || rangeAttack)
                attack();
            else if(numOfIterations > attackIteration)
                walk();
            
        }
    }
  
    
    public boolean attackIterationStarted()
    {
        boolean ok;
        if(rangeAttack)
            ok = Skill.SkillDone();
        else
            ok = attackIterationStarted;
        attackIterationStarted = false;
        return ok;
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
    
    public Vector3f getPlayerWalkDirection()
    {
        return playerWalkDirection;
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
        
        if(rangeAttack)
        {
            skill();
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
        localNode.lookAt(destination, upVector);
        playerWalkDirection.set(destination);
        localNode.setLocalTranslation(playerWalkDirection);
        localNode.detachAllChildren();
        localNode.attachChild(standNode);
        standCh.reset(true);
        standCh.setAnim("Stand");
        standCh.setLoopMode(LoopMode.Loop);
        standCh.setSpeed(animSpeed);
        x = z = 0.0f;
    }
    
    private void walk()
    {
        walk = false;
        numOfIterations--;
        localNode.lookAt(destination, upVector);
        dimensionChanging();
        localNode.detachAllChildren();
        walkCh.reset(true);
        
        if(x == 0 || z == 0)
            walkCh.setAnim("Walk", 0.0f);
        else
            walkCh.setAnim("WalkDig", 0.0f);
        x = z = 0;
        
        walkCh.setLoopMode(LoopMode.DontLoop);
        walkCh.setSpeed(animSpeed);

        localNode.attachChild(walkNode);
        
    }
    
    private void skill()
    {
        if(good)
        {
            switch((int)startPosition.z)
            {
                case 0:
                case 7:
                    Skill.waterWind(new Vector3f(localNode.getLocalTranslation()), new Vector3f(destination), rootNode, assetManager);
                    break;
             
                case 1:
                case 6:
                    Skill.waterCircle(new Vector3f(localNode.getLocalTranslation()), new Vector3f(destination), rootNode, assetManager);
                    break;
             
                case 2:
                case 5:
                    Skill.water(new Vector3f(localNode.getLocalTranslation()), new Vector3f(destination), rootNode, assetManager);
                    break;
                
                case 3:
                    Skill.waterWave(new Vector3f(localNode.getLocalTranslation()), new Vector3f(destination), rootNode, assetManager);
                    break;
                
                default:
                    break;
            }
        }
        else
        {
            switch((int)startPosition.z) 
            {
                case 0:
                case 7:
                    Skill.fireWind(new Vector3f(localNode.getLocalTranslation()), new Vector3f(destination), rootNode, assetManager);
                    break;
             
                case 1:
                case 6:
                    Skill.fireCircle(new Vector3f(localNode.getLocalTranslation()), new Vector3f(destination), rootNode, assetManager);
                    break;
             
                case 2:
                case 5:
                    Skill.fire(new Vector3f(localNode.getLocalTranslation()), new Vector3f(destination), rootNode, assetManager);
                    break;
                
                case 3:
                    Skill.fireWave(new Vector3f(localNode.getLocalTranslation()), new Vector3f(destination), rootNode, assetManager);
                    break;
                
                default:
                    break;
            }
        }
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
