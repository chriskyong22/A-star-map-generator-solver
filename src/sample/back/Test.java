package sample.back;

import java.awt.Point;
import java.util.ArrayList; 

public class Test{
    public static void main(String[] args){

        double averages[][][] = new double[4][5][4];
        int nodesVisited[][][] = new int[4][5][50];
        double cost[][][] = new double[4][5][50];
        double runtimes[][][] = new double[4][5][50];
        double memory[][][] = new double[4][5][50];




        for (int i = 0; i < 50; i++) {
            System.out.println("ITERATION " + i + ":");
            Grid newMap = new Grid();
            newMap.generateGrid();
            for(int algo = 0; algo < 4; algo++){
                Search algorithm = null;
                switch(algo){
                    case 0:
                        algorithm = new Search(newMap, 0);
                        break;
                    case 1:
                        algorithm = new Search(newMap, 1);
                        break;
                    case 2:
                        algorithm = new Search(newMap, 1.25);
                        break;
                    case 3:
                        algorithm = new Search(newMap, 2);
                        break;
                }
                for (int heuristic = 0; heuristic < 5; heuristic++) {
                    if(algo == 0){
                        heuristic = 6;
                    }
                    double memoryUsed = 0;
                    do{
                        Runtime runtime = Runtime.getRuntime();
                        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                        System.out.println("Used memory before: " + (usedMemory) + " bytes");
                        long startTime = System.nanoTime();
                        ArrayList<Cell> nodesVisit = algorithm.generateNormalPath(heuristic);
                        long endTime = System.nanoTime();
                        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
                        if(algo == 0){
                            heuristic = 4;
                        }
                        memory[algo][heuristic][i] = ((usedMemoryAfter - usedMemory));
                        System.out.println("Memory increased by: " + memory[algo][heuristic][i] + " bytes");
                        nodesVisited[algo][heuristic][i] = nodesVisit.size();
                        System.out.println("The nodes visited is: " + nodesVisited[algo][heuristic][i]);
                        cost[algo][heuristic][i] = newMap.getCell(newMap.getEndVertex().x, newMap.getEndVertex().y).getCost(0);
                        System.out.println("The cost is: " + cost[algo][heuristic][i]);
                        runtimes[algo][heuristic][i] = (endTime - startTime);
                        System.out.println("The runtime is: " + runtimes[algo][heuristic][i] + " nano-seconds");
                        memoryUsed = memory[algo][heuristic][i];
                        System.out.println("\n\n\n\n");
                    }while(memoryUsed < 0);
                }
            }




        }
        // NOTE first algorithm is uniform so I just made it store the values to heuristic 4 to not waste time
        for(int algo = 0; algo < 4; algo++) {
            System.out.println("Algorithm " + algo);
            for (int heuristic = 0; heuristic < 5; heuristic++) {
                for (int temp = 0; temp < 50; temp++) {
                    averages[algo][heuristic][0] += nodesVisited[algo][heuristic][temp];
                    averages[algo][heuristic][1] += cost[algo][heuristic][temp];
                    averages[algo][heuristic][2] += runtimes[algo][heuristic][temp];
                    averages[algo][heuristic][3] += memory[algo][heuristic][temp];
                }
                System.out.println("Heuristic " + heuristic + ": ");
                averages[algo][heuristic][0] /= 50;
                averages[algo][heuristic][1] /= 50;
                averages[algo][heuristic][2] /= 50;
                averages[algo][heuristic][3] /= 50;
                System.out.println("Nodes Visited: " + averages[algo][heuristic][0]);
                System.out.println("Cost: " + averages[algo][heuristic][1]);
                System.out.println("Runtime: " + averages[algo][heuristic][2]);
                System.out.println("Memory usage: " + averages[algo][heuristic][3]);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }
            System.out.println();
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