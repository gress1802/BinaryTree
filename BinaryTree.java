import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;


public class BinaryTree {
    //Implements a Binary Tree of Strings
    private class Node {
        private Node left;
        private String data;
        private Node right;
        private Node parent; //reference to the parent node 
        // the parent is null for the root node

        private Node(Node L, String d, Node r, Node p) {
        left = L;
        data = d;
        right = r;
        parent = p;

        }
    }
    private Node root;
    private String open = "(";//These are attributes that are set in the constructors
    private String close = ")";//They are here so that the toString method can correctly build the string again
    private String empty = "!";
    public BinaryTree() {
    //create an empty tree
        root = null;
    }
    
    public BinaryTree(String d) {
    //create a tree with a single node
    root = new Node(null,d,null,null);
    
    } 

    public BinaryTree(BinaryTree b1, String d, BinaryTree b2) { 
    //merge the trees b1 AND b2 with a common root with data d
    // this constructor must make a copy of the contents of b1 and b2
        Node b1RootCopy = copyRoot(b1.root);
        Node b2RootCopy = copyRoot(b2.root);
        Node x = new Node(b1RootCopy, d, b2RootCopy, null);
        this.root = x; //using a variable x so that we can set the parent value of the two trees
        if(b1RootCopy != null) b1RootCopy.parent = x; //These two lines (41 and 42) both assign the parent pointers for the tree
        if(b2RootCopy != null) b2RootCopy.parent = x;

    }

    private Node copyRoot(Node r){
        if(r == null){ //makes a copy of an empty tree
            return null;
        }
        Node rootCopy = new Node(r.left,r.data,r.right,r.parent);
        if(rootCopy.left != null) rootCopy.left.parent = rootCopy;
        if(rootCopy.right != null) rootCopy.right.parent = rootCopy;
        return rootCopy;
    }

    public BinaryTree(String t, String open, String close, String empty) {
        /*create a binary tree from the post order format discussed 
        in class. Assume t is a syntactically correct string 
        representation of the tree. Open and close are the strings 
        which represent the beginning and end markers of a tree.
        Empty represents an empty tree.
        The example in class used ( ) and ! for open, close and 
        empty respectively.
        The data in the tree will not include strings matching 
        open, close or empty. 
        All tokens (data, open, close and empty) will be separated 
        By white space
        Most of the work should be done in a private recursive 
        method 
        */
        this.open = open;
        this.close = close;
        this.empty = empty;
        if(t.equals("")){
            root = null;
        }else{
            Scanner scan = new Scanner(t);
            this.root = recursiveAdd(scan,open,close,empty).root;
            scan.close();
        }
    }

    public void binaryPrintThree(){ //testing method that prints the root and its two childeren
        System.out.println("Root:"+root.data+" Right:"+root.right.data+" Left:"+root.left.data+" Right Parent: "+root.right.parent.data+" Left Parent: "+root.left.parent.data);
    }

    private BinaryTree recursiveAdd(Scanner scan, String open, String close, String empty){
        String d;
        BinaryTree b1;
        BinaryTree b2;
        String t = scan.next();
        if(t.equals(empty)) return new BinaryTree();
        b1 = recursiveAdd(scan,open,close,empty);
        b2 = recursiveAdd(scan,open,close,empty);
        d = scan.next();
        while(d.equals(open) | d.equals(close) | d.equals(empty)){ //loop that sets d to the next value node in the string
            d = scan.next();
        }
        scan.next(); //closing now )
        return new BinaryTree(b1,d,b2);


        
    }
 

    public class PostorderIterator implements Iterator<String> {
        //An iterator that returns data in the tree in an post order pattern
        //the implementation must use the parent pointer and must not use an 
        //additional data structure
        Node temp = root; //Helper attribute to traverse the tree
        boolean notFirst = false; //This attribute is used in order to not check the tree's parent pointer for null on the first call of next()
        
        public PostorderIterator(){
        }

        public void findBottom(Node tempRoot){
            notFirst = true;
            while(tempRoot.left != null | tempRoot.right != null){
                if(tempRoot.left != null){
                    tempRoot = tempRoot.left;
                }else if(tempRoot.right != null){
                    tempRoot = tempRoot.right;
                }
                temp = tempRoot;
            }
            temp = tempRoot;

            
        }


        
        
        public boolean hasNext() {
            return !(temp.parent == null);
        }
        
