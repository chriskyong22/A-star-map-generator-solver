package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.back.*;
import sample.back.Cell;

import java.awt.*;
import java.awt.Label;

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
    @FXML
    private Button randGSButton;
    @FXML
    private ListView<String> heuristicList;
    @FXML
    private TextField weightField2;

    //Grid Object
    Grid g = new Grid();
    //Available Heuristics
    String[] heuristics = new String[]{"0 - Euclidean Distance/4", "1 - Manhattan Distance/4", "2 - Diagonal Distance/4", "3 - Chebyshev Distance/4", "4 - Euclidean Distance"};
    //Number of heuristics
    String[] numHeuristics = new String[]{"1","2","3","4","5"};

    /**
     * This method generates a random map after the generateMe button is clicked
     */
    @FXML
    public void generateME(){

        g = new Grid();
        g.generateGrid();
        g.writeToFile("map4.txt");
        buildGrid(g,false, -1);

    }

    /**
     * This method imports a grid from a file.
     */
    @FXML
    public void importME(){

        String filepath = pathField.getText();
        if (filepath.isEmpty()) return;

        g = new Grid(filepath);
        buildGrid(g,false, -1);


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
    public void buildGrid(Grid g, boolean b, int heuristicSelection){
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
                    infoArea.setText("[" + finalI + ":" + finalJ + "]" + "\n");
                    for(int heuristicSelected = 0; heuristicSelected < g.map[finalJ][finalI].getHCostSize(); heuristicSelected++){
                        infoArea.appendText("For Heuristic " + heuristicSelected + "\nCell Cost: " + g.map[finalJ][finalI].getCost(heuristicSelected) + "\n" + "HCost is: " + g.map[finalJ][finalI].getHCost(heuristicSelected) + "\n");
                    }
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
            Cell startCell = g.getCell(startX, startY);
            while (startCell != goalCell) {
                cells[goalCell.getY()][goalCell.getX()].setFill(Color.YELLOW);
                goalCell = goalCell.getParent(heuristicSelection);
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
        int goalY = Integer.parseInt(input.substring(0, input.indexOf(',')));
        int goalX = Integer.parseInt(input.substring(input.indexOf(',') + 1));
        g.setEnd(goalX,goalY);
        buildGrid(g,false, -1);
    }

    /**
     * Sets the grid's start and updates grid visual
     */
    @FXML
    public void setGridStart(){
        String input = startField.getText();
        int startY = Integer.parseInt(input.substring(0, input.indexOf(',')));
        int startX = Integer.parseInt(input.substring(input.indexOf(',') + 1));
        g.setStart(startX,startY);
        buildGrid(g,false, -1);
    }

    /**
     * Randomizes goal start pair
     */
    @FXML
    public void randomizeGS(){
        g.generateStartGoalVertex();
        buildGrid(g,false,-1);
    }

    @FXML
    public void weightedClicked(){
        weightField.setVisible(true);
        weightField2.setVisible(false);
        heuristicList.getItems().clear();
        heuristicList.getItems().addAll(heuristics);
        heuristicList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    @FXML
    public void aStarClicked(){
        weightField.setVisible(false);
        weightField2.setVisible(false);
        heuristicList.getItems().clear();
        heuristicList.getItems().addAll(heuristics);
        heuristicList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    @FXML
    public void uniformClicked(){
        weightField.setVisible(false);
        weightField2.setVisible(false);
    }
    @FXML
    public void seqClicked(){
        weightField.setVisible(true);
        weightField2.setVisible(true);
        heuristicList.getItems().clear();
        heuristicList.getItems().addAll(numHeuristics);
        heuristicList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * Generates a path based on a selected search algorithm
     */
    @FXML
    public void generatePath(){
        String selection = heuristicList.getSelectionModel().getSelectedItem();
        int heuristicSelected = Character.getNumericValue(selection.charAt(0));

        if(uniRadio.isSelected()){
            Search test = new Search(g, 0);
            test.generateNormalPath(0);
            buildGrid(g,true, 0);
        }
        if(aRadio.isSelected()){
            Search test = new Search(g, 1);
            test.generateNormalPath(heuristicSelected);
            buildGrid(g,true, 0);
        }
        if(weightedRadio.isSelected()){
            double weight = Double.parseDouble(weightField.getText());
            Search test = new Search(g, weight);
            test.generateNormalPath(heuristicSelected);
            buildGrid(g,true, 0);
        }
        if(seqRadio.isSelected()){
            double w1 = Double.parseDouble(weightField.getText());
            double w2 = Double.parseDouble(weightField2.getText());
            int numOfHeuristics = Integer.parseInt(selection);
            Search test = new Search(g, 1);
            int selected = test.generateSequentialPath(numOfHeuristics, w1,w2);
            buildGrid(g, true, selected);
        }
    }

}
