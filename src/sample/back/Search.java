package sample.back;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.awt.Point;

public class Search {
    private double weight;
    public Grid map;
    Search(Grid map){
        this.map = map;
        this.weight = 1;
    }

    Search(Grid map, double weight){
        this.map = map;
        this.weight = weight;
    }

    ArrayList<Cell> generatePath(){
        ArrayList<Cell> tested = new ArrayList<Cell>();
        PriorityQueue<Cell> fringe = new PriorityQueue<Cell>();
        Point start = map.getStart();
        Point goal = map.getEndVertex();
        Cell startCell = map.getCell(start.x, start.y);
        Cell goalCell = map.getCell(goal.x, goal.y);

        //Initialization of A*
        startCell.setParent(map.getCell(start.x, start.y));
        startCell.setCost(0);
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
                double g = current.getCost() + computeCost(current, neighbor);
                double h = computeHeuristic(neighbor, goalCell);
                double f = computeFullCost(weight, h, g);
                if(neighbor.getCost() > f){
                    neighbor.setCost(f);
                    neighbor.setParent(current);
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

    //I don't think this is needed, I think computeCost already covers this case
    double computeHeuristic(Cell start, Cell goal, double weight){
        double distance = 0;
        //
        //overload for different weight
        //
        double distanceX = Math.abs(start.getX() - goal.getX());
        double distanceY = Math.abs(start.getY() - goal.getY());
        distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
        distance *= weight;

        return distance;
    }
    double calculatePathCost(){
        Point goal = map.getEndVertex();
        Cell current = map.getCell(goal.x, goal.y);
        Cell end = map.getCell(goal.x, goal.y);
        double cost = end.getCost();
        while(!current.getParent().equals(current)){
            cost -= weight * computeHeuristic(current, end);
            current = current.getParent();
        }
        return cost;
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