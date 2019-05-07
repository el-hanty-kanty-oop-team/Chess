/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GamePackage;

import Maps.DefaultMap;
import Maps.Map;
import PiecesAndAnimation.PiecesBehaviors;
import PiecesAndAnimation.PiecesFactory;
import PlayerPackage.AI;
import PlayerPackage.Player;
import Tools.Vector3i;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.collision.CollisionResults;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.sql.Time;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 * Class Controls the game
 * @author shaks
 */
public class Chess extends AbstractAppState
{
    private Camera cam;
    private FlyByCamera flyCam;
    private InputManager inputManager;
    private Node rootNode;
    private AppStateManager stateManager;
    
    private Cell specialMove;
    private Map defaultMap; 
    private Pair currentSelected, lastSelected, lastSelectedPiece; 
    private PiecesBehaviors piecesTypeSelected;
    private boolean firstPlayer = true, moveDone = false, promotionDone = false, checkPromotion, engineMove = false, ok = false, updateCalledEngine = false, AI = true, isPromotion, userChoiceDone, loadIsDone = false;
    private final Vector3f camLocation1 = new Vector3f(-6.5f, 8.0f, 3.5f), camLocation2 = new Vector3f(13.5f, 8.0f, 3.5f), camDirection = new Vector3f(3.5f, 0.0f, 3.5f);
    private final Player p1 = new Player("hahaha", Color.White) ;
    private final Player p2 = new Player("hehehee", Color.Black) ;
    private final AI ai1 = new AI(Color.White), ai2 = new AI(Color.Black);
    private final Game game = new Game(GameMode.Multiplayer, ai1, ai2);
    private AudioNode audioCheck, soundTraack;
    private SimpleApplication app;
    private SpotLight spot;
    private DirectionalLight dl;
    private String pieceString;
    private int win = 0, depth = 0, userChoice;
   
