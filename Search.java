import java.util.ArrayList;
import java.util.PriorityQueue;
import java.awt.Point;
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
public class Search {
    ArrayList<Point> expanded;
    PriorityQueue<Point> fringe;

    Search(Grid map){

    }
}