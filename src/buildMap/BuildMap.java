package buildMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class BuildMap {
	ArrayList<ImageTags> imgTags;
	ImageTags itags;
	double threshold1, threshold2;
	//float threshold3;
	int cutNode;
	Map map;
	int sequenceLength;
	int dimension = 0;
	int nClass =0;
	int loopClose;
	public void setThreshold1(double threshold1) {
		this.threshold1 = threshold1;
	}

	public void setThreshold2(double threshold2) {
		this.threshold2 = threshold2;
	}

	public void setCutNode(int cutNode) {
		this.cutNode = cutNode;
	}

	public BuildMap (double th1, double th2, int cn) {
		imgTags=new ArrayList<ImageTags>();
		threshold1=th1;
		threshold2=th2;
		cutNode=cn;
		
	}
	
	public void readTags (String base, double threshold, int seqLenght, String dataPath, int dim, int clas , int loop) {
		String fileName;
		FileReader fr=null;
		BufferedReader br=null;
		String line;
		nClass = clas;
		dimension = dim;
		sequenceLength = seqLenght;
		loopClose = loop;
		// First, read the image coordinates files
		double []xcoord, ycoord;
		String[] cats;
		xcoord= new double[sequenceLength+1];
		ycoord= new double[sequenceLength+1];
		cats=new String[sequenceLength+1];
		
		for (int i=0; i<sequenceLength+1; i++) {
			xcoord[i]=ycoord[i]=-1;
		}
		
		try {
			//fr = new FileReader (new File ("IDOL_DUMBO_Cl1.txt"));
			fr = new FileReader (new File (dataPath));
			br = new BufferedReader(fr);
			// The first line is the name of the file, ignored
			line = br.readLine();
			while ((line=br.readLine())!=null) {
				String[] sp = line.split(" ");
				int i=Integer.parseInt(sp[0]);
				xcoord[i]=Double.parseDouble(sp[1]);
				ycoord[i]=Double.parseDouble(sp[2]);
				cats[i]=sp[4];
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
		finally {
			try {
				if (fr != null) {   
					fr.close();
				}
			} 
			catch (Exception e2){ 
				e2.printStackTrace();
			}
		}
		
		// Process all the images and read the tags
		for (int i=1; i<sequenceLength+1; i++) {
			fileName=base+i+".txt";
			try {
				itags=new ImageTags(fileName);
				itags.setThreshold((float)threshold);
				itags.setCategory(cats[i]);
				if (xcoord[i]!=-1) {
					itags.setCoords(xcoord[i], ycoord[i]);
				}
				else {
					for (int j=i-2, k=i+1; j>0 || k<sequenceLength; j--, k++) {
						if (j>0 && xcoord[j]!=-1) {
							itags.setCoords(xcoord[j], ycoord[j]);
							break;
						}
						if (k<sequenceLength && xcoord[k]!=-1) {
							itags.setCoords(xcoord[k], ycoord[k]);
							break;
						}
					}
				}
				fr = new FileReader (new File (fileName));
				br = new BufferedReader(fr);
				// The first line is the name of the file, ignored
				//line = br.readLine(); //------------------------------------------------------
				while ((line=br.readLine())!=null) {
					itags.addTag(line);
				}
				
				imgTags.add(itags);
			}
			catch(Exception e) {
				e.printStackTrace();
			} 
			finally {
				try {
					if (fr != null) {   
						fr.close();
					}
				} 
				catch (Exception e2){ 
					e2.printStackTrace();
				}
			}
		}
	}
	
	public void buildMap () {
		//FileMethods.saveFile("Th1= "+threshold1+" Th2= " +threshold2+" CN= "+ cutNode+"------\n", "Distancias", true);	
		map = new Map();
		map.setWeights(cutNode);
		double minDist, dist;
		int cont=0;
		Node auxNode, auxNode2;
		@SuppressWarnings("unused")
		boolean foundNode, foundEdge;
		
		// For the first image, create a node
		Node currentNode = map.createNode(imgTags.get(0));
		for (int i=1; i<sequenceLength; i++) {
			cont++;
			if ((cont%1000)==0) System.out.println("Processing img="+i);
			// Find the closest node
			minDist=Double.MAX_VALUE;
			auxNode2=null;
			for (int n=0; n<map.getMapSize(); n++) {
				auxNode=map.getNode(n);
				if (auxNode!=currentNode) {
					dist=auxNode.distance(imgTags.get(i));
					//dist=auxNode.x2(auxNode.histoMean,imgTags.get(i));
					//dist=auxNode.kullback(auxNode.histoMean,imgTags.get(i));
					if (dist<minDist) {
						minDist=dist;
						auxNode2=auxNode;
					}
				}
			}
			dist = currentNode.distance(imgTags.get(i));
			//dist = currentNode.x2(currentNode.histoMean,imgTags.get(i));
			//dist = currentNode.kullback(currentNode.histoMean,imgTags.get(i));
			//FileMethods.saveFile(String.valueOf(dist)+"\n","Distancias", true);	

			//if(i<loopClose){   //Indicar en que imagen se cierra la primera vuelva, luego de esto solo añade la imagen al nodo q tenga la menor distancia
			if (dist<threshold2) {
				currentNode.add(imgTags.get(i));
			}
			else {
				if (minDist<dist && minDist<threshold1) {
					foundEdge=false;
					map.createEdge(currentNode,auxNode2);
					currentNode=auxNode2;
					currentNode.add(imgTags.get(i));
				}
				else {
					// Final decision: create a new node
					auxNode = currentNode;
					currentNode = map.createNode(imgTags.get(i));
					map.createEdge(auxNode, currentNode);
				}
			}
//			}else{
//				//Solo agregar al nodo con la menor distancia
//				currentNode=auxNode2;
//				currentNode.add(imgTags.get(i));
//				
//			}
			

		}//end for
		//Calcular Metrica Aqui
		
		
	}
	
	public void printMap () {
		map.printMap();
	}
}
