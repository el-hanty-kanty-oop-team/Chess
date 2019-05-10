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
import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * controls and integrates animation 
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
    private final InputManager inputManager;
    private AudioNode audioWalk, audioDie;
    private final Camera cam;
    private final float animSpeed;
    private boolean attack, attackUpdate, death, walk, attackIterationStarted, isMoveDone, isText;
    private float x, z;
    private int numOfIterations;
    private ActionListener actionListener;
    
    
    
    /**
     * Initilaizing fileds
     * @param app SimppleApplication that runs the game
     */
    public PieceAnimation(SimpleApplication app)
    {
        
        actionListener = new ActionListener() 
        {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) 
            {
                if(name.equalsIgnoreCase("ShowNames") && !isPressed)
                {
                    if(isText)
                    {
                        if(localNode.hasChild(headText))
                        {   
                            localNode.detachChild(headText);
                        }
                        
                        isText = false;
                    }    
                    else if(!isText)
                    {
                        if(!localNode.hasChild(headText))
                        {
                            localNode.attachChild(headText);
                        }
                     
                        isText = true; 
                    }
                }
            }
        };
        
        
        animSpeed = 2.0f;
        attack = attackIterationStarted = death = isMoveDone = walk =  false;
        isText =  true;
        modelScale = 0.5f;
        
        
        assetManager = app.getAssetManager();
        inputManager = app.getInputManager();
        cam = app.getCamera();
        rootNode = app.getRootNode();    
        stateManager = app.getStateManager();
                
        font = assetManager.loadFont("Interface/Fonts/Console.fnt");

        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        audioWalk = new AudioNode(app.getAssetManager(), "Sounds/walk.ogg");
        audioDie = new AudioNode(app.getAssetManager(), "Sounds/die.ogg");
        
        audioDie.setPositional(false);
        audioDie.setLooping(true);
        audioDie.setVolume(100);
        
        audioWalk.setPositional(false);
        audioWalk.setLooping(true);
        audioWalk.setVolume(100);
        
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
    
    /**
     * loading animation and setting head to text inner text and color 
     * @param stateManager
     * @param app 
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);
        
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        
        LoadModel();
        initKeys();
        // font color
        if(good)
            headText.setColor(ColorRGBA.Blue);
        else
            headText.setColor(ColorRGBA.Red);

        headText.setSize(0.2f);      // font size
        headText.setLocalTranslation(0f, headText.getHeight() + 1.2f ,0); // position
        
        localNode.attachChild(headText);
        localNode.setLocalTranslation(playerWalkDirection);
        localNode.addLight(dl);
        rootNode.attachChild(localNode);
    }
    
    /**
     * A func called auto whene animaiton is done 
     * settnig animation insteractions when a specifi anim is done
     * @param control 
     * @param channel channel that controls the animation (run/stop)
     * @param animName animation name
     */
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
            {
                walk();
            }
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
    
    /**
     * intitilaizing keys
     */
    private void initKeys()
    {
        inputManager.addMapping("ShowNames", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "ShowNames");
    }
    /**
     * A func called auto whene animaiton is setted 
     * settnig animation insteractions when a specific anim is setted
     * @param control 
     * @param channel channel that controls the animation (run/stop)
     * @param animName animation name
     */
    @Override   
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) 
    {
      // unused
        if(animName.equalsIgnoreCase("Stand"))
        {
            isMoveDone = true;
            if(isText && !localNode.hasChild(headText))
                localNode.attachChild(headText);
        }
        else if(animName.equalsIgnoreCase("Walk") || animName.equalsIgnoreCase("WalkDig"))
        {
            audioWalk.playInstance();
        }
        else if(animName.equalsIgnoreCase("Death"))
        {
            audioDie.playInstance();
        }
    }

    /**
     * getting number of moves need to reach the destination
     * @param newDirection destination of current node
     * @param str type of move
     */
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
    
    /**
     * @return maximum value 
     */
    public float max(float x, float y)
    {
        if(x > y)
            return x;
        return y;
    }
    
    /**
     * 
     * @return absoluate value 
     */
    public float abs(float x)
    {
        if(x < 0)
            return -1 * x;
        return x;
    }
  
    /**
     * changing head to text position if user changed the camera and checking for current anim statue
     * called auto from SimpleAppApplication
     * @param tpf time per frame
     */
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
  
    
    /**
     * 
     * @return true if attack animaton started, false otherwise 
     */
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
    
    /**
     * 
     * @return true if current model reached the destination 
     */
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
            x = 0.0f;

        if(destination.z > playerWalkDirection.z)
            z = 1.0f * scale;
        else if(destination.z < playerWalkDirection.z)
            z = -1.0f * scale;
        else
            z = 0.0f;   
    }
   
    public abstract boolean isEquale(Spatial selectedObject);
    protected abstract void LoadModel();
    protected abstract void loadAnim();
    protected abstract void loadTexture();
    protected abstract void setChannelsAndControls();
    
}
