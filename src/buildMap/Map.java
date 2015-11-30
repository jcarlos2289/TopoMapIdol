package buildMap;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Map {
	ArrayList<Node> nodes;
	ArrayList<Edge> edges;
	boolean useHisto=true;
	float[] weights;
	float coefA, coefB, coefC, coefD, coefE;
	
	public Map () {
		nodes=new ArrayList<Node>();
		edges=new ArrayList<Edge>();
		coefA=-1;
		coefB=-1;
		coefC=-1;
		coefD=-1;
		coefE=-1;
	}
	
	public void setWeights (int size) {
		weights= new float[size];
		for (int i=0; i< size; i++) {
			weights[i]=(i/(float) size);
			//System.err.println(weights[i]);
		}
	}
	
	public Node createNode (ImageTags i) {
		Node n=new Node(useHisto, weights);
		n.add(i);
		nodes.add(n);
		return n;
	}
	
	public void printMap () {
		boolean [][]adj;
		adj=new boolean[nodes.size()][nodes.size()];
		
		for (int i=0; i<nodes.size(); i++) {
			nodes.get(i).printCoords();
		}
		for (int i=0; i<edges.size(); i++) 
			adj[nodes.indexOf(edges.get(i).getA())][nodes.indexOf(edges.get(i).getB())]=true;

		FileWriter fichero = null;
        PrintWriter pw = null;
        
        try {
        	fichero = new FileWriter("adj");
        	pw = new PrintWriter(fichero);

    		for (int i=0; i<nodes.size(); i++) {
    			for (int j=0; j<nodes.size(); j++) 
    				if (adj[i][j]) pw.print("1 ");
    				else pw.print("0 ");
    			pw.println("");
    		}
    	} catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
        		if (fichero != null)
        			fichero.close();
        	} catch (Exception e2) {
        		e2.printStackTrace();
        	}
        }
	}
	
	public int getMapSize () {
		return nodes.size();
	}
	
	public void addNode (Node n) {
		nodes.add(n);
	}
	
	public Node getNode (int i) {
		if (i>nodes.size()) 
			return null;
		return nodes.get(i);
	}
	
	public void createEdge (Node a, Node b) {
		for (int i=0; i<edges.size(); i++) {
			if ((edges.get(i).a==a && edges.get(i).b==b) ||
				(edges.get(i).a==b && edges.get(i).b==a))
				return;
		}
		Edge e = new Edge(a, b);
		edges.add(e);
	}
	
	public float getMapDev_E(){
		float E=0;
		
		
		float xAcum=0, yAcum=0, xMean, yMean, xDev, yDev;
		for (Node node : nodes) {
			
			xAcum+=node.representative.xcoord;
			yAcum+=node.representative.ycoord;
			
		}
			
		xMean = xAcum/nodes.size();
		yMean = yAcum/nodes.size();
		
		xAcum =0;
		yAcum = 0;
		
		for(Node node:nodes){
			xAcum +=Math.pow(node.representative.xcoord-xMean, 2)/nodes.size();
			yAcum  +=Math.pow(node.representative.ycoord-yMean, 2)/nodes.size();
		}
		
		xDev= (float) Math.sqrt(xAcum);
		yDev= (float) Math.sqrt(yAcum);
		E =  (float) Math.sqrt(Math.pow(xDev, 2)+ Math.pow(yDev, 2));
				
		return E;
	}
	
	
	public float getAvgEdgeDist_C(){
		float C=0;
		float distAcum=0;
		
		for (Edge e : edges) {
			distAcum+= e.getDistance()/edges.size();
			C= distAcum;
			}
				
		return C;
	}
	
	
	
	public ArrayList<NodeCoef> getNodeCoeficientsByNode(float dmax){
		ArrayList<NodeCoef> NodeMetricCoeficients = new ArrayList<NodeCoef>();
		
		for (Node node : nodes) {
			NodeMetricCoeficients.add(new NodeCoef(node.getQCat_A(), node.getQConnAvg_B(edges), node.getQImgDev_D()));
			}
		
		return NodeMetricCoeficients;
	}
	
	public NodeCoef AvgCoeficients(ArrayList<NodeCoef> NodeMetricCoeficients, float dmax){
		NodeCoef AvgCoef = new NodeCoef(0,0,0);
		
		float acumA = 0, acumB =0, acumD=0;
		//NodeCoef normValue;
		for (NodeCoef nodeCoef : NodeMetricCoeficients) {
			//normValue = normalize(nodeCoef, dmax);
			acumA += nodeCoef.getA()/nodes.size();
			acumB += nodeCoef.getB()/nodes.size();
			acumD += nodeCoef.getD()/nodes.size();
		}
		//Averaged Coeficients in the nodes of the map
		AvgCoef = new NodeCoef(acumA, acumB, acumD);
		
		return AvgCoef;
	}
	
		
	public NodeCoef normalize(NodeCoef nodeCoef, float dmax){
		NodeCoef dat = new NodeCoef(0, 0, 0);
		//gets the avg values for the noeficients by node to normalize 		
		dat.setA((1-nodeCoef.getA()));
		dat.setB(   (nodes.size()-1-nodeCoef.getB())/(nodes.size()-2)    );
		dat.setD(	(dmax/2 - nodeCoef.getD()) /(dmax/2) );
				
		return dat;
	}
	/*
	public ArrayList<NodeCoef> DetailNomrCoeficientsByNode(ArrayList<NodeCoef> NodeMetricCoeficients, float dmax){
		ArrayList<NodeCoef> NodeMetricCoeficientsNorm = new ArrayList<NodeCoef>();
		
				
		float acumA = 0, acumB =0, acumD=0;
		NodeCoef normValue;
		for (NodeCoef nodeCoef : NodeMetricCoeficients) {
			normValue = normalize(nodeCoef, dmax);
			acumA = normValue.getA();
			acumB = normValue.getB();
			acumD = normValue.getD();
			NodeMetricCoeficientsNorm.add(new NodeCoef(acumA, acumB, acumD));
			
		}
		
		return  NodeMetricCoeficientsNorm;
		
	}
	*/
	
	
	public String printMetricTable(float dmax){
		String table ="<html><h2>Metrics Coeficients</h2><br> "
				+ "<table border=\"1\"   style=\"font-size:10px\" >"
				+ "<tr><th>Node</th><th>A</th><th>B</th><th>D</th></tr>";
				//+ "<tr><th>Node</th><th>A</th><th>B</th><th>D</th><th>A<sub>Norm</sub></th><th>B<sub>Norm</sub></th><th>D<sub>Norm</sub></th></tr>";
		
		//Get the coeficients by node in an arraylist
		ArrayList<NodeCoef> NodeMetricCoeficients = getNodeCoeficientsByNode(dmax);
		//ArrayList<NodeCoef> NodeMetricCoeficientsNorm = DetailNomrCoeficientsByNode(NodeMetricCoeficients,dmax);
		
		//Get the AVG Coeficients
		NodeCoef AvgCoef = AvgCoeficients(NodeMetricCoeficients, dmax);
		//Get the AVG Coeficients Normalized
		NodeCoef AvgCoefNorm = normalize(AvgCoef, dmax);
		
		int i =0;
		for (Node node : nodes) {
			table += "<tr>";
			table += node.printNodeMet(i, edges);
			//table += "<td>"+(NodeMetricCoeficientsNorm.get(i).getA()) +"</td><td>"+NodeMetricCoeficientsNorm.get(i).getB() +"</td><td>"+NodeMetricCoeficientsNorm.get(i).getD() +"</td>";
			table += "</tr>";
			++i;
		}
		
		table+="</table><br>";
		
				
		
		float E =0, C =0, En=0, Cn=0;
		
		E = getMapDev_E();
		C = getAvgEdgeDist_C();
		En = E/(dmax/2);
		Cn = (dmax-C)/dmax;
			
		table +="<h2>Dmax=  "+dmax+"</h2>";
		table +="<h2>Nodes=  "+nodes.size()+"</h2>";
		table +="<h2>Edges=  "+edges.size()+"</h2>";
		table +="<h2>A= "+ AvgCoef.getA()+"  A<sub>Norm</sub>=  "+AvgCoefNorm.getA()+"</h2>";
		table +="<h2>B= "+ AvgCoef.getB()+"  B<sub>Norm</sub>=  "+AvgCoefNorm.getB()+"</h2>";
		table +="<h2>D= "+ AvgCoef.getD()+"  D<sub>Norm</sub>=  "+AvgCoefNorm.getD()+"</h2>";
		table +="<h2>C= "+ C+ "  C<sub>Norm</sub>=  "+Cn+"</h2>";
		table +="<h2>E= "+ E+ "  E<sub>Norm</sub>=  "+En+"</h2>";
			
				
		
		float metric = getMapMetric(dmax);
			
		table +="<h1>Metric=  "+metric+"</h1><br>";
		table+= "</html>";
		return table;
				
		
	}
	
	public float getMapMetric(float dmax){
		float metric=0;
		//Get the coeficients by node in an arraylist
		ArrayList<NodeCoef> NodeMetricCoeficients = getNodeCoeficientsByNode(dmax);
		
		//Get the AVG Coeficients
		NodeCoef AvgCoef = AvgCoeficients(NodeMetricCoeficients, dmax);
			
		
		float E=0, C=0;
	
		float wA=20, wB=20, wC=20, wD=20, wE =20;
		
		//Normalized Values 
		NodeCoef AvgCoefNorm = normalize(AvgCoef, dmax);	
		E = getMapDev_E()/(dmax/2);
		C = (dmax-getAvgEdgeDist_C())/dmax;
	
		metric = AvgCoefNorm.getA()*wA 
				+AvgCoefNorm.getB()*wB
				+AvgCoefNorm.getD()*wD
				+C*wC
				+E*wE;
		
		
		coefA=AvgCoefNorm.getA();
		coefB=AvgCoefNorm.getB();
		coefC=C;
		coefD=AvgCoefNorm.getD();
		coefE=E;
				
		return metric;
	}
	
	public String getMapInfo(String name, String th1, String th2, String cutNode){
		
				ArrayList<Integer> classCount = new ArrayList<Integer>();
				String cats ="Node Categories\n";
				String text = "<html>\n";
				text +="<h1> Sequence: "+ name+"</h1><br>";
				text += "<p>Th1 = "+ th1 + "</p><p>Th2 = " +th2 + "</p><p>CN = "+ cutNode+"</p><p> Nodes: "+this.getMapSize()+"</p><br>";
							
				text += "<table border=\"1\"   style=\"font-size:8px\"    >";
				text += "<tr><th>Node</th>";
				for(int i = 0;i < 10; ++i )
					text+="<th>Tag "+ String.valueOf(i+1)+ "</th>";
				text +="</tr>\n";
				
				
				int h = 0;
				
				for (Iterator<Node> iterator = nodes.iterator(); iterator.hasNext();) {
					
					Node node =  iterator.next();
					classCount.add(node.getCategoryAmount());
					ArrayList<String> nodetags = node.getTop10Nodes();
					text +="<tr>";
					text +="<td>" + ++h +"</td>";
					for (String tag : nodetags) {
						text +="<td>"+tag +"</td>";
					}
					text +="</tr>\n";
					
					cats+="<br>Node "+(h-1)+"\n";
					cats += node.countCategory();
					cats+= "\n\n\n";
				
					
				}
				
				Collections.sort(classCount);
				
				String catPercentage="<h2>Categories Count Percentage</h2><br><p>Cat_Amout &nbsp;&nbsp; %</p><br>";
				
				Set<Integer> data = new HashSet<>(classCount);
		    
				for(int dat: data){
					int  val = Collections.frequency(classCount, dat);
					catPercentage+="<p>"+dat+"&nbsp;&nbsp;"+(((float)val)*100/nodes.size())+"%</p>";
				}
				
				catPercentage+="<br>";
						
				text += "</table>";
				text +="\n\n\n<br> <p>" +catPercentage+"</p><br>";
										
				text +="\n\n\n<br> <p>" +cats+"</p>";
				text += "\n</html>";
		
		
		return text;
		
	}
	
	
	class NodeCoef{
		float A, B, D;
		
		public NodeCoef(float a, float b, float d){
			A =a;
			B=b;
			D=d;
		}
		
		public float getA(){
			return A;
		}
		
		public float getB(){
			return B;
		}
		
		public float getD(){
			return D;
		}
		
		
		public void setA(float a){
			A = a;
		}
		
		public void setB(float b){
			B = b;
		}
		
		public void setD(float d){
			 D = d;
		}
		
	}
	
	
	class Edge {
		Node a, b;
		
		public Edge (Node ia, Node ib) {
			a=ia;
			b=ib;
		}
		
		public Node getA () {
			return a;
		}
		
		public Node getB () {
			return b;
		}
		
		public float getDistance(){
			float distance=0;
			
			distance = (float) Math.sqrt(Math.pow((a.representative.xcoord-b.representative.xcoord),2) +Math.pow((a.representative.ycoord- b.representative.ycoord),2));
			return distance;
			
		}
		
	}

}
