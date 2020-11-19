package ex1.src;


import java.util.Collection;
import java.util.HashMap;

/**
 * This class represents an undirectional weighted graph,by implementing weighted_graph interface.
 * It support a large number of nodes by saving them in HashMap.
 *
 */

public class WGraph_DS implements weighted_graph {
	private int NumOfNode, NumOfEdge, Mc;
	private HashMap<Integer,node_info> HmOfNodes;

	public WGraph_DS() {
		this.Mc=this.NumOfEdge=this.NumOfNode=0;
		this.HmOfNodes=new HashMap<>();
	}


	public WGraph_DS(weighted_graph g) {
		this.HmOfNodes=new HashMap<>();
		for(node_info x: g.getV()) {
			addNode(x.getKey());
		}
		for(node_info x: g.getV()) {
			for(node_info y: g.getV(x.getKey())) {
				if(x instanceof NodeInfo) {
					NodeInfo tmp1=(NodeInfo)x;
					connect(y.getKey(), x.getKey(), tmp1.getweight(y.getKey()));  
				}
			}
		}
		this.NumOfNode=g.nodeSize();
		this.NumOfEdge=g.edgeSize();
		this.Mc=g.getMC();
	}


	/**
	 * This class represents a node in undirectional weighted graph,by implementing node_info.
	 * Every singel node posses:
	 * -key
	 * -tag
	 * -informaition
	 * -list of neighbors
	 * -list of weights
	 *
	 */
	private class NodeInfo implements node_info{
		private int key;
		private double tag;
		private String info;
		private HashMap<Integer, node_info> neighbors;
		private HashMap<Integer, Double> NeWithWeight;

		public NodeInfo(int key) {
			this.key=key;
			this.info="";
			this.tag=0;
			neighbors=new HashMap<>();
			NeWithWeight=new HashMap<>();
		}

		public boolean hasNi(int key) {
			if(neighbors.isEmpty()) return false;
			if(neighbors.containsKey(key)) return true;
			return false;
		}

		public Collection<node_info> getNi(){return this.neighbors.values();}

		public double getweight(int key) {
			if(hasNi(key)) {
				return NeWithWeight.get(key);
			}
			return -1.0;
		}

		public void setweight(int key,double weight) {
			if(hasNi(key)) {
				NeWithWeight.replace(key, weight);
			}
		}
		public void addNi(node_info n, double weight) {
			neighbors.put(n.getKey(), n);
			NeWithWeight.put(n.getKey(), weight);
		}

		@Override
		public int getKey() {return this.key;}

		@Override
		public String getInfo() {return this.info;}

		@Override
		public void setInfo(String s) {this.info=new String(s);}

		@Override
		public double getTag() {return this.tag;}

		@Override
		public void setTag(double t) {this.tag=t;}

		public void removeNode(node_info tmp1) {
			neighbors.remove(tmp1.getKey());
			NeWithWeight.remove(tmp1.getKey());
		}

	}


	@Override
	public node_info getNode(int key) {
		if(this.HmOfNodes.isEmpty()) return null;
		if(this.HmOfNodes.containsKey(key)) return HmOfNodes.get(key);
		return null;
	}

	/**
	 * return true iff (if and only if) there is an edge between node1 and node2
	 * this method run in O(1) time.
	 * @param node1 the i_d of the first node
	 * @param node2 the i_d of the second node
	 * @return
	 */
	@Override
	public boolean hasEdge(int node1, int node2) {
		if(node1>=0&&node2>=0) {
			if(node1==node2) return true;
			if(this.HmOfNodes.get(node1) instanceof NodeInfo&&this.HmOfNodes.get(node2) instanceof NodeInfo) {
				NodeInfo tmp1=(NodeInfo)this.HmOfNodes.get(node1);
				NodeInfo tmp2=(NodeInfo)this.HmOfNodes.get(node2);
				if(tmp1!=null&& tmp2!=null) {
					if(tmp1.hasNi(node2)) return true;

				}
			}
		}
		return false;
	}

	 /**
     * this method return the weight between node1 to node2 if exist,
     * else it return -1
     * @param node1 key of the first node
     * @param node2 key of the second node
     * @return
     */
	@Override
	public double getEdge(int node1, int node2) {
		if(node1>=0&&node2>=0) {
			if(node1==node2) return 0;
			if(hasEdge(node1, node2)) {
				node_info tmp=getNode(node1);
				NodeInfo tmp2=(NodeInfo)tmp;
				return tmp2.getweight(node2);	
			}
		}
		return -1.0;
	}

