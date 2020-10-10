package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
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
    private TextArea infoArea;
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
    @FXML
    private RadioButton aRadio;
    @FXML
    private RadioButton weightedRadio;
    @FXML
    private RadioButton seqRadio;
    @FXML
    private TextField weightField;
    @FXML
    private Button pathButton;
    @FXML
    private RadioButton uniRadio;

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
        buildGrid(g,false);

    }

    /**
     * This method imports a grid from a file.
     */
    @FXML
    public void importME(){

        String filepath = pathField.getText();
        if (filepath.isEmpty()) return;

        g = new Grid(filepath);
        buildGrid(g,false);


    }

    /**
     * This method builds a grid into the output pane.
     * Color Coding:
     * Blocked Cell - BLACK
     * Regular Unblocked Cell - White
     * Hard to Traverse - Gray
     * Regular Cell w/ Highway - Light Blue
     * Hard to Traverse w/ Highway - Dark Blue
     * Start - Lime Green
     * Goal - Red
     * Path - Yellow
     * @param g - Input Grid object
     */
    public void buildGrid(Grid g,boolean b){
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
                int finalI = i;
                int finalJ = j;
                cells[i][j].setOnMouseClicked(event -> {
                    infoArea.setText("[" + finalJ + ":" + finalI + "]" + "\n" + "Cell Cost: " + g.map[finalJ][finalI].getCost() + "\n" + "HCost is: " + g.map[finalJ][finalI].getHCost());
                });

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
                        cells[i][j].setFill(Color.LIGHTBLUE);
                        break;
                    case 'b':
                        cells[i][j].setFill(Color.DARKBLUE);
                        break;
                }
                cells[i][j].setStroke(Color.BLACK);
                gridPane.getChildren().add(cells[i][j]);
            }
        }

        //Draw Path
        if (b) {
            Cell goalCell = g.getCell(goalX, goalY);
            while (!goalCell.getParent().equals(goalCell)) {
                cells[goalCell.getY()][goalCell.getX()].setFill(Color.YELLOW);
                goalCell = goalCell.getParent();
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
        int goalX = Integer.parseInt(input.substring(0, input.indexOf(',')));
        int goalY = Integer.parseInt(input.substring(input.indexOf(',') + 1));
        g.setEnd(goalX,goalY);
        buildGrid(g,false);
    }

    /**
     * Sets the grid's start and updates grid visual
     */
    @FXML
    public void setGridStart(){
        String input = startField.getText();
        int startX = Integer.parseInt(input.substring(0, input.indexOf(',')));
        int startY = Integer.parseInt(input.substring(input.indexOf(',') + 1));
        g.setStart(startX,startY);
        buildGrid(g,false);
    }

    @FXML
    public void weightedClicked(){
        weightField.setVisible(true);
    }

    /**
     * Generates a path based on a selected search algorithm
     */
    @FXML
    public void generatePath(){
        if(uniRadio.isSelected()){
            Search test = new Search(g, 0);
            test.generatePath();
        }
        if(aRadio.isSelected()){
            Search test = new Search(g, 1);
            test.generatePath();
        }
        if(weightedRadio.isSelected()){
            double weight = Double.parseDouble(weightField.getText());
            Search test = new Search(g, weight);
            test.generatePath();
        }

        buildGrid(g,true);
    }

}
