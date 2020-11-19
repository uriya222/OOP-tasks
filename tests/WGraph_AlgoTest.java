package ex1.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ex1.src.WGraph_Algo;
import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import ex1.src.weighted_graph_algorithms;

class WGraph_AlgoTest {
	private static Random _rnd = null;
	
	private void AddNodeToGraph(weighted_graph g1, int arr[]) {
		for (int i = 0; i < arr.length; i++) {
			g1.addNode(arr[i]);
		}
	}
	
	private boolean CheckList(weighted_graph_algorithms g2, ArrayList<Integer> match, int[] a) {
		int mone=0,x;
		for(node_info i: g2.shortestPath(a[0], a[1])) {
			x=match.get(mone++);
			if(x!=i.getKey()) return false;
		}
		return true;
	}
	
	private void ConnectNodesInGraph(weighted_graph g1, double [][] a) {
		for (int i = 0; i < a.length; i++) {
            g1.connect((int)(a[i][0]), (int)(a[i][1]), a[i][2]);
		}
		
	}

	public static weighted_graph graph_creator(int v_size, int e_size, int seed) {
		weighted_graph g = new WGraph_DS();
		_rnd = new Random(seed);
		int key=(int)(Math.random()*5000);
		for(int i=0;i<v_size;i++) {
			g.addNode(key);
			key++;
		}
		node_info [] nodes = nodes(g);
		while(g.edgeSize() < e_size) {
			int a = nextRnd(0,v_size);
			int b = nextRnd(0,v_size);
			int i = nodes[a].getKey();
			int j = nodes[b].getKey();
			double w=(Math.random()*100)+1;
			g.connect(i,j,w);
		}
		return g;
	}
	private static int nextRnd(int min, int max) {
		double v = nextRnd(0.0+min, (double)max);
		int ans = (int)v;
		return ans;
	}
	private static double nextRnd(double min, double max) {
		double d = _rnd.nextDouble();
		double dx = max-min;
		double ans = d*dx+min;
		return ans;
	}
	private static node_info[] nodes(weighted_graph g) {
		int size = g.nodeSize();
		Collection<node_info> V = g.getV();
		node_info[] nodes = new node_info[size];
		V.toArray(nodes); // O(n) operation
		return nodes;
	}

    /**
     * this test build graph g1 with 2 nodes and an edge between with weight 80.6
     * build graph_algorithm g2 and init g2 to g1.
     * cheking if edge_size in both graphs are equal.
     */
	@Test
	public void test0() {
		weighted_graph g1=new WGraph_DS();
		int[] arr= {3,4};
		AddNodeToGraph(g1, arr);
		double [][] a= {{3,4,80.6}};
		ConnectNodesInGraph(g1,a);
		weighted_graph_algorithms g2=new WGraph_Algo();
		g2.init(g1);
		assertEquals(g2.getGraph().edgeSize(), g1.edgeSize());
	}
	
