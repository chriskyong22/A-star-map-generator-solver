package sample.back;

import java.awt.Point;
import java.util.ArrayList; 

public class Test{
    public static void main(String[] args){

        double averages[][] = new double[5][4];
        int nodesVisited[][] = new int[5][50];
        double cost[][] = new double[5][50];
        double runtimes[][] = new double[5][50];
        double memory[][] = new double[5][50];



        for(int i = 0; i < 50; i++){
            System.out.println("ITERATION " + i + ":");
            Grid newMap = new Grid();
            newMap.generateGrid();
            Search temp = new Search(newMap, 1);
            for(int heuristic = 0; heuristic < 5; heuristic++){
                Runtime runtime = Runtime.getRuntime();
                long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Used memory before: " + (usedMemory) + " bytes");
                long startTime = System.nanoTime();
                ArrayList<Cell> nodesVisit = temp.generateNormalPath(heuristic);
                long endTime = System.nanoTime();
                long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("The nodes visited is: " + nodesVisit.size());
                nodesVisited[heuristic][i] = nodesVisit.size();
                memory[heuristic][i] = ((usedMemoryAfter - usedMemory));
                cost[heuristic][i] = newMap.getCell(newMap.getEndVertex().x, newMap.getEndVertex().y).getCost(0);;
                System.out.println("The cost is: " + newMap.getCell(newMap.getEndVertex().x, newMap.getEndVertex().y).getCost(0));
                runtimes[heuristic][i] = (endTime - startTime);
                System.out.println("Memory increased by: " + (usedMemoryAfter - usedMemory) + " bytes" );
                System.out.println("The runtime is: " + ((endTime - startTime)) + " nano-seconds");
                System.out.println("\n\n\n\n");
            }
        }

        for(int heuristic = 0; heuristic < 5; heuristic++){
            for(int i = 0; i < 50; i++){
                averages[heuristic][0] += nodesVisited[heuristic][i];
                averages[heuristic][1] += cost[heuristic][i];
                averages[heuristic][2] += runtimes[heuristic][i];
                averages[heuristic][3] += memory[heuristic][i];
            }
            System.out.println("Heuristic " + heuristic + ": ");
            averages[heuristic][0] /= 50;
            averages[heuristic][1] /= 50;
            averages[heuristic][2] /= 50;
            averages[heuristic][3] /= 50;
            System.out.println("Nodes Visited: " + averages[heuristic][0]);
            System.out.println("Cost: " + averages[heuristic][1]);
            System.out.println("Runtime: " + averages[heuristic][2]);
            System.out.println("Memory usage: " + averages[heuristic][3]);
        }



        /*
        //Grid newMap = new Grid("map4.txt");
        newMap.printGrid();
        newMap.printStartVertex();
        newMap.printEndVertex();
        newMap.printHardToTraverse();
        Point start = newMap.getStart();
        Point end = newMap.getEndVertex();

        newMap.generateGrid();
        System.out.println();
        newMap.printGrid();
        newMap.printStartVertex();
        newMap.printEndVertex();
        newMap.printHardToTraverse();
        newMap.writeToFile("map1.txt");

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
        */
    }
}