package sample.back;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.awt.Point;

public class Search {
    ArrayList<Point> expanded;
    PriorityQueue<Point> fringe;

    Search(Grid map){

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
    /**
     * Compute full cost f(x) with no given weight of the heuristic
     * @param heuristic The value of the heuristic
     * @param costFromStart Cost from start to this cell
     * @return full cost
     */
    double computeFullCost(double heuristic, double costFromStart){
        return heuristic + costFromStart;
    }

    double computeHeuristic(Cell start, Cell goal){
        double distance = 0;
        //CURRENTLY using the heuristic .25 * Euclidean distance,
        // not sure how to make the heuristic interchangable without function pointers atm
        double distanceX = Math.abs(start.getX() - goal.getX());
        double distanceY = Math.abs(start.getY() - goal.getY());
        distance = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
        distance *= .25;

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