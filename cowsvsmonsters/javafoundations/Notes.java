//Ashley Thomas
//Notes.java

public class Notes {
  
  /*
   * String j = "John";
   * String p = "Paul";
   * String g = "George";
   * String r = "Ringo";
   * String t = "Takis";
   * LinkedBinaryTree<String> t1,t2,t3,beatles;
   * 
   * t1 = new LinkedBinaryTree<String>(j);  //single node tree
   * t2 = new LinkedBinaryTree<String>(p, new LinkedBinaryTree<String>(), new LinkedBinaryTree<String>(g));
   *                                 //root        //left subtree                //right subtree
   * t3 = new LinkedBinaryTree<String>(r, t1, new LinkedBinaryTree());
   * 
   * beatles = new LinkedBinaryTree<String>(t, t2, t3);
   * Tree looks like:
   *                    Takis
   *          Paul                 Ringo
   *      N/A     George       John      N/A
   * 
   * System.out.println(t1);   //John
   * System.out.println(t2);   //right now, the class uses in order traversal to print them out
   *                           //Paul, George
   * The above things use toString() --> create an iterator using iterator() (in LinkedBinaryTree class)
   *     iterator() chooses the traversal path (right now, in order)
   *     public String toString() {
   *         Iterator<T> iter = iterator();
   *         String result;
   *         while (iter.hasNext())
   *             result += iter.next() + "\n";
   *         return result; }
   * 
   * System.out.println(t2.getLeft());   //just an empty String :)
   * System.out.println(t2.getLeft().getLeft())   //should throw an exception, because there is no left subtree of an empty node
   *                                              //exception comes from getLeft() method
   * 
   * 
   * 4 Traversal Methods
   *   in order (L, Root, R)
   *     Paul, George, Takis, John, Ringo
   *   pre order (Root, L, R) 
   *     Takis, Paul, George, Ringo, John
   *   post order (L, R, Root)
   *     George, Paul, Takis, John, Ringo
   *   level order (in order of the levels, from top to bottom :) )
   *     Takis, Paul, Ringo, George, John
   */
  
  
}