public class Cell implements Comparable<Cell> {
    private char cellType; 
    private double heuristicvalue;
    Cell(){
        cellType = '1';
        heuristicvalue = 0;
    }
    Cell(char type){
        this.cellType = type;
        heuristicvalue = 0;
    }
    char getType(){
        return cellType; 
    }

    void setHeuristic(double value){
        this.heuristicvalue = value;
    }

    double getHeuristic(){
        return heuristicvalue;
    }

    public int compareTo(Cell other){
        if(other.getHeuristic() > this.heuristicvalue){
            return -1;
        }else if(other.getHeuristic() == this.heuristicvalue){
            return 0;
        }else{
            return 1;
        }
    }

    void setType(char type){
        this.cellType = type;
    }

}