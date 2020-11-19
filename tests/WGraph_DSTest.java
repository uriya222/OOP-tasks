package ex1.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;



class WGraph_DSTest {
	private static Random _rnd = null;
	static long start;

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

	@BeforeAll
	public static void startTime() {
		start= new Date().getTime();
	}
	
	@AfterAll
	public static void endTime() {
		long end= new Date().getTime();
		System.out.println("Toatal tests time is: "+(end-start)/1000.0);
	}
	
	/**
	 * this test make an empty weighted_graph and cheaks 
	 * if edgesize=nodesize=Mc=0
	 */
	@Test
	public void test0() {
		weighted_graph g=graph_creator(0,0,1);
		assertEquals(g.edgeSize(), 0);
		assertEquals(g.nodeSize(), 0);
		assertEquals(g.getMC(), 0);
	}

	@Test
	public void test1() {
		weighted_graph g=graph_creator(1,0,1);
		assertEquals(g.edgeSize(), 0);
		assertEquals(g.nodeSize(), 1);
		assertEquals(g.getMC(), 1);
	}

	@Test
	public void test2() {
		weighted_graph g=graph_creator(10,11,1);
		assertEquals(g.edgeSize(), 11);
		assertEquals(g.nodeSize(), 10);
	}

	@Test
	public void test3() {
		weighted_graph g=new WGraph_DS();
		for (int i = 0; i < 10; i++) {
			g.addNode(i);
		}
		g.connect(0, 1, 2);
		g.connect(1, 0, 2);
		assertEquals(g.getMC(), 11);
		g.connect(2, 2, 0);
		assertEquals(g.getMC(), 11);
		assertEquals(g.edgeSize(), 1);
		g.connect(1, 2, 3.5);
		g.connect(2, 0, 4);
		assertTrue(g.hasEdge(1,0));
		g.removeEdge(2, 1);
		assertFalse(g.hasEdge(1, 2));
		assertEquals(g.edgeSize(), 2);
		g.removeEdge(2, 1);
		assertEquals(g.edgeSize(), 2);
	}
	
	@Test
	public void test4() {
		weighted_graph g=new WGraph_DS();
		for (int i = 0; i < 10; i++) {
			g.addNode(i);
		}
		g.connect(0, 1, 2);
		assertEquals(g.getMC(), 11);
		g.connect(0, 1, 3);
		assertEquals(g.getMC(), 12);
	}
	
	@Test
	public void test5() {
		weighted_graph g=new WGraph_DS();
		for (int i = 0; i < 10; i++) {
			g.addNode(i);
		}
		g.addNode(12);
		assertNull(g.getNode(11));
		assertNull(g.removeNode(11));
		g.connect(0, 1, 3);
		g.connect(1, 2, 3);
		g.connect(2, 3, 3);
		g.connect(0, 2, 3);
		g.connect(0, 4, 3);
		g.connect(4, 1, 3);
		g.connect(4, 5, 3);
		assertEquals(g.getMC(), 18);
		g.removeNode(0);
		assertEquals(g.nodeSize(), 10);
		assertEquals(g.edgeSize(), 4);
		assertEquals(g.getMC(), 22);
		g.removeNode(0);
		assertEquals(g.getMC(), 22);
		assertEquals(g.nodeSize(), 10);
		
	}
	
	@Test
	public void test6() {
		weighted_graph g=graph_creator(500000,1000000,1);
		assertEquals(g.edgeSize(), 1000000);
		assertEquals(g.nodeSize(), 500000);
	}
	
	@Test
	public void test7() {
		weighted_graph g=new WGraph_DS();
		for (int i = 0; i < 10; i++) {
			g.addNode(i);
		}
		g.connect(0, 1, 2);
		assertEquals(g.getEdge(0, 1), 2);
		g.connect(0, 1, 3.5);
		assertEquals(g.getEdge(0, 1), 3.5);
		g.removeEdge(0, 1);
		assertEquals(g.getEdge(0, 1), -1);
	}

}
