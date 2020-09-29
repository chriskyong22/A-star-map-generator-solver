import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

public class Grid{
    private final int columnSize = 160;
    private final int rowSize = 120;
    private Point startVertex;
    private Point endVertex;
    private Point[] hardToTraverse = null;
    private char[][] map;
    Grid(){
        this.map = new char[rowSize][columnSize];
        for(int x = 0; x < rowSize; x++){
            for(int y = 0; y < columnSize; y++){
                this.map[x][y] = '1';
            }
        }
        startVertex = null;
        endVertex = null;
    }

    void generateGrid(){
        initializeHardTraverse();
        initializeRivers();
        initializeBlocked();
        initializeStartGoalVertex();
    }

    void initializeStartGoalVertex(){
        Point start = generateStartGoalVertex();
        int distance = 0;
        Point end = null;
        while(distance < 100){
            distance = 0;
            end = generateStartGoalVertex();
            distance += Math.abs(start.getX() - end.getX());
            distance += Math.abs(start.getY() - end.getY());
        }
        this.startVertex = start;
        this.endVertex = end;
    }

    Point generateStartGoalVertex(){
        Point newPoint = null;
        boolean validPoint = false;
        while(validPoint != true){
            int quadrantic = (int) (Math.random() * 4);
            int x = 0;
            int y = 0;
            switch(quadrantic){
                case 0: //top
                    x = (int) (Math.random() * 20);
                    y = (int) (Math.random() * columnSize);
                    break;
                case 1: // bottom
                    x = (int) (rowSize - 20 + (Math.random() * 20));
                    y = (int) (Math.random() * columnSize);
                    break;
                case 2: // left
                    x = (int) (Math.random() * rowSize);
                    y = (int) (Math.random() * 20);
                    break;
                case 3: // right
                    x = (int) (Math.random() * rowSize);
                    y = (int) (columnSize - 20 + (Math.random() * 20));
                    break;
                default:
                    System.out.println("Error: somehow trying to generate a point outside of the regions for start/end points");
                    newPoint = null;
                    break;
            }
            if(map[x][y] != '0'){
                newPoint = new Point(x, y);
                validPoint = true;
            }
        }
        return newPoint;
    }

    void initializeBlocked(){
        int blockLeft = (int) ((0.20 * (columnSize * rowSize)));
        System.out.println("Populating " + blockLeft + " blocked spaces");
        System.out.println();
        while(blockLeft >= 0){
            int x = (int) (Math.random() * rowSize);
            int y = (int) (Math.random() * columnSize);
            if(map[x][y] != 'a' && map[x][y] != 'b'){
                map[x][y] = '0';
                blockLeft--;
            }
        }
    }

