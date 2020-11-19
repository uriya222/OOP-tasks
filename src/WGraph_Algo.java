package ex1.src;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * This class represents an Undirected (positive) Weighted Graph Theory algorithms,
 * by implementing weighted_graph_algorithms interface, including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected();
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file);
 * 6. Load(file);
 *
 */

public class WGraph_Algo implements weighted_graph_algorithms{
	private weighted_graph g;

	public WGraph_Algo() {
		g=new WGraph_DS();
	}

	@Override
	public void init(weighted_graph g) { this.g=g;}

	@Override
	public weighted_graph getGraph() {return this.g;}

	/** 
	 * Compute a deep copy of this graph.
	 * @return the copy
	 */
	@Override
	public weighted_graph copy() {
		weighted_graph copy1 =new WGraph_DS(this.g);
		return copy1;
	}
	/**
	 * Returns true if and only if (iff) there is a valid path from EVREY node to eachother node.
	 * using the BFS algorithm
	 * @return
	 */
	@Override
	public boolean isConnected() {
		int firstKey=0;
		if(g.getV().isEmpty()) return true;
		else {
			for(node_info x: g.getV()) { 
				if(x!=null) x.setTag(0);	
			}
			for(node_info x:g.getV()) { 
				if(x!=null) {
					firstKey=x.getKey();
					break;
				}
			}
			Queue<node_info> q=new LinkedList<node_info>();
			node_info n= g.getNode(firstKey);
			q.add(n);
			n.setTag(1);
			if(g.getV().size()>=2) {
				while(!(q.isEmpty())) {
					if(g.getV(n.getKey())==null) return false;
					else{
						for(node_info x: g.getV(n.getKey())) {
							if(x.getTag()==0) {
								q.add(x);
								x.setTag(1);
							}
						}
					}
					n=q.remove();
				}
				for(node_info x: g.getV()) {
					if(x!=null) {
						if(x.getTag()==0) return false;
					}
				}
			}
		}
		return true;
	}
	/**
	 * The dijkstras algorithm:
	 * return true iff there is a path between source node to destination node. 
	 * @param src the i_d of source node.
	 * @param dest the i_d of destination node.
	 * @param size the biggest key the graph contain.
	 * @param prev is Array[size] which contain the keys that lead to destination in the shortest way.
	 * @param S is Array[size] which contain the smallest path (calculating by weight) from every node to node src.
	 * @return
	 */
	private void dijkstras_algo(int src, int dest, int size, int[] prev, double[] S){
		int tmp2=0;
		double dist=0;
		boolean visited[] = new boolean[size]; 
		Queue<Integer> q=new LinkedList<Integer>();
		for(node_info x: g.getV()) { 
			if(x!=null) x.setTag(0);	
		}
		for (int i = 0; i < size; i++) { 
			visited[i] = false; 
			S[i] = Integer.MAX_VALUE; 
			prev[i] = -1; 
		}
		visited[src]=true;
		S[src]=0;
		q.add(src);
		while(!q.isEmpty()) {
			int u=q.remove();
			for (node_info x:g.getV(u)) {
				if (visited[x.getKey()] == false) {
					dist=S[x.getKey()];
					S[x.getKey()] = Math.min(S[x.getKey()], S[u]+g.getEdge(x.getKey(), u)); 
					x.setTag(S[x.getKey()]);
					if(S[x.getKey()]!=dist) prev[x.getKey()] = u;
				}
			}
			visited[u] = true;
			tmp2=SmallestW(S,visited);
			if(tmp2>-1) q.add(tmp2);
		}
	}
	/**
	 * simple method part of the dijkstras algorithm :
	 * finding the minimum in array S with some limits:
	 * limit 1- at the same place in visited array it must be false
	 * limit 2- with giving place in S the same index must be a key to a node in my graph(for NullPointerExeption)
	 * @param S 
	 * @param visited
	 * @return
	 */
	private int SmallestW(double [] S, boolean[] visited) {
		double min=Double.MAX_VALUE;
		int indexMin=-1;
		for (int i = 0; i < S.length; i++) {
			if((S[i]<min)&&(visited[i]==false)&&(g.getNode(i)!=null)) {
				min=S[i];
				indexMin=i;
			}
		}
		return indexMin;
	}
	/**
	 * returns the length of the shortest path between src to dest
	 * if no such path --> returns -1
	 * using the dijkstras_algo method.
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		if(this.g==null) return -1;
		if(g.getNode(src)==null||g.getNode(dest)==null) return -1;
		if(src==dest) return 0;
		int size=-1;
		for(node_info x: g.getV()) {
			if(x!=null) 
				if(x.getKey()>size) size=x.getKey();
		}
		size++;
		int [] prev=new int[size];
		double [] SotrestFromA=new double[size];
		dijkstras_algo( src, dest, size, prev, SotrestFromA);
		if(SotrestFromA[dest]==Integer.MAX_VALUE) return -1;
		return SotrestFromA[dest];

	}

	/**
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 * Note if no such path --> returns null;
	 * using the dijkstras_algo method.
	 * using a Stack to reverse the nodes in the List.
	 * @param src - start node
	 * @param dest - end (target) node
	 * @return
	 */
	@Override
	public List<node_info> shortestPath(int src, int dest) {
		if(this.g==null) return null;
		LinkedList<node_info> path=new LinkedList<node_info>();	
		if(g.getNode(src)==null||g.getNode(dest)==null) return path;
		Stack<node_info> st= new Stack<node_info>();
		if(src==dest) return path;
		int size=-1;
		for(node_info x: g.getV()) {
			if(x!=null) 
				if(x.getKey()>size) size=x.getKey();
		}
		size++;
		int [] prev=new int[size];
		double [] SotrestFromA=new double[size];
		dijkstras_algo(src, dest, size, prev, SotrestFromA);
		if(SotrestFromA[dest]==Integer.MAX_VALUE) return path;
		int tmp=dest;
		while(tmp!=-1) {
			st.push(g.getNode(tmp));
			tmp=prev[tmp];
		}
		while(!st.isEmpty()) {
			path.add(st.pop());
		}
		return path;
	}

