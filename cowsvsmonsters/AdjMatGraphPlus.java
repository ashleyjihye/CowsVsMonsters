/********************************************************************
  * Ashley Thomas
  * Exam 3
  * CS 230
  * 11/24/2013
  * 
  * EDITED: by Leah Ferguson and Ashley Thomas 12/4/2013 for Cows vs. Monsters
  * 
  * AdjMatGraphPlus.java
  * Implementation of the GraphPlus.java interface using adjacency matrix
  * of booleans. 
  * I believe all methods work as they should.
  * KNOWN FEATURES/BUGS: 
  * It handles unweighted graphs only, but it can be extended.
  * topologicalSort() method will crash if a graph with a cycle is used
  ********************************************************************/
import javafoundations.*;
import java.util.*;
import java.io.*;

//---------------------------------------------------------------------------------------------------------------------------
//The AdjMatGraphPlus class reads in a tgf file and it creates an adjacent matrix representation of the graph and its different
//vertices and arcs, functionality includes adding and removing vertices, and adding and removing edges and arcs. 
//It also saves a graph created in the main method as a tgf file which can then be opened in the yed program.
//Added functionality from a previous implementation (AdjMatGraph) allows the user to clone the graph, check if a vertex is 
//a sink or source and get lists of these, sort the graph, and traverse the graph using a DFS or BFS approach.
//Extended functionality in dealing with non-existent vertices was added. The program prints to the user that the vertex he/she 
//entered did not exist and returned a value consistent with the method's return type. Could have alternatively used exceptions
//and try/catch clauses, but since the current implementation was used in our solutions for Assn. 7, I decided to keep
//it this way, since no functionality is lost by doing it this way and it is still clean and concise.
//---------------------------------------------------------------------------------------------------------------------------

public class AdjMatGraphPlus<T> implements GraphPlus<T>
{
  private final int NOT_FOUND = -1;  //return this value if a vertex does not exist
  private final int DEFAULT_CAPACITY = 1; //1, to test expandCapac
  
  private int n;    //number of vertices in the graph
  private boolean[][] arcs;   //adjacency matrix of arcs
  private T[] vertices;    //values of vertices
  