    void initializeRivers(){
        int numberOfRivers = 4;
        ArrayList<Point> attempted = new ArrayList<Point>();
        ArrayList<Point> allRiverMoves = new ArrayList<Point>();
        while(numberOfRivers > 0){
            Point currentAttempt = getRandomBoundary(attempted);
            if(currentAttempt == null){
                resetMap(allRiverMoves);
                allRiverMoves.clear();
                numberOfRivers = 4;
                continue;
            }
            int x = (int) currentAttempt.getX();
            int y = (int) currentAttempt.getY();
            attempted.add(currentAttempt);

            int direction = 0;
            System.out.println("Starting [X: " + x + " Y: " + y + "]");
            ArrayList<Point> initialMove;
            if(!checkBoundaries(x+20, y)){
                direction = 3;
                initialMove = move(x,y, direction);
                x += 19;
            }else if(!checkBoundaries(x-20, y)){
                direction = 1;
                initialMove = move(x,y, direction);
                x -= 19;
            }else if(!checkBoundaries(x, y+20)){
                direction = 2;
                initialMove = move(x,y, direction);
                y += 19;
            }else{
                direction = 4;
                initialMove = move(x,y, direction);
                y -= 19;
            }
            System.out.print(" Direction: " + direction + "\n");
            ArrayList<Point> currentRiverMoves = new ArrayList<Point>();
            if(initialMove == null){
                continue;
            }else{
                currentRiverMoves.addAll(initialMove);
                applyPositions(currentRiverMoves);
                initialMove.clear();
            }
            int riverLength = 20;
            System.out.println("[Vertical/Horizontal completed]: X: " + x + " Y: " + y);
            while(x < rowSize - 1 && y < columnSize - 1 && x > 0 && y > 0){
                int chance = (int) ((Math.random() * 10) + 1);
                if(chance <= 6){ // Same direction
                }else if(chance <= 8){ //Left Direction
                    direction = ((direction - 1) == 0) ? 4 : (direction - 1);
                }else{ //Right Direction
                    direction = (direction % 4) + 1;
                }
                System.out.println("The direction selected for this iteration is: " + direction);
                ArrayList<Point> temp;
                switch(direction){
                    case 1:
                        temp = move(x - 1, y, direction);
                        riverLength += ((x - 20) < 0) ? x : 20;
                        x = x - 20;
                        break;
                    case 2:
                        temp = move(x,y + 1, direction);
                        riverLength += ((y + 20) > columnSize) ? columnSize - y : 20;
                        y = y + 20;
                        break;
                    case 3:
                        temp = move(x + 1,y, direction);
                        riverLength += ((x + 20) > rowSize) ? rowSize - x : 20;
                        x = x + 20;
                        break;
                    case 4:
                        temp = move(x, y - 1, direction);
                        riverLength += ((y - 20) < 0) ? y : 20;
                        y = y - 20;
                        break;
                    default: 
                        System.out.println("The direction was out of bounds: " + direction);
                        temp = null;
                        break;
                }
                if(temp == null){
                    riverLength = -1;
                    break;
                }
                System.out.println("[UPDATED] X: " + x + " Y: " + y + " River Length: " + riverLength);
                if(x == 0 || y == 0 || y == columnSize - 1 || x == rowSize - 1){ //Next iteration starts exactly on the boundary, should not expand anymore (this is an edgecase)
                    temp.add(new Point(x,y));
                    riverLength++;
                    System.out.println("Finished Generating this River, reached boundaries");
                }
                currentRiverMoves.addAll(temp);
                applyPositions(temp);
                printGrid();
            }
            System.out.println("[Final] X: " + x + " Y: " + y + " River Length: " + riverLength);
            if(riverLength < 100){
                resetMap(currentRiverMoves);
            }else{
                allRiverMoves.addAll(currentRiverMoves);
                numberOfRivers = numberOfRivers - 1;
            }
        }
    }
    /**
     * This method will traverse 20 units in the indicated direction, starting at x, y (includes (x, y) as the first move). If it encounters a river title, it will return null.
     * @param x - starting x position
     * @param y - starting y position
     * @param direction - direction to move (1 = UP, 2 = RIGHT, 3 = DOWN, 4 = LEFT)
     * @return an Arraylist of points to update if success, NULL otherwise 
     */
    ArrayList<Point> move(int x, int y, int direction){
        ArrayList<Point> positions = new ArrayList<Point>();
        if(direction == 3){ // Go down
            for(int i = x; i < Math.min(x + 20, rowSize); i++){
                if(map[i][y] == 'a' || map[i][y] == 'b'){
                    return null;
                }else{
                    Point newPoint = new Point(i, y);
                    positions.add(newPoint);
                }
            }
            return positions;
        }else if(direction == 1){ // Go Up
            for(int i = x; i > Math.max(x - 20, -1); i--){
                if(map[i][y] == 'a' || map[i][y] == 'b'){
                    return null;
                }else{
                    Point newPoint = new Point(i, y);
                    positions.add(newPoint);
                }
            }
            return positions;
        }else if(direction == 4){ // Go left
            for(int i = y; i > Math.max(y - 20, -1); i--){
                if(map[x][i] == 'a' || map[x][i] == 'b'){
                    return null;
                }else{
                    Point newPoint = new Point(x, i);
                    positions.add(newPoint);
                }
            }
            return positions;
        }else{ //Go right
            for(int i = y; i < Math.min(y + 20, columnSize); i++){
                if(map[x][i] == 'a' || map[x][i] == 'b'){
                    return null;
                }else{
                    Point newPoint = new Point(x, i);
                    positions.add(newPoint);
                }
            }
            return positions;
        }
    }
    /** 
     * @param listOfAttempted - ArrayList of points already used 
     * @return A randomly generated point that is on the boundaries of the map that has not been used.
     * null if the list of attempted points is half of the all the boundaries points
     * @Note The corners of the boundaries are excluded because we move away from the boundaries without hitting another boundary (since we cannot generate diagonally for the rivers)
    */
    Point getRandomBoundary(ArrayList<Point> listOfAttempted){
        if(listOfAttempted.size() >= ((2 * rowSize) + (columnSize * 2))/2 ){
            return null;
        }
        int x = 1;
        int y = 1;
        while(true){
            int quadrantic = (int) (Math.random() * 4);
            switch(quadrantic){
                case 0: //Top
                    x = 0;
                    y = (int) ((Math.random() * columnSize) + 1);
                    if(y == columnSize){
                        y--;
                    }
                    break;   
                case 1: //Bottom
                    x = rowSize - 1;
                    y = (int) ((Math.random() * columnSize) + 1);
                    if(y == columnSize){
                        y--;
                    }
                    break;
                case 2: //Left
                    x = (int) ((Math.random() * rowSize) + 1);
                    if(x == rowSize){
                        x--;
                    } 
                    y = 0;
                    break;
                case 3: //Right
                    x = (int) ((Math.random() * rowSize) + 1);
                    if(x == rowSize){
                        x--;
                    } 
                    y = columnSize - 1;
                    break;
                default: System.out.println("Error: quadrantic unknown");
            }
            if(!listOfAttempted.contains(new Point(x, y))){
                break;
            }
        }
        return new Point(x,y);
    }
    /**
     * Checks if a point is on the boundaries
     * @param x 
     * @param y 
     * @return true if point is on boundaries, otherwise false
     */
    boolean checkBoundaries(int x, int y){
        return x <= 0 || x >= (rowSize - 1) || y <= 0 || y >= (columnSize - 1);
    }