	/**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
	@Override
	public boolean save(String file) {
		weighted_graph gCopy=new WGraph_DS(this.g);
		int first=0;
		try {
			PrintWriter pw=new PrintWriter(new File(file));
			StringBuilder sb= new StringBuilder();
			int i=1;
			sb.append("This weighted graph contain this list of nodes and there neighbors:\n");
			sb.append("MC="+g.getMC()+"\n");
			for(node_info y: gCopy.getV()) {
				first=0;
				sb.append("Node "+(i++)+"- {i_d-");
				sb.append(y.getKey()+",");
				sb.append("tag-"+y.getTag()+",");
				sb.append("info-"+y.getInfo()+"} ");
				sb.append("his neighbors are: {");
				for(node_info z : gCopy.getV(y.getKey())) {
					if (first!=0) sb.append(";");
					sb.append(z.getKey()+",distance is: "+g.getEdge(y.getKey(), z.getKey()));
					first++;
				}
				sb.append("}.\n");
				pw.write(sb.toString());
				sb.setLength(0);
			}
			pw.close();
			return true;	
		}
		catch (Exception e) {
			return false;
		}

	}
	
	/**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
	@Override
	public boolean load(String file) {
		String line="",s2,s_key,s_tag,s_info,nei_key,nei_dist,mc;
		String FirstLine="This weighted graph contain this list of nodes and there neighbors:";
		String SecondLine="MC=";
		String [] nodes, nei;
		int x,y,parse,tmp,a,mone=0,save=0;
		weighted_graph g2=new WGraph_DS();
		weighted_graph g3=this.g;
		this.g=g2;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((line= br.readLine())!=null) {
				if(mone==0) { 
					if(!line.equals(FirstLine)) {
						this.g=g3;
						return false;
					}
				}
				if(mone==1) {
					if(!line.contains(SecondLine)) {
						this.g=g3;
						return false;
					}
					mc=line.substring(3);
					save=Integer.parseInt(mc);
				}
				if(mone>1) {
					x=line.indexOf("{")+1;
					y=line.indexOf("}");
					s2=line.substring(x, y);
					nodes=s2.split(",");
					s_key=nodes[0].substring(nodes[0].indexOf("-")+1);
					s_tag=nodes[1].substring(nodes[1].indexOf("-")+1);
					s_info=nodes[2].substring(nodes[2].indexOf("-")+1);
					parse=Integer.parseInt(s_key);
					this.g.addNode(parse);
					this.g.getNode(parse).setTag(Double.parseDouble(s_tag));
					this.g.getNode(parse).setInfo(s_info);
					x=y+22;
					y=line.length()-2;
					if(x!=y) {
						s2=line.substring(x,y);
						nei=s2.split(";");
						for (int i = 0; i < nei.length; i++) {
							tmp=nei[i].indexOf(",");
							nei_key=nei[i].substring(0,	tmp);
							a=Integer.parseInt(nei_key);
							nei_dist=nei[i].substring(tmp+13);
							this.g.connect(parse, a, Double.parseDouble(nei_dist));
						}
					}
				}
				mone++;
			}
			if(mone==1) ((WGraph_DS)(this.g)).setMC(g3.getMC());
			((WGraph_DS)(this.g)).setMC(save);
			br.close();
			return true;
		}
		catch (Exception e) {
			this.g=g3;
			return false;
		}
	}


	@Override
	public boolean equals(Object g2) {
		WGraph_DS tmp1 = (WGraph_DS) getGraph();
		WGraph_DS tmp2 = (WGraph_DS)((WGraph_Algo)(g2)).getGraph();
		return tmp1.equals(tmp2);
	}
}
