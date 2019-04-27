
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PiecesAndAnimation;

import Tools.Vector3i;
import com.jme3.scene.Spatial;

/**
 *
 * @author delll
 */
public interface PiecesBehaviors 
{
    public Vector3i getPieceDimension(Spatial s);
    public Vector3i getPieceDimension(int x, int z);
    public Vector3i getPieceIndex(Spatial s);
    public Vector3i getPieceIndex(int x, int y);
    public void Move(Vector3i pieceIndex, Vector3i to);
    public boolean isMoveDone();
    public boolean checkPromotion(int i, int j);
    public void promote(int i, int j, int type);
}