    /**
     * @param positions - ArrayList of points to update to RiverType cells in the map
     */
    void applyPositions(ArrayList<Point> positions){
        for(Point position : positions){
            if(map[(int) position.getX()][(int) position.getY()]  == '1'){
                map[(int) position.getX()][(int) position.getY()] = 'a';
            }else{
                map[(int) position.getX()][(int) position.getY()] = 'b';
            }
        }
    }
    /**
     * @param positions - ArrayList of points to remove RiverType cells in the map
     */
    void resetMap(ArrayList<Point> positions){
        for(Point position : positions){
            if(map[(int) position.getX()][(int) position.getY()]  == 'a'){
                map[(int) position.getX()][(int) position.getY()] = '1';
            }else{
                map[(int) position.getX()][(int) position.getY()] = '2';
            }
        }
    }

    void initializeHardTraverse(){
        int numberOfPoints = 0;
        hardToTraverse = new Point[8];
        while(numberOfPoints < 8){
            // Calculate the min/max of the 31x31 region 
            int x = (int) (rowSize * Math.random());
            int y = (int) (columnSize * Math.random()); 
            hardToTraverse[numberOfPoints] = new Point(x, y);
            int minX = Math.max(x - 15, 0);
            int minY = Math.max(y - 15, 0);
            int maxX = Math.min(x + 15, rowSize - 1);
            int maxY = Math.min(y + 15, columnSize - 1);
            System.out.println("The random point is: " + x + " " + y + " and the regionX's min is " + minX + " and the regionY's min is " + minY + " and the max X is " + maxX + " and the max Y is " + maxY);
            for(int regionX = minX; regionX <= maxX; regionX++){
                for(int regionY = minY; regionY <= maxY; regionY++){
                    if(((int) (Math.random() * 2)) == 1){
                        this.map[regionX][regionY] = '2';
                    }
                }
            }
            numberOfPoints++; 
        }
    }

    void printGrid(){
        for(int x = 0; x < rowSize; x++){
            for(int y = 0; y < columnSize; y++){
                System.out.print(map[x][y]);
            }
            System.out.println();
        }
    }

    void printStartVertex(){
        if(this.startVertex != null){
            System.out.println("[Start Vertex] X: " + this.startVertex.getX() + " Y: " + this.startVertex.getY());
        }else{
            System.out.println("The start vertex has not been initialized yet");
        }
    }
    void printHardToTraverse(){
        if(hardToTraverse == null){
            System.out.println("Map has not been generated yet");
            return;
        }
        System.out.println("The Hard To Traverse Points: ");
        for(int i = 0; i < 8; i++){
            System.out.println("[X: " + hardToTraverse[i].getX() + "] [Y: " + hardToTraverse[i].getY() + "]");
        }
    }
    void printEndVertex(){
        if(this.endVertex != null){
            System.out.println("[Goal Vertex] X: " + this.endVertex.getX() + " Y: " + this.endVertex.getY());
        }else{
            System.out.println("The end vertex has not been initialized yet");
        }
    }

    int getType(int x, int y){
        return this.map[x][y];
    }

    void writeToFile(String filename){
        try{
            BufferedWriter fileObj = new BufferedWriter(new FileWriter(filename));
            fileObj.write((int) startVertex.getX() + " " + (int) (startVertex.getY()) + "\n");
            fileObj.write((int) (endVertex.getX()) + " " + (int) (endVertex.getY()) + "\n");
            for(int i = 0; i < hardToTraverse.length; i++){
                fileObj.write((int) hardToTraverse[i].getX() + " " + (int) (hardToTraverse[i].getY()) + "\n");
            }
            for(int x = 0; x < rowSize; x++){
                for(int y = 0; y < columnSize; y++){
                    fileObj.write(map[x][y]);
                }
                if(x != rowSize - 1){
                    fileObj.write("\n");
                }
            }
            fileObj.close();
            System.out.println("Done writing out");
        }catch(Exception e){
            System.out.println("An error occurred when trying to create the file");
            e.printStackTrace();
        }
    }
}