/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAndAnimation.Skills;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.Cinematic;
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
    private static float skillSpeed = 10.f;
    private static boolean skillDone = false;
    
    public static boolean SkillDone()
    {
        boolean ok = skillDone;
        skillDone = false;
        return ok;
    }
    
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
        fire.setLocalTranslation(from);
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
                skillDone = true;
            }
        };
        motionControl.setSpeed(skillSpeed * 2.0f);
        motionControl.play();
    }

    public static void fireCircle(Vector3f from, Vector3f to, final Node rootNode, AssetManager assetManager)
    {
        float shift = 0.1f;
        float x = to.x, z = to.z;
        from.y = to.y = 0.7f;
        check(from, to, shift);
        
        final ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 300);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
        fire.setLocalTranslation(from);
        fire.setMaterial(mat_red);
        fire.setImagesX(15);
        fire.setImagesY(1); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.5f, 0));
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(1f);
        fire.getParticleInfluencer().setVelocityVariation(1f);
        rootNode.attachChild(fire);
        
        
        MotionPath path = new MotionPath();
        path.addWayPoint(from);
        path.addWayPoint(to);
        
        circlePath(path, 50, 1.0f, x, z);
        
        MotionEvent motionControl = new MotionEvent(fire, path)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire);
                skillDone = true;
            }
        };
        motionControl.setSpeed(skillSpeed);
        motionControl.play();
    } 
    
    public static void fireWave(Vector3f from, Vector3f to, final Node rootNode, AssetManager assetManager)
    {
        float shift = 0.1f;
        float x = to.x, z = to.z;
        from.y = to.y = 0.7f;
        check(from, to, shift);
        final ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 300);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/shockwave.png"));
        fire.setLocalTranslation(from);
        fire.setMaterial(mat_red);
        fire.setImagesX(1);
        fire.setImagesY(1); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.5f, 0));
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(1f);
        fire.getParticleInfluencer().setVelocityVariation(1f);
        rootNode.attachChild(fire);
        final ParticleEmitter fire2 = fire.clone();
        
        MotionPath path1 = new MotionPath(), path2 = new MotionPath();
        path1.addWayPoint(from);
        path1.addWayPoint(to);
 
        circlePath(path2, 50, 0.5f, x, z);
        
        final MotionEvent motionControl2 = new MotionEvent(fire2, path2)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire2);
            }
        };
        MotionEvent motionControl1 = new MotionEvent(fire, path1)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire);
                rootNode.attachChild(fire2);
                motionControl2.play();
                skillDone = true;
            }
        };
        
        motionControl1.setSpeed(skillSpeed);
        motionControl2.setSpeed(skillSpeed * 1.5f);
        motionControl1.play();        
    }
    
    public static void fireWind(Vector3f from, Vector3f to, final Node rootNode, AssetManager assetManager)
    {
        float shift = 0.1f;
        float x = to.x, z = to.z;
        from.y = to.y = 0.7f;
        check(from, to, shift);
        final ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 300);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/Debris.png"));
        fire.setLocalTranslation(from);
        fire.setMaterial(mat_red);
        fire.setImagesX(3);
        fire.setImagesY(3); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(221, 221, 221, 0.2f));   // red
        fire.setStartColor(new ColorRGBA(204, 255, 255, 0.2f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.5f, 0));
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(1f);
        fire.getParticleInfluencer().setVelocityVariation(1f);
        rootNode.attachChild(fire);
        final ParticleEmitter fire2 = fire.clone();
        
        MotionPath path1 = new MotionPath(), path2 = new MotionPath();
        path1.addWayPoint(from);
        path1.addWayPoint(to);
 
        circlePath(path2, 50, 0.5f, x, z);
        
        final MotionEvent motionControl2 = new MotionEvent(fire2, path2)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire2);
            }
        };
        MotionEvent motionControl1 = new MotionEvent(fire, path1)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire);
                rootNode.attachChild(fire2);
                motionControl2.play();
                skillDone = true;
            }
        };
        
        motionControl1.setSpeed(skillSpeed);
        motionControl2.setSpeed(skillSpeed * 1.5f);
        motionControl1.play();        
    }
    
    public static void water(Vector3f from, Vector3f to, final Node rootNode, AssetManager assetManager)
    {
        float shift = 0.1f;
        from.y = to.y = 0.7f;
        check(from, to, shift);
        
        final ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 300);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        fire.setLocalTranslation(from);
        fire.setMaterial(mat_red);
        fire.setImagesX(2);
        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(213, 100, 1, 0.5f));   // red
        fire.setStartColor(new ColorRGBA(213, 100, 1, 0.5f));
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
                skillDone = true;
            }
        };
        motionControl.setSpeed(skillSpeed * 2.0f);
        motionControl.play();
    }

    public static void waterCircle(Vector3f from, Vector3f to, final Node rootNode, AssetManager assetManager)
    {
        float shift = 0.1f;
        float x = to.x, z = to.z;
        from.y = to.y = 0.7f;
        check(from, to, shift);
        
        final ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 300);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
        fire.setLocalTranslation(from);
        fire.setMaterial(mat_red);
        fire.setImagesX(15);
        fire.setImagesY(1); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(213, 100, 1, 0.5f)); 
        fire.setStartColor(new ColorRGBA(213, 100, 1, 0.5f));
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.5f, 0));
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(1f);
        fire.getParticleInfluencer().setVelocityVariation(1f);
        rootNode.attachChild(fire);
        
        
        MotionPath path = new MotionPath();
        path.addWayPoint(from);
        path.addWayPoint(to);
        
        circlePath(path, 50, 1.0f, x, z);
        
        MotionEvent motionControl = new MotionEvent(fire, path)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire);
                skillDone = true;
            }
        };
        motionControl.setSpeed(skillSpeed);
        motionControl.play();
    } 
    
    public static void waterWave(Vector3f from, Vector3f to, final Node rootNode, AssetManager assetManager)
    {
        float shift = 0.1f;
        float x = to.x, z = to.z;
        from.y = to.y = 0.7f;
        check(from, to, shift);
        final ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 300);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/shockwave.png"));
        fire.setLocalTranslation(from);
        fire.setMaterial(mat_red);
        fire.setImagesX(1);
        fire.setImagesY(1); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(213, 100, 1, 0.5f));
        fire.setStartColor(new ColorRGBA(213, 100, 1, 0.5f));
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.5f, 0));
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(1f);
        fire.getParticleInfluencer().setVelocityVariation(1f);
        rootNode.attachChild(fire);
        final ParticleEmitter fire2 = fire.clone();
        
        MotionPath path1 = new MotionPath(), path2 = new MotionPath();
        path1.addWayPoint(from);
        path1.addWayPoint(to);
 
        circlePath(path2, 50, 0.5f, x, z);
        
        final MotionEvent motionControl2 = new MotionEvent(fire2, path2)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire2);
            }
        };
        MotionEvent motionControl1 = new MotionEvent(fire, path1)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire);
                rootNode.attachChild(fire2);
                motionControl2.play();
                skillDone = true;
            }
        };
        
        motionControl1.setSpeed(skillSpeed);
        motionControl2.setSpeed(skillSpeed * 1.5f);
        motionControl1.play();        
    }
    
    public static void waterWind(Vector3f from, Vector3f to, final Node rootNode, AssetManager assetManager)
    {
        float shift = 0.1f;
        float x = to.x, z = to.z;
        from.y = to.y = 0.7f;
        check(from, to, shift);
        final ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 300);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/Debris.png"));
        fire.setLocalTranslation(from);
        fire.setMaterial(mat_red);
        fire.setImagesX(3);
        fire.setImagesY(3); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(213, 100, 1, 0.5f));
        fire.setStartColor(new ColorRGBA(213, 100, 1, 0.5f));
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.5f, 0));
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(1f);
        fire.getParticleInfluencer().setVelocityVariation(1f);
        rootNode.attachChild(fire);
        final ParticleEmitter fire2 = fire.clone();
        
        MotionPath path1 = new MotionPath(), path2 = new MotionPath();
        path1.addWayPoint(from);
        path1.addWayPoint(to);
 
        circlePath(path2, 50, 0.5f, x, z);
        
        final MotionEvent motionControl2 = new MotionEvent(fire2, path2)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire2);
            }
        };
        MotionEvent motionControl1 = new MotionEvent(fire, path1)
        {
            @Override
            public void onStop()
            {
                rootNode.detachChild(fire);
                rootNode.attachChild(fire2);
                motionControl2.play();
                skillDone = true;
            }
        };
        
        motionControl1.setSpeed(skillSpeed);
        motionControl2.setSpeed(skillSpeed * 1.5f);
        motionControl1.play();        
    }
    
    private static void circlePath(MotionPath path, int points, float rad, float x, float z)
    {
        float angle = (float) (2 * Math.PI / points);
        for(int i = 0; i < points; i ++)
        {
            float pX = (float)(x + rad * Math.cos(angle * i));
            float pZ = (float)(z + rad * Math.sin(angle * i));
            path.addWayPoint(new Vector3f(pX, 0.7f, pZ));
        }
    }
}
