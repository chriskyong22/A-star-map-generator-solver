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
        ArrayList<Cell> temp = test.generateNormalPath(0);
        Point goal = newMap.getEndVertex();
        Cell goalCell = newMap.getCell(goal.x, goal.y);
        while(!goalCell.getParent(0).equals(goalCell)) {
            goalCell = goalCell.getParent(0);
        }
        newMap.printGrid();
        System.out.println("The cost is: " + newMap.getCell(goal.x, goal.y).getCost(0) +  " | Found in: " + temp.size() + " moves");
       // Grid newMap = new Grid("map3.txt");
        //newMap.printGrid();
        int x = test.generateSequentialPath(1, 1, 3);
        goalCell = newMap.getCell(goal.x, goal.y);
        System.out.println("Cost of Heuristic " + x + ": " + goalCell.getCost(x));
        //x = test.generateSequentialPath(4);
        goalCell = newMap.getCell(goal.x, goal.y);
        Cell startCell = newMap.getCell(newMap.getStart().x, newMap.getStart().y);
        Cell temp1 = goalCell;
        while(temp1 != startCell) {
            temp1.setType('!');
            temp1 = temp1.getParent(x);
        }
        newMap.printGrid();
    }
}