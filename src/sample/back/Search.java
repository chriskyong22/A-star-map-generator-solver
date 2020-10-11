package sample.back;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.awt.Point;

public class Search {
    private double weight;
    public Grid map;

    public Search(Grid map){
        this.map = map;
        this.weight = 1;
    }

    public Search(Grid map, double weight){
        this.map = map;
        this.weight = weight;
    }

    double Key(Cell current, Cell goal, int heuristicSelection, final double weight1){
        double g = current.getCost(heuristicSelection);
        double h = computeHeuristic(current, goal, heuristicSelection);
        return g + (weight1 * h);
    }

    void ExpandState(PriorityQueue<Cell> open_table[], PriorityQueue<Cell> close_table[], Cell current, int heuristicSelected, Cell goalCell, final double w1){
        open_table[heuristicSelected].poll();
        ArrayList<Point> neighbors = map.getNeighbors(current.getX(), current.getY());
        for(Point neighborPoint : neighbors) {
            Cell neighbor = map.getCell(neighborPoint.x, neighborPoint.y);

            if(!neighbor.getVisited(heuristicSelected)){
                neighbor.setCost(Integer.MAX_VALUE, heuristicSelected);
                neighbor.setParent(null, heuristicSelected);
            }

            double g = current.getCost(heuristicSelected) + computeCost(current, neighbor);
            if(neighbor.getCost(heuristicSelected) > g){

                neighbor.setCost(g, heuristicSelected);
                neighbor.setParent(current, heuristicSelected);

                if(!close_table[heuristicSelected].contains(neighbor)){
                    double f = Key(neighbor, goalCell, heuristicSelected, w1);
                    neighbor.setHCost(f, heuristicSelected);
                    if(open_table[heuristicSelected].contains(neighbor)){
                        open_table[heuristicSelected].remove(neighbor);
                    }
                    open_table[heuristicSelected].add(neighbor);
                }

            }

        }

    }

    public int generateSequentialPath(int numOfHeuristics){
        if(numOfHeuristics <= 0){
            return -1;
        }
        if(numOfHeuristics == 1){
            generateNormalPath();
            return 0;
        }
        double w1 = 1.0;
        double w2 = 3.0;

        Point start = map.getStart();
        Point goal = map.getEndVertex();

        Cell startCell = map.getCell(start.x, start.y);
        Cell goalCell = map.getCell(goal.x, goal.y);

        map.setParentSize(numOfHeuristics);
        map.setCostSize(numOfHeuristics);
        map.setHCostSize(numOfHeuristics);
        map.setVisitedSize(numOfHeuristics);

        PriorityQueue<Cell> open_table[] = new PriorityQueue[numOfHeuristics];
        PriorityQueue<Cell> close_table[] = new PriorityQueue[numOfHeuristics];

        for (int heuristicSelected = 0; heuristicSelected < numOfHeuristics; heuristicSelected++) {
            int finalI = heuristicSelected;

            open_table[heuristicSelected] = new PriorityQueue<Cell>(11, (cell1, cell2) -> Double.compare(cell1.getHCost(finalI), cell2.getHCost(finalI)));

            close_table[heuristicSelected] = new PriorityQueue<Cell>(11, (cell1, cell2) -> Double.compare(cell1.getHCost(finalI), cell2.getHCost(finalI)));

            startCell.setCost(0, heuristicSelected);
            goalCell.setCost(Integer.MAX_VALUE, heuristicSelected);
            startCell.setParent(null, heuristicSelected);
            goalCell.setParent(null, heuristicSelected);
            double value = Key(startCell, goalCell, heuristicSelected, w1);
            startCell.setHCost(value, heuristicSelected);
            open_table[heuristicSelected].add(startCell);
        }

        while (open_table[0].peek().getHCost(0) < Integer.MAX_VALUE){
            for (int heuristicSelected = 1; heuristicSelected < numOfHeuristics; heuristicSelected++) {
                if (open_table[heuristicSelected].peek().getHCost(heuristicSelected) <= w2 * open_table[0].peek().getHCost(0)){
                    if (goalCell.getCost(heuristicSelected) <= open_table[heuristicSelected].peek().getHCost(heuristicSelected)){
                        if (goalCell.getCost(heuristicSelected) < Integer.MAX_VALUE){
                            System.out.println("Successfully found a path by heuristic: " + heuristicSelected + ".");
                            System.out.println("The cost of " + goalCell.getX() + " " + goalCell.getY() + ": " + goalCell.getCost(heuristicSelected));
                            return heuristicSelected;
                        }
                    }else {
                        Cell s = open_table[heuristicSelected].peek();
                        s.setVisited(heuristicSelected, true);
                        ExpandState(open_table, close_table, s, heuristicSelected, goalCell, w1);
                        close_table[heuristicSelected].add(s);
                    }
                }else {
                    if (goalCell.getCost(0) <= open_table[0].peek().getHCost(0)){
                        if (goalCell.getCost(0) < Integer.MAX_VALUE){
                            System.out.println("Successfully found a path by heuristic: 0.");
                            System.out.println("The cost of " + goalCell.getX() + " " + goalCell.getY() + ": " + goalCell.getCost(0));
                            return 0;
                        }
                    }else{
                        Cell s = open_table[0].peek();
                        s.setVisited(0, true);
                        ExpandState(open_table, close_table, s, 0, goalCell, w1);
                        close_table[0].add(s);
                    }
                }
            }
        }
        System.out.println("FAILURE: could not find a path given all the heuristics");
        return -1;
    }


