import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 * class bbst has the implementation for insert, search & delete operations of Red-Black Tree
 * @author : Ananda Kishore Sirivella
 */

public class bbst {
	private Node root;
	private enum Color {RED, BLACK};
	static enum Opt {increase, reduce, count, inrange, next, previous, quit};
	
	// Function to print all the node details and its children detials.
	public void toString(Node x){
		if (x != null){
			String str = "Node: " + x.key + "; color : " + x.color ;
			if (x.left != null) str += "; left-child : " + x.left.key + "; color : " + x.left.color ;
			if (x.right != null) str += "; right-child : " + x.right.key + "; color : " + x.right.color ;
			System.out.println(str);
			
			if (x.left != null)	toString(x.left);
			if (x.right != null) toString(x.right);
		}
	}
	
	/*
	 * Main function for execution
	 * arg[0] contains the filename, using with we build red-black tree
	 * Operation on Red-black tree are carried out using system.in
	 */
	public static void main (String[] args) throws Exception {
		long time = System.currentTimeMillis();
		bbst rb = new bbst();
		Scanner scan = new Scanner(System.in);
		try {
			rb.loadbstfromfile(args[0]);
			//rb.loadbstfromfile("C:\\Games\\Project\\test_100.txt");
			System.out.println("--------------------------------------------------------------------------------");
			System.out.println("Time to build Red-Black tree : " + (System.currentTimeMillis() - time) + " milli seconds");
			System.out.println("--------------------------------------------------------------------------------");
			System.out.println("Proceed with the operations.");
			//rb.toString(rb.root);
			
			String cmd = "";
			StringTokenizer tkn;
			while((cmd = scan.nextLine())!=null) {
				tkn = new StringTokenizer(cmd);
				switch(Opt.valueOf(tkn.nextToken())) {
					case increase : rb.Increase(Integer.parseInt(tkn.nextToken()), Integer.parseInt(tkn.nextToken()));
						break;
					case reduce : rb.Reduce(Integer.parseInt(tkn.nextToken()), Integer.parseInt(tkn.nextToken()));
						break;
					case count : rb.Count(Integer.parseInt(tkn.nextToken()));
						break;
					case inrange : rb.InRange(Integer.parseInt(tkn.nextToken()), Integer.parseInt(tkn.nextToken()));
						break;
					case next : rb.Next(Integer.parseInt(tkn.nextToken()));
						break;
					case previous : rb.Previous(Integer.parseInt(tkn.nextToken()));
						break;	
					case quit : System.out.println("The End");
						System.exit(0);
						break;
					default: System.out.println("Not a valid operation");
						break;
				}
			}
		} finally {
			scan.close();
		}
	}
	