	@Override
	public void addNode(int key) {
		if(getNode(key)==null) {
			HmOfNodes.put(key, new NodeInfo(key)); 
			NumOfNode++;
			Mc++;
		}
	}
	/**
	 * Connect an edge between node1 to node2 with weight w.
	 * if the edge already exist but the weight is different it change it.
	 * if the edge already exist and the weight is the same it do nothing.
	 * this method run in O(1) time.
	 * @param node1 the i_d of the first node
	 * @param node2 the i_d of the second node
	 * @param w the wieght between node1 and node 2
	 */
	@Override
	public void connect(int node1, int node2, double w) {
		if(node1!=node2) {
			if(HmOfNodes.containsKey(node1)&&HmOfNodes.containsKey(node2)) {
				if((this.HmOfNodes.get(node1) instanceof NodeInfo)&&(this.HmOfNodes.get(node2) instanceof NodeInfo)) {
					NodeInfo tmp1=(NodeInfo)this.HmOfNodes.get(node1);
					NodeInfo tmp2=(NodeInfo)this.HmOfNodes.get(node2);
					if(!(tmp1.hasNi(tmp2.getKey()))) {
						tmp1.addNi(tmp2,w);
						tmp2.addNi(tmp1,w);
						Mc++;
						NumOfEdge++;
					}
					else if(w!=tmp1.getweight(tmp2.getKey())){
						tmp1.setweight(tmp2.getKey(),w);
						tmp2.setweight(tmp1.getKey(),w);
						Mc++;
					}
				}
			}
		}
	}

	@Override
	public Collection<node_info> getV() {return this.HmOfNodes.values();}

	@Override
	public Collection<node_info> getV(int node_id) {
		node_info n=getNode(node_id);
		if(n==null) return null;
		if(n instanceof NodeInfo) {
			NodeInfo tmp1=(NodeInfo)n;
			return tmp1.getNi();
		}
		return null;
	}
   

	/**
	 * Delete the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * This method run in O(n), |V|=n, as all the edges are removed.
	 * @return the data of the removed node (null if none). 
	 * @param key
	 */
	@Override
	public node_info removeNode(int key) {
		if(HmOfNodes.containsKey(key)){
			if(HmOfNodes.get(key) instanceof NodeInfo) {
				NodeInfo tmp1=(NodeInfo)this.HmOfNodes.get(key);
				if(tmp1.getNi()!=null) {
					Object[] t=tmp1.getNi().toArray();
					for (int i = 0; i < t.length; i++) {
						if(t[i] instanceof NodeInfo && t[i]!=null) {
							NodeInfo x= (NodeInfo) t[i];
							removeEdge(x.getKey(), key);
						}
					}
				}
				NumOfNode--;
				Mc++;
				return HmOfNodes.remove(key);
			}
		}
		return null;
	}

	/**
	 * Delete the edge (node1-----node2) from the graph if he exist, 
	 * this method run in O(1) time.
	 * @param node1
	 * @param node2
	 * @return the data of the removed edge (null if none).
	 */
	@Override
	public void removeEdge(int node1, int node2) {
		if(node1!=node2) {
			if(HmOfNodes.containsKey(node1)&&HmOfNodes.containsKey(node2)) {
				if((this.HmOfNodes.get(node1) instanceof NodeInfo)&&(this.HmOfNodes.get(node2) instanceof NodeInfo)) {
					NodeInfo tmp1=(NodeInfo)this.HmOfNodes.get(node1);
					NodeInfo tmp2=(NodeInfo)this.HmOfNodes.get(node2);
					if(tmp1.hasNi(node2)) {
						tmp1.removeNode(tmp2);
						tmp2.removeNode(tmp1);
						NumOfEdge--;
						Mc++;
					}
				}
			}
		}
	}
	
	@Override
	public boolean equals(Object g2) {
		WGraph_DS g3 =(WGraph_DS)(g2);
		for(node_info x: getV()) {
			if(g3.getNode(x.getKey())==null) return false;
			for(node_info y: getV(x.getKey())) {
				if(g3.hasEdge(y.getKey(), x.getKey())) {
					if(g3.getEdge(x.getKey(), y.getKey())!=getEdge(x.getKey(), y.getKey())) return false;
				}
			}
		}
		if(nodeSize()!=g3.nodeSize()) return false;
		if(edgeSize()!=g3.edgeSize()) return false;
		if(getMC()!=g3.Mc) return false;
		return true;
	}

	@Override
	public int nodeSize() {return this.NumOfNode;}

	@Override
	public int edgeSize() {return this.NumOfEdge;}

	@Override
	public int getMC() {return this.Mc;}
	
	public void setMC(int mc) { this.Mc=mc;}



}
