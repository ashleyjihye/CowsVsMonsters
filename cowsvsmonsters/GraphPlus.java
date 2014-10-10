/********************************************************************
  *  GraphPlus.java  (expanded interface for Exam 3)
  *
  *  Defines an expanded Graph interface for a graph data structure.
  *  There are a number of methods to be defined for Exam 3.
  *  You can use as your starting point the sample solutions of HW7
  *  or use your own implementation of HW7.
  *
  *  @author Takis Metaxas
  *  @date April 18, 2013
  *********************************************************************/

import java.util.*;

public interface GraphPlus<T>
{
  /** Returns true if this graph is empty, false otherwise. */
  public boolean isEmpty();
  
  /** Returns the number of vertices in this graph. */
  public int n();
  
  /** Returns the number of arcs in this graph. */
  public int m();
  
  /** Returns true iff a directed edge exists from v1 to v2. */
  public boolean isArc (T vertex1, T vertex2);
  
  /** Returns true iff an edge exists between two given vertices
    * which means that two corresponding arcs exist in the graph */
  public boolean isEdge (T vertex1, T vertex2);
  
  /** Returns true IFF the graph is undirected, that is, for every
    * pair of nodes i,j for which there is an arc, the opposite arc
    * is also present in the graph.  */
  public boolean isUndirected();
  
  /** Adds a vertex to this graph, associating object with vertex.
    * If the vertex already exists, nothing is inserted. */
  public void addVertex (T vertex);
  
  /** Removes a single vertex with the given value from this graph.
    * If the vertex does not exist, it does not change the graph. */
  public void removeVertex (T vertex);
  
  /**     Inserts an arc from vertex1 to vertex2.
    If the vertices exist. Else it does not change the graph. */
  public void addArc (T vertex1, T vertex2);
  
  /** Removes an arc from vertex v1 to vertex v2,
    * if the vertices exist. Else it does not change the graph. */
  public void removeArc (T vertex1, T vertex2);
  
  /** Inserts an edge between two vertices of this graph,
    * if the vertices exist. Else does not change the graph. */
  public void addEdge (T vertex1, T vertex2);
  
  /** Removes an edge between two vertices of this graph,
    if the vertices exist, else does not change the graph. */
  public void removeEdge (T vertex1, T vertex2);
  
  /** Retrieve from a graph the vertices x following vertex v (v->x)
    and returns them onto a linked list */
  public LinkedList<T> getSuccessors(T vertex);
  
  /** Retrieve from a graph the vertices x pointing to vertex v (x->v)
    and returns them onto a linked list */
  public LinkedList<T> getPredecessors(T vertex);
  
  /** Returns a string representation of the adjacency matrix. */
  public String toString();
  
  /** Saves the current graph into a .tgf file.
    If it cannot save the file, a message is printed. */
  public void saveTGF(String tgf_file_name);
  
  
  
  
  
// Methods to implement for Exam 3
  // NOTES:
  
  // I award partial credit, so make sure you show and document your code even if it is not fully functional.
  // The points indicated above assume that you have provided a driver with good testing code that proves that your implementation works.
  
  // For hard copy, please bring to class the cover page documenting what works and what does not, and the implementation AdjMatGraph.java. No need for printing other files (including any extra driver you may have created).
  
  // In the soft copy you should also include any driver code and a transcript of your testing showing what you expected to see and what your program actually computes.
  // If you created your own .tgf files to test your code, do include these files with your soft copy.
  
  // Make sure you also submit the evaluation questionnaire of the exam you received by email.
  
  // GOOD LUCK!
  
  
  /******************************************************************
    * [10 pts] Clones the graph by saving the current one on the disk 
    * as TEMP.tgf using saveTGF() and creating a new one using the 
    * second constructor. 
    * @return the new graph.
    * FEATURE: It does not try to delete the file from the disk
    *****************************************************************/
  public GraphPlus<T> clone ();
  
  
  /******************************************************************
    * [10 pts] Checks if a vertex is a sink (points to no other vertex)
    * @return true if the vertex is a sink, false if it is not.
    ******************************************************************/
  public boolean isSink (T vertex);
  
  /******************************************************************
    * [10 pts] Checks if a vertex is a source (no vertex points to it)
    * @return true if the vertex is a source, false if it is not
    ******************************************************************/
  public boolean isSource (T vertex);
  
  /******************************************************************
    * [15 pts] Retrieves the vertices that are sinks and 
    * @return all the sinks in a linked list
    ******************************************************************/
  public LinkedList<T> allSinks();
  
  /******************************************************************
    * [15 pts] Retrieves the vertices that are sources and 
    * @return all the sources in a linked list
    ******************************************************************/
  public LinkedList<T> allSources();
  
  /******************************************************************
    * [5 pts] Checks if a vertex is a isolated, that is,
    * it is neither pointing to another vertex nor is pointed by one
    * @return true if the vertex is isolated, false if it is not
    ******************************************************************/
  public boolean isIsolated (T vertex);
  
  
  /******************************************************************
    * [15 pts] Topologically sort vertices of a directed acyclic graph
    * using one of the two algorithms presented in class. 
    * PREREQUISITE: The input graph must be a DAG, i.e., have NO CYCLES.
    * @return the topologically sorted vertices in a linked list
    ******************************************************************/
  public LinkedList<T> topologicalSort();
  
  /******************************************************************
    * [10 pts] Returns a LinkedList contining a DEPTH first search traversal
    * of the graph starting at the given index. If the index is not valid, 
    * it returns an empty list.
    * You can use as a starting point the pseudocode in the handout
    * or the Iterator code in the book.
    * @return a linked list with the vertices in depth-first order
    *****************************************************************/
  public LinkedList<T> DFS(T vertex);
  
  /******************************************************************
    * [10 pts] Returns a LinkedList contining a BREADTH first search traversal 
    * of a graph starting at the given index. If the index is not valid, 
    * it returns an empty list
    * You can use as a starting point the pseudocode in the handout
    * or the Iterator code in the book.
    * @return a linked list with the vertices in breadth-first order
    *****************************************************************/
  public LinkedList<T> BFS(T vertex);
  
}
