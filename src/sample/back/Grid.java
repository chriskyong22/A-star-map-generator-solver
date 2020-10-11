package sample.back;

import java.awt.Point;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import sample.back.*;


public class Grid{
    public final int columnSize = 160;
    public final int rowSize = 120;
    public Point startVertex;
    public Point endVertex;
    public Point[] hardToTraverse;
    public Cell[][] map;

    public Grid(){
        this.map = new Cell[rowSize][columnSize];
        for(int x = 0; x < rowSize; x++){
            for(int y = 0; y < columnSize; y++){
                this.map[x][y] = new Cell(x, y);
            }
        }
        startVertex = null;
        endVertex = null;
        hardToTraverse = null;
    }

    public Grid(String filepath){
        this.map = new Cell[rowSize][columnSize];
        this.hardToTraverse = new Point[8];
        generateMapFromFile(filepath);
    }

    void generateMapFromFile(String filePath){
        try{
            BufferedReader fileObj = new BufferedReader(new FileReader(filePath));
            int index = 0;
            for(int i = 0; i < 10; i++){
                String[] vertex = fileObj.readLine().split(" ");
                if(i == 0){
                    this.startVertex = new Point(Integer.parseInt(vertex[0]), Integer.parseInt(vertex[1]));
                }else if(i == 1){
                    this.endVertex = new Point(Integer.parseInt(vertex[0]), Integer.parseInt(vertex[1]));
                }else{
                    this.hardToTraverse[index] = new Point(Integer.parseInt(vertex[0]), Integer.parseInt(vertex[1]));
                    index++;
                }
            }
            for(int i = 0; i < rowSize; i++){
                String line = fileObj.readLine().replaceAll("\\s", "");
                for(int j = 0; j < columnSize; j++){
                    map[i][j] = new Cell(line.charAt(j), i, j);
                }
            }
            System.out.println("Successfully populated the map given the file");
            fileObj.close();
        }catch(Exception e){
            System.out.println("There was an error reading the file to generate the map");
            e.printStackTrace();
        }
    }


    public void generateGrid(){
        generateHardTraverseCells();
        generateRivers();
        System.out.println("Finished Generating all Rivers: ");
        printGrid();
        generateBlockedCells();
        generateStartGoalVertex();
    }

    void generateStartGoalVertex(){
        Point start = generateStartGoalPoints();
        int distance = 0;
        Point end = null;
        while(distance < 100){
            end = generateStartGoalPoints();
            double distanceX = Math.abs(start.getX() - end.getX());
            double distanceY = Math.abs(start.getY() - end.getY());
            distance = (int) Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
        }
        this.startVertex = start;
        this.endVertex = end;
    }

