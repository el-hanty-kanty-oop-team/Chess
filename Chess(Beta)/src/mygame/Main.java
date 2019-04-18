package mygame;

import Maps.DefaultMap;
import Maps.Map;
import OriginPieces.OriginPiece;
import PiecesAnimation.Bishop;
import PiecesAnimation.Knight;
import PiecesAnimation.Pawn;
import PiecesAnimation.Piece;
import Tools.Vector3i;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import javafx.util.Pair;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication
{
    
    private final float moveSpeed = 0.032f;
    private Piece piece[][];
    private Map defaultMap; 
    private Pair currentSelected, lastSelected; 
    private OriginPiece originPiece;
    
    private final ActionListener actionListener = new ActionListener() 
    {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) 
        {
               
           if(name.equals("FlyByTheCam"))
            {
                // System.out.println(keyPressed);
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
            else if (name.equals("pick target") && !keyPressed) 
            {

                lastSelected = currentSelected;
                // Reset results list.
                CollisionResults results = new CollisionResults();
                // Convert screen click to 3d position
                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                // Aim the ray from the clicked spot forwards.
                Ray ray = new Ray(click3d, dir);
                // Collect intersections between ray and all nodes in results list.
                rootNode.collideWith(ray, results);
                Vector3i dimention = null;
                if(results.size() > 0)
                {
                    Node selectedModel = results.getCollision(0).getGeometry().getParent();
                    
                    if(lastSelected == null || !((String)lastSelected.getValue()).equalsIgnoreCase("Piece"))
                        dimention = originPiece.getPieceIndex(selectedModel);
                    if(dimention != null)
                    {
                        currentSelected = new Pair(dimention, "Piece");
                        System.out.println(dimention);
                    }
                    else
                    {
                        
                        dimention = originPiece.getPieceDimension(selectedModel);
                        if(dimention != null)
                            currentSelected = new Pair(dimention, "Map");
                        else
                         dimention = defaultMap.getCellIndex(selectedModel);
                        
                        if(dimention != null)
                            currentSelected = new Pair(dimention, "Map");
                    }
                }

            }
           
        }
    };
    
    private final AnalogListener analogListener = new AnalogListener() 
    {
        @Override
        public void onAnalog(String name, float intensity, float tpf) 
        {
             // else if ...
        }
    };
    
    public static void main(String[] args) 
    {
        Main app = new Main();      
        app.start();
    }

    public Vector3f getSelectedPiece(Spatial selectedModel)
    {
        for(int i = 0; i < piece.length; i ++)
            for(int j = 0; j < piece[i].length; j ++)
                if(piece[i][j].isEquale(selectedModel) == true)
                    return new Vector3f(i, 0, j);
        return null;
    }
    
    public void initModels()
    {
        piece = new Piece[2][8];
        for(int i = 0; i < piece.length; i ++)
            for(int j = 1; j < piece[i].length; j ++)
                piece[i][j] = new Pawn(this, i, j, true);
        piece[0][0] = new Bishop(this, 0, 0, true);
        piece[1][0] = new Bishop(this, 1, 0, true);
        
        
        defaultMap = new DefaultMap(this);
        
        currentSelected = null;
        lastSelected = null;
        
        originPiece = new OriginPiece(this);
    }

    public void initKeys()
    {
        inputManager.addMapping("FlyByTheCam", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("pick taregt", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "FlyByTheCam");
        inputManager.addListener(actionListener, "pick target");
    }
    
    private void updatePieces()
    {
        
        if(currentSelected != null && lastSelected != null && !currentSelected.equals(lastSelected) && ((String)lastSelected.getValue()).equalsIgnoreCase("Piece"))
        {
            //System.out.println(((Vector3f)lastSelected.getKey()) + " Last selected");
            Vector3i from = (Vector3i)lastSelected.getKey();
            Vector3i to = (Vector3i)currentSelected.getKey();
            originPiece.Move(from, to);
            
           /* 
            if(lastSelected.getValue().toString().equalsIgnoreCase("piece") && currentSelected.getValue().toString().equalsIgnoreCase("map"))
            {
                piece[fromRow][fromCol].update(direction, "walk");
            }
            else if(!lastSelected.equals(currentSelected) && lastSelected.getValue().toString().equalsIgnoreCase("piece") && currentSelected.getValue().toString().equalsIgnoreCase("piece"))
            {
                System.out.println(fromRow + " " + fromCol);
                System.out.println(toRow + " " + toCol);

                piece[fromRow][fromCol].update(direction, "attack");
              //  piece[toRow][toCol].update(direction, "death");

            }*/
            currentSelected = null;
            lastSelected = null;

           
        }
    }
    
    @Override
    public void simpleInitApp() 
    {
        initKeys();
        initModels();
        inputManager.setCursorVisible(false);
      
        for(int i = 0; i < piece.length; i ++)
            for(int j = 0; j < piece[i].length; j ++)
                stateManager.attach(piece[i][j]);
        stateManager.attach(originPiece);
        
        stateManager.attach(defaultMap);
    }    
    
    
    @Override
    public void simpleUpdate(float tpf) 
    {
        //TODO: add update code
        updatePieces();
        /*
            hna 7rk el pieces bta3tk bra7tk
            al3po hna mtl3po4 fe 7ta tnya
            lw fe bug aw exception zhr 2b2 2oly
            tb3n dh m4 el 25eer bs deh 7aga t3ml beha test le el engine
            @1st paramter mkan el piece 3bara 3n vector3i (new Vector3i(r, 0, c))
            @2nd paramter el mkan el 3awz trw7w bardo 3bara 3n vector3i (new Vector3i(r, 0, c))
           originPiece.Move(1st, 2nd);
        */
    }

    @Override
    public void simpleRender(RenderManager rm) 
    {
        //TODO: add render code
    }
}
