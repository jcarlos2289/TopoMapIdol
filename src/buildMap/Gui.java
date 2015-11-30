package buildMap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Set;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Gui extends JFrame implements ActionListener {
	private static final long serialVersionUID = -7457350242559078527L;
	private CanvasMap cm;
	int width = 1000, height = 900;
	JButton process, clusterbt, clusterCoefBt;
	JCheckBox originalButton, graphButton, backButton, showNodes,clusters,highTags;
	JTextField th1, th2, th3;
	JComboBox<String> nodes;
	JComboBox<String> tagList;
	double threshold1, threshold2;
	int cutNode;
	boolean original = true, showMap = true, background = true,
			mapGenerated = false, nodesMode = false, selectNodeChanged = false, showCluster = false, tagMode = false, selectTagChanged = false;
	int selectedNode = -1 ;
	String selectedTag = null;
	
	BuildMap bm;
	Kmeans km;
	String name ;
	
	 JMenu jmOperations, jmShows;
	 JMenuItem jmiGetCluster, jmiGenCluster, jmiCapture, jmiGenMap;
     JCheckBoxMenuItem originalCB, graphCB, backCB, showNodesCB, clustersCB, highTagsCB;
	
	public Gui() {
		threshold1 = 0.001;
		threshold2 = 0.01;
		cutNode = 15;
		bm = new BuildMap(threshold1, threshold2, cutNode);
		// bm.readTags("/Users/miguel/Dropbox/Investigacion/Desarrollo/MapaTopologico/tagsNewCollege/NewCollegeTags/PanoStitchOutput_LisaNewCollegeNov3_");
		//bm.readTags("/home/jcarlos2289/workspacejava/tagsNewCollege/NewCollegePlaces/NewCollege_",0.000000001,8127);
		//bm.readTags("/home/jcarlos2289/Documentos/tagsNewCollege/NewCollegePlaces/NewCollege_",0.000000001);

		//bm.readTags("/Users/miguel/Dropbox/Investigacion/Desarrollo/MapaTopologico/tagsNewCollege/NewCollegeTags/PanoStitchOutput_LisaNewCollegeNov3_");
		//bm.readTags("/home/jcarlos2289/workspace/tagsNewCollege/NewCollegePlaces/NewCollege_",0.000000001);
		
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Dumbo/dum_cloudy1/dum_cloudy1_Places/IDOL_DUMBO_Cl1_",-0.000000001,917,"IDOL_DUMBO_Cl1.txt",205,5,2000000);
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_PlacesAlexNet/IDOL_MINNIE_Cl1_",-0.000000001,915, "IDOL_MINNIE_Cl1.txt",205,5,2000000);
		//name = "MinnieCl1_PlacesAlexNet";
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Dumbo/dum_cloudy1/dum_cloudy1_ImageNet/IDOL_DUMBO_Cl1_",-0.000000001,917,"IDOL_DUMBO_Cl1.txt",1000);
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_ImageNet/IDOL_MINNIE_Cl1_",-1.00,915, "IDOL_MINNIE_Cl1.txt",1000);
//name = "MinnieCl1_ImageNetCaffe";
	    //bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Dumbo/dum_cloudy1/dum_cloudy1_Hybrid/IDOL_DUMBO_Cl1_",-0.000000001,917,"IDOL_DUMBO_Cl1.txt",1183);
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_Hybrid/IDOL_MINNIE_Cl1_",-0.000000001,915, "IDOL_MINNIE_Cl1.txt",1183);
//name = "MinnieCl1_HybridAlexNet";
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_ImageNetGoogleNet/IDOL_MINNIE_Cl1_",-0.000000001,915, "IDOL_MINNIE_Cl1.txt",1000);
	//	bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Dumbo/dum_cloudy1/dum_cloudy1_ImageNetGoogleNet/IDOL_DUMBO_Cl1_",-0.000000001,917,"IDOL_DUMBO_Cl1.txt",1000);
//name = "DumboCl1_ImageNetGoogLeNet";
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_ImageNetAlex/IDOL_MINNIE_Cl1_",-0.000000001,915, "IDOL_MINNIE_Cl1.txt",1000);
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Dumbo/dum_cloudy1/dum_cloudy1_ImageNetAlex/IDOL_DUMBO_Cl1_",-0.000000001,917,"IDOL_DUMBO_Cl1.txt",1000);
		//name = "MinnieCl1_ImageNetAlexNet";
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_ImageNetRCNN/IDOL_MINNIE_Cl1_",-0.000000001,915, "IDOL_MINNIE_Cl1.txt",1000);
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Dumbo/dum_cloudy1/dum_cloudy1_ImageNetRCNN/IDOL_DUMBO_Cl1_",-0.000000001,917,"IDOL_DUMBO_Cl1.txt",1000);
        // name = "DumboCl1_ImageNetRCNN";
		
//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_ImageNetVGG/IDOL_MINNIE_Cl1_",-0.000000001,915, "IDOL_MINNIE_Cl1.txt",1000);
	    //bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Dumbo/dum_cloudy1/dum_cloudy1_ImageNetVGG/IDOL_DUMBO_Cl1_",-0.000000001,917,"IDOL_DUMBO_Cl1.txt",1000);
         //name = "DumboCl1_ImageNetVGG";



		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_ImageNetMerge/IDOL_MINNIE_Cl1_",-0.000000001,915, "IDOL_MINNIE_Cl1.txt",1000);
	    //bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Dumbo/dum_cloudy1/dum_cloudy1_ImageNetMerge/IDOL_DUMBO_Cl1_",-0.000000001,917,"IDOL_DUMBO_Cl1.txt",1000);
			
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_ImageNetSUM/IDOL_MINNIE_Cl1_",-0.000000001,915, "IDOL_MINNIE_Cl1.txt",1000);
	    //bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Dumbo/dum_cloudy1/dum_cloudy1_ImageNetSUM/IDOL_DUMBO_Cl1_",-0.000000001,917,"IDOL_DUMBO_Cl1.txt",1000);
		
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_ImageNetFusion/IDOL_MINNIE_Cl1_",-0.000000001,1830, "IDOL_MINNIE_Cl1_FUSION.txt",1000);
			
		//----------------------------Individuales----------------------------------------------------------------
		
		//Cloudy
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_HybridAlexNet/IDOL_MINNIE_Cl1_", -0.00000001, 915, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy1/IDOL_MINNIE_Cl1.txt",1183, 5, 2000000000);
		//name= "MinnieCloudy1_HybridAlexNet";
		
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy2/min_cloudy2_HybridAlexNet/IDOL_MINNIE_Cl2_", -0.00000001, 968, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy2/IDOL_MINNIE_Cl2.txt",1183, 5, 2000000000);
		//name= "MinnieCloudy2_HybridAlexNet";
		
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy3/min_cloudy3_HybridAlexNet/IDOL_MINNIE_Cl3_", -0.00000001, 894, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy3/IDOL_MINNIE_Cl3.txt",1183, 5, 2000000000);
		//name= "MinnieCloudy3_HybridAlexNet";
		
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy4/min_cloudy4_HybridAlexNet/IDOL_MINNIE_Cl4_", -0.00000001, 975, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_cloudy4/IDOL_MINNIE_Cl4.txt",1183, 5, 2000000000);
		//name= "MinnieCloudy4_HybridAlexNet";
		
		
		//Night
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_night1/min_night1_HybridAlexNet/IDOL_MINNIE_Ni1_", -0.00000001, 1039, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_night1/IDOL_MINNIE_Ni1.txt",1183, 5, 2000000000);
		//name= "MinnieNight1_HybridAlexNet";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_night2/min_night2_HybridAlexNet/IDOL_MINNIE_Ni2_", -0.00000001, 1181, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_night2/IDOL_MINNIE_Ni2.txt",1183, 5, 2000000000);
		//name= "MinnieNight2_HybridAlexNet";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_night3/min_night3_HybridAlexNet/IDOL_MINNIE_Ni3_", -0.00000001, 921, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_night3/IDOL_MINNIE_Ni3.txt",1183, 5, 2000000000);
		//name= "MinnieNight3_HybridAlexNet";

		bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_night4/min_night4_HybridAlexNet/IDOL_MINNIE_Ni4_", -0.00000001, 864, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_night4/IDOL_MINNIE_Ni4.txt",1183, 5, 2000000000);
		name= "MinnieNight4_HybridAlexNet";


		//Sunny
				
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_sunny1/min_sunny1_HybridAlexNet/IDOL_MINNIE_Su1_", -0.00000001, 853, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_sunny1/IDOL_MINNIE_Su1.txt",1183, 5, 2000000000);
		//name= "MinnieSunny1_HybridAlexNet";
				
		//bm.readTags("/home/rvg/Descargas/KTH_IDOL/KTH_Minnie/min_sunny2/min_sunny2_HybridAlexNet/IDOL_MINNIE_Su2_", -0.00000001, 849, "/home/rvg/Descargas/KTH_IDOL/KTH_Minnie/min_sunny2/IDOL_MINNIE_Su2.txt",1183, 5, 2000000000);
		//name= "MinnieSunny2_HybridAlexNet";

		//bm.readTags("/home/rvg/Descargas/KTH_IDOL/KTH_Minnie/min_sunny3/min_sunny3_HybridAlexNet/IDOL_MINNIE_Su3_", -0.00000001, 1014, "/home/rvg/Descargas/KTH_IDOL/KTH_Minnie/min_sunny3/IDOL_MINNIE_Su3.txt",1183, 5, 2000000000);
		//name= "MinnieSunny3_HybridAlexNet";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_sunny4/min_sunny4_HybridAlexNet/IDOL_MINNIE_Su4_", -0.00000001, 890, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie/min_sunny4/IDOL_MINNIE_Su4.txt",1183, 5, 2000000000);
		//name= "MinnieSunny4_HybridAlexNet";
		
			
		
		
