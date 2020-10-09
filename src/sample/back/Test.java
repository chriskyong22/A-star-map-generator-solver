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
        Point start = newMap.getStart();
        Point end = newMap.getEndVertex();
        /*
        newMap.generateGrid();
        System.out.println();
        newMap.printGrid();
        newMap.printStartVertex();
        newMap.printEndVertex();
        newMap.printHardToTraverse();
        newMap.writeToFile("map1.txt");
        */
        Search test = new Search(newMap, 1);
        ArrayList<Cell> temp = test.generatePath();
        Point goal = newMap.getEndVertex();
        Cell goalCell = newMap.getCell(goal.x, goal.y);
        while(!goalCell.getParent().equals(goalCell)) {
            goalCell.setType('!');
            goalCell = goalCell.getParent();
        }
        newMap.printGrid();
        System.out.println("The cost is: " + newMap.getCell(goal.x, goal.y).getCost() +  " | Found in: " + temp.size() + " moves");
       // Grid newMap = new Grid("map3.txt");
        //newMap.printGrid();
    }
}