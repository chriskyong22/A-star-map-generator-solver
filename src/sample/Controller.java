package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.back.*;

import java.awt.*;

public class Controller {

    //inject FXML Objects
    @FXML
    private Button importButton;
    @FXML
    private Button generateButton;
    @FXML
    private TextField infoField;
    @FXML
    private Button infoButton;
    @FXML
    private TextField pathField;
    @FXML
    private Pane gridPane;
    @FXML
    private Button startButton;
    @FXML
    private Button goalButton;
    @FXML
    private TextField goalField;
    @FXML
    private TextField startField;

    //Grid Object
    Grid g = new Grid();

    /**
     * This method generates a random map after the generateMe button is clicked
     */
    @FXML
    public void generateME(){

        g = new Grid();
        g.generateGrid();
        g.writeToFile("map4.txt");
        buildGrid(g);

    }

    /**
     * This method imports a grid from a file.
     */
    @FXML
    public void importME(){

        String filepath = pathField.getText();
        if (filepath.isEmpty()) return;

        g = new Grid(filepath);
        buildGrid(g);


    }

    /**
     * This method builds a grid into the output pane.
     * Color Coding:
     * Blocked Cell - BLACK
     * Regular Unblocked Cell - White
     * Hard to Traverse - Gray
     * Regular Cell w/ Highway - Blue
     * Hard to Traverse w/ Highway - Dark Blue
     * Start - Lime Green
     * Goal - Red
     * Path - Yellow
     * @param g - Input Grid object
     */
    public void buildGrid(Grid g){
        //Clear Pane
        gridPane.getChildren().clear();

        //Initialize variables
        int width = g.columnSize;
        int height = g.rowSize;
        int rectangleW = 8;
        int rectangleH = 8;
        Point start = g.getStart();
        int startX =(int) start.getX();
        int startY =(int) start.getY();
        Point goal = g.getEndVertex();
        int goalX = (int) goal.getX();
        int goalY = (int) goal.getY();

        Rectangle[][] cells = new Rectangle[(int) width][(int) height];

        //Draw Rectangles in Pane
        for (int j = 0; j < height; ++j){
            //draw row
            for (int i = 0; i < width; ++i){
                //draw column
                cells[i][j] = new Rectangle();
                cells[i][j].setX(i*rectangleH);
                cells[i][j].setY(j*rectangleW);
                cells[i][j].setWidth(rectangleH);
                cells[i][j].setHeight(rectangleW);

                switch(g.map[j][i].getType()){
                    case '0':
                        cells[i][j].setFill(Color.BLACK);
                        break;
                    case '1':
                        cells[i][j].setFill(Color.WHITE);
                        break;
                    case '2':
                        cells[i][j].setFill(Color.GRAY);
                        break;
                    case 'a':
                        cells[i][j].setFill(Color.BLUE);
                        break;
                    case 'b':
                        cells[i][j].setFill(Color.DARKBLUE);
                        break;
                }
                gridPane.getChildren().add(cells[i][j]);
            }
        }

        //Color start and end pos
        cells[startY][startX].setFill(Color.LIMEGREEN);
        cells[goalY][goalX].setFill(Color.RED);
    }

    /**
     * Sets the grid's goal and updates grid visual
     */
    @FXML
    public void setGridGoal(){
        String input = goalField.getText();
        int goalX = Character.getNumericValue(input.charAt(0));
        int goalY = Character.getNumericValue(input.charAt(2));


        g.setEnd(goalX,goalY);
        buildGrid(g);
    }

    /**
     * Sets the grid's start and updates grid visual
     */
    @FXML
    public void setGridStart(){
        String input = startField.getText();
        int startX = Character.getNumericValue(input.charAt(0));
        int startY = Character.getNumericValue(input.charAt(2));

        g.setStart(startX,startY);
        buildGrid(g);
    }


}