//------------------------------------------------Fusion		
//CLOUDY
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/min_cloudy_PlacesAlexNet/IDOL_MINNIE_Cl_",-0.000000001,3752, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/IDOL_MINNIE_Cl.txt",205);
		//name = "MinnieCloudy_PlacesAlexNet";
		
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/min_cloudy_ImageNetAlexNet/IDOL_MINNIE_Cl_",-0.000000001,3752, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/IDOL_MINNIE_Cl.txt",205);
		//name = "MinnieCloudy_ImageNetAlexNet";
		
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/min_cloudy_ImageNetCaffeNet/IDOL_MINNIE_Cl_",-0.000000001,3752, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/IDOL_MINNIE_Cl.txt",205);
		//name = "MinnieCloudy_ImageNetCaffeNet";
		
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/min_cloudy_ImageNetGoogLeNet/IDOL_MINNIE_Cl_",-0.000000001,3752, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/IDOL_MINNIE_Cl.txt",205);
		//name = "MinnieCloudy_ImageNetGoogLeNet";
		
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/min_cloudy_ImageNetVGG/IDOL_MINNIE_Cl_",-0.000000001,3752, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/IDOL_MINNIE_Cl.txt",205);
		//name = "MinnieCloudy_ImageNetVGG";
		
		 //bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/min_cloudy_HybridAlexNet/IDOL_MINNIE_Cl_",-0.000000001,3752, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_cloudy/IDOL_MINNIE_Cl.txt",1183,5, 200000000);
	    //name = "MinnieCloudy_HybridAlexNet";

