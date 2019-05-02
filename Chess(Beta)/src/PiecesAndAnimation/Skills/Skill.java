/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAndAnimation.Skills;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author shaks
 */
public class Skill 
{
    public static void check(Vector3f from, Vector3f to,float shift)
    {
        if(from.x != to.x && from.z != to.z)
        {
            if(Math.abs(from.z + shift - to.z) < Math.abs(from.z - shift - to.z))
            {
                from.z += shift;
                to.z += shift;
            }
            else 
            {
                from.z -= shift;
                to.z -= shift;
            }
            
            if(Math.abs(from.x + shift - to.x) < Math.abs(from.x - shift - to.x))
            {
                from.x += shift;
                to.x += shift;
            }
            else 
            {
                from.x -= shift;
                to.x -= shift;
            }
        }
        else if(from.z != to.z)
        {
            if(Math.abs(from.z + shift - to.z) < Math.abs(from.z - shift - to.z))
            {
                from.z += shift;
                to.z += shift;
            }
            else 
            {
                from.z -= shift;
                to.z -= shift;
            }
        }
        else if(from.x != to.x)
        {
            if(Math.abs(from.x + shift - to.x) < Math.abs(from.x - shift - to.x))
            {
                from.x += shift;
                to.x += shift;
            }
            else 
            {
                from.x -= shift;
                to.x -= shift;
            }
        }
    }
    
    public static void fire(Vector3f from, Vector3f to, final Node rootNode, AssetManager assetManager)
    {
        float shift = 0.1f;
        from.y = to.y = 0.7f;
        check(from, to, shift);
        final ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 300);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        fire.setMaterial(mat_red);
        fire.setImagesX(2);
        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(1.5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(3f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        rootNode.attachChild(fire);
        MotionPath path = new MotionPath();
        path.addWayPoint(from);
        path.addWayPoint(to);
        MotionEvent motionControl = new MotionEvent(fire, path)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire);
            }
        };
        motionControl.setSpeed(25.0f);
        motionControl.play();
    }

    public static void water(Vector3f from, Vector3f to, final Node rootNode, AssetManager assetManager)
    {
        float shift = 0.1f;
        from.y = to.y = 0.7f;
        check(from, to, shift);
        final ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 300);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        fire.setMaterial(mat_red);
        fire.setImagesX(15);
        fire.setImagesY(1); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(100, 149, 237, 1));   // Cornflower Blue	
        fire.setStartColor(new ColorRGBA(135, 206,  250, 1)); // Lightsky Blue	
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(1.5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(3f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        rootNode.attachChild(fire);
        MotionPath path = new MotionPath();
        path.addWayPoint(from);
        path.addWayPoint(to);
        MotionEvent motionControl = new MotionEvent(fire, path)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire);
            }
        };
        motionControl.setSpeed(25.0f);
        motionControl.play();
    }    
}
