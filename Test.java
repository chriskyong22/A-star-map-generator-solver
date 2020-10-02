import java.awt.Point;
import java.util.ArrayList; 

public class Test{
    public static void main(String[] args){
        //Grid newMap = new Grid();
        //newMap.printGrid();
        //newMap.printStartVertex();
        //newMap.printEndVertex();
        //newMap.printHardToTraverse();
        //newMap.generateGrid();
        //System.out.println();
        //newMap.printGrid();
        //newMap.printStartVertex();
        //newMap.printEndVertex();
        //newMap.printHardToTraverse();
        //newMap.writeToFile("map3.txt");
        Grid newMap = new Grid("map3.txt");
        newMap.printGrid();
    }
}