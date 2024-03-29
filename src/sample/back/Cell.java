package sample.back;

public class Cell implements Comparable<Cell> {
    private char cellType; 
    private double[] cost;
    private double[] hcost;
    private Cell[] parent;
    private int x;
    private int y;
    private boolean[] visited;

    Cell(int x, int y){
        cellType = '1';
        cost = new double[1];
        cost[0] = Integer.MAX_VALUE;
        hcost = new double[1];
        hcost[0] = Integer.MAX_VALUE;
        parent = new Cell[1];
        parent[0] = null;
        this.x = x;
        this.y = y;
    }

    Cell(char type, int x, int y){
        this.cellType = type;
        cost = new double[1];
        cost[0] = Integer.MAX_VALUE;
        hcost = new double[1];
        hcost[0] = Integer.MAX_VALUE;
        parent = new Cell[1];
        parent[0] = null;
        this.x = x;
        this.y = y;
    }

    void setParent(Cell parent, int i){
        this.parent[i] = parent;
    }
    public Cell getParent(int i){
        return this.parent[i];
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

    public int getX(){
        return this.x;
    }

    public void setCostSize(int n){
        this.cost = new double[n];
    }

    public void setParentSize(int n){
        this.parent = new Cell[n];
    }
    public void setVisitedSize(int n){
        this.visited = new boolean[n];
    }

    public void setHCostSize(int n){
        this.hcost = new double[n];
    }

    public void setVisited(int heuristic, boolean value){
        this.visited[heuristic] = value;
    }

    public boolean getVisited(int heuristic){
        return this.visited[heuristic];
    }

    public int getY(){
        return this.y;
    }

    public void setCost(double value, int i){
        this.cost[i] = value;
    }

    public double getCost(int i){
        return cost[i];
    }

    public int getHCostSize(){
        return this.hcost.length;
    }

    public int getParentSize(){
        return this.parent.length;
    }

    public int getCostSize(){
        return this.cost.length;
    }

    public int getVisitedSize(){
        return this.visited.length;
    }

    public void setHCost(double value, int i){
        this.hcost[i] = value;
    }
    public double getHCost(int i){
        return this.hcost[i];
    }


    @Override
    public int compareTo(Cell other){
        return Double.compare(this.hcost[0], other.getHCost(0));
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