	/**
     * this test build graph g1 with 5 nodes and 4 edges.
     * build graph_algorithm g2 and init g2 to g1.
     * build graph_algorithm g3 and copy g2 to g3.
     * cheking if the nodes in both graphs (g1,g3) are equal. 
     * remove one node from g3 and checking that g2 still stay the same.
     */
	@Test
	public void test1() {
		weighted_graph g1=new WGraph_DS();
		int[] arr= {0,1,2,3,4};
		AddNodeToGraph(g1, arr);
		double [][] a= {{0,1,1},{1,3,1},{4,0,5},{0,2,3}};
		ConnectNodesInGraph(g1,a);
		weighted_graph_algorithms g2=new WGraph_Algo();
		g2.init(g1);
		weighted_graph g3=g2.copy();
		for(node_info x: g3.getV()) {
			assertNotNull(g1.getNode(x.getKey()));
		}
		g3.removeNode(0);
		assertNotNull(g2.getGraph().getNode(0));
	}
	@Test
	public void testCopy() {
		weighted_graph g1=new WGraph_DS();
		int[] arr= {0,1,2,3,4};
		AddNodeToGraph(g1, arr);
		double [][] a= {{0,1,1},{1,3,1},{4,0,5},{0,2,3}};
		ConnectNodesInGraph(g1,a);
		weighted_graph_algorithms g2=new WGraph_Algo();
		g2.init(g1);
		weighted_graph g3=g2.copy();
		g1.removeNode(0);
		assertTrue(g3.hasEdge(0, 1));
		assertTrue(g3.hasEdge(0, 2));
		assertTrue(g3.hasEdge(0, 4));
		assertNotNull(g3.getNode(0));	
	}
	/**
	 * this test checks if 3 basic kinds of weighted_graph is connected:
	 * 1) empty graph - return true
	 * 2) singel node - return true
	 * 3) two singel nodes - return false
	 */
	@Test
	public void testConnected1() {
		weighted_graph g1=new WGraph_DS();
		weighted_graph_algorithms g2=new WGraph_Algo();
		g2.init(g1);
		assertTrue(g2.isConnected());
		g1.addNode(0);
		assertTrue(g2.isConnected());
		g1.addNode(1);
		assertFalse(g2.isConnected());
	}
	/**
	 * this test checks if more complex weighted_graph is connected
	 * using methods like "remove node", "remove edge" 
	 */
	@Test
	public void testConnected2() {
		weighted_graph g1=new WGraph_DS();
		int [] arr={0,1,2,3,4}; 
		AddNodeToGraph(g1,arr);
		double [][] a= {{0,1,1},{1,3,1},{4,0,5},{0,2,3}};
		ConnectNodesInGraph(g1,a);
		weighted_graph_algorithms g2=new WGraph_Algo();
		g2.init(g1);
		assertTrue(g2.isConnected());
		g2.getGraph().removeNode(2);
		assertTrue(g2.isConnected());
		g2.getGraph().removeNode(1);
		assertFalse(g2.isConnected());
		weighted_graph g3=graph_creator(10000, 500, 1);
		for (int i = 0; i < 10000; i++) {
			g3.removeEdge(1, i);
		}
		g2.init(g3);
		assertFalse(g2.isConnected());
	}
	
	@Test
	public void testShortDistSize1() {
		weighted_graph g1=new WGraph_DS();
		weighted_graph_algorithms g2=new WGraph_Algo();
		g2.init(g1);
		assertEquals(g2.shortestPathDist(1, 3), -1);
		int [] arr1={10};
		AddNodeToGraph(g1,arr1);
		assertEquals(g2.shortestPathDist(10, 10), 0);
		int[] arr3={30};
		AddNodeToGraph(g1,arr3);
		assertEquals(g2.shortestPathDist(10, 30), -1);
		int [] arr2={0,1,2,3,4}; 
		AddNodeToGraph(g1,arr2);
		double [][] a= {{0,1,6},{0,3,1},{1,2,5},{1,3,2},{1,4,2},{2,4,5},{3,4,1}};
		ConnectNodesInGraph(g1,a);
		g2.init(g1);
		assertEquals(g2.shortestPathDist(0, 2), 7);
		g1.removeNode(4);
		assertEquals(g2.shortestPathDist(0, 2), 8);
		g1.removeEdge(1, 2);
		assertEquals(g2.shortestPathDist(0, 2), -1);
	}
	
