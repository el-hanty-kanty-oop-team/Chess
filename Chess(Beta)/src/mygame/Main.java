package mygame;

import GamePackage.Chess;
import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ScreenController
{
    private boolean AIstate ;
    private int AILevel ;
    private String piecesTypeSelected ;
    private Chess chess;
    private String finalCase ;
    private boolean  start = false ;
    public  Nifty nifty;

    public static void main(String[] args) 
    {
        Main app = new Main();      
        app.start();
    }

    private void startGame()
    {
             guiViewPort.setEnabled(false);
             chess = new Chess(this,piecesTypeSelected , AIstate, AILevel);
             stateManager.attach(chess);
     
    }
    public void nextScreen(String s)
    {
            if(s.equalsIgnoreCase("Easy")||s.equalsIgnoreCase("medium")||s.equalsIgnoreCase("Hard"))
            {
                    if(s.equalsIgnoreCase("Easy")) AILevel = 2 ; 
                    else if(s.equalsIgnoreCase("medium"))AILevel = 3 ;
                    else AILevel = 5 ;

                s = "piecesType" ; 
                AIstate = true ;
            }

           System.out.println(s);
           nifty.addXml("Interface/screen.xml");
           nifty.gotoScreen(s);
    }
    
    
    public void piecesType(String s)
    {
      if(guiViewPort.isEnabled()){
         piecesTypeSelected = s  ;
         nifty.addXml("Interface/screen.xml");
         nifty.gotoScreen("emptyScreen");   

         start = true ;
      }
    }
    
    public void quitGame()
    {
       this.stop();
    }
    
    public void Choose(String st)
    {
        System.out.print(st);
         nifty.addXml("Interface/screen.xml");
         nifty.gotoScreen("emptyScreen"); 
         guiViewPort.setEnabled(false);
         chess.userChoice( Integer.parseInt(st));
       
    }
    
    
    public String gameOver()
    {
        return finalCase  ;
    }
    
    
    private void intializeUI()
    {
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
                assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);
        nifty = niftyDisplay.getNifty();
        
        nifty.fromXml("Interface/screen.xml", "start", this);
        guiViewPort.addProcessor(niftyDisplay);
        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
   
    }
    /**
     *
     */
    @Override
    public void simpleInitApp() 
    {
        intializeUI();
                
    }

    @Override
    public void simpleUpdate(float tpf)
    {
        if( chess!= null && chess.isPromotion())
        {
             guiViewPort.setEnabled(true);
            nifty.addXml("Interface/screen.xml");
            nifty.gotoScreen("userChoice");
          
        
        }
        
        if(chess != null && chess.isGameDone() != 0)
        {
        
            if(chess.isGameDone() == 1) // Black wins
            {
                
            try {
                    Thread.sleep((long) 10000.0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            System.out.println("Black won");
                
             finalCase="Black Won" ;   
             nifty.addXml("Interface/screen.xml");
             nifty.gotoScreen("BlackWon");
             guiViewPort.setEnabled(true);
             }
            else if(chess.isGameDone() == -1) // White wins
            {
                try {
                    Thread.sleep((long) 10000.0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("White won");
                finalCase = "White Won" ;
                 nifty.addXml("Interface/screen.xml");
                 nifty.gotoScreen("WhiteWon");
                guiViewPort.setEnabled(true);
             }
            else
            {
            try {
                    Thread.sleep((long) 10000.0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
           
                System.out.println("Draw");
            finalCase= "Draw" ;
                 nifty.addXml("Interface/screen.xml");
                 nifty.gotoScreen("Draaw");
                 guiViewPort.setEnabled(true);
            }
            //TODO: load gui again
            rootNode.detachAllChildren();
            chess.detach();
            
            stateManager.detach(chess);
            chess = null;
            System.gc();
        }
        if (start){
        startGame();
        start= false ;
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onStartScreen() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onEndScreen() {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