    Point generateStartGoalPoints(){
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
            if(map[x][y].getType() != '0'){
                newPoint = new Point(x, y);
                validPoint = true;
            }
        }
        return newPoint;
    }

    void generateBlockedCells(){
        int blockLeft = (int) ((0.20 * (columnSize * rowSize)));
        System.out.println("Populating " + blockLeft + " blocked spaces");
        System.out.println();
        while(blockLeft >= 0){
            int x = (int) (Math.random() * rowSize);
            int y = (int) (Math.random() * columnSize);
            if(map[x][y].getType() == '1' || map[x][y].getType() == '2'){
                map[x][y].setType('0');
                blockLeft--;
            }
        }
    }

    void generateRivers(){
        int numberOfRivers = 4;
        ArrayList<Point> attempted = new ArrayList<Point>();
        ArrayList<Point> allRiverMoves = new ArrayList<Point>();
        while(numberOfRivers > 0){
            Point currentAttempt = getRandomBoundary(attempted);
            if(currentAttempt == null){
                resetMap(allRiverMoves);
                allRiverMoves.clear();
                attempted.clear();
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
                    System.out.println("Finished Generating this River, reached boundaries");
                }
                currentRiverMoves.addAll(temp);
                applyPositions(temp);
                //printGrid();
            }
            System.out.println("[Final] X: " + x + " Y: " + y + " River Length: " + riverLength);
            if(riverLength < 100){
                resetMap(currentRiverMoves);
            }else{
                allRiverMoves.addAll(currentRiverMoves);
                numberOfRivers = numberOfRivers - 1;
                printGrid();
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
                if(map[i][y].getType() == 'a' || map[i][y].getType() == 'b'){
                    return null;
                }else{
                    Point newPoint = new Point(i, y);
                    positions.add(newPoint);
                }
            }
            return positions;
        }else if(direction == 1){ // Go Up
            for(int i = x; i > Math.max(x - 20, -1); i--){
                if(map[i][y].getType() == 'a' || map[i][y].getType() == 'b'){
                    return null;
                }else{
                    Point newPoint = new Point(i, y);
                    positions.add(newPoint);
                }
            }
            return positions;
        }else if(direction == 4){ // Go left
            for(int i = y; i > Math.max(y - 20, -1); i--){
                if(map[x][i].getType() == 'a' || map[x][i].getType() == 'b'){
                    return null;
                }else{
                    Point newPoint = new Point(x, i);
                    positions.add(newPoint);
                }
            }
            return positions;
        }else{ //Go right
            for(int i = y; i < Math.min(y + 20, columnSize); i++){
                if(map[x][i].getType() == 'a' || map[x][i].getType() == 'b'){
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
        if(listOfAttempted.size() >= (int) (((2 * rowSize) + (columnSize * 2)) * .5) ){
            return null;
        }
        int x = 0;
        int y = 1;
        while(true){
            int quadrantic = (int) (Math.random() * 4);
            switch(quadrantic){
                case 0: //Top
                    x = 0;
                    y = (int) ((Math.random() * columnSize) + 1);
                    while(y >= columnSize - 1){
                        y--;
                    }
                    break;
                case 1: //Bottom
                    x = rowSize - 1;
                    y = (int) ((Math.random() * columnSize) + 1);
                    while(y >= columnSize - 1){
                        y--;
                    }
                    break;
                case 2: //Left
                    x = (int) ((Math.random() * rowSize) + 1);
                    while(x >= rowSize - 1){
                        x--;
                    }
                    y = 0;
                    break;
                case 3: //Right
                    x = (int) ((Math.random() * rowSize) + 1);
                    while(x >= rowSize - 1){
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
     * Checks if a point is on the boundaries or past the boundaries 
     * @param x 
     * @param y 
     * @return true if point is on boundaries or past the boundaries, otherwise false
     */
    boolean checkBoundaries(int x, int y){
        return x <= 0 || x >= (rowSize - 1) || y <= 0 || y >= (columnSize - 1);
    }

    boolean checkOutOfBounds(int x, int y){
        return x < 0 || x >= rowSize || y < 0 || y >= columnSize;
    }

    /**
     * Get the neighbors of a designed point (ignoring blocked cells)
     * @param x
     * @param y
     * @return null if invalid point, otherwise an Arraylist of points representing the valid neighbors
     */
    ArrayList<Point> getNeighbors(int x, int y){
        //Safety check to make sure you're not getting neighbors for out of bounds points 
        if(checkOutOfBounds(x, y)){
            System.out.println("Out of bounds point entered: [X: " + x + "] [Y: " + y + "]");
            return null;
        }

        //Check the 3x3 area, starting from top left corner to bottom right corner
        ArrayList<Point> neighbors = new ArrayList<Point>();
        for(int posX = x - 1; posX <= x + 1; posX++){
            for(int posY = y - 1; posY <= y + 1; posY++){
                if(!checkOutOfBounds(posX, posY) && (posX != x || posY != y) && !map[posX][posY].isBlocked()){
                    neighbors.add(new Point(posX, posY));
                }
            }
        }
        return neighbors;
    }


    /**
     * Used to update the River cells in the map 
     * @param positions - ArrayList of points to update to RiverType cells in the map
     */
    void applyPositions(ArrayList<Point> positions){
        for(Point position : positions){
            if(map[(int) position.getX()][(int) position.getY()].getType() == '1'){
                map[(int) position.getX()][(int) position.getY()].setType('a');
            }else if(map[(int) position.getX()][(int) position.getY()].getType() == '2'){
                map[(int) position.getX()][(int) position.getY()].setType('b');
            }else{
                System.out.println("ERROR: found unidentified element at: " + (int) position.getX() + " " + (int) position.getY());
            }
        }
    }
    /**
     * @param positions - ArrayList of points to remove RiverType cells in the map
     */
    void resetMap(ArrayList<Point> positions){
        for(Point position : positions){
            if(map[(int) position.getX()][(int) position.getY()].getType() == 'a'){
                map[(int) position.getX()][(int) position.getY()].setType('1');
            }else{
                map[(int) position.getX()][(int) position.getY()].setType('2');
            }
        }
    }

    public void setParentSize(int n){
        for(int x = 0; x < rowSize; x++){
            for(int y = 0; y < columnSize; y++){
                map[x][y].setParentSize(n);
            }
        }
    }

    public void setCostSize(int n){
        for(int x = 0; x < rowSize; x++){
            for(int y = 0; y < columnSize; y++){
                map[x][y].setCostSize(n);
            }
        }
    }
    public void setHCostSize(int n){
        for(int x = 0; x < rowSize; x++){
            for(int y = 0; y < columnSize; y++){
                map[x][y].setHCostSize(n);
            }
        }
    }
    void generateHardTraverseCells(){
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
                        this.map[regionX][regionY].setType('2');
                    }
                }
            }
            numberOfPoints++; 
        }
    }

    public Cell getCell(int x, int y){
        return map[x][y];
    }

    public void printGrid(){
        for(int x = 0; x < rowSize; x++){
            for(int y = 0; y < columnSize; y++){
                System.out.print(map[x][y].getType());
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

    /**
     * 
     * @param x
     * @param y
     * @return the type of the cell, 'i' if invalid cell (meaning out of bounds)
     */
    char getType(int x, int y){
        if(x < 0 || x >= rowSize || y < 0 || y >= columnSize){
            return 'i';
        }else{
            return this.map[x][y].getType();
        }
    }

    public Point getStart(){
        return startVertex;
    }

    public void setStart(int x, int y){
        this.startVertex = new Point(x,y);
    }

    public void setEnd(int x, int y){
        this.endVertex = new Point(x,y);
    }

    public Point getEndVertex(){
        return endVertex;
    }

    public void writeToFile(String filename){
        try{
            BufferedWriter fileObj = new BufferedWriter(new FileWriter(filename));
            fileObj.write((int) startVertex.getX() + " " + (int) (startVertex.getY()) + "\n");
            fileObj.write((int) (endVertex.getX()) + " " + (int) (endVertex.getY()) + "\n");
            for(int i = 0; i < hardToTraverse.length; i++){
                fileObj.write((int) hardToTraverse[i].getX() + " " + (int) (hardToTraverse[i].getY()) + "\n");
            }
            for(int x = 0; x < rowSize; x++){
                for(int y = 0; y < columnSize; y++){
                    fileObj.write(map[x][y].getType());
                }
                fileObj.write("\n");
            }
            fileObj.close();
            System.out.println("Done writing out");
        }catch(Exception e){
            System.out.println("An error occurred when trying to create the file");
            e.printStackTrace();
        }
    }


}