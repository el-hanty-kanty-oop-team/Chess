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
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.AnimationEvent;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
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
public abstract class PieceAnimation extends AbstractAppState  implements AnimEventListener 
{
    protected final AssetManager assetManager;
    protected final InputManager inputManager;
    protected final Node rootNode, localNode;
    protected final Vector3f playerWalkDirection, destination, upVector; 
    protected AnimChannel channel, attackCh, deathCh, standCh, walkCh;
    protected AnimControl control, attackAnimControl, deathAnimControl, standAnimControl, walkAnimControl;
    protected BitmapText headText;
    protected BitmapFont font;
    protected float modelScale;
    protected int attackIteration;
    protected Material mat;
    protected Node attackNode, deathNode, standNode, walkNode;
    protected Vector3f startPosition;
   
    private final ActionListener actionListener;
    private final AppStateManager stateManager;
    private final Camera cam;
    private final float animSpeed;
    private boolean attack, death, walk;
    private float x, z;
    private int numOfIterations;
    
    public PieceAnimation(SimpleApplication app)
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
                        channel.setAnim("walk", 0.50f);
                        channel.setLoopMode(LoopMode.DontLoop);
                    }
                }
                else if (name.equals("pick target")) 
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
        animSpeed = 2.0f;
        attack = death = walk = false;
        modelScale = 0.5f;
        
        
        assetManager = app.getAssetManager();
        cam = app.getCamera();
        inputManager = app.getInputManager();
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
        initKeys();
        
        headText.setSize(font.getCharSet().getRenderedSize());      // font size
        headText.setColor(ColorRGBA.Blue);                             // font color
        headText.setLocalTranslation(playerWalkDirection); // position
        localNode.attachChild(headText);
        localNode.setLocalTranslation(playerWalkDirection);
        localNode.addLight(dl);
        rootNode.attachChild(localNode);
                
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
    
    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) 
    {
        
        if(animName.equalsIgnoreCase("Walk"))
        {
            if(numOfIterations == 0)
            {
                System.out.println("Stand");
                stand();
            }
            else
            {
                //walkCh.reset(true);
                dimensionChanging();
                playerWalkDirection.addLocal(x, 0.0f, z);
                localNode.setLocalTranslation(playerWalkDirection);
                System.out.println("Walk has been done");
                walk();
                x = z = 0.0f;
            }
            x = z = 0.0f;
        }
        else if(animName.equalsIgnoreCase("Stand"))
        {
        }
  /*    
        
        //
        playerWalkDirection.addLocal(x, 0.f, z);
        localNode.setLocalTranslation(playerWalkDirection);  
*/
        //System.out.println("anim cycle has been done" + playerWalkDirection + " " + numOfIterations);
     
        /*else 
        {
            numOfIterations--;
            if(animName.equals("Walk"))
                walk = true;
        }
        if(death)
        {
            playerWalkDirection.set(startPosition);
        }*/
    }
    
    @Override   
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) 
    {
      // unused
        System.out.println("PiecesAndAnimation.PiecesAnimation.PieceAnimation.onAnimChange()");
    }

  /** Custom Keybinding: Map named actions to inputs. */
    protected void initKeys() 
    {
        inputManager.addMapping("pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "pick target");
    }
    
    public void update(Vector3f newDirection, String str)
    {
        destination.set(newDirection);
        numOfIterations = (int) max(abs(playerWalkDirection.x - destination.x), abs(playerWalkDirection.z - destination.z));
        if(numOfIterations > 0)
        {
           // walk();
            if(str.equalsIgnoreCase("attack"))
                attack = true;
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
    }
    
    private void attack()
    {
        attack = false;
        if(!localNode.hasChild(attackNode))
        {
            localNode.detachChildAt(0);
            localNode.attachChild(attackNode);
        }
        attackCh.setLoopMode(LoopMode.Loop);   
        attackCh.setSpeed(1.0f * animSpeed);
    }
    
    private void death()
    {
        if(!localNode.hasChild(deathNode))
        {
            localNode.detachChildAt(0);
            localNode.attachChild(deathNode);
        }
        deathCh.setLoopMode(LoopMode.DontLoop);
        deathCh.setSpeed(1.0f * animSpeed);
       
    }
    
    private void stand()
    {
        dimensionChanging();
        playerWalkDirection.addLocal(x, 0.0f, z);
        localNode.setLocalTranslation(playerWalkDirection);
        localNode.detachAllChildren();
        localNode.attachChild(standNode);
        localNode.lookAt(destination, upVector);
        standCh.setLoopMode(LoopMode.Loop);
        standCh.setSpeed(1.0f * animSpeed);
        x = z = 0.0f;
    }
    
    private void walk()
    {
        numOfIterations--;
        //float speed = distance(localNode.getLocalTranslation(), destination);
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
        walk = false;
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
    
    private float distance(Vector3f v1, Vector3f v2)
    {
        float x, y, z;
  
        x = v1.x - v2.x;
        y = v1.y - v2.y;
        z = v1.z - v2.z;
        
        x *= x;
        y *= y;
        z *= z;
     
        return (float)Math.sqrt(x + y + z);
    }
    public abstract boolean isEquale(Spatial selectedObject);
   // public abstract void vaildPositions();
    
    protected abstract void LoadModel();
    protected abstract void loadAnim();
    protected abstract void loadTexture();
    protected abstract void setChannelsAndControls();
    
}
