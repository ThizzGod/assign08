package assign08;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;


/**
 * 
 * @author Daniel Kopta
 * This Graph class acts as a starting point for your maze path finder.
 * Add to this class as needed.
 */
public class Graph {

	// The graph itself is just a 2D array of nodes
	private Node[][] nodes;
	
	// The node to start the path finding from
	private Node start;
	
	// The size of the maze
	private int width;
	private int height;
	
	/**
	 * Constructs a maze graph from the given text file.
	 * @param filename - the file containing the maze
	 * @throws Exception
	 */
	public Graph(String filename) throws Exception
	{
		BufferedReader input;
		input = new BufferedReader(new FileReader(filename));

		if(!input.ready())
		{
			input.close();
			throw new FileNotFoundException();
		}

		// read the maze size from the file
		String[] dimensions = input.readLine().split(" ");
		height = Integer.parseInt(dimensions[0]);
		width = Integer.parseInt(dimensions[1]);

		// instantiate and populate the nodes
		nodes = new Node[height][width];
		for(int i=0; i < height; i++)
		{
			String row = input.readLine().trim();

			for(int j=0; j < row.length(); j++)
				switch(row.charAt(j))
				{
				case 'X':
					nodes[i][j] = new Node(i, j);
					nodes[i][j].isWall = true;
					break;
				case ' ':
					nodes[i][j] = new Node(i, j);
					break;
				case 'S':
					nodes[i][j] = new Node(i, j);
					nodes[i][j].isStart = true;
					start = nodes[i][j];
					break;
				case 'G':
					nodes[i][j] = new Node(i, j);
					nodes[i][j].isGoal = true;
					break;
				default:
					throw new IllegalArgumentException("maze contains unknown character: \'" + row.charAt(j) + "\'");
				}
		}
		input.close();
	}
	
	/**
	 * Outputs this graph to the specified file.
	 * Use this method after you have found a path to one of the goals.
	 * Before using this method, for the nodes on the path, you will need 
	 * to set their isOnPath value to true. 
	 * 
	 * @param filename - the file to write to
	 */
	public void printGraph(String filename)
	{
		try
		{
			PrintWriter output = new PrintWriter(new FileWriter(filename));
			output.println(height + " " + width);
			for(int i=0; i < height; i++)
			{
				for(int j=0; j < width; j++)
				{
					output.print(nodes[i][j]);
				}
				output.println();
			}
			output.close();
		}
		catch(Exception e){e.printStackTrace();}
	}

	
	
	/**
	 * Traverse the graph with BFS (shortest path to closest goal)
	 * A side-effect of this method should be that the nodes on the path
	 * have had their isOnPath member set to true.
	 * @return - the length of the path
	 */
	public int CalculateShortestPath()
	{
		Queue<Node> q = new LinkedList<Graph.Node>();
		q.offer(start);
		Node curr = null;

		
		while (!q.isEmpty()) {
			curr = q.poll();
			if (curr.isGoal) {
				return calculatePathLength(curr);
			}
			Node[] neighbors = new Node[4];
			neighbors[0] = nodes[curr.row][curr.col - 1 ];
			neighbors[1] = nodes[curr.row][curr.col + 1 ];
			neighbors[2] = nodes[curr.row - 1][curr.col];
			neighbors[3] = nodes[curr.row + 1][curr.col];
			
			for (Node next : neighbors) {
				if (!next.isWall && !next.visited) {
					next.visited = true;
					next.cameFrom = curr;
					q.offer(next);
				}
			}
		}
		return -1;
	}
	
	/**
	 * Traverse the graph with DFS (any path to any goal)
	 * A side-effect of this method should be that the nodes on the path
	 * have had their isOnPath member set to true.
	 * @return - the length of the path
	 */
	public int CalculateAPath()
	{
		return dfs(start);		
	}
	
	public int dfs(Node node) {
		node.visited = true;
		if (node.isGoal) {
			return calculatePathLength(node);
		} 
		Node[] neighbors = new Node[4];
		neighbors[0] = nodes[node.row][node.col - 1 ];
		neighbors[1] = nodes[node.row][node.col + 1 ];
		neighbors[2] = nodes[node.row - 1][node.col];
		neighbors[3] = nodes[node.row + 1][node.col];

		for (Node next : neighbors ) {
			if (!next.visited && !next.isWall) {
				next.cameFrom = node;
				int pathLength = dfs(next);
				if (pathLength > 0) {
					return pathLength;
				}
			} 
			
		}
		return -1;
	}

	public int calculatePathLength(Node current) {
		int counter = 0;
		while (current != start) {
			current.isOnPath = true;
			current = current.cameFrom;
			counter++;
		}
		return counter;
	}
	
	/**
	 * @author Daniel Kopta
	 * 	A node class to assist in the implementation of the graph.
	 * 	You will need to add additional functionality to this class.
	 */
	private static class Node
	{
		// The node's position in the maze
		private int row, col;
		
		// The type of the node
		private boolean isStart;
		private boolean isGoal;
		private boolean isOnPath;
		private boolean isWall;
		
		private Node cameFrom;
		private boolean visited;
		
		
		// TODO: You will undoubtedly want to add more members and functionality to this class.
				
		public Node(int r, int c)
		{
			isStart = false;
			isGoal = false;
			isOnPath = false;
			row = r;
			col = c;
			cameFrom = null;
			visited = false;
		}
		
		@Override
		public String toString()
		{
			if(isWall)
				return "X";
			if(isStart)
				return "S";
			if(isGoal)
				return "G";
			if(isOnPath)
				return ".";
			return " ";
		}
	}
	
}