        public String next(){
            if(notFirst){
                if(!hasNext()){
                    return "Empty Tree!";
                }
            }
            if(temp == null){
                return "Empty Tree!";
            }else if(temp == root){
                findBottom(temp);
                return temp.data;
            }else{
                Node tempParent = temp.parent;
                if(tempParent.right == temp){
                    temp = tempParent;
                    tempParent = temp.parent;
                    return temp.data;
                }
                else if(tempParent.left != null && tempParent.left.data == temp.data){
                    if(tempParent.right == null){
                        temp = tempParent;
                        return tempParent.data;
                    }else{
                        findBottom(tempParent.right);
                        return temp.data;
                    }
                }else if(tempParent.right != null && tempParent.right.data == temp.data){
                    if(tempParent.left == null){
                        temp = tempParent;
                        return tempParent.data;
                    }else{
                        findBottom(tempParent.left);
                        return temp.data;
                    }
                }
                return temp.data;
            }
        }
        
        public void remove() {//removes the node that temp is currently equal to
        }

 
    }
 
    public class InorderIterator implements Iterator<String> {
        //An iterator that returns data in the tree in an in order pattern
        //This implementation must use a stack and must not use the parent pointer 
        //You must use Java’s stack class
        Stack<Node> data;
        Node temp;

        public InorderIterator() {
            data = new Stack<Node>();
            temp = root;
            setup();
        }

        public void setup(){
            while(temp != null){
                data.push(temp);
                temp = temp.left; //adding the left most nodes to the stack (Including the head)
            }
        }
        
        public boolean hasNext() {
            return !data.empty();
        }
        
        public String next() {
            if(!hasNext()) return "Empty Tree!";
            Node ret = data.pop();
            if(ret.right != null){
                Node tempRight = ret.right;
                while(tempRight != null){
                    data.push(tempRight);
                    tempRight = tempRight.left;
                }
            }
            return ret.data;
        }
        
        public void remove() {
        //optional method not implemented
        }
    }

    public class PreorderIterator implements Iterator<String> {
        //An iterator that returns data in the tree in a pre order pattern
        //This iterator will recursively do the complete iteration saving the
        //values in the order they are visited in a list (you can use ArrayList or 
        //LinkedList
        LinkedList<Node> data;
        int x;

        public PreorderIterator() {
            data = new LinkedList<Node>();
            preOrder(root);
            x = 0;
        }

        public void preOrder(Node r){
            if(r != null){
                data.add(r);
                preOrder(r.left);
                preOrder(r.right);
            }
        }
        
        public boolean hasNext() {
            return (x != 1+data.indexOf(data.getLast()));
        }
        
        public String next() {
            while(hasNext()){
                if(data.size() == 0) return "Empty Tree!";
                String ret = data.get(x).data;
                x++;
                return ret;
            }
            return "Empty Tree!";
        }
        
        public void remove() {
        //optional method not implemented
        }
    }
    
    public class LevelorderIterator implements Iterator<String> {
        //An iterator that returns data in the tree in a level order pattern
        //This implementation uses a FIFOQueue
       
        Queue<Node> data;

        public LevelorderIterator() {
            data = new LinkedList<Node>();
            data.add(root);
        
        }
        
        public boolean hasNext() {
            return !data.isEmpty();
        }
        
        public String next() {
            if(!hasNext()){
                return "Empty Tree!";
            }

            Node currentNode = data.poll();
            if(currentNode.left != null) data.add(currentNode.left);
            if(currentNode.right != null) data.add(currentNode.right);
            return currentNode.data;
        }
        
        public void remove() {

        //optional method not implemented
        }
    }

    public Iterator<String> inorder() {
        return new InorderIterator();
        //return a new in order iterator object 
    } 
    
    public Iterator<String> postorder() {
        return new PostorderIterator();
        //return a new post order iterator object 
    } 
     
    public Iterator<String> preorder() {
        return new PreorderIterator();
        //return a new pre order iterator object 
    } 
     
    public Iterator<String> levelorder() {
        return new LevelorderIterator();
        //return a new level order iterator object 
    }  
    
    public String ret; //Helper attribute for String for returning
    public String toStringHelper(Node r){ //recursive method that helps produce a string representation of the tree
        if(r == null){ ret =ret +empty+" "; return ret;}
        ret = ret +open+" ";
        toStringHelper(r.left);
        toStringHelper(r.right);
        ret = ret+r.data + " "+close+" ";
        return ret;
        
        
    }

    public String toString() {// A toString() method
        ret = "";
        return ret = toStringHelper(this.root);

        //returns the string representation of the tree using the post order format 
        // discussed in class. If the tree was created from a string, use the 
        //the values of open, close and empty given to the constructor otherwise 
        //use (, ) and ! for open, close and empty respectively
        //most of the work should be done in a recursive private method.
    }

}


