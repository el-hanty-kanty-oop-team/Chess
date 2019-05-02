package GamePackage;
public class Cell {
    private int Row, Column;
    public String type;
    public int special_move;
    public Cell(int x, int y) {
        this.Row = x;
        this.Column = y;
        this.special_move = 0;
    }
    
    public Cell() {
        this.Row = -1;
        this.Column = -1;
    }
    
    public boolean isEqual(Cell c){
        return c.Row == this.Row && c.Column == this.Column;
    }
    
    public Cell(int x, int y, int special_move) {
        this.Row = x;
        this.Column = y;
        this.special_move = special_move;
    }
    public Cell(int Row, int Column, String type) {
        this.Row = Row;
        this.Column = Column;
        this.type = type;
    }
 
    public Cell(Cell cell) {
        this.Row = cell.getRow();
        this.Column = cell.getColumn();
        this.type = cell.getType();
        this.special_move = cell.special_move;
    }
    public int getRow() {
        return Row;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    
}
