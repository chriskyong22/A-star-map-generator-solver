public class Test extends Grid{
    public static void main(String[] args){
        Grid newMap = new Grid("filePath");
        newMap.printGrid();
        newMap.printStartVertex();
        newMap.printEndVertex();
        newMap.printHardToTraverse();
        // newMap.generateGrid();
        // System.out.println();
        // newMap.printGrid();
        // newMap.printStartVertex();
        // newMap.printEndVertex();
        // newMap.printHardToTraverse();
        // newMap.writeToFile("filePath");

    }
}