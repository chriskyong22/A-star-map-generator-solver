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
     * @param cost Cost from start to this cell
     * @return full cost 
     */
    double computeFullCost(double weight, double heuristic, double cost){
        return (heuristic * weight) + cost;
    }
    /**
     * Compute full cost f(x) with no given weight of the heuristic
     * @param heuristic The value of the heuristic
     * @param cost Cost from start to this cell 
     * @return full cost
     */
    double computeFullCost(double heuristic, double cost){
        return heuristic + cost;
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
         * 
         * IMPLEMENT
         */
        return 0;
    }

}