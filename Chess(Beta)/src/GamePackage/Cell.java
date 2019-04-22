/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GamePackage;
public class Cell {
    private int Row, Column;
    private String type;
    
    public Cell(int Row, int Column, String type) {
        this.Row = Row;
        this.Column = Column;
        this.type = type;
    }
 
    public Cell(Cell cell) {
        this.Row = cell.getRow();
        this.Column = cell.getColumn();
        this.type = new String(cell.gettype());
    }
    
    public int getRow() {
        return Row;
    }
 
    public void setRow(int Row) {
        this.Row = Row;
    }
 
    public int getColumn() {
        return Column;
    }
 
    public void setColumn(int Column) {
        this.Column = Column;
    }
    
    public String gettype() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}