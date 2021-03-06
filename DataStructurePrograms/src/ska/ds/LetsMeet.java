package ska.ds;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Main class for solving "Let’s Do Lunch" problem
 * @author sagrawal
 * @email sagrawal@iastate.edu
 * @version 1.0
 */
public class LetsMeet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader bufferedReader;
		Map<String,MainNode> nodeMap = new HashMap<String,MainNode>();
		Set<MainNode> avoidSet = new HashSet<MainNode>();
		Set<MainNode> peggyPoints = new HashSet<MainNode>();
		Set<MainNode> samPoints = new HashSet<MainNode>();
		Set<String> result1 = new TreeSet<String>();
		Set<String> result2 = new TreeSet<String>(); 
		try{	
			/**
			 * Taking input from standard input 
			 */
			
			
			bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			int type = 0;
			String pointLine = "";
			String points[];

			MainNode node1,node2;
			boolean flag = false;
			/**
			 * Taking data from standard input for different category
			 */
			while((pointLine = bufferedReader.readLine())!=null){
				if(pointLine.length() == 0 && type != 2){
					break;
				}
				switch (pointLine) {
				case "Map:":
					type = 1;
					flag = false;
					break;
				case "Avoid:":
					type = 2;
					flag = false;
					break;
				case "Peggy:":
					type = 3;
					flag = false;
					break;
				case "Sam:":
					type = 4;
					flag = false;
					break;
				default:
					//type = 5;
					flag = true;
					break;
				}

				if(type == 2 && flag){
					points = pointLine.split(" ");
					for (int i = 0; i < points.length; i++) {
						if(!points[i].isEmpty())
							avoidSet.add(nodeMap.get(points[i]));	
					}
				}
				if(type == 3 && flag){
					points = pointLine.split(" ");
					for (int i = 0; i < points.length; i++) {
						if(!points[i].isEmpty())
							peggyPoints.add(nodeMap.get(points[i]));	
					}
				}
				if(type == 4 && flag){
					points = pointLine.split(" ");
					for (int i = 0; i < points.length; i++) {
						if(!points[i].isEmpty())
							samPoints.add(nodeMap.get(points[i]));	
					}
				}
				/**
				 * For input points creating map and nodes
				 */
				if(type == 1 && flag){
					if(pointLine != null){
						points = pointLine.split(" ");
						if(points.length>0){
							/**
							 * If First Node, add directly
							 */
							if(nodeMap.isEmpty()){
								node1 = new MainNode(points[0]);
								node2 = new MainNode(points[1]);
								node1.addToNodeSet(node2);
								node2.setParent(node1);

								nodeMap.put(points[0], node1);
								nodeMap.put(points[1], node2);
							}
							else{
								if(!nodeMap.containsKey(points[0])){
									node1 = new MainNode(points[0]);
									nodeMap.put(points[0], node1);
								}
								else{
									node1 = nodeMap.get(points[0]);
								}
								if(!nodeMap.containsKey(points[1])){
									node2 = new MainNode(points[1]);
									nodeMap.put(points[1], node2);
								}
								else{
									node2 = nodeMap.get(points[1]);
								}
								node1.addToNodeSet(node2);
								node2.setParent(node1);

							}
						}
					}
				}
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}


		try{
			/**
			 * Finding path for each starting point of Peggy
			 */
			Iterator<MainNode> peggyIterator = peggyPoints.iterator();
			while(peggyIterator.hasNext()){
				MainNode n = nodeMap.get(peggyIterator.next().getName());
				if(!avoidSet.contains(n))
					result1 = pathTraverser(n,avoidSet,result1);
			}
			/**
			 * Removing avoid points from parent set
			 */
			if(!avoidSet.isEmpty())
				removeAvoidPoint(avoidSet);
		}
		catch(NullPointerException n){
			n.printStackTrace();
		}

		/**
		 * Getting all the parent of Sam points using Iterator
		 */

		Iterator<MainNode> samIterator = samPoints.iterator();
		while(samIterator.hasNext()){
			MainNode n = nodeMap.get(samIterator.next().getName());

			result2 = getAllParent(n,avoidSet,result2);
			/**
			 * Adding node to the total list
			 */
			result2.add(n.getName());
		}

		/**
		 * Storing meeting points which are common in both results
		 */

		Iterator<String> it = result1.iterator();
		String str;
		while(it.hasNext()){
			str = it.next();
			/**
			 * Printing the meeting points on standard output
			 */
			if(result2.contains(str)){
				System.out.println(str);
			}
		}
	}

	/**
	 * Function for removing avoid points from the Node set 
	 * @param avoidSet
	 */	
	public static void removeAvoidPoint(Set<MainNode> avoidSet){

		Iterator<MainNode> nodeIt = avoidSet.iterator();
		try {
			while(nodeIt.hasNext()){
				MainNode removeNode = nodeIt.next();
				Iterator<MainNode> it = removeNode.getNodeSet().iterator();
				MainNode temp;
				while(it.hasNext()){
					temp = it.next();
					temp.getParent().remove(removeNode);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Getting all parents
	 * @param n
	 * @param avoidSet
	 * @param result2
	 * @return
	 */
	public static Set<String> getAllParent(MainNode n, Set<MainNode> avoidSet, Set<String> result2) {
		Iterator<MainNode> it;
		MainNode temp;
		if(n == null)
			return result2;
		else{
			it = n.getParent().iterator();

			while(it.hasNext()){
				temp = it.next();
				result2.add(temp.getName());
				n = temp;
				getAllParent(n, avoidSet, result2);
			}
		}
		return result2;
	}


	/**
	 * FInding the path from given node using BFS
	 * @param v
	 * @param avoidSet
	 * @param result
	 * @return
	 */
	public static Set<String> pathTraverser(MainNode v, Set<MainNode> avoidSet, Set<String> result){

		Set<MainNode> visited = new HashSet<MainNode>();
		Queue<MainNode> queue = new LinkedList<MainNode>();
		queue.add(v);
		visited.add(v);
		result.add(v.getName());
		MainNode n;

		while (!queue.isEmpty()) {
			n = queue.remove();

			for(MainNode n1:n.getNodeSet()){

				if(!avoidSet.contains(n1) && !visited.contains(n1)){
					queue.add(n1);
				}

				if(!avoidSet.contains(n1)){
					visited.add(n1);
					result.add(n1.getName());
				}
			}
		}
		return result;
	}
}

/**
 * Node Class
 * @author sagrawal
 *
 */
class MainNode {
	private String name;
	private Set<MainNode> parent = new HashSet<MainNode>();
	private Set<MainNode> nodeSet = new HashSet<MainNode>();

	/**
	 * Constructor using fields
	 * @param name
	 */
	public MainNode(String name) {
		this.name = name;
	}

	/**
	 * Adding Nodes to Nodeset
	 * @param node2
	 */
	public void addToNodeSet(MainNode node2) {
		this.nodeSet.add(node2);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parent
	 */
	public Set<MainNode> getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(MainNode n) {
		this.parent.add(n);
	}


	/**
	 * @return the nodeSet
	 */
	public Set<MainNode> getNodeSet() {
		return nodeSet;
	}

	@Override
	public int hashCode(){
		return name.length();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MainNode){
			MainNode otherVer = (MainNode) obj;
			return name.equals(otherVer.getName());
		}else{
			return false;
		}
	}
}