    public ArrayList<Cell> generateNormalPath(){
        ArrayList<Cell> tested = new ArrayList<Cell>();
        PriorityQueue<Cell> fringe = new PriorityQueue<Cell>();
        Point start = map.getStart();
        Point goal = map.getEndVertex();
        Cell startCell = map.getCell(start.x, start.y);
        Cell goalCell = map.getCell(goal.x, goal.y);

        //Initialization of A*
        startCell.setParent(map.getCell(start.x, start.y), 0);
        startCell.setCost(0, 0);
        startCell.setHCost(computeHeuristic(startCell, goalCell), 0);
        fringe.add(map.getCell(start.x, start.y));

        while(!fringe.isEmpty()){
            Cell current = fringe.poll();
            if(current.getX() == goal.x && current.getY() == goal.y){
                System.out.println("Successfully found a path from start to goal");
                return tested;
            }
            tested.add(current);

            ArrayList<Point> neighbors = map.getNeighbors(current.getX(), current.getY());
            for(Point neighborPoint : neighbors){
                Cell neighbor = map.getCell(neighborPoint.x, neighborPoint.y);
                if(tested.contains(neighbor)){
                    continue;
                }
                if(!fringe.contains(neighbor)){
                    neighbor.setHCost(Integer.MAX_VALUE, 0);
                    neighbor.setCost(Integer.MAX_VALUE, 0);
                    neighbor.setParent(null, 0);
                }
                double g = current.getCost(0) + computeCost(current, neighbor);
                if(neighbor.getCost(0) > g){
                    neighbor.setCost(g,0);
                    double h = computeHeuristic(neighbor, goalCell);
                    double f = computeFullCost(weight, h, g);
                    neighbor.setHCost(f, 0);
                    neighbor.setParent(current, 0);
                    if(fringe.contains(neighbor)){
                        fringe.remove(neighbor);
                    }
                    fringe.add(neighbor);
                }
            }
        }
        System.out.println("Failure: could not find a path from start to goal");
        return null;
    }
    /**
     * Compute the full cost f(x) with a given weight (f(x) = h(x) * weight + g(x))
     * @param weight The weight of the heuristic 
     * @param heuristic The value of the heuristic 
     * @param costFromStart Cost from start to this cell
     * @return full cost 
     */
    double computeFullCost(double weight, double heuristic, double costFromStart){
        return (heuristic * weight) + costFromStart;
    }

    double computeHeuristic(Cell start, Cell goal){
        double distance = 0;
        //CURRENTLY using the heuristic: .25 * Euclidean distance,
        // not sure how to make the heuristic interchangable without function pointers atm
        double distanceX = Math.abs(start.getX() - goal.getX());
        double distanceY = Math.abs(start.getY() - goal.getY());
        distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
        distance *= .25;

        return distance;
    }

    // Heuristic computation for A* sequential
    double computeHeuristic(Cell start, Cell goal, int heuristicSelection){
        double distance = 0;

        double distanceX = Math.abs(start.getX() - goal.getX());
        double distanceY = Math.abs(start.getY() - goal.getY());

        switch(heuristicSelection){
            case 0: //Euclidian distance  * .25
                distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
                distance *= .25;
                break;
            case 1: //Manhattan distance * .25
                distance = distanceX + distanceY;
                distance *= .25;
                break;
            case 2: //Diagonal Distance
                distance = 1 * (distanceX + distanceY) + ((Math.sqrt(2) - 2 * 1) * Math.min(distanceX, distanceY));
                distance *= .25;
                break;
            case 3: //Euclidian distance squared
                distance = (distanceX * distanceX) + (distanceY * distanceY);
                distance *= .25;
                break;
            default:
                System.out.println("Heuristic was not selected, defaulting to uniform search where h = 0");
                break;
        }
        return distance;
    }

    /**
     * Compute the cost between traversing between the two ADJACENT cells 
     * @param start Cell 1
     * @param goal Cell 2
     * @return Cost of traversing between the two adjacent cells depending on their terrain 
     */
    double computeCost(Cell start, Cell goal){
        /**
         * Moving between two Regular:
         *  Vertical or Horizontal: 1
         *  Diagonal: sqrt(2)
         *
         * Moving between two HT:
         *  Vertical or Horizontal: 2
         *  Diagonal: sqrt(8)
         *
         * Moving between HT and Regular Nodes:
         *  Vertical or Horizontal: 1.5
         *  Diagonal: ( sqrt(2) + sqrt(8) ) /2
         *
         * If the two nodes are both highway(river nodes):
         *  Multiply the cost by (.25)
         */
        if(start.isBlocked() || goal.isBlocked()){
            System.out.println("ERROR: This case should not possible");
            return -1;
        }
        double cost = 0;
        if(start.isHard() && goal.isHard()){
            if(start.isDiagonal(goal)){
                cost = Math.sqrt(8);
            }else{
                cost = 2;
            }
        }else if(start.isNormal() && goal.isNormal()){
            if(start.isDiagonal(goal)){
                cost = Math.sqrt(2);
            }else{
                cost = 1;
            }
        }else{
            if(start.isDiagonal(goal)){
                cost = (Math.sqrt(2) + Math.sqrt(8))/2;
            }else{
                cost = 1.5;
            }
        }

        if(start.isRiver() && goal.isRiver()){
            return .25 * cost;
        }else{
            return cost;
        }
    }

}