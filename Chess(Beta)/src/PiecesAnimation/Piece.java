/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAnimation;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author shaks
 */
public abstract class Piece extends AbstractAppState  implements AnimEventListener 
{
    protected final AssetManager assetManager;
    protected final float modelScale;
    protected final InputManager inputManager;
    protected final Node rootNode, localNode;
    protected final Vector3f playerWalkDirection, destination, upVector; 
    protected AnimChannel channel, attackCh, deathCh, standCh, walkCh;
    protected AnimControl control, attackAnimControl, deathAnimControl, standAnimControl, walkAnimControl;
    protected BitmapText headText;
    protected BitmapFont font;
    protected int attackIteration;
    protected Material mat;
    protected Node attackNode, deathNode, standNode, walkNode;
    protected Vector3f startPosition;
    
   
    private final ActionListener actionListener;
    private final AnalogListener analogListener;
    private final Camera cam;
    private final float animSpeed;
    private boolean attack, death, walk;
    private float x, z;
    private int numOfIterations;
    
    public Piece(SimpleApplication app)
    {
        
        
        actionListener = new ActionListener()    
        {
            @Override
            public void onAction(String name, boolean keyPressed, float tpf) 
            {
                if (name.equals("Walk") && !keyPressed) 
                {
                    if (channel.getAnimationName().equals("walk")) 
                    {
                        //System.out.println("HERE 3");

                        channel.setAnim("walk", 0.50f);
                        channel.setLoopMode(LoopMode.DontLoop);
                    }
                }
            }
        };
        
        analogListener = new AnalogListener() 
        {
            @Override
            public void onAnalog(String name, float intensity, float tpf) 
            {
                if (name.equals("pick target")) 
                {
                    CollisionResults results = new CollisionResults();      // Reset results list.
                    // Convert screen click to 3d position
                    Vector2f click2d = inputManager.getCursorPosition();    
                    Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                    Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                    
                    Ray ray = new Ray(click3d, dir);                        // Aim the ray from the clicked spot forwards.
                    rootNode.collideWith(ray, results);                     // Collect intersections between ray and all nodes in results list.
                    for (int i = 0; i < results.size(); i++)                // (Print the results so we see what is going on:)
                    {
                        Spatial selectedModel = results.getClosestCollision().getGeometry().getParent();
                    }

                }
            }
        };
        
        modelScale = 0.5f;
        attack = death = walk = false;
        animSpeed = 2.0f;
        
        
        assetManager = app.getAssetManager();
        rootNode = app.getRootNode();    
        inputManager = app.getInputManager();
        cam = app.getCamera();
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
        
        //System.out.println("HERE 0");
        
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);
        
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        
        LoadModel();
        initKeys();
        
        headText.setSize(font.getCharSet().getRenderedSize());      // font size
        headText.setColor(ColorRGBA.Blue);                             // font color
        headText.setLocalTranslation(playerWalkDirection); // position
        localNode.attachChild(headText);
        localNode.setLocalTranslation(playerWalkDirection);
        localNode.addLight(dl);
        rootNode.attachChild(localNode);
        
        
        
        //System.out.println("HERE 1");
    }
    
    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) 
    {
        playerWalkDirection.addLocal(x, 0.f, z);
        numOfIterations = (int) max(abs(playerWalkDirection.x - destination.x), abs(playerWalkDirection.z - destination.z));
     //   System.out.println("anim cycle has been done" + playerWalkDirection + " " + numOfIterations);
        if(numOfIterations == 0)
        {
            localNode.detachAllChildren();
            localNode.attachChild(standNode);
            localNode.lookAt(localNode.getLocalTranslation(), upVector);
            standCh.setLoopMode(LoopMode.Loop);
        }
        if(death)
        {
            playerWalkDirection.set(startPosition);
        }
        localNode.setLocalTranslation(playerWalkDirection);  
    }
    
    @Override   
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) 
    {
      // unused
        System.out.println("HERE 5");
        
    }

  /** Custom Keybinding: Map named actions to inputs. */
    protected void initKeys() 
    {
      inputManager.addMapping("pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
      
      inputManager.addListener(actionListener, "Walk");
      inputManager.addListener(analogListener, "pick target");
    }
    
    public void update(Vector3f newDirection, String str)
    {
        
       // System.out.println("update 1");
        
        destination.set(newDirection);
        
        if(str.equalsIgnoreCase("attack"))
            attack = true;
        else if(str.equalsIgnoreCase("death"))
            death = true;
        else if(str.equalsIgnoreCase("walk"))
            walk = true;
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
        headText.setLocalTranslation(playerWalkDirection); // position

        numOfIterations = (int) max(abs(playerWalkDirection.x - destination.x), abs(playerWalkDirection.z - destination.z));
        x = 0.0f;
        z = 0.0f;
        if(destination.x != playerWalkDirection.x)
        {
            if(destination.x > playerWalkDirection.x)
                x = 1.0f;
            else 
                x = -1.0f;
        }
        if(destination.z != playerWalkDirection.z)
        {
            if(destination.z > playerWalkDirection.z)
                z = 1.0f;
            else 
                z = -1.0f;      
        }
        
        if(attack && numOfIterations == attackIteration)
        {
            if(!localNode.hasChild(attackNode))
            {
                localNode.detachChildAt(0);
                localNode.attachChild(attackNode);
            }
            attackCh.setLoopMode(LoopMode.Loop);   
            attackCh.setSpeed(1.0f * animSpeed);
        }
        else if((walk || attack) && numOfIterations != attackIteration)
        {
         //   System.out.println("Walk " + x + " "  + z);
            if(!localNode.hasChild(walkNode))
            {  
                localNode.detachChildAt(0);
                localNode.attachChild(walkNode);
            }
            localNode.lookAt(destination, upVector);
            walkCh.setLoopMode(LoopMode.Loop);
            walkCh.setSpeed(1.0f * animSpeed);
           
        }
        else if(death)
        {
            if(!localNode.hasChild(deathNode))
            {
                localNode.detachChildAt(0);
                localNode.attachChild(deathNode);
            }
            deathCh.setLoopMode(LoopMode.DontLoop);
            deathCh.setSpeed(1.0f * animSpeed);
        }
        
        if(numOfIterations == 0)
        {
            attack =  walk = false;
        }
    }
    
    
    public abstract boolean isEquale(Spatial selectedObject);
   // public abstract void vaildPositions();
    
    protected abstract void LoadModel();
    protected abstract void loadAnim();
    protected abstract void loadTexture();
    protected abstract void setChannelsAndControls();
    
}
