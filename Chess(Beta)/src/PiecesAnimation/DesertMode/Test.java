/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAnimation.DesertMode;

import PiecesAndAnimation.PiecesAnimation.PieceAnimation;
import PiecesAnimation.DesertMode.Pawn;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author shaks
 */
public class Test extends AbstractAppState
{
    private SimpleApplication app;
    private Node rootNode, localNode;
    private AssetManager assetManager;
    private PieceAnimation piece;
    public Test(SimpleApplication app) 
    {
        this.app = app;
        localNode = new Node();
        rootNode = app.getRootNode();
        assetManager = app.getAssetManager();
    }
    
    public void initialize(AppStateManager stateManager, Application application)
    {
        super.initialize(stateManager, application);
        piece = new Pawn(app, 0, 1, true);
        stateManager.attach(piece);
        localNode.attachChild(assetManager.loadModel("Models/Animations/bishopStand01/bishopStand.j3o"));
        localNode.setLocalTranslation(Vector3f.ZERO);
        rootNode.attachChild(piece.getLocalNode());
    }

    
    
}