  // First constructor 
  public AdjMatGraphPlus() {
    n = 0;
    this.arcs = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];//Creates an empty arc array that allocates space for the arcs
    this.vertices = (T[])(new Object[DEFAULT_CAPACITY]); //Creates an empty vertices array that allocates space for the vertices
    //Start with size of DEFAULT_CAPACITY to test our extendCapac() method
  }
  
  /** Second Constructor
   * Creates a graph from a .tgf files whose vertices are cow objects.
   * @param inputfile Takes in a .tgf file containing our graph.
   */
  public AdjMatGraphPlus(String inputfile){
    n = 0;
    this.arcs = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
    this.vertices = (T[])(new Object[DEFAULT_CAPACITY]);
    
    try {
      Scanner scan = new Scanner(new File(inputfile));
      while (!scan.next().equals("#")) { //While there are still vertices to be made, they are created
        String vertex = scan.next();
        if (Integer.parseInt(vertex) <= 17){
          Cow cow = new Cow(vertex, true);
          addVertex((T)cow);
        }else{
          Cow cow = new Cow(vertex, false);
          addVertex((T)cow);
        }
        
      }
      
      while (scan.hasNext()) { //After the pound symbol it creates the arcs that are in the file
        int vertex1 = Integer.parseInt(scan.next());
        int vertex2 = Integer.parseInt(scan.next());
        addArc(vertices[vertex1-1], vertices[vertex2-1]);  //subtract 1 since the vertices in the file start at 1, but our array starts at index 0
      }
      scan.close();
      
    }
    catch (IOException ex) {
      System.out.println(" ***(T)ERROR*** The file was not found: " + ex);
    }
  }
  
  /**
   * Returns the object at a given index.
   * @param index Takes an in int that is an index.
   * @return Returns the object at the given vertex.
   */
  public T getVertex(int index){
    return vertices[index];
  }
  
  /**
   * Sets the value of the given vertex to be the given object.
   * @param index The index of the vertex we wish to change the value of.
   * @param vertex The object we wish the vertex to hold.
   */
  public void setVertex(int index, T vertex){
    vertices[index] = vertex;
  }
  
  
  /** Returns true if this graph is empty, false otherwise. */
  public boolean isEmpty() {
    return (n == 0);
  }
  
  /** Returns the number of vertices in this graph. */
  public int n() {
    return n;
  }
  
  /** Returns the number of arcs in this graph. */
  public int m() {
    int numArcs = 0;//Keeps track of how many arcs there are
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (arcs[i][j])
          numArcs++;
      }
    }
    return numArcs;
  }
  
  /** Returns true iff a directed edge exists b/w given vertices */
  public boolean isArc (T vertex1, T vertex2) {
    int index1 = getIndex(vertex1);
    int index2 = getIndex(vertex2);
    
    if (index1 == NOT_FOUND || index2 == NOT_FOUND) { //If either vertex does not exist automatically return false
      System.out.println("You did not input vertices that are actually in the graph.");
      return false;
    }
    
    return (arcs[index1][index2]);   
  }
  
  /** Returns true iff an edge exists between two given vertices
    * which means that two corresponding arcs exist in the graph */
  public boolean isEdge (T vertex1, T vertex2) {
    int index1 = getIndex(vertex1);
    int index2 = getIndex(vertex2);
    
    if (index1 == NOT_FOUND || index2 == NOT_FOUND) {//If either vertex does not exist automatically return false
      System.out.println("You did not input vertices that are actually in the graph.");
      return false;
    }
    
    return (isArc(vertex1, vertex2) && isArc(vertex2, vertex1));//Will only return true if both of the arcs exist 
  }
  
  /** Returns true IFF the graph is undirected, that is, for every
    * pair of nodes i,j for which there is an arc, the opposite arc
    * is also present in the graph.  */
  public boolean isUndirected() {
    
    for (int i = 0; i < n; i++) {
      for (int j = 0; j <= i; j++) {  //This inner loop only goes to j <= i because we only need to check the bottom "half" (the triangle created by
        //splitting the matrix using the diagonal) since the if statement checks the arcs in the bottom half with the arcs
        //in the top half
        if (arcs[i][j] != arcs[j][i])  //for each arc, checks to see if the corresponding arc has the same value
          return false;
      }
    }
    return true;  //all arcs corresponded with each other, so must be undirected
  }
  
  /** Adds a vertex to this graph, associating object with vertex.
    * If the vertex already exists, nothing is inserted. */
  public void addVertex (T vertex) {
    if (getIndex(vertex) >= 0) //vertex is already in our graph
      return;
    if (vertices.length == n) {//If vertices array is out of space expand the capacity of the array
      expandCapac();
    }
    vertices[n] = vertex;//Adds vertex
    n++;
  }
  
  //method expandCapac() expands the capacities of the two arrays
  private void expandCapac() {
    T[] temp = (T[])(new Object[vertices.length * 2]);//The following expands the capacity of the vertices array
    for (int s = 0; s < vertices.length; s++)
      temp[s] = vertices[s];
    
    vertices = temp;
    
    boolean[][] temp1 = new boolean[vertices.length][vertices.length];//Expands the capacity of the arcs array
    for (int t = 0; t < vertices.length/2; t++) {
      for (int x = 0; x < vertices.length/2; x++)
        temp1[t][x] = arcs[t][x];
    }
    
    arcs = temp1; 
  }
  
  /** Removes a single vertex with the given value from this graph.
    * If the vertex does not exist, it does not change the graph. */
  
  public void removeVertex (T vertex) {
    int index = getIndex(vertex);
    if (index == NOT_FOUND) {//If vertex does not exist do nothing
      System.out.println("Could not remove " + vertex.toString() + " because it does not exist in the graph.");
      return;
    }
    n--;
    
    for (int i = index; i < n; i++)
      vertices[i] = vertices[i+1];
    
    for (int i = index; i < n; i++) {
      for (int j = 0; j <= n; j++)
        arcs[i][j] = arcs[i+1][j];
    }
    
    for (int i = index; i < n; i++) {
      for (int j = 0; j < n; j++)
        arcs[j][i] = arcs[j][i+1];
    }
  }
  
  //Helper method that finds the index of the vertex given in the vertices array
  public int getIndex (T vertex) {
    for (int i = 0; i < n; i++) {
      if (vertices[i].equals(vertex))
        return i;
    }
    return NOT_FOUND;//Vertex was not found so it returns -1.
  }
  
  /** Inserts an arc between two vertices of this graph,
    * if the vertices exist. Else it does not change the graph. */
  public void addArc (T vertex1, T vertex2) {
    int index1 = getIndex(vertex1);
    int index2 = getIndex(vertex2);
    
    if (index1 == NOT_FOUND || index2 == NOT_FOUND || index1 == index2) {//If either vertex is not found or the two vertices inputed are the same do nothing
      return;
    }
    
    arcs[index1][index2] = true; //Adds the arc
  }
  
  /** Removes an arc between two vertices of this graph,
    * if the vertices exist. Else it does not change the graph. */
  public void removeArc (T vertex1, T vertex2) {
    int index1 = getIndex(vertex1);
    int index2 = getIndex(vertex2);
    
    if (index1 == NOT_FOUND || index2 == NOT_FOUND) {//If either vertex is not found do nothing
      return;
    }
    
    arcs[index1][index2] = false; //Removes the arc
  }
  
  /** Inserts an edge between two vertices of this graph,
    * if the vertices exist. Else does not change the graph. */
  public void addEdge (T vertex1, T vertex2) {
    int index1 = getIndex(vertex1);
    int index2 = getIndex(vertex2);
    
    if (index1 == NOT_FOUND || index2 == NOT_FOUND || index1 == index2) {//If either vertex is not found or the two vertices inputed are the same do nothing
      return;
    }
    
    addArc(vertex1, vertex2); //Adds an arc from index1 to index2
    addArc(vertex2, vertex1); //Adds an arc from index2 to index1
  }
  
  /** Removes an edge between two vertices of this graph,
    if the vertices exist, else does not change the graph. */
  public void removeEdge (T vertex1, T vertex2) {
    int index1 = getIndex(vertex1);
    int index2 = getIndex(vertex2);
    
    if (index1 == NOT_FOUND || index2 == NOT_FOUND) {//If either vertex is not found do nothing
      return;
    }
    
    removeArc(vertex1, vertex2); //Removes the arc from index1 to index2
    removeArc(vertex2, vertex1); //Removes the arc from index2 to index1
  }
  
  /** Retrieve from a graph the vertices adjacent to vertex v.
    Assume that the vertex is in the graph */
  public LinkedList<T> getSuccessors(T vertex) {
    
    LinkedList<T> successors = new LinkedList<T>();
    int index = getIndex(vertex);
    if (index == NOT_FOUND)  //If vertex given does not exist return an empty list
      return successors;
    for (int i = 0; i < n; i++) {
      if (arcs[index][i]) //If there is an arc from the vertex at our index to the vertex at index i add this vertex to the successors linked list
        successors.add(vertices[i]);
    }
    return successors;
  }
  
  /** Retrieve from a graph the vertices x preceding vertex v (x->v)
    and returns them onto a linked list */
  public LinkedList<T> getPredecessors(T vertex) {
    
    LinkedList<T> predecessors = new LinkedList<T>();
    int index = getIndex(vertex);
    if (index == NOT_FOUND)  //If vertex given does not exist return an empty list
      return predecessors;
    for (int i = 0; i < n; i++) {
      if (arcs[i][index]) {//If there is an arc from the vertex at index i to the vertex at our index add this vertex to the predecessors linked list
        predecessors.add(vertices[i]);
      }
    }
    return predecessors;
  }
  
  public LinkedList<Integer> getPredecessorIndices(int index) {
    LinkedList<Integer> predecessors = new LinkedList<Integer>();
    if (index == NOT_FOUND)
      return predecessors; //If vertex given does not exist return an empty list
    for (int i = 0; i < n; i++) {
      if (arcs[i][index]) {//If there is an arc from the vertex at index i to the vertex at our index add this vertex to the predecessors linked list
        predecessors.add(i);
      }
    }
    return predecessors;
  }

  // Returns a string representation of the boolean true or false
  private String trueFalse(boolean b) {
    if (b == true)
      return "1";
    return "-";
  }
  
  /** Returns a string representation of the adjacency matrix. */
  //Added some extra lines in case # of vertices n was bigger than 9
  //in order to make the representation of the graph clearer and 
  //make spacing better.
  //As you can tell, I used constants and didn't name them, because
  //they're only used once (in this method), so I chose stylistically
  //not to name them.
  public String toString() {
    String result = "Arcs\n-----\n";
    result += "i ";
    if (n > 9)
      result += " ";
    for (int i = 0; i < n; i++) { //Prints out the vertices
      
      result += vertices[i].toString() + " ";
    }
    
    result += "\n";
    
    for (int j = 0; j < n; j++) { 
      result += vertices[j].toString() + " "; //Prints out the vertex that should be at that row
      if (j < 9)
        result += " ";
      for (int k = 0; k < n; k++) {// Prints out the arcs in that row
        result += trueFalse(arcs[j][k]) + " ";
        if (k >= 8)
          result += " ";
      }
      result += "\n";
    }
    return result;
  }
  
  /** Saves the current graph into a .tgf file.
    If it cannot save the file, a message is printed. */
  public void saveTGF(String tgf_file_name) {
    try {
      PrintWriter writer = new PrintWriter(new File(tgf_file_name));
      
      //prints vertices by iterating through array "vertices"
      for (int i = 0; i < n(); i++){
        writer.print((i+1) + " " + vertices[i]);
        writer.println("");
      }
      writer.print("#"); // Prepare to print the edges
      writer.println("");
      
      //prints arcs by iterating through 2D array
      for (int i = 0; i < n(); i++){
        for (int j = 0; j < n(); j++){
          if (arcs[i][j] == true){
            writer.print((i+1) + " " + (j+1));
            writer.println("");
          }
        }
      }
      writer.close();
    } catch (IOException ex) {
      System.out.println("***ERROR*** The file could not be written: " + ex);
    }
  }
  
  /******************************************************************
    * Clones the graph by saving the current one on the disk 
    * as TEMP.tgf using saveTGF() and creating a new one using the 
    * second constructor.
    * @return the new graph.
    * FEATURE: It does not try to delete the file from the disk
    *****************************************************************/
  public GraphPlus<T> clone () 
  {
    saveTGF("TEMP.tgf");   //save the current graph as a tgf file
    GraphPlus<T> cgraph = new AdjMatGraphPlus<T>("TEMP.tgf");  //create a new graph using the tgf file we just made
    return cgraph;
  }
  
  /******************************************************************
    * Checks if a vertex is a sink, (points to no other vertex)
    * @return true if the vertex is a sink, false if it is not.
    * Added functionality: will return false if vertex doesn't exist
    ******************************************************************/
  public boolean isSink (T vertex)
  {
    if (getIndex(vertex) == NOT_FOUND) { //If invalid vertex is given, do nothing and return false
      System.out.println("You entered an invalid index.");
      return false;
    }
    //Since a sink is a vertex that points to no other vertices, and getSuccessors returns a list of the vertices
    //following the vertex given, we just need to make sure that it returns an empty list for vertex to be a sink
    return (getSuccessors(vertex).isEmpty());
  }
  
  /******************************************************************
    * Retrieves the vertices that are sinks and 
    * @return all the sinks in a linked list
    ******************************************************************/
  public LinkedList<T> allSinks()
  {
    LinkedList<T> sinks = new LinkedList<T>();
    
    for (int i = 0; i < n; i++) {  //go through all the vertices in our array
      if (isSink(vertices[i]))   //if the vertex is a sink, add it to our LinkedList
        sinks.add(vertices[i]);
    }
    return sinks;  
  }
  
  
  /******************************************************************
    * Checks if a vertex is a source, (no vertex points to it)
    * @return true if the vertex is a source, false if it is not
    ******************************************************************/
  public boolean isSource (T vertex)
  {
    if (getIndex(vertex) == NOT_FOUND) {   //If invalid vertex is given, do nothing and return false
      System.out.println("You entered an invalid index.");
      return false;
    }
    //Since a source is a vertex that has nothing pointing to it, and getPredecessors returns a list of the vertices
    //pointing to the vertex given, we just need to make sure that it returns an empty list for vertex to be a source
    return (getPredecessors(vertex).isEmpty());
  }
  
  /******************************************************************
    * Retrieves the vertices that are sources and
    * @return all the sources in a linked list
    ******************************************************************/
  public LinkedList<T> allSources()
  {
    LinkedList<T> sources = new LinkedList<T>();  
    for (int i = 0; i < n; i++) {  //go through all the vertices in the array
      if (isSource(vertices[i]))   //if the vertex is a source, add it to our LinkedList
        sources.add(vertices[i]);
    }
    return sources; 
  }
  
  /******************************************************************
    * Checks if a vertex is a isolated, b/c it's source and sink
    * @return true if the vertex is isolated, false if it is not
    ******************************************************************/
  public boolean isIsolated (T vertex)
  {
    if (getIndex(vertex) == NOT_FOUND) {   //If invalid vertex is given, do nothing and return false
      System.out.println("You entered an invalid index.");
      return false;
    }
    return (isSource(vertex) && isSink(vertex));
  }
  
  /******************************************************************
    * Topologically sort vertices of a directed acyclic graph
    * using one of the two algorithms presented in class. 
    * PREREQUISITE: The input graph must be a DAG, i.e., have NO CYCLES.
    * KNOWN BUG: It will get into an infinite loop if the graph has a cycle
    * @return the topologically sorted vertices in a linked list
    * 
    * Since it is a prerequisite to have no cycles, I did not check
    * for this in my code.
    ******************************************************************/
  public LinkedList<T> topologicalSort()
  {
    LinkedList<T> sorted = new LinkedList<T>();
    
    GraphPlus<T> cgraph = clone();  //use this cloned graph when sorting the graph in order
    //not to destroy your original graph
    while (cgraph.n() != 0) {   //while the graph is not empty, keep going
      LinkedList<T> sources = cgraph.allSources();  //find all the sources in the graph (these are the vertices that have no predecessor)
      for (T source : sources) {
        //System.out.println(source);    //for debugging
        cgraph.removeVertex(source); //remove the vertex from the graph
        //using this method also removes its edges from the graph
        sorted.add(source);          //add the vertex to our sorted list
      }
    }
    return sorted;     //return the list of vertices in topological order
  }
  
  /******************************************************************
    * Returns a LinkedList contining a DEPTH first search traversal 
    * starting at the given index. If the index is not valid, 
    * it returns an empty list
    * @return a linked list with the vertices in depth-first order
    *****************************************************************/
  public LinkedList<T> DFS(T vertex)
  {
    LinkedList<T> resultList = new LinkedList<T>();  //the list that will be returned containing the vertices
    
    int initialIndex = getIndex(vertex);
    if (initialIndex == NOT_FOUND){  //If entered vertex does not exist, return an empty list
      System.out.println("Invalid vertex was entered.");
      return resultList;
    }
    
    int currentVertex;    //the index of the current vertex we are looking at
    
    LinkedStack<Integer> traversalStack = new LinkedStack<Integer>();   //how we will keep track of when to stop.
    //Repeatly put vertices into the stack as we deal with them
    //We know we are done when the stack is empty, ie when all
    //vertices have been dealt with
    boolean[] visited = new boolean[n];    //an array that keeps track of which vertices have already been dealt with, ie. visited
    boolean found;
    
    traversalStack.push(initialIndex);    //push the vertex given to us onto the stack
    resultList.add(vertex);    //add it to our resultList
    visited[initialIndex] = true;    //it has now been visited
    
    while (!traversalStack.isEmpty())      //while we still have more vertices to traverse over, keep going
    {
      currentVertex = traversalStack.peek();
      found = false;
      for (int i = 0; i < n && !found; i++) {  
        if (arcs[currentVertex][i] && !visited[i])  //find an adjacent vertex that has not been visited yet
        {
          traversalStack.push(i);     //push it onto the stack
          resultList.add(vertices[i]); //add it to our list since it has now been visited
          visited[i] = true;
          found = true;
        }
      }
      if (!found && !traversalStack.isEmpty())   //if you cannot find an unvisited vertex from the vertex
        //on the top of the stack, backtrack
        traversalStack.pop();
    }
    return resultList;
  }
  
  /******************************************************************
    * Returns a LinkedList contining a BREADTH first search traversal 
    * starting at the given index. If the index is not valid, 
    * it returns an empty list
    * @return a linked list with the vertices in breadth-first order
    *****************************************************************/
  public LinkedList<T> BFS(T vertex)
  {
    LinkedList<T> resultList = new LinkedList<T>();  //the list that will be returned containing the vertices
    
    int initialIndex = getIndex(vertex);
    if (initialIndex == NOT_FOUND){  //If entered vertex does not exist, return an empty list
      System.out.println("Invalid vertex was entered.");
      return resultList;
    }
    
    int currentVertex;
    
    LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();   //how we will keep track of when to stop.
    //Repeatly put vertices into the queue as we deal with them
    //We know we are done when the stack is empty, ie. when all
    //vertices have been dealt with
    boolean[] visited = new boolean[n];   //make a new array that keeps track of which vertices have been viisted
    
    traversalQueue.enqueue(initialIndex);   //enqueue our initial vertex
    visited[initialIndex] = true;           //our initial vertex has now been visited ie. traversed
    
    while (!traversalQueue.isEmpty())
    {
      currentVertex = traversalQueue.dequeue();    //dequeue a vertex from the queue
      resultList.add(vertices[currentVertex]);     //add it to our resultList because it's been "traversed"
      for (int i = 0; i < n; i++) {     //find every unvisited vertex that is adjacent to our current vertex
        if (arcs[currentVertex][i] && !visited[i])
        {
          traversalQueue.enqueue(i);   //add it to our queue
          visited[i] = true;           //mark it as visited
        }
      }
    }
    return resultList;
  }
  
  /******************************************************************
    Testing program. 
    ******************************************************************/
  public static void main (String args[]){
    System.out.println("Create a graph using the second constructor and the .tgf file representing our gameboard.");
    AdjMatGraphPlus<Cow> graph = new AdjMatGraphPlus<Cow>("graph.tgf");
    System.out.println(graph);
    System.out.println("Testing getVertex(0) [0]: ");
    Cow vertex0 = graph.getVertex(0);
    System.out.println(vertex0);
    System.out.println("Set vertex 0 to be a different cow name test");
    Cow newCow = new Cow("test", true);
    graph.setVertex(0, newCow);
    System.out.println("Testing getVertex(0) [test]: ");
    Cow vertex1 = graph.getVertex(0);
    System.out.println(vertex1);
    System.out.println("Printing the updated graph:");
    System.out.println(graph);
  }
}