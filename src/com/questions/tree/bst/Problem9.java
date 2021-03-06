package com.questions.tree.bst;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;
import com.util.Read;
import com.data.tree.binary.BinarySearchTree;
import com.data.tree.binary.BinarySearchTree.Node;

import java.util.Arrays;
import com.questions.Problem;
import com.questions.Solution;


public class Problem9 extends Problem{
	public Problem9(){
		super();
		this.solutions.add(new Solution1());
		this.solutions.add(new Solution2());
		this.solutions.add(new Solution3());
	}

	public String question(){
		return "Find the least common ancestore (deepest ancestor) of two nodes in a binary tree. " +
				"A least common ancestor is a node of which both the given nodes " +
				"are descendant.";
	}
	
	public Map readParameters(PrintWriter writer, BufferedReader reader) throws Exception{
	    Map<String, Object> options = new HashMap<String, Object>();
	        
		Read read = new Read(writer, reader);

		BinarySearchTree tree = null;
		String treetype = read.string("Tree type (Manual/Random): ");
		tree = ("manual".equalsIgnoreCase(treetype)) ? 
			read.binaryTree() : 
			read.randomBinaryTree();
			
		writer.println("========== GENERATE TREE ==============");
		tree.print(writer);
		writer.println("========== END OF TREE ==============");
		
		
		options.put("tree", tree);
		
		Integer v1 = read.integer("Value of first node: ");
		Integer v2 = read.integer("Value of second node: ");
		
		if(v2 < v1){
			Integer tmp = v1;
			v1 = v2;
			v2 = tmp;
		}
		
		options.put("v1", v1);
		options.put("v2", v2); 
		
		return options;
	}
	
	public class Solution3 implements Solution{

		@Override
		public void execute(Map options, PrintWriter out) {
			BinarySearchTree tree = (BinarySearchTree) options.get("tree");
			Comparable v1 = (Comparable) options.get("v1");
			Comparable v2 = (Comparable) options.get("v2");
			
			//assume that v1 is always less than v2 -- readParameter is taking care of that
			Node current = tree.getHead();
			while(current != null){
				Comparable value = current.value();
				int d1 = v1.compareTo(value);
				int d2 = v2.compareTo(value);

				// if both are smaller than current, go left
				if(d1 < 0 && d2 < 0) current = current.left();
				// if both are bigger than go right
				else if (d1 > 0 && d2 > 0) current = current.right();
				// else we found deepest ancestor --hence break
				else break;
			}
		
			if(current == null) out.println("Failed to find deepest ancestor");
			else out.println("Deepest ancestor is " + current.value());
			
		}

		@Override
		public String describe() {
			return "Start from root, if root's number is between given two numbers or " +
					"equal to one of them, return root's number, else if root less than " +
					"both given numbers, go to root's right child, repeat " +
					"else if root greater than both given numbers. go to root's left child, repeat";
			
		}

		@Override
		public String timeComplexity() {
			return "O(log(N))";
		}

		@Override
		public String spaceComplexity() {
			return "O(1)";
		}
		
	}
	
	public class Solution1 implements Solution{
	        private BinarySearchTree tree = null;
		private Comparable v1 = null;
		private Comparable v2 = null;
		