//Sunny
		
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/min_sunny_PlacesAlexNet/IDOL_MINNIE_Su_",-0.000000001,3606, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/IDOL_MINNIE_Su.txt",205);
		//name = "MinnieSunny_PlacesAlexNet";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/min_sunny_ImageNetAlexNet/IDOL_MINNIE_Su_",-0.000000001,3606, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/IDOL_MINNIE_Su.txt",205);
		//name = "MinnieSunny_ImageNetAlexNet";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/min_sunny_ImageNetCaffeNet/IDOL_MINNIE_Su_",-0.000000001,3606, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/IDOL_MINNIE_Su.txt",205);
		//name = "MinnieSunny_ImageNetCaffeNet";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/min_sunny_ImageNetGoogLeNet/IDOL_MINNIE_Su_",-0.000000001,3606, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/IDOL_MINNIE_Su.txt",205);
		//name = "MinnieSunny_ImageNetGoogLeNet";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/min_sunny_ImageNetVGG/IDOL_MINNIE_Su_",-0.000000001,3606, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/IDOL_MINNIE_Su.txt",205);
		//name = "MinnieSunny_ImageNetVGG";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/min_sunny_HybridAlexNet/IDOL_MINNIE_Su_",-0.000000001,3606, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_sunny/IDOL_MINNIE_Su.txt",1183,5,2000000);
		//name = "MinnieSunny_HybridAlexNet";



