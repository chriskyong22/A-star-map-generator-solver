package sample.back;

import java.awt.Point;
import java.util.ArrayList; 

public class Test{
    public static void main(String[] args){
        Grid newMap = new Grid("map4.txt");
        newMap.printGrid();
        newMap.printStartVertex();
        newMap.printEndVertex();
        newMap.printHardToTraverse();
        /*
        newMap.generateGrid();
        System.out.println();
        newMap.printGrid();
        newMap.printStartVertex();
        newMap.printEndVertex();
        newMap.printHardToTraverse();
        newMap.writeToFile("map4.txt");
         */
        Search test = new Search(newMap);
        ArrayList<Cell> temp = test.generatePath();
        Cell last = temp.get(temp.size() - 1);
        while(!last.getParent().equals(last)) {
            last.setType('!');
            last = last.getParent();
        }
        newMap.printGrid();
       // Grid newMap = new Grid("map3.txt");
        //newMap.printGrid();
    }
}