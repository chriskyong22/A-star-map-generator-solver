public class Test extends Grid{
    public static void main(String[] args){
        System.out.println("Hello World");
        Grid newMap = new Grid();
        newMap.generateGrid();
        System.out.println();
        newMap.printGrid();
        newMap.printStartVertex();
        newMap.printEndVertex();
        newMap.printHardToTraverse();
        newMap.writeToFile("insertPath");
    }
}