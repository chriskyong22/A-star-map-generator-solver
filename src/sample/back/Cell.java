package sample.back;

public class Cell implements Comparable<Cell> {
    private char cellType; 
    private double cost;
    private Cell parent;
    private int x;
    private int y;

    Cell(int x, int y){
        cellType = '1';
        cost = 0;
        parent = null;
        this.x = x;
        this.y = y;
    }

    Cell(char type, int x, int y){
        this.cellType = type;
        cost = 0;
        parent = null;
        this.x = x;
        this.y = y;
    }

    void setParent(Cell parent){
        this.parent = parent;
    }
    Cell getParent(){
        return this.parent;
    }

    public char getType(){
        return cellType; 
    }

    boolean isBlocked(){
        return (cellType == '0');
    }
    
    boolean isRiver(){
        return (cellType == 'a') || (cellType == 'b');
    }

    boolean isNormal() {
        return (cellType == '1' || cellType == 'a');
    }
    boolean isHard(){
        return (cellType == '2' || cellType == 'b');
    }
    boolean isDiagonal(Cell cell2){
        return ( ((this.x - 1  == cell2.getX()) && (this.y - 1 == cell2.getY())) ||
                 ((this.x - 1  == cell2.getX()) && (this.y + 1 == cell2.getY())) ||
                 ((this.x + 1  == cell2.getX()) && (this.y - 1 == cell2.getY())) ||
                 ((this.x + 1  == cell2.getX()) && (this.y + 1 == cell2.getY()))
               );
    }

    int getX(){
        return this.x;
    }

    int getY(){
        return this.y;
    }

    void setCost(double value){
        this.cost = value;
    }

    double getCost(){
        return cost;
    }

    public int compareTo(Cell other){
        if(other.x == this.x && other.y == this.y){
            return 0;
        }else{
            return -1;
        }
    }

    void setType(char type){
        this.cellType = type;
    }

}
