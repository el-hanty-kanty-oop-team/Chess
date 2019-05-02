
package mygame;

import GamePackage.*;
import PlayerPackage.*;
import Maps.DefaultMap;
import Maps.Map;
import PiecesAndAnimation.PiecesBehaviors;
import PiecesAndAnimation.PiecesFactory;
import Tools.Vector3i;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.collision.CollisionResults;
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
import com.jme3.scene.Node;
import java.time.Clock;
import java.util.ArrayList;
import javafx.util.Pair;


/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication
{
    private Cell specialMove;
    private Map defaultMap; 
    private Pair currentSelected, lastSelected, lastSelectedPiece; 
    private PiecesBehaviors piecesTypeSelected;
    private boolean firstPlayer = true, moveDone = false, promotionDone = false, checkPromotion, engineMove = false, ok = false, updateCalledEngine = false, AI = true;
    private final Vector3f camLocation1 = new Vector3f(-6.5f, 8.0f, 3.5f), camLocation2 = new Vector3f(13.5f, 8.0f, 3.5f), camDirection = new Vector3f(3.5f, 0.0f, 3.5f);
    private final Player p1 = new Player("hahaha", Color.White) ;
    private final Player p2 = new Player("hehehee", Color.Black) ;
    private final AI ai1 = new AI(Color.White), ai2 = new AI(Color.Black);
    private final Game game = new Game(GameMode.Multiplayer, ai1, ai2);

    private final ActionListener actionListener = new ActionListener() 
    {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) 
        {
            if(name.equals("FlyByTheCam"))
            {
                if(!keyPressed)
                {   
                    updateCam();
                    flyCam.setEnabled(false);
                    inputManager.setCursorVisible(true);
                }
                else
                {
                    flyCam.setEnabled(true); 
                    inputManager.setCursorVisible(false);
                }
            }
            else if (name.equals("pick target") && !keyPressed) 
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
                        defaultMap.removeHighlights();
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
    
    
    public static void main(String[] args) 
    {
        Main app = new Main();      
        app.start();
    }

    @Override
    public void simpleInitApp() 
    {
        AI = false;
        initModels();
        initKeys();
        setCam();
        addLight();
        stateManager.attach(defaultMap);
        stateManager.attach((AppState)piecesTypeSelected); 
    }  
    
    public void initModels()
    {
        defaultMap = new DefaultMap(this);
        piecesTypeSelected = PiecesFactory.GetPiecesType(this, "magicalPieces");
        
        currentSelected = null;
        lastSelected = null;
    }

    public void initKeys()
    {
        inputManager.addMapping("FlyByTheCam", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "FlyByTheCam");
        inputManager.addListener(actionListener, "pick target");
    }
    
      
    // setting cam location for first time
    private void setCam()
    {
        flyCam.setMoveSpeed(20);
        cam.setLocation(camLocation1);
        inputManager.setCursorVisible(false);
        cam.lookAt(camDirection, Vector3f.ZERO);
    }
    
    // lighting the game
    private void addLight()
    {
        // for "unshaded" material
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(3.5f, 0.0f, 3.5f));
        rootNode.addLight(dl);
        
        // for "lighting" material "Doesn't affect unshadedMaterial"
        Vector3f lightTarget = new Vector3f(12, 3.5f, 30);
        SpotLight spot=new SpotLight();
        spot.setSpotRange(1000);
        spot.setSpotInnerAngle(5*FastMath.DEG_TO_RAD);
        spot.setSpotOuterAngle(10*FastMath.DEG_TO_RAD);
        spot.setPosition(new Vector3f(3.5f, 50.0f, 3.5f));
        spot.setDirection(new Vector3f(3.5f, 0.0f, 3.5f).subtract(spot.getPosition()));     
        spot.setColor(ColorRGBA.White.mult(1));
        rootNode.addLight(spot);
    }
    
    @Override
    public void simpleUpdate(float tpf) 
    {
        //TODO: add update code
        updatePieces(); // user Interaction
        updateCam();
        if(AI && !updateCalledEngine)
            Engine();
        stateManager.update(tpf);
    }
    
    private void updatePieces()
    {
        if(isUpdatePiecesVaild())
        {  
           // System.out.println("Update Pieces");
            Vector3i from = (Vector3i)lastSelected.getKey();
            Vector3i to = (Vector3i)currentSelected.getKey();
            String type = (String)lastSelected.getValue();
            lastSelectedPiece = new Pair(from.x, from.z);
            Cell fromC = new Cell(piecesTypeSelected.getPieceDimension(from.x, from.z).x, piecesTypeSelected.getPieceDimension(from.x, from.z).z), toC = new Cell(to.x, to.z);
            defaultMap.removeHighlights();
            
            if(specialMove != null && specialMove.getRow() == toC.getRow() && specialMove.getColumn() == toC.getColumn())
            {
                game.update(fromC, specialMove);
                if(piecesTypeSelected.getSelectedPieceType(from.x, from.z).equalsIgnoreCase("pawn"))
                    piecesTypeSelected.enPassant(from, to);
                else if(piecesTypeSelected.getSelectedPieceType(from.x, from.z).equalsIgnoreCase("king"))
                    piecesTypeSelected.castling(from, to);
            }
            else
            {
                game.update(fromC, toC);
                piecesTypeSelected.Move(from, to);
            }
            
            
           // System.out.println(fromC.getRow() + " " + fromC.getColumn()  + " To " + to.x + " " + to.z);
            if(fromC.getRow() > 1)
                System.out.println("Black Moved");
            else
                System.out.println("White Moved");
            
            engineMove = !engineMove;
            if(((String)lastSelected.getValue()).equalsIgnoreCase("Engine"))
            {
                updateCalledEngine = false;
            }
            
            //TODO: gui display who wins the game
            if(game.isCheckmated(Color.Black))
            {
                System.out.println(" hhhh 5srt ");
              //  app.stop() ; 
            }
            else if(game.isCheckmated(Color.White))
            {
                System.out.println(" hhhh 5rst tany :P ");
            }
            else if(game.draw(Color.Black) || game.draw(Color.White))
            {
                System.out.println(" Draaaaaaaaaaaaaaaaaaaw ");
            }
            
            currentSelected = null;
            lastSelected = null;
            specialMove = null;
        }
    }  
    
    private void HighlightAvailableMoves(int i, int j)
    {
        if (currentSelected != null  && ((String)currentSelected.getValue()).equalsIgnoreCase("Piece"))
        {
            
            Vector3i dimension = piecesTypeSelected.getPieceDimension(i, j);
            ArrayList<Cell> list = game.board.pieces[dimension.x][dimension.z].possible_moves(new Cell(dimension.x, dimension.z), game.board);
  
            for(Cell c : list)
            {
                if (c.special_move != 0)
                {
                    System.out.println("Special Move at " + c.getRow() + " " + c.getColumn());
                    specialMove = c;
                }
                
                defaultMap.highLightCell(c, "Move");
            }   
        }
    }
    
    
    // handling user "Weird" clicks
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
    
    private void Engine()
    {
       if(engineMove && ok)
       {
            updateCalledEngine = true;
            SingleMove sm = ai2.root(5, true, game.board);
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
                HighlightAvailableMoves(to.getRow(), to.getColumn());
            }
       }
    }
    
    // update the cam with each move
    private void updateCam()
    {
        // using OR gatae as "simpleUpdate" is being called every single Second xD  
        moveDone |= piecesTypeSelected.isMoveDone();
        
        if(moveDone)
        {
            int i = (int)lastSelectedPiece.getKey(), j = (int)lastSelectedPiece.getValue();
            checkPromotion |= piecesTypeSelected.checkPromotion(i, j);
            
            if(!checkPromotion)
            {
                promotionDone = true;
            }
            else
            {
               // System.out.println("Promotion is Vaild");
                int type = 3;
                Vector3i to = piecesTypeSelected.getPieceDimension(i, j);
                Cell toC = new Cell(to.x, to.z);
                //type = uerChoice() ;  .// xml 
                //Todo: get user selcted type "GUI" 0 -> rock, 1 -> bishop, 2 -> knight, 3 -> queen
                game.makePromotion(toC, type);
                piecesTypeSelected.promote(i, j, type);
                checkPromotion = false;
                promotionDone = true;
            }
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
}