		public void execute(Map options, PrintWriter writer){
		    if(options == null) return;
		    this.tree = (BinarySearchTree) options.get("tree");
		    this.v1 = (Comparable) options.get("v1");
		    this.v2 = (Comparable) options.get("v2");
		    
		    if(v1.compareTo(v2) > 0 ){
		        Comparable tmp = v1;
		        v1 = v2;
		        v2 = tmp;
		    }
		    
		    //Sanity checks
		    if(v1.compareTo(v2) == 0){
		        writer.println("Two values are same");
		        return;
		    }
		    
		    writer.println("Objective: find least common ancestor for " + v1 + ", " + v2);
		    
		    writer.println("Step1: Generate InOrder List");
		    Comparable[] inOrder = tree.inOrder();
		    int length = inOrder.length;
		    writer.println(Arrays.toString(inOrder));
		    
		    
		    // get candidate list. It all the nodes starting from v1 to v2
		    writer.println("\nStep2: Locate v1 and v2");
		    Map<Comparable, Comparable> candidates = new HashMap<Comparable, Comparable>();
		    int i=0;
		    while( i < length && inOrder[i].compareTo(v1) != 0) i++;
		    int j = inOrder.length-1;
		    while(j > -1 && inOrder[j].compareTo(v2) != 0) j--;
		    
		    //Sanity check
		    if(i == length || j == -1){
		        writer.println("Either v1 or v2 or both are beyond valid range");
		        return;
		    }
		    writer.println("Candidates position: " + i + ", " + j);
		    
		    writer.println("\nStep3: Get Candidate list -- all nodes betwen v1 and v2 inclusive");
		    for(int k=i; k <= j; k++){
		    	writer.print(inOrder[k] + ", ");
		    	candidates.put(inOrder[k], inOrder[k]);
		    }
		     
		    
		    if(candidates.size() == 2){
		        writer.println("Ancestor is " + inOrder[i+1]);
		        return;
		    } 
		    
		     
		    //Get Post Order List
		    writer.println("\nStep4: Get Post Order List");
		    Comparable[] postOrder = tree.postOrder();
		    writer.println(Arrays.toString(postOrder));
		    
		    
		    writer.println("\n Step5: Find the candidate that appears last in postOrder");
		    for(int k=length-1; k>= 0; k--){
		       if(!candidates.containsKey(postOrder[k])) continue;
		       writer.println("Ancestor is: " + postOrder[k]);
		       return;
		    }
		    
		    writer.println("Failed to find any ancestor");
		    return;
		    
		    
		    	
		}
		
	
		public String describe(){
		     return "Traverse the tree using inOrder and PostOrder. In order will give list of nodes that are in between the two given nodes. Then check which of these nodes appear at last in the postOrder list.  Assumes no duplicates";
		}
		
		public String timeComplexity(){
			//TODO: return time complexity of the solution
			return "O(N)";
		}
		
		public String spaceComplexity(){
			//TODO: return space complexity of the solution
			return "O(3N) -- in worst case, you will need to store all n nodes three times: inOrder, preOrder, candidates";
		}
		
	}
	
		
	public class Solution2 implements Solution{
	        private BinarySearchTree tree = null;
		private Comparable v1 = null;
		private Comparable v2 = null;
		
		public void execute(Map options, PrintWriter writer){
		    if(options == null) return;
		    this.tree = (BinarySearchTree) options.get("tree");
		    this.v1 = (Comparable) options.get("v1");
		    this.v2 = (Comparable) options.get("v2");
		    
		    if(v1.compareTo(v2) > 0 ){
		        Comparable tmp = v1;
		        v1 = v2;
		        v2 = tmp;
		    }
		    
		    //Sanity checks
		    if(v1.compareTo(v2) == 0){
		        writer.println("Two values are same");
		        return;
		    }
		    
		    writer.println("Objective: find least common ancestor for " + v1 + ", " + v2);
		    
		    BinarySearchTree.Node node = leastCommonAncestor(tree.getHead());
		    
		    if(node != null) writer.println("Ancestor is: " + node.value());
		    else writer.println("Failed to find common ancestor");
		    
		    
		    	
		}
		
		private BinarySearchTree.Node leastCommonAncestor(BinarySearchTree.Node node){
		   if(node == null) return null;
		   
		   if(v1.compareTo(node.value()) == 0) return node;
		   if(v2.compareTo(node.value()) == 0) return node;
		   
		   BinarySearchTree.Node left = leastCommonAncestor(node.left());
		   BinarySearchTree.Node right = leastCommonAncestor(node.right());
		   
		   if(left != null && right != null) return node;
		   else if(left != null) return left;
		   else if(right != null) return right;
		   else return null;
		       
		}
		
	
		public String describe(){
		     return "Uses recursive logic. Fails to handle missing values.";
		}
		
		public String timeComplexity(){
			//TODO: return time complexity of the solution
			return "O(N)";
		}
		
		public String spaceComplexity(){
			//TODO: return space complexity of the solution
			return "O(1)";
		}
		
	}

	
}