//Night
		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/min_night_PlacesAlexNet/IDOL_MINNIE_Ni_",-0.000000001,4005, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/IDOL_MINNIE_Ni.txt",205,5, 200000000);
		//name = "Minnienight_PlacesAlexNet";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/min_night_ImageNetAlexNet/IDOL_MINNIE_Ni_",-0.000000001,4005, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/IDOL_MINNIE_Ni.txt",205);
		//name = "MinnieNight_ImageNetAlexNet";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/min_night_ImageNetCaffeNet/IDOL_MINNIE_Ni_",-0.000000001,4005, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/IDOL_MINNIE_Ni.txt",205);
		//name = "MinnieNight_ImageNetCaffeNet";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/min_night_ImageNetGoogLeNet/IDOL_MINNIE_Ni_",-0.000000001,4005, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/IDOL_MINNIE_Ni.txt",205);
		//name = "MinnieNight_ImageNetGoogLeNet";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/min_night_ImageNetVGG/IDOL_MINNIE_Ni_",-0.000000001,4005, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/IDOL_MINNIE_Ni.txt",205);
		//name = "MinnieNight_ImageNetVGG";

		//bm.readTags("/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/min_night_HybridAlexNet/IDOL_MINNIE_Ni_",-0.000000001,4005, "/home/jcarlos2289/Descargas/KTH_IDOL/KTH_Minnie_Fusion/min_night/IDOL_MINNIE_Ni.txt",1183,5,2000000);
		//name = "MinnieNight_HybridAlexNet3";
		
		getContentPane().setLayout(new BorderLayout());
		setSize(width, height);
		setTitle(name);
		cm = new CanvasMap(this);
		//setTitle("Topological Mapping");
		getContentPane().add(cm, BorderLayout.CENTER);
		getContentPane().add(getToolBar(), BorderLayout.NORTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		
	}

	public JPanel getToolBar() {
		
		    jmOperations = new JMenu();
	        //jmOperations.addActionListener(this);
	        jmOperations.setText("Operations");

	        jmiGenMap = new JMenuItem("Gen Map");
	        jmiGenMap.addActionListener(this);
	        jmOperations.add(jmiGenMap);

	        jmiGetCluster = new JMenuItem();
	        jmiGetCluster.setText("Get K Cluster");
	        jmiGetCluster.addActionListener(this);
	        jmOperations.add(jmiGetCluster);

	        jmiGenCluster = new JMenuItem();
	        jmiGenCluster.setText("Gen Cluster");
	        jmiGenCluster.addActionListener(this);
	        jmOperations.add(jmiGenCluster);

	        jmiCapture = new JMenuItem("Capture Screen");
	        jmiCapture.addActionListener(this);
	        jmiCapture.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
	        jmOperations.add(jmiCapture);

	        jmShows = new JMenu("View");
	        
	        originalCB= new javax.swing.JCheckBoxMenuItem("OriginalMap");
	        originalCB.setSelected(true);
	        originalCB.addActionListener(this);
	                
	        graphCB = new javax.swing.JCheckBoxMenuItem("Graph");
	        graphCB.addActionListener(this);
	        graphCB.setSelected(true);
	        graphCB.setEnabled(mapGenerated);
	        
	        
	        backCB = new javax.swing.JCheckBoxMenuItem("Background Image");
	        backCB.setSelected(true);
	        backCB.addActionListener(this);
	        
	           
	        showNodesCB = new javax.swing.JCheckBoxMenuItem("Show Nodes");
	        showNodesCB.setSelected(false);
	        showNodesCB.setEnabled(false);
	        showNodesCB.addActionListener(this);
	        
	        
	        clustersCB =new JCheckBoxMenuItem("Clusters");
	        clustersCB.addActionListener(this);
	        clustersCB.setSelected(false);
			clustersCB.setEnabled(false);
			
			highTagsCB = new JCheckBoxMenuItem("Show High Tag");
			highTagsCB.setSelected(false);
			highTagsCB.setEnabled(false);
			highTagsCB.addActionListener(this);
			
			
	                
	        jmShows.add(originalCB);
	        jmShows.add(graphCB);
	        jmShows.add(backCB);
	        jmShows.add(showNodesCB);
	        jmShows.add(clustersCB);
	        jmShows.add(highTagsCB);
	        
	        //JPanel jp2 = new JPanel();
	        JMenuBar jMenuBar1 = new JMenuBar();
	        jMenuBar1.add(jmOperations);
	        jMenuBar1.add(jmShows);

	        this.setJMenuBar(jMenuBar1);
		
		
		
		
		JPanel jp = new JPanel();
		jp.setSize(width, 100);
		jp.setLayout(new BoxLayout(jp, BoxLayout.LINE_AXIS));
		jp.setAlignmentX(LEFT_ALIGNMENT);
		process = new JButton("Gen Map");
		process.addActionListener(this);
		//jp.add(process);

		clusterbt = new JButton("Gen Clus");
		clusterbt.addActionListener(this);
		//jp.add(clusterbt);
		
		clusterCoefBt = new JButton("ClusCoef");
		clusterCoefBt.addActionListener(this);
		//jp.add(clusterCoefBt);

		originalButton = new JCheckBox("Original Map");
		originalButton.setSelected(true);
		originalButton.addActionListener(this);
		//jp.add(originalButton);
		
		graphButton = new JCheckBox("Graph");
		graphButton.setSelected(true);
		graphButton.setEnabled(mapGenerated);
		graphButton.addActionListener(this);
		//jp.add(graphButton);
	
		backButton = new JCheckBox("BackImage");
		backButton.setSelected(true);
		backButton.addActionListener(this);
		//jp.add(backButton);
		
		clusters = new JCheckBox("ShowCluster");
		clusters.setSelected(false);
		clusters.setEnabled(false);
		clusters.addActionListener(this);
	//	jp.add(clusters);
		
		highTags = new JCheckBox("ShowHighTag");
		highTags.setSelected(false);
		highTags.setEnabled(false);
		highTags.addActionListener(this);
		//jp.add(highTags);
		
		
		JLabel lab1 = new JLabel("Threshold1");
		jp.add(lab1);
		th1 = new JTextField(String.valueOf(threshold1));
		th1.addActionListener(this);
		jp.add(th1);
		JLabel lab2 = new JLabel("Threshold2");
		jp.add(lab2);
		th2 = new JTextField(String.valueOf(threshold2));
		th2.addActionListener(this);
		jp.add(th2);
		JLabel lab3 = new JLabel("CutNode");
		jp.add(lab3);
		th3 = new JTextField(String.valueOf(cutNode));
		th3.addActionListener(this);
		jp.add(th3);
		
		showNodes = new JCheckBox("ShowNodes");
		showNodes.setSelected(false);
		showNodes.setEnabled(false);
		showNodes.addActionListener(this);
		//jp.add(showNodes);
		
		String[] aux = {"Select Node"};
		nodes = new JComboBox<String>(aux);
		nodes.addActionListener(this);
		nodes.setEnabled(nodesMode);
		jp.add(nodes);
		
		
		String[] aux2 = {"Select Tag"};
		tagList = new JComboBox<String>(aux2);
		tagList.addActionListener(this);
		tagList.setEnabled(tagMode); 
		jp.add(tagList);
		
		
		
		return jp;
	}

	public void genComboNodes() {
		int size = bm.map.nodes.size();
		String[] aux = new String[size + 1];
		aux[0] = "Select Node";
		for (int i = 1; i < size + 1; i++) {
			aux[i] = String.valueOf(i - 1) + " "
					+ bm.map.getNode(i - 1).getSize();
		}
		nodes.setModel(new DefaultComboBoxModel<String>(aux));
		nodes.setSelectedIndex(0);
		//Imprimir lista de nodos
		/*String lst = "";
		for (int i = 0; i < aux.length; i++) {
			lst += aux[i] + "\n";
		}*/

		// FileMethods.saveFile(lst, "NodeList", false);
	}
	
	public void genComboTag(){
		int size = bm.dimension;
		String[] aux = new String[size + 1];
		aux[0] = "Select Tag";
		
		ImageTags auxImage;
		
		auxImage = bm.imgTags.get(0);
		Set<String> keyset = auxImage.tags.keySet();
		
		ArrayList<String> keys =new ArrayList<String>();
		
		for (String object : keyset) {
			keys.add(object);
		}
		
		Collections.sort(keys);
		
		for (int i = 1; i < size + 1; i++) {
			aux[i] =keys.get(i-1);
		}
		
	
		tagList.setModel(new DefaultComboBoxModel<String>(aux));
		tagList.setSelectedIndex(0);
		
	}
	

	public static void main(String[] args) {
		Gui g = new Gui();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		 g.setVisible(true);
	     g.toFront();
		
		// Each tag contains a name and probability assigned to it by the recognition engine.
        //System.out.println(String.format("  %s (%.4f)", tag.getName(), tag.getProbability()));
		
		/*
			
		double val=0;
        if (args.length ==0||args.length < 1){
            System.out.println("Ingresa el Th2");
            return;
        }else
            val = Double.parseDouble(args[0]);
        
        g.name+="_"+args[0];
       
        g.setTitle(g.name);
        g.setVisible(true);
        g.toFront();

		/*
		String DATARESUME ;
        float incremento = (float) 0.001;
        float th1 = (float) 0.001;
        float th2 = (float) val;
        int vuelta=1;
        //DecimalFormatSymbols simbolos;
        DecimalFormatSymbols simbol = new DecimalFormatSymbols();
        simbol.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.######", simbol);
       
        for (int i = 0; i <1; i++) {
            for (int j = 0; j <30; j++) {
                g.bm.setThreshold1(th1);
                g.bm.setThreshold2(th2);
                g.bm.buildMap();
                System.out.printf("i= %d\tj= %d\t Ciclo= %d/30\t th1= %.4f\t th2= %.4f\n", i,j,vuelta, th1, th2);
                th1+=incremento;
               
                        //String.format("%,6f",g.bm.threshold1)   
                float metric = g.bm.map.getMapMetric(g.cm.MaxDistance());
                DATARESUME=formateador.format(g.bm.threshold1)+ ";"
               
                        + formateador.format(g.bm.threshold2)+ ";"
                        +g.bm.cutNode+ ";"+g.bm.map.nodes.size()+ ";"
                        +g.bm.map.edges.size()+ ";"   
                        +g.bm.map.coefA+ ";"   
                        +g.bm.map.coefB+ ";"   
                        +g.bm.map.coefC+ ";"   
                        +g.bm.map.coefD+ ";"   
                        +g.bm.map.coefE+ ";"   
                        +metric+"\n";
                FileMethods.saveFile(DATARESUME, g.name+"_MetricsData", true);
               
                ++vuelta;
                }
            th2+=0.001;
            th1=(float)  0.001;
        }
        //---------------------------Segunda Vuelta--------------------------
       /*
        incremento = (float) 0.001;
        th1 = (float) 0.001;
        th2 = (float) 0.038;
        //vuelta=1;
        //DecimalFormatSymbols simbolos;
               
        for (int i = 0; i <12; i++) {
            for (int j = 0; j <30; j++) {
                g.bm.setThreshold1(th1);
                g.bm.setThreshold2(th2);
                g.bm.buildMap();
                System.out.printf("i= %d\tj= %d\t Ciclo= %d\t th1= %.4f\t th2= %.4f\n", i,j,vuelta, th1, th2);
                th1+=incremento;
               
                        //String.format("%,6f",g.bm.threshold1)   
                float metric = g.bm.map.getMapMetric(g.cm.MaxDistance());
                DATARESUME=formateador.format(g.bm.threshold1)+ ";"
               
                        + formateador.format(g.bm.threshold2)+ ";"
                        +g.bm.cutNode+ ";"+g.bm.map.nodes.size()+ ";"
                        +g.bm.map.edges.size()+ ";"   
                        +g.bm.map.coefA+ ";"   
                        +g.bm.map.coefB+ ";"   
                        +g.bm.map.coefC+ ";"   
                        +g.bm.map.coefD+ ";"   
                        +g.bm.map.coefE+ ";"   
                        +metric+"\n";
                FileMethods.saveFile(DATARESUME, g.name+"_MetricsData", true);
               
                ++vuelta;
                }
            th2+=0.001;
            th1=(float)  0.001;
        }
               
       */
       // g.setVisible(false);
      //  g.dispose();
	
		/*
		System.out.println("Width-X= "+g.cm.getMaxWidth());
		System.out.println("Heigth-Y= "+g.cm.getMaxHeight());
		System.out.println("DMAX= " +g.cm.MaxDistance());
		
		g.dispose();
		
		*/
		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == process) {
			graphButton.setEnabled(false);
			showNodes.setEnabled(false);
			bm.buildMap();
			graphButton.setEnabled(true);
			showNodes.setEnabled(true);
			genComboNodes();
			genComboTag();
			
			mapGenerated = true;
			cm.repaint();
			cm.showNodeDetails();
			cm.showMapInfo();
			
				
			
			//String DATARESUME ="Th1;Th2;CN;Nodes;Edges;Metric\n";
		//	float metric = bm.map.getMapMetric(cm.MaxDistance());
			//DATARESUME+=bm.threshold1+ ";"+ bm.threshold2+ ";"+bm.cutNode+ ";"+bm.map.nodes.size()+ ";"+bm.map.edges.size()+ ";"+metric+"\n";
			//FileMethods.saveFile(DATARESUME, name+"_MetricsData", true);
			return;
		}
		if (e.getSource() == clusterbt) {
		    // Kmeans
			int k =Integer.parseInt( JOptionPane.showInputDialog("How many clusters?","4"));
			km= new Kmeans(k, bm.dimension, bm.imgTags);
			km.findMeans();
			//mapGenerated = false;
			showCluster=true;
			clusters.setEnabled(true);
			clusters.setSelected(true);
			cm.repaint();
			return;
		}
		
				
		if (e.getSource() == clusterCoefBt) {
			Kmeans km2;
			km2 = new Kmeans(1,bm.dimension, bm.imgTags);
			ArrayList<Float> coef = new ArrayList<Float>();
			int k=1; 
			
			if(k==1)
				FileMethods.saveFile("K;s2\n", "K_Variances_"+name, false);
			
			
			do {
				km2.setK(k);
				Float coefValue =km2.findMeansCoef();
				coef.add(coefValue); 
				FileMethods.saveFile(String.valueOf(k)+";"+String.valueOf(coefValue)+"\n", "K_Variances_"+name, true);
				++k;
				if ((k%100)==0) System.out.println("K="+k);
			}
			while(k<=800);
			
			
			/*			
			try {
				DrawLineChart.viewChart(coef,name);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				*/
			
			
			return;
		}
		
		
		
		if (e.getSource() == originalButton) {
			//showCluster = false;
			original = originalButton.isSelected();
			cm.repaint();
			return;
		}
		if (e.getSource() == graphButton) {
			showMap = graphButton.isSelected();
			cm.repaint();
			return;
		}
		if (e.getSource() == backButton) {
			background = backButton.isSelected();
			cm.repaint();
			return;
		}
		
		if (e.getSource() == clusters) {
			showCluster = clusters.isSelected();
			cm.repaint();
			return;
		}
		
		if (e.getSource() == th1) {
			threshold1 = Double.parseDouble(th1.getText());
			bm.setThreshold1(threshold1);
			cm.repaint();
			return;
		}
		if (e.getSource() == th2) {
			threshold2 = Double.parseDouble(th2.getText());
			bm.setThreshold2(threshold2);
			cm.repaint();
			return;
		}
		if (e.getSource() == th3) {
			System.out.println(th3.getText());
			bm.setCutNode(Integer.parseInt(th3.getText()));
			if (bm.map != null)
				bm.map.setWeights(Integer.parseInt(th3.getText()));
			cm.repaint();
			return;
		}
		if (e.getSource() == showNodes) {
			nodesMode = showNodes.isSelected();
			selectedNode = -1;
			nodes.setEnabled(true);
			cm.repaint();
			return;
		}

		if (e.getSource() == nodes) {
			if (((String) nodes.getSelectedItem()).equals("Select Node")) {
				selectedNode = -1;
			} else {
				selectedNode = Integer.valueOf(
						((String) nodes.getSelectedItem()).split(" ")[0]);
				selectNodeChanged = true;
				cm.repaint();
			}
			return;
		}
		
		if (e.getSource() == tagList) {
			if (((String) tagList.getSelectedItem()).equals("Select Tag")) {
				selectedTag = null;
			} else {
				selectedTag = (String) tagList.getSelectedItem();
				selectTagChanged = true;
				tagMode =true;
				nodesMode=false;
				cm.repaint();
			}
			return;
		}
		
		
		
		
		//-*----------------------------------------------
		
		
		
		   if (e.getSource() == originalCB) {
	            original = originalCB.isSelected();
	            cm.repaint();
	            return;
	        }
	        if (e.getSource() == graphCB) {
	            showMap = graphCB.isSelected();
	            cm.repaint();
	            return;
	        }
	        if (e.getSource() == backCB) {
	            background = backCB.isSelected();
	            cm.repaint();
	            return;
	        }
	        
	         if (e.getSource() == showNodesCB) {
	            nodesMode = showNodesCB.isSelected();
	            selectedNode = -1;
	            nodes.setEnabled(true);
	            cm.repaint();
	            return;
	        }
	         
	         if(e.getSource()== highTagsCB){
	              	tagMode=highTags.isSelected();
	        	// tagList.setEnabled(highTags.isSelected());
	             
	        	 cm.repaint();
	        	 return;
	         }
	         
	     	if (e.getSource() == clustersCB) {
				showCluster = clustersCB.isSelected();
				cm.repaint();
				return;
			}
	     	         
	          if (e.getSource() == jmiGenMap) {
	        	graphCB.setEnabled(false);
	  			showNodesCB.setEnabled(false);
	  			bm.buildMap();
	  			graphCB.setEnabled(true);
	  			showNodesCB.setEnabled(true);
	  			//highTagsCB.setEnabled(true);
	  			genComboNodes();
	  			genComboTag();
	  			tagList.setEnabled(true);
	  			highTagsCB.setEnabled(true);
	  			highTagsCB.setSelected(true);
	  			tagMode =true;
	  			mapGenerated = true;
	  			cm.repaint();
	  			cm.showNodeDetails();
	  			cm.showMapInfo();
	            return;
	        }
	          
	          if (e.getSource() == jmiCapture) {
	        	  Date date = new Date();
	        	  DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	        	  String imgName = name+"_"+bm.threshold1+"_"+bm.threshold2+"_"+hourdateFormat.format(date);
		  			cm.createImage(imgName);
		  			return;
		  		}
	          
	          if (e.getSource() == jmiGenCluster) {
	  		    // Kmeans
	  			int k =Integer.parseInt( JOptionPane.showInputDialog("How many clusters?","4"));
	  			km= new Kmeans(k, bm.dimension, bm.imgTags);
	  			km.findMeans();
	  			//mapGenerated = false;
	  			showCluster=true;
	  			clustersCB.setEnabled(true);
	  			clustersCB.setSelected(true);
	  			cm.repaint();
	  			return;
	  		}
	  			  				
	  		if (e.getSource() == jmiGetCluster) {
	  			Kmeans km2;
	  			km2 = new Kmeans(1,bm.dimension, bm.imgTags);
	  			ArrayList<Float> coef = new ArrayList<Float>();
	  			int k=1; 
	  			
	  			if(k==1)
	  				FileMethods.saveFile("K;s2\n", "K_Variances_"+name, false);
	  			
	  			
	  			do {
	  				km2.setK(k);
	  				Float coefValue =km2.findMeansCoef();
	  				coef.add(coefValue); 
	  				FileMethods.saveFile(String.valueOf(k)+";"+String.valueOf(coefValue)+"\n", "K_Variances_"+name, true);
	  				++k;
	  				if ((k%100)==0) System.out.println("K="+k);
	  			}
	  			while(k<=800);
	  			
	  		
	  			
	  			return;
	  		}
	          
	        
	        
		
		
	}

}
