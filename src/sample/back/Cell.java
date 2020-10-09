package sample.back;

public class Cell implements Comparable<Cell> {
    private char cellType; 
    private double cost;
    private Cell parent;
    private int x;
    private int y;

    Cell(int x, int y){
        cellType = '1';
        cost = Integer.MAX_VALUE;
        parent = null;
        this.x = x;
        this.y = y;
    }

    Cell(char type, int x, int y){
        this.cellType = type;
        cost = Integer.MAX_VALUE;
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

    @Override
    public int compareTo(Cell other){
        if(this.cost == other.cost){
            return 0;
        }else if(this.cost > other.cost){
            return 1;
        }else{
            return -1;
        }
    }

    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof Cell)){
            return false;
        }
        return (this.x == ((Cell) other).getX() && this.y == ((Cell) other).getY());
    }

    void setType(char type){
        this.cellType = type;
    }

}
