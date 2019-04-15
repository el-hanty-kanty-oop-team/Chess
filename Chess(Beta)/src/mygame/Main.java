package mygame;

import Maps.DefaultMap;
import Maps.Map;
import PiecesAnimation.Bishop;
import PiecesAnimation.Knight;
import PiecesAnimation.Pawn;
import PiecesAnimation.Piece;
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
                Vector3f dimention = null;
                Node selectedModel = results.getCollision(0).getGeometry().getParent();

                dimention = getSelectedPiece(selectedModel);
                if(dimention != null)
                    currentSelected = new Pair(dimention, "Piece");

                dimention = defaultMap.getCellIndex(selectedModel);
                if(dimention != null)
                    currentSelected = new Pair(dimention, "Map");
                System.out.println(currentSelected + " " + lastSelected);


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
            for(int j = 1; j < piece[i].length - 1; j ++)
                piece[i][j] = new Pawn(this, i, j, true);
        piece[0][0] = new Bishop(this, 0, 0, true);
        piece[1][0] = new Bishop(this, 1, 0, true);
        piece[0][7] = new Knight(this, 0, 7, true);
        piece[1][7] = new Knight(this, 1, 7, true);
        
        
        defaultMap = new DefaultMap(this);
        
        currentSelected = new Pair(new Vector3f(), new String());
        lastSelected = new Pair(new Vector3f(), new String());
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
        
        if(currentSelected != null && lastSelected != null)
        {
            int fromRow = (int)((Vector3f)lastSelected.getKey()).x;
            int fromCol = (int)((Vector3f)lastSelected.getKey()).z;

            int toRow = (int)((Vector3f)lastSelected.getKey()).x;
            int toCol = (int)((Vector3f)lastSelected.getKey()).z;
            //System.out.println(((Vector3f)lastSelected.getKey()) + " Last selected");
            Vector3f direction = (Vector3f)currentSelected.getKey();
            
            
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

            }
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
        
        stateManager.attach(defaultMap);
    }    
    
    @Override
    public void simpleUpdate(float tpf) 
    {
        //TODO: add update code
        updatePieces();
    }

    @Override
    public void simpleRender(RenderManager rm) 
    {
        //TODO: add render code
    }
}