    private final ActionListener actionListener = new ActionListener() 
    {
       
        /**
         * A func called auto when mapped action peformerd to handel user input
         * @param name mapping Name
         * @param keypressed false means pressed, true released
         * @param tpf time per frame, we dont use it but needed to override
         */
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) 
        {
            if(name.equals("FlyByTheCam"))
            {
                if(!keyPressed)
                {   
                    flyCam.setEnabled(false);
                    inputManager.setCursorVisible(true);
                }
                else
                {
                    flyCam.setEnabled(true); 
                    inputManager.setCursorVisible(false);
                }
            }
            else if (name.equals("pick target") && !keyPressed && ((AI && !engineMove) || (!AI)) ) 
            {
                if(currentSelected != null && currentSelected.getValue().toString().equalsIgnoreCase("Piece") && ((firstPlayer && ((Vector3i)currentSelected.getKey()).x < 2) || (!firstPlayer && ((Vector3i)currentSelected.getKey()).x > 1)))
                    lastSelected = currentSelected;

                CollisionResults results = new CollisionResults();
                // Convert screen click to 3d position
                Vector2f click2d = inputManager.getCursorPosition(); // get mouse position on the screen
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone(); // converting mouse position to game world position
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                // Aim the ray from the clicked spot forwards.
                Ray ray = new Ray(click3d, dir);
                // Collect intersections between ray and all nodes in results list.
                rootNode.collideWith(ray, results);
                Vector3i dimention = null;

                if(results.size() > 0)
                {
                    Node selectedModel = results.getCollision(0).getGeometry().getParent();
                    dimention = piecesTypeSelected.getPieceIndex(selectedModel);                   

                    if(dimention != null && ((firstPlayer && dimention.x < 2 ) || (!firstPlayer && dimention.x > 1 )))
                    {
                        currentSelected = new Pair(new Vector3i(dimention), "Piece");
                        defaultMap.removeBlueHighlights();
                        HighlightAvailableMoves(dimention.getX(), dimention.getZ());
                    }
                    else if(dimention != null)
                    {
                        dimention = piecesTypeSelected.getPieceDimension(selectedModel);
                        currentSelected = new Pair(new Vector3i(dimention), "Map");
                    }
                    else
                    {
                         dimention = defaultMap.getCellIndex(selectedModel);

                        if(dimention != null)
                            currentSelected = new Pair(new Vector3i(dimention), "Map");
                    }

                    System.out.println(currentSelected + " " + lastSelected);
                }
            }
           
        }
    };
    
    /**
     * 
     * @param app SimppleApplication that runs the game
     * @param piecesString type of pieces (WhiteAndBlackoriginal, zombiePieces, magicalPieces)
     * @param AI true vs Comp, false MultiPlayer
     * @param depth depth of AI diffculty
     */
    public Chess(SimpleApplication app, String piecesString, boolean AI, int depth)
    {
        this.app = app;
        this.pieceString = piecesString;
        this.AI = AI;
        this.depth = depth;
        
        cam = app.getCamera();
        flyCam = app.getFlyByCamera();
        inputManager = app.getInputManager();
        rootNode = app.getRootNode();
        stateManager = app.getStateManager();
    }

    /**
     * initialize the gamme
     * @param stateManager
     * @param app 
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) 
    {
        super.initialize(stateManager, app);
        initModels();
        initKeys();
        initAudio();
        setCam();
        addLight();
        stateManager.attach(defaultMap);
        stateManager.attach((AppState)piecesTypeSelected);
        synchronized(this)
        {
            loadIsDone = true;
            notify();
        }
    }  
    
    /**
     * releaseing the memory when the game over
     */
    public void detach()
    {
        soundTraack.stop();
        piecesTypeSelected.detach();
        rootNode.removeLight(spot);
        rootNode.removeLight(dl);
        app.getViewPort().setBackgroundColor(ColorRGBA.Black);
        app.getViewPort().clearProcessors();
        app.getViewPort().setClearFlags(true, true, true);
        stateManager.cleanup();
        stateManager.detach(defaultMap);
        stateManager.detach((AppState)piecesTypeSelected);
        app.restart();
    }
    
    /**
     * initializing audio
     */
    private void initAudio()
    {
        audioCheck = new AudioNode(app.getAssetManager(), "Sounds/kingCheck.ogg");
        soundTraack = new AudioNode(app.getAssetManager(), "Sounds/TwoStepsFromHellVictory.ogg");
        audioCheck.setLooping(true);
        audioCheck.setPositional(false);
        audioCheck.setVolume(100);
        
        soundTraack.setLooping(true);
        soundTraack.setPositional(false);
        soundTraack.setVolume(0.2f);
        soundTraack.play();
    }
    
    /**
     * initializing selected pieces and the seleceted board
     */
    private void initModels()
    {
        defaultMap = new DefaultMap(app);
        piecesTypeSelected = PiecesFactory.GetPiecesType(app, pieceString);   
        currentSelected = null;
        lastSelected = null;
    }

    /**
     * initilizing map keys and buttons triggers
     */
    private void initKeys()
    {
        inputManager.addMapping("FlyByTheCam", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "FlyByTheCam");
        inputManager.addListener(actionListener, "pick target");
    }
    
    /** 
     * setting cam location for first time
     */
    private void setCam()
    {
        flyCam.setMoveSpeed(20);
        flyCam.setEnabled(false);
        cam.setLocation(camLocation1);
        inputManager.setCursorVisible(true);
        cam.lookAt(camDirection, Vector3f.ZERO);
    }
    
    /**
     * lighting the game
     */
    private void addLight()
    {
        // for "unshaded" material
        dl = new DirectionalLight();
        dl.setDirection(new Vector3f(3.5f, 0.0f, 3.5f));
        rootNode.addLight(dl);
        
        // for "lighting" material "Doesn't affect unshadedMaterial"
        Vector3f lightTarget = new Vector3f(12, 3.5f, 30);
        spot=new SpotLight();
        spot.setSpotRange(1000);
        spot.setSpotInnerAngle(5*FastMath.DEG_TO_RAD);
        spot.setSpotOuterAngle(10*FastMath.DEG_TO_RAD);
        spot.setPosition(new Vector3f(3.5f, 45.0f, 3.5f));
        spot.setDirection(new Vector3f(3.5f, 0.0f, 3.5f).subtract(spot.getPosition()));     
        spot.setColor(ColorRGBA.White.mult(1));
        rootNode.addLight(spot);
    }
    
    /**
     * func called every single second auto from the engine
     * update pieces and checking game state if needed
     * update the camera
     * calls AI if seleceted
     * @param tpf time per frame (don't need it but mandatory for overriding)
     */
    @Override
    public void update(float tpf) 
    {
        if(isUpdatePiecesVaild())
        {
            updatePieces(); // user Interaction
        } 
        
        updateCam();
        if(AI && !updateCalledEngine)
        {
            Engine();
        }
        
        if(lastSelectedPiece != null && (!AI || (AI && (int)lastSelectedPiece.getKey() < 2)))
        {
            int i = (int)lastSelectedPiece.getKey(), j = (int)lastSelectedPiece.getValue();
            if(userChoiceDone)
            {
                int type = 3;
                userChoiceDone = false;
                type = userChoice();
                Vector3i to = piecesTypeSelected.getPieceDimension(i, j);
                Cell toC = new Cell(to.x, to.z);
                game.makePromotion(toC, type);
                piecesTypeSelected.promote(i, j, type);
                checkPromotion = false  ;
            }
        }
    }
    
    /**
     * @return 1 if white wins, -1 if black wins, 2 if draw, 0 otherwise  
     */
    public int isGameDone()
    {
        return win;
    }
    
    /**
     * update pieces position, attack and kill pieces
     * and check for gamae state (white wins, black wins, draw)
     * and run chec audio if king checked
     */
    private void updatePieces()
    {
        
        Vector3i from = (Vector3i)lastSelected.getKey();
        Vector3i to = (Vector3i)currentSelected.getKey();
        String type = (String)lastSelected.getValue();
        lastSelectedPiece = new Pair(from.x, from.z);
        Cell fromC = new Cell(piecesTypeSelected.getPieceDimension(from.x, from.z).x, piecesTypeSelected.getPieceDimension(from.x, from.z).z), toC = new Cell(to.x, to.z);
        defaultMap.removeBlueHighlights();
        defaultMap.removeRedHighlights();

        if(specialMove != null && specialMove.getRow() == toC.getRow() && specialMove.getColumn() == toC.getColumn())
        {
            game.update(fromC, specialMove);
            if(piecesTypeSelected.getSelectedPieceType(from.x, from.z).equalsIgnoreCase("pawn"))
                piecesTypeSelected.enPassant(from, to);
            else if(piecesTypeSelected.getSelectedPieceType(from.x, from.z).equalsIgnoreCase("king"))
            {
                piecesTypeSelected.castling(from, to);
                for(int i = 0; i < 8; i ++)
                {
                    for(int j = 0; j < 8; j ++)
                    {
                        if(game.board.pieces[i][j] == null)
                        {
                            System.out.print("NUll ");
                        }
                        else
                        {
                            System.out.print(game.board.pieces[i][j].getClass().toString() + " ");
                        }
                    }
                    System.out.println("");
                }
            }
        }
        else
        {
            game.update(fromC, toC);
            piecesTypeSelected.Move(from, to);
        }

        engineMove = !engineMove;
        if(((String)lastSelected.getValue()).equalsIgnoreCase("Engine"))
        {
            updateCalledEngine = false;
        }

        if(game.isCheckmated(Color.Black))
        {
            win = -1;
        }
        else if(game.isCheckmated(Color.White))
        {
            win = 1;
        }
        else if(game.draw(Color.Black) || game.draw(Color.White))
        {
            win = 2;
        }

        to = piecesTypeSelected.getPieceDimension(0, 4);
        toC = new Cell(to.x, to.z);

        if(!game.board.whiteKing.check_my_king(toC, toC, game.board.whiteKing, game.board.pieces))
        {
            audioCheck.playInstance();
            defaultMap.highLightCell(toC, "Attack");
        }

        to = piecesTypeSelected.getPieceDimension(3, 4);
        toC = new Cell(to.x, to.z);

        if(!game.board.blackKing.check_my_king(toC, toC, game.board.blackKing, game.board.pieces))
        {
            audioCheck.playInstance();
            defaultMap.highLightCell(toC, "Attack");
        }

        currentSelected = null;
        lastSelected = null;
        specialMove = null;

    }  
    
    /**
     * Highlight All possible Moves for current selected piece
     * @param i index of current selected piece
     * @param j index of current selected piece
     */
    private void HighlightAvailableMoves(int i, int j)
    {
        if ((currentSelected != null  && ((String)currentSelected.getValue()).equalsIgnoreCase("Piece")) || (lastSelected != null && ((String)lastSelected.getValue()).equalsIgnoreCase("engine")))
        {
            
            Vector3i dimension = piecesTypeSelected.getPieceDimension(i, j);
            ArrayList<Cell> list = game.board.pieces[dimension.x][dimension.z].possible_moves(new Cell(dimension.x, dimension.z), game.board);
  
            for(Cell c : list)
            {
                if (c.special_move != 0)
                {
                    specialMove = c;
                }
                
                defaultMap.highLightCell(c, "Move");
            }   
        }
    }
    
    /**
     * handling user "Weird" clicks
     * @return true if user clicks are valid 
     */
    private boolean isUpdatePiecesVaild()
    {
        if(lastSelected == null || currentSelected == null)
            return false;
        
        Vector3i toI = (Vector3i)currentSelected.getKey();
        Cell to = new Cell(toI.x, toI.z);
        //System.out.println(to.getRow() + " " + to.getColumn() + " " +defaultMap.isCellHighlighted(to));
        int x = ((Vector3i)lastSelected.getKey()).x, z = ((Vector3i)lastSelected.getKey()).z;
        
        return (!currentSelected.equals(lastSelected) && ((String)lastSelected.getValue()).equalsIgnoreCase("Piece") && ((String)currentSelected.getValue()).equalsIgnoreCase("Map") 
            && !(piecesTypeSelected.getPieceDimension(x, z).x == ((Vector3i)currentSelected.getKey()).x && piecesTypeSelected.getPieceDimension(x, z).z == ((Vector3i)currentSelected.getKey()).z)
            &&  defaultMap.isCellHighlighted(to) && ((firstPlayer && x < 2) || (!firstPlayer && x > 1))) || (engineMove && ((String)lastSelected.getValue()).equalsIgnoreCase("Engine"));
    }
    
    /**
     * engine plays if its engine move annd current player's move was done
     */
    private void Engine()
    {
       if(engineMove && ok)
       {
            updateCalledEngine = true;
            SingleMove sm = ai2.root(depth, true, game.board);
            Cell from = sm.getFrom(), to = sm.getTo();
            System.out.println("Engine Move");
            
            System.out.println(from.getRow() + " " + from.getColumn()  + " to " +  to.getRow() +  " " + to.getColumn());
            if(from.getRow() == -1 || from.getColumn() == -1 || to.getRow() == -1 || to.getColumn() == -1)
            {
                System.out.print("Out of Moves");
            }
            else
            {
                System.out.println("color " + game.board.pieces[from.getRow()][from.getColumn()].getColor());
                int r = piecesTypeSelected.getPieceIndex(from.getRow(), from.getColumn()).x, c = piecesTypeSelected.getPieceIndex(from.getRow(), from.getColumn()).z;
                lastSelected = new Pair(new Vector3i(r, 0, c), "Engine");
                currentSelected = new Pair(new Vector3i(to.getRow(), 0, to.getColumn()), "Map");
                HighlightAvailableMoves(r, c);
            }
       }
    }
    
    /**
     * update the cam with each move
     */
    private void updateCam()
    {
        // using OR gatae as "simpleUpdate" is being called every single Second xD  
        moveDone |= piecesTypeSelected.isMoveDone();
        
        if(moveDone)
        {
            int i = (int)lastSelectedPiece.getKey(), j = (int)lastSelectedPiece.getValue();
            checkPromotion |= piecesTypeSelected.checkPromotion(i, j);
            isPromotion = checkPromotion;
            if(checkPromotion && AI && i > 1)
            {
                int type = 3;
                Vector3i to = piecesTypeSelected.getPieceDimension(i, j);
                Cell toC = new Cell(to.x, to.z);
                game.makePromotion(toC, type);
                piecesTypeSelected.promote(i, j, type);
                checkPromotion = false  ;
            }
            promotionDone = true;
        }
        
        if(moveDone && promotionDone)
        {   
            ok = !ok;
            firstPlayer = !firstPlayer;
            if(firstPlayer)
                cam.setLocation(camLocation1);
            else if(!AI)
                cam.setLocation(camLocation2);
        
            moveDone = promotionDone = false;
          //  System.out.println("Move is done");
        }
        cam.lookAt(camDirection, Vector3f.ZERO);  
    }
    
    public boolean isPromotion()
    {
        boolean check = isPromotion;
        isPromotion = false;
        return check;
    }
    
    /**
     * get user pormotion choice
     * @param userChoice 0 -> rock, 1 -> bishop, 2 -> knight, 3 -> queen
     */
    public void userChoice(int userChoice)
    {
        this.userChoice = userChoice;
        synchronized(this)
        {
            userChoiceDone = true;
            notify();
        }
        
    }
    
    /**
     * 
     * @return user poromotion choice 
     */
    public int userChoice()
    {
        return userChoice;
    }
}
