# Weighted Graph

This reposetory include 3 JAVA classes,3 interfaces and 2 JUint tests that represents:  
1. NodeInfo- inside class implements node_info- view of a vertex,its neighbors list and weights, contain the following methods:
   * double getweight(int key)
   * setweight(int key,double weight)
   * addNi(node_info n, double weight)
   * getters and setters
   
   
2. WGraph_DS-  implements graph- an undirectional unweighted graph, contain the following methods:
   * node_info getNode(int key) 
   * boolean hasEdge(int node1, int node2)
   * double getEdge(int node1, int node2)
   * addNode(int key)
   * connect(int node1, int node2, double w)
   * node_info removeNode(int key)
   * removeEdge(int node1, int node2)
   * getters and setters
   
   
3. WGraph_Algo- implements graph_algorithms - represents the "regular" Graph Theory algorithms, contain the following methods:
    * init(weighted_graph g)
    * copy()
    * boolean isConnected()
    * double shortestPathDist(int src, int dest)
    * List<node_info> shortestPath(int src, int dest)
    * save(file)
    * load(file)
    
    
Exemples: \
  let's take graph to be a country and the nodes to be cities: \
  city 1- {i_d=1,info="Jerusalem"} his neighbors are-{2,distance is-10 | 3,distance is-20} \
  city 2- {i_d=2,info="Bet El"} his neighbors are-{1,distance is-10} \
  city 3- {i_d=3,info="Tel Aviv"} his neighbors are-{1,distance is-20 | 4,distance is-5} \
  city 4- {i_d=4,info="Bat Yam"} his neighbors are-{3,distance is-5} \
  city 5- {i_d=5,info="Kiriyat Shmona"} his neighbors are-{}
  
  
  
  this graph may look like this:
            
   
                          |----20----"Jerusalem"----10----"Bet El"
                          |
    "Bat Yam" ----5----"Tel Aviv"            "Kiriyat Shmona"
  
  
  
  this graph in not connected \
  the faster way to get from "Bat Yam" to "Bet El" is through "Tel Aviv" and "Jerusalem" with total distance of 35 \
  "Kiriyat Shmona" is not accessible from every other city

    