	@Test
	public void testShortDistSize2() {
		weighted_graph g1=new WGraph_DS();
		int [] arr={0,1,2,3,4}; 
		AddNodeToGraph(g1,arr);
		double [][] a= {{0,1,6},{0,3,1},{1,2,5},{1,3,2},{1,4,2,},{2,4,5},{3,4,1}};
		ConnectNodesInGraph(g1,a);
		weighted_graph_algorithms g2=new WGraph_Algo();
		g2.init(g1);
		assertEquals(g2.shortestPathDist(0, 2), 7);
		g1.removeNode(4);
		assertEquals(g2.shortestPathDist(0, 2), 8);
		g1.removeEdge(1, 2);
		assertEquals(g2.shortestPathDist(0, 2), -1);
	}
	
	
	@Test
	public void testShortDistList() {
		weighted_graph g1=new WGraph_DS();
		weighted_graph_algorithms g2=new WGraph_Algo();
		ArrayList<Integer> match = new ArrayList<Integer>();
		g2.init(g1);
		int [] a1= {0,2};
		assertTrue(CheckList(g2,match,a1));
		int [] arr1={10};
		AddNodeToGraph(g1,arr1);
		match.add(10);
		int[] a2= {10,10};
		assertTrue(CheckList(g2,match,a2));
		int[] arr3={30};
		AddNodeToGraph(g1,arr3);
		match.removeAll(match);
		int[] a3= {10,30};
		assertTrue(CheckList(g2,match,a3));
		int [] arr={0,1,2,3,4}; 
		AddNodeToGraph(g1,arr);
		double [][] a= {{0,1,6},{0,3,1},{1,2,5},{1,3,2},{1,4,2,},{2,4,5},{3,4,1}};
		ConnectNodesInGraph(g1,a);
		g2.init(g1);	
		match.add(0);
		match.add(3);
		match.add(4);
		match.add(2);
		assertTrue(CheckList(g2,match,a1));
		g1.removeNode(4);
		match.removeAll(match);
		match.add(0);
		match.add(3);
		match.add(1);
		match.add(2);
		assertTrue(CheckList(g2,match,a1));
		g1.removeEdge(1, 2);
		assertTrue(CheckList(g2,match,a1));
	}
	
	@Test
	public void testLoadAndSave1(){
		weighted_graph g1=new WGraph_DS();
		int [] arr={0,1,2,3,4,5,6,7,8,9,10}; 
		AddNodeToGraph(g1,arr);
		double [][] a= {{0,1,1},{1,5,6},{5,9,2},{2,9,1},{1,2,16.3},{1,7,4},{7,3,8.2},{0,6,1.5},{6,10,18},{10,2,1},{8,9,3},{8,2,4},{0,4,20}};
		ConnectNodesInGraph(g1,a); 
		weighted_graph_algorithms g2=new WGraph_Algo();
		weighted_graph_algorithms g3=new WGraph_Algo();
		g2.init(g1);
		assertEquals(g2.shortestPathDist(0, 2), 10);
		assertTrue(g2.save("test1"));
		assertTrue(g3.load("test1"));
		assertEquals(g2,g3);
		
		
	}
	
	@Test
	public void testLoadAndSave2(){
		weighted_graph g1=new WGraph_DS();
		weighted_graph g2=new WGraph_DS();
		int [] arr={0,1,2,3,4,5,6,7,8,9,10}; 
		AddNodeToGraph(g1,arr);
		double [][] a= {{0,1,1},{1,5,6},{5,9,2},{2,9,1},{1,2,16.3},{1,7,4},{7,3,8.2},{0,6,1.5},{6,10,18},{10,2,1},{8,9,3},{8,2,4},{0,4,20}};
		ConnectNodesInGraph(g1,a); 
		weighted_graph_algorithms g3=new WGraph_Algo();
		weighted_graph_algorithms g4=new WGraph_Algo();
		g1.removeEdge(2, 9);
		g3.init(g1);
		assertEquals(g3.shortestPathDist(0, 2), 16);
		assertTrue(g3.save("test2"));
		assertTrue(g4.load("test2"));
		g2=g4.getGraph();
		g2.connect(2,9,1);
		assertEquals(g3.shortestPathDist(0, 2), 16);
		assertEquals(g4.shortestPathDist(0, 2), 10);
		assertNotEquals(g4,g3);
	}
	
	
	

}