	/*
	 * Helper function to load the inputs from a file & build the Red-Black tree
	 */
	public void loadbstfromfile(String file){
		BufferedReader br = null;
	    try {
	    	
			br = new BufferedReader(new FileReader(file));
		    int i = 0;
		    String line;
		    StringTokenizer tkn;
		    line = br.readLine();
		    
		    int[] key = new int[Integer.parseInt(line)];
		    int[] val = new int[Integer.parseInt(line)];
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	tkn = new StringTokenizer(line);
		    	key[i] = Integer.parseInt(tkn.nextToken());
		    	val[i] = Integer.parseInt(tkn.nextToken());
		    	i++;
		    }
		    insertSorted(key, val, 0, i, 0, computeRedHeight(i));
		    
		} catch (FileNotFoundException e) {
			System.err.println("Unable to find the file:" + file);
		} catch (IOException e) {
			System.err.println("Unable to find the file:" + file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				br.close();
			} catch (IOException e) {
				System.err.println("Unable to close Buffered Reader");
			}
		}

	}

	/*
	 * Node class, for all the node key, data, its attributes & its helper function
	 */
	public class Node {
		int key, value;
		public Color color;
		public Node left, right, parent;
		
		public Node(int k){
			key = k;
		}
		public Node(int k, int v){
			key = k;
			value = v;
		}
		public Node(int k, int v, Color c){
			key = k;
			value = v;
			color = c;
		}
		public String getKey(){
			return "" + key;
		}
		public int getValue(){
			return value;
		}
		public Node getLeft(){
			return left;
		}
		public Node getRight(){
			return right;
		}
		public Node getParent(){
			return parent;
		}
		public Node getUncle(){
			if(parent != null && parent.parent != null){
				if(parent == parent.parent.left){
					return parent.parent.right;
				}else {
					return parent.parent.left;
				}
			}
			return null;
		}
		
		public void setKey(int k){
			key = k;
		}
		public void setValue(int v){
			value = v;
		}
		public void setLeft(Node l){
			left = l;
		}
		public void setRight(Node r){
			right = r;
		}
		public void setParent(Node p){
			parent = p;
		}
    }
	
	/*
	 * Function to increase count of a specific ID,create a ID if it doesn't exist.
	 * id : Node key
	 * value : Value to increment the node.value with
	 * 
	 * output: Final node value
	 */
	public void Increase(int id, int value){
		Node node;
		node = search(id);
		
		if(node == null){
			node = add(id, value);
		}else{
			node.value = node.value + value;
		}
		System.out.println(node.value);
	}

	/*
	 * Function to decrease count of a specific ID,delete a ID if its value is 0.
	 * id : Node key
	 * value : Value to decrement the node.value with
	 * 
	 * output: Final node value or 0
	 */
	public void Reduce(int id, int value){
		Node node;
		node = search(id);
		
		if(node != null){
			if(node.value <= value){
				delete(node);
				System.out.println("0");
			}else{
				node.value = node.value - value;
				System.out.println(node.value);
			}
		}else{
			System.out.println("0");
		}
	}

	/*
	 * Function to return the count of a specific ID.
	 * id : Node key
	 * 
	 * output: Node value
	 */
	public void Count(int id){
		Node node;
		node = search(id);
		
		if(node != null){
			System.out.println(node.value);
		}else{
			System.out.println("0");
		}
	}

	/*
	 * Function to find the sum of values between ID1 & ID2.
	 * id1 : Node 1 key
	 * id2 : Node 2 key
	 * 
	 * output: sum of the values of the ID between ID1 & ID2
	 */
	public void InRange(int id1, int id2){
		int temp;
		if(id1 > id2){
			temp = id2;
			id1 = id2;
			id2 = temp;
		}

		Node x = root;
		while(x != null){
			if(x.key >= id1 && x.key <= id2) break;
			if(x.key > id2) x = x.left;
			else if(x.key < id1) x = x.right;
		}
		ArrayList<Integer> value = new ArrayList<>();
		inorderRange(x, id1, id2, value);
		int sum = 0;
	    for(Integer i: value) sum = sum+i;
		System.out.println(sum);
	
	}
	
	// Helper function for inRange
	private void inorderRange(Node n,int id1,int id2,ArrayList<Integer> v){
		if(n == null) return;
		if(n.key >= id1) inorderRange(n.getLeft(),id1,id2,v);
		if(n.key >= id1 && n.key <= id2) v.add(n.getValue());
		if(n.key <= id2) inorderRange(n.getRight(),id1,id2,v);
	}

	/*
	 * Function to find the next lowest ID that is greater that theID and its value.
	 * id : Node key
	 * 
	 * output: next lowest node ID & its value
	 */
	public void Next(int key){
		Node x = root;
		Node y = null;
		int flag = 0;
		while(x != null){
			if(x.key < key){
				y = x;
				x = x.right;
				flag = 1;
			}
			else if(x.key > key){
				y = x;
				x = y.left;
				flag = 2;
			}
			else{
				y = x;
				flag = 0;
				break;
			}
		}
		y = (flag == 2)? y: findSuccessor(y);
		if(y != null) System.out.println(y.key + " " + y.value);
		else System.out.println("0 0");
	}

	/*
	 * Function to find the greatest key that is less that theID and its value.
	 * id : Node key
	 * 
	 * output: next greatest node ID & its value
	 */	
	public void Previous(int key){
		Node x = root;
		Node y = null;
		int flag = 0;
		while(x!=null){
			if(x.key < key){
				y = x;
				x = x.right;
				flag = 1;
			}
			else if(x.key > key){
				y = x;
				x = x.left;
				flag = 2;
			}
			else{
				y = x;
				flag = 0;
				break;
			}
		}
		y = (flag == 2)? y: findPredecessor(y);
		if(y != null) System.out.println(y.key + " " + y.value);
		else System.out.println("0 0");
	}
	
	public Node findSuccessor(int key){
		Node n = search(key);
		return findSuccessor(n);
	}
	
	public Node findPredecessor(int key){
		Node n = search(key);
		return findPredecessor(n);
	}

	public Node findSuccessor(Node n){
		if(n == null) return null;
		Node succ;
		if(n.right != null){
			succ = n.right; 
			while(succ.left != null){
				succ = succ.left;
			}
		}
		else {
			succ = n.parent;
			while(succ != null && n == succ.right){
				n = succ;
				succ = succ.parent;	
			}
		}
		if(succ == null) return null;
		return succ;
	}

	public Node findPredecessor(Node n){
		if(n == null) return null;
		Node pred;
		if(n.left != null){
			pred = n.left; 
			while(pred.right != null){
				pred = pred.right;
			}
		}
		else {
			pred = n.parent;
			while(pred != null && n == pred.left){
				n = pred;
				pred = pred.parent;
			}
		}
		if(pred == null) return null;
		return pred;
	}

	public Node min(Node x){
		while (x.left != null){
			x = x.left;
		}
		return x;
	}
	
	public Node max(Node x){
		while (x.right != null){
			x = x.right;
		}
		return x;
	}

	/*
	 * Helper function incase if you wanted to add a new node by its key and value
	 * input: key & its value
	 * 
	 * output: new node
	 */
	public Node add(int key, int val){
		Node node = new Node(key);
		node.value = val;
		node.color = Color.RED;
		
		if (root == null) {
			node.color = Color.BLACK;
			node.parent = null;
			root = node;
		}else{
			insert(node);
		}
		return node;
	}

	/*
	 * Helper function incase if you wanted to remove a node by its key
	 * input: key
	 */
	public void remove(int key){
		Node node;
		node = search(key);
		
		delete(node);
	}
	
	/*
	 * BST search logic
	 */
	public Node search(int k){
		Node T = root;
		while (T != null && k != T.key){
			if (k <= T.key) T = T.left;
			else T = T.right;
		}
		return T;
	}
	
	/*
	 * Function for leftRotate the Tree
	 * x = node to left-rotate
	 * T = tree head
	 * 
	 */
	public void leftRotate(Node x){
		Node y;
		
		y = x.right;
		x.right = y.left;
		if (y.left != null) y.left.parent = x;
		y.parent = x.parent;
		if (x.parent == null) root = y;
		else if ( x == x.parent.left) x.parent.left = y;
		else x.parent.right = y;
		y.left = x;
		x.parent = y;
	}
	

	/*
	 * Function for rightRotate of the Tree
	 * x = node to right-rotate
	 * T = tree head
	 * 
	 */
	public void rightRotate(Node x){
		Node y;
		
		y = x.left;
		x.left = y.right;
		if (y.right != null) y.left.parent = x;
		y.parent = x.parent;
		if (x.parent == null) root = y;
		else if (x == x.parent.right) x.parent.right = y;
		else x.parent.left = y;
		y.right= x;
		x.parent =  y;
	}
	
	/*
	 * Function for initial creation of Red-black tree in O(n)
	 * input:
	 * 	key : list of keys
	 * 	value : list of value
	 * 	start & end : the start & end of the sub-tree
	 * 	count : present height of the tree, 0 at the root and log(n) at the leafs
	 * 	height: the maximum value of the height a tree can grow when constructed evenly. 
	 */
	private Node insertSorted(int[] key,int[] value,int start,int end,int count,int height){
		if(start > end) return null;
		int mid = (start+end)/2;
		if(mid >= key.length) return null;
		Node  node = new Node(key[mid], value[mid], Color.BLACK);
		if(root == null) root = node;
		
		Node  left = insertSorted(key, value, start, mid-1,count+1,height);
		node.left = left;
		if(left != null) left.parent = node;
		
		Node  right = insertSorted(key, value, mid+1, end,count+1,height);
		node.right = right;
		if(right!=null) right.parent = node;
		
		if(count==height) node.color = Color.RED;
		return node;
	}
	/*
	 * Helper function to computer the maximum Redheight
	 */
    private static int computeRedHeight(int size) {
        int height = 0;
        for (int x = size - 1; x >= 0; x = x / 2 - 1){
            height++;
        }
        return height;
    }
	
    /*
     * Function to insert a single node into Red-Black tree
     * z = node to insert
     */
	public void insert(Node z){
		Node x,y;
		
		
		y = null;
		x = root;
		while (x != null){
			y = x;
			if (z.key < x.key) x =  x.left;
			else x = x.right;
		}
		z.parent = y;
		if (y == null) root = z;
		else if (z.key < y.key) y.left = z;
		else y.right = z;
		z.left = null;
		z.right = null;
		z.color = Color.RED;
		insertFixup(z);
	}

    /*
     * Helper function to color-fixup after insertion of a node 
     */	
	public void insertFixup(Node z){
		while (z != root && z.parent.color == Color.RED){
			if (z.parent == z.parent.parent.left){
				if (z.parent.parent.right != null && z.parent.parent.right.color == Color.RED){
					z.parent.color = Color.BLACK;
					z.parent.parent.right.color = Color.BLACK;
					z.parent.parent.color = Color.RED;
					z = z.parent.parent;
				}else{
					if (z == z.parent.right){
						z = z.parent;
						leftRotate(z);
					}
					z.parent.color = Color.BLACK;
					z.parent.parent.color = Color.RED;
					rightRotate(z.parent.parent);
				}
			}else {
				if (z.parent.parent.left != null && z.parent.parent.left.color == Color.RED){
					z.parent.color = Color.BLACK;
					z.parent.parent.left.color = Color.BLACK;
					z.parent.parent.color = Color.RED;
					z = z.parent.parent;
				}else{
					if (z == z.parent.parent.left){
						z = z.parent;
						rightRotate(z);
					}
					z.parent.color = Color.BLACK;
					z.parent.parent.color = Color.RED;
					leftRotate(z.parent.parent);
				}
			}
		}
		root.color = Color.BLACK;
	}
	

	//Transplanting node u with node v
	public void transplant(Node u,Node v){
		if (u.parent == null) root = v;
		else if (u == u.parent.left) u.parent.left = v;
		else u.parent.right = v;
		v.parent = u.parent;
	}

    /*
     * Function to delete a single node from Red-Black tree
     * z = node to delete
     */
	public Node delete(Node z) {
	    Node x = null,y;
	    Color y_orig_color;
	    
	    y = z;
	    y_orig_color = y.color;
	    if (z.left == null && z.right != null) {
	        x = z.right;
	        transplant(z,z.right);
	    }else if (z.right == null && z.left != null) {
	        x = z.left;
	        transplant(z,z.left);
	    }else if(z.right != null && z.left != null){
	    	y = z.right;
	    	while(y.left != null){
	    		y = y.left;
	    	}
	    	y_orig_color = y.color;
	    	x = y.right;
	    	if (y.parent == z){
	    		if(y != null && x != null){
	    			x.parent = y;
	    		}
	    	}else {
	    		if(y.right != null){
	    			transplant(y,y.right);
	    			y.right = z.right;
	    			y.right.parent = y;
	    		}
	    	}
	    	transplant(z,y);
	    	y.left = z.left;
	    	y.left.parent = y;
	    	y.color = z.color;
	    }

	    if (y_orig_color == Color.BLACK) {
	        deleteFixup(x);
	    }

	    return y;
	}

    /*
     * Helper function to color fixup the red-black tree
     */
	public void deleteFixup(Node x) {
		Node w;
		
		while (x != null && x != root && x.color == Color.BLACK){
			if (x == x.parent.left){
				w = x.parent.right;
				if (w.color == Color.RED){
					w.color = Color.BLACK;
					x.parent.color = Color.RED;
					leftRotate(x.parent);
					w = x.parent.right;
				}
				if (w.left.color == Color.BLACK && w.right.color == Color.BLACK){
					w.color = Color.RED;
					x = x.parent;
				}else{
					if (w.right.color == Color.BLACK){
						w.left.color = Color.BLACK;
						w.color = Color.RED;
						rightRotate(w);
						w = x.parent.right;
					}
					w.color = x.parent.color;
					x.parent.color = Color.BLACK;
					w.right.color = Color.BLACK;
					leftRotate(x.parent);
					x = root;
				}
			}else{
				w = x.parent.left;
				if (w.color == Color.RED){
					w.color = Color.BLACK;
					x.parent.color = Color.RED;
					rightRotate(x.parent);
					w = x.parent.left;
				}
				if (w.left.color == Color.BLACK && w.left.color == Color.BLACK){
					w.color = Color.RED;
					x = x.parent;
				}else if(w.left != null && w.right != null){
					if (w.left.color == Color.BLACK){
						w.right.color = Color.BLACK;
						w.color = Color.RED;
						leftRotate(w);
						w = x.parent.left;
					}
					w.color = x.parent.color;
					x.parent.color = Color.BLACK;
					w.left.color = Color.BLACK;
					rightRotate(x.parent);
					x = root;
				}
			}
		}
		root.color = Color.BLACK;
	}
}