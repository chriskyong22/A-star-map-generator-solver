package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.back.*;

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


    /**
     * This method generates a random map after the generateMe button is clicked
     */
    @FXML
    public void generateME(){

        Grid newMap = new Grid();
        newMap.generateGrid();
        newMap.writeToFile("map4.txt");
        buildGrid(newMap);

    }

    /**
     * This method imports a grid from a file.
     */
    @FXML
    public void importME(){

        String filepath = pathField.getText();
        if (filepath.isEmpty()) return;

        Grid g = new Grid(filepath);
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
     * @param g - Input Grid object
     */
    public void buildGrid(Grid g){
        gridPane.getChildren().clear();

        int width = g.columnSize;
        int height = g.rowSize;
        int rectangleW = 6;
        int rectangleH = 6;


        Rectangle[][] cells = new Rectangle[(int) width][(int) height];

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
    }
}
