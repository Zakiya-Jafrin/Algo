package com.questions.tree.bst;


import java.util.Map;
import java.util.HashMap;
import com.util.Read;
import com.data.BinaryTree;
import java.util.Arrays;
import com.questions.Problem;
import com.questions.Solution;


public class Problem9 extends Problem{
	public Problem9(){
		super();
		this.solutions.add(new Solution1());
		this.solutions.add(new Solution2());
	}

	public String question(){
		return "Find the least common ancestore of two nodes in a binary tree. A least common ancestor is a node that of which both the give nodes are descendant.";
	}
	
	public Map readParameters() throws Exception{
	    Map<String, Object> options = new HashMap<String, Object>();
	        
		Read read = new Read();

		BinaryTree tree = null;
		String treetype = read.string("Tree type (Manual/Random): ");
		tree = ("manual".equalsIgnoreCase(treetype)) ? 
			read.binaryTree() : 
			read.randomBinaryTree();
			
		System.out.println("========== GENERATE TREE ==============");
		tree.print();
		System.out.println("========== END OF TREE ==============");
		
		
		options.put("tree", tree);
		
		Integer v1 = read.integer("Value of first node: ");
		Integer v2 = read.integer("Value of second node: ");
		options.put("v1", v1);
		options.put("v2", v2); 
		
		return options;
	}
	
	
	
	public class Solution1 implements Solution{
	        private BinaryTree tree = null;
		private Comparable v1 = null;
		private Comparable v2 = null;
		
		public void execute(Map options){
		    if(options == null) return;
		    this.tree = (BinaryTree) options.get("tree");
		    this.v1 = (Comparable) options.get("v1");
		    this.v2 = (Comparable) options.get("v2");
		    
		    if(v1.compareTo(v2) > 0 ){
		        Comparable tmp = v1;
		        v1 = v2;
		        v2 = tmp;
		    }
		    
		    //Sanity checks
		    if(v1.compareTo(v2) == 0){
		        System.out.println("Two values are same");
		        return;
		    }
		    
		    System.out.println("Objective: find least common ancestor for " + v1 + ", " + v2);
		    
		    System.out.println("Step1: Generate InOrder List");
		    Comparable[] inOrder = tree.inOrder();
		    int length = inOrder.length;
		    System.out.println(Arrays.toString(inOrder));
		    
		    
		    // get candidate list. It all the nodes starting from v1 to v2
		    System.out.println("\nStep2: Locate v1 and v2");
		    Map<Comparable, Comparable> candidates = new HashMap<Comparable, Comparable>();
		    int i=0;
		    while( i < length && inOrder[i].compareTo(v1) != 0) i++;
		    int j = inOrder.length-1;
		    while(j > -1 && inOrder[j].compareTo(v2) != 0) j--;
		    
		    //Sanity check
		    if(i == length || j == -1){
		        System.out.println("Either v1 or v2 or both are beyond valid range");
		        return;
		    }
		    System.out.println("Candidates position: " + i + ", " + j);
		    
		    System.out.println("\nStep3: Get Candidate list -- all nodes betwen v1 and v2 inclusive");
		    for(int k=i; k <= j; k++){
		    	System.out.print(inOrder[k] + ", ");
		    	candidates.put(inOrder[k], inOrder[k]);
		    }
		     
		    
		    if(candidates.size() == 2 || candidates.size() == 3){
		        System.out.println("Ancestor is " + inOrder[i+1]);
		        return;
		    } 
		    
		     
		    //Get Post Order List
		    System.out.println("\nStep4: Get Post Order List");
		    Comparable[] postOrder = tree.postOrder();
		    System.out.println(Arrays.toString(postOrder));
		    
		    
		    System.out.println("\n Step5: Find the candidate that appears last in postOrder");
		    for(int k=length-1; k>= 0; k--){
		       if(!candidates.containsKey(postOrder[k])) continue;
		       System.out.println("Ancestor is: " + postOrder[k]);
		       return;
		    }
		    
		    System.out.println("Failed to find any ancestor");
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
	        private BinaryTree tree = null;
		private Comparable v1 = null;
		private Comparable v2 = null;
		
		public void execute(Map options){
		    if(options == null) return;
		    this.tree = (BinaryTree) options.get("tree");
		    this.v1 = (Comparable) options.get("v1");
		    this.v2 = (Comparable) options.get("v2");
		    
		    if(v1.compareTo(v2) > 0 ){
		        Comparable tmp = v1;
		        v1 = v2;
		        v2 = tmp;
		    }
		    
		    //Sanity checks
		    if(v1.compareTo(v2) == 0){
		        System.out.println("Two values are same");
		        return;
		    }
		    
		    System.out.println("Objective: find least common ancestor for " + v1 + ", " + v2);
		    
		    BinaryTree.Node node = leastCommonAncestor(tree.getHead());
		    
		    if(node != null) System.out.println("Ancestor is: " + node.value());
		    else System.out.println("Failed to find common ancestor");
		    
		    
		    	
		}
		
		private BinaryTree.Node leastCommonAncestor(BinaryTree.Node node){
		   if(node == null) return null;
		   
		   if(v1.compareTo(node.value()) == 0) return node;
		   if(v2.compareTo(node.value()) == 0) return node;
		   
		   BinaryTree.Node left = leastCommonAncestor(node.left());
		   BinaryTree.Node right = leastCommonAncestor(node.right());
		   
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