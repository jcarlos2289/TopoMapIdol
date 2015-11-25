package buildMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

//import org.jfree.data.category.CategoryDataset;
//import org.jfree.data.category.DefaultCategoryDataset;

import buildMap.Map.Edge;

public class Node {
	ArrayList<ImageTags> images;
	ImageTags representative;
	ArrayList<ArrayList<Double>> distances;
	ArrayList<Double> sumDistances;
	HashMap<String, Float> histoMean;
	HashMap<String, Float> histoVariance;
	HashMap<String, Float> histoCuadSum;
	HashMap<String, Float> histoStanDev;
	HashMap<String, Float> histoMeanDev;
	HashMap<String, Float> histoSumMeanDev;
	//HashMap<String,Float> w;
	//float wSum;

	float nodeVariance;
	boolean useHisto;
	float[] weights;

	public Node(boolean hm, float[] ws) {
		useHisto = hm;
		images = new ArrayList<ImageTags>();
		if (!useHisto) {
			distances = new ArrayList<ArrayList<Double>>();
			sumDistances = new ArrayList<Double>();
		} else {
			histoMean = new HashMap<String, Float>();
			histoCuadSum = new HashMap<String, Float>();
			histoVariance = new HashMap<String, Float>();
			histoStanDev = new HashMap<String, Float>();
			histoMeanDev = new HashMap<String, Float>();
			histoSumMeanDev = new HashMap<String, Float>();
			//w = new HashMap<String,Float>();
			nodeVariance =0;
			//wSum=0;

		}
		weights = ws;
	}

	public int getSize() {
		return images.size();
	}

	public void printMeanCoords() {
		double xmean = 0.0, ymean = 0.0;
		int cont = 0;

		for (int i = 0; i < images.size(); i++) {
			if (images.get(i).xcoord != -1) {
				xmean += images.get(i).xcoord;
				ymean += images.get(i).ycoord;
				cont++;
			}
		}
		System.out.println(xmean / cont + " " + ymean / cont);
	}

	public void printCoords() {
		if (representative.xcoord != -1)
			System.out.println(representative.xcoord + " " + representative.ycoord);
		else {
			for (int i = 0; i < images.size(); i++) {
				if (images.get(i).xcoord != -1) {
					System.out.println(images.get(i).xcoord + " " + images.get(i).ycoord);
					break;
				}
			}
		}
	}

	public void add(ImageTags img) {
		double minDist = Double.MAX_VALUE, auxDist, auxSumDist = 0.0;
		int minImg = 0;
		double auxx, auxy;
		ArrayList<Double> di = new ArrayList<Double>();
		Iterator<String> iterator;
		String elem;

		if (!useHisto) { // I don't use this
			if (images.size() == 0) {
				images.add(img);
				representative = img;
				di.add(0.0);
				distances.add(di);
				sumDistances.add(0.0);
			} else {
				for (int i = 0; i < images.size(); i++) {
					auxDist = distance(img, images.get(i));
					distances.get(i).add(auxDist);
					di.add(auxDist);
					sumDistances.set(i, sumDistances.get(i) + auxDist);
					auxSumDist += auxDist;
				}
				images.add(img);
				sumDistances.add(auxSumDist);
				di.add(0.0);
				distances.add(di);
				for (int i = 0; i < sumDistances.size(); i++) {
					if (sumDistances.get(i) < minDist) {
						minDist = sumDistances.get(i);
						minImg = i;
					}
				}
				representative = images.get(minImg);
			}
		} else {// The processes begin here
			if (images.size() == 0) { // When the node is created, only one image is present
				images.add(img);
				representative = new ImageTags("aux");
				representative.xcoord = img.xcoord;
				representative.ycoord = img.ycoord;
				iterator = img.getKeys().iterator();
				while (iterator.hasNext()) {
					elem = iterator.next();
					histoMean.put(elem, img.tags.get(elem));
					// number of nodes n = 1 in the begining
					histoVariance.put(elem, (float) (Math.pow(img.tags.get(elem) - histoMean.get(elem), 2) ));
					histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
					
				}
			} else {// When the node have some images inside, without weights
				images.add(img);
				int n = images.size();
				double[] xmedian = new double[images.size()], ymedian = new double[images.size()];
				if (images.size() < 2 * weights.length) { // only when we don't need to use the weights
					iterator = img.getKeys().iterator();
					while (iterator.hasNext()) {
						elem = iterator.next();
						if (histoMean.get(elem) != null) { // the tag is present// in the hash
						    //float ram = histoMean.get(elem);
							histoMean.put(elem, (histoMean.get(elem) * (n - 1) + img.tags.get(elem)) / n);
							} else {
							// The actual tag don't exits in the hash, so we add
							histoMean.put(elem, img.tags.get(elem) / n);
							}
					}
					for (int i = 0; i < n; i++) {
						xmedian[i] = images.get(i).xcoord;
						ymedian[i] = images.get(i).ycoord;
					}
					//HashMap g = New HashMap<String, ArrayList<Double>>;
					// by JC
					// --------------------------------------------------------------------
					// calcular la varianza sin los pesos
					iterator = images.get(0).getKeys().iterator();
					while (iterator.hasNext()) {
						elem = iterator.next(); // The first element is added to reset all the elements in the hash
						histoVariance.put(elem, (float) (Math.pow(images.get(0).tags.get(elem) - histoMean.get(elem), 2)/ images.size()));
						histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
						}

					ImageTags imagesAux;
					// int imgSize = images.size();
					for (int i = 1; i < images.size(); ++i) {
						imagesAux = images.get(i);
						Iterator<String> keyAux = imagesAux.getKeys().iterator();
						while (keyAux.hasNext()) {
							String key = keyAux.next();
							if (histoVariance.get(key) != null) { // the actual tag is present in the hashMap
								histoVariance.put(key, (float) (histoVariance.get(key)+ Math.pow(imagesAux.tags.get(key) - histoMean.get(key), 2) / (images.size())));
							} else // the tag isn't in the hashMap
								histoVariance.put(key,(float) (Math.pow(imagesAux.tags.get(key) - histoMean.get(key), 2)/ (images.size())));
							histoStanDev.put(key, (float) Math.sqrt(histoVariance.get(key)));
							}
					}

				} else {// Recalculate the histoMean using all the values, using
						// the weights
					float sumWeights =0;
					float auxWeight =0;
					
					for (int i = 0; i < weights.length; i++) {
						auxWeight +=  weights[i];
					}
					
					sumWeights = 2*auxWeight + n -2*weights.length;
					
					iterator = images.get(0).getKeys().iterator();
					while (iterator.hasNext()) {
						elem = iterator.next(); // The first element is added  and multiply by the first weight, to reset all the elements in the hash
						histoMean.put(elem, images.get(0).tags.get(elem) * weights[0] / sumWeights);
						//histoVariance.put(elem,(float) (Math.pow(images.get(0).tags.get(elem)  - histoMean.get(elem), 2))* weights[0]/ sumWeights);
						//histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
						
						//add the last value
						/*histoMean.put(elem, histoMean.get(elem) +  images.get(n-1).tags.get(elem) * weights[0] / n);
						histoVariance.put(elem,(float) (histoVariance.get(elem) + Math.pow(images.get(0).tags.get(elem) * weights[0] - histoMean.get(elem), 2))/ images.size());
						histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));*/
						
						//actualizar
						}
					ImageTags it;
					for (int i = 1; i < n; i++) {
						it = images.get(i);
						iterator = it.getKeys().iterator();
						while (iterator.hasNext()) {
							elem = iterator.next();
							if (histoMean.get(elem) != null) { // the tag is present in the hash
								if (i < weights.length) {
									histoMean.put(elem, histoMean.get(elem) + (it.tags.get(elem) * weights[i] / sumWeights));
									//histoVariance.put(elem,(float) (histoVariance.get(elem) + Math.pow(images.get(i).tags.get(elem)  - histoMean.get(elem), 2)* weights[i]/ sumWeights));
									//histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								} else if (i > images.size() - weights.length) {
									histoMean.put(elem,histoMean.get(elem) + (it.tags.get(elem) * weights[(n - 1) - i] / sumWeights));
									//histoVariance.put(elem, (float) (histoVariance.get(elem) + Math.pow(images.get(i).tags.get(elem)  - histoMean.get(elem), 2)* weights[(n - 1) - i]/ sumWeights));
									//histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								} else {
									histoMean.put(elem, histoMean.get(elem) + (it.tags.get(elem) / sumWeights));
									//histoVariance.put(elem,	(float) (histoVariance.get(elem) + Math.pow(images.get(i).tags.get(elem)  - histoMean.get(elem), 2)/ sumWeights));
									//histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								}
							} else { // The actual tag don't exits in the hash,so we add
								// Deberia hacer una comprobacion similar a la
								// de arriba para que tome en cuenta los
								// pesos?-------------------------------------------------
								if (i < weights.length) {
									histoMean.put(elem, (it.tags.get(elem) * weights[i] / sumWeights));
									//histoVariance.put(elem,	(float) (Math.pow(images.get(i).tags.get(elem)  - histoMean.get(elem), 2)* weights[i]/ sumWeights));
									//histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								} else if (i > images.size() - weights.length) {
									histoMean.put(elem, (it.tags.get(elem) * weights[(n - 1) - i] / sumWeights));
									//histoVariance.put(elem, (float) (Math.pow(images.get(i).tags.get(elem)  - histoMean.get(elem),2) * weights[(n - 1) - i]/ sumWeights));
									//histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								} else {
									// original
									histoMean.put(elem, it.tags.get(elem)/ sumWeights);
									//histoVariance.put(elem,(float) (Math.pow(images.get(i).tags.get(elem) - histoMean.get(elem), 2))/ sumWeights);
									//histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								}
							}
						}
					}//end for calculo media
					
					
					// solo para varianza
					
					iterator = images.get(0).getKeys().iterator();
					while (iterator.hasNext()) {
						elem = iterator.next(); // The first element is added  and multiply by the first weight, to reset all the elements in the hash
						//histoMean.put(elem, images.get(0).tags.get(elem) * weights[0] / sumWeights);
						histoVariance.put(elem,(float) (Math.pow(images.get(0).tags.get(elem)  - histoMean.get(elem), 2))* weights[0]/ sumWeights);
						histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
						
						//add the last value
						/*histoMean.put(elem, histoMean.get(elem) +  images.get(n-1).tags.get(elem) * weights[0] / n);
						histoVariance.put(elem,(float) (histoVariance.get(elem) + Math.pow(images.get(0).tags.get(elem) * weights[0] - histoMean.get(elem), 2))/ images.size());
						histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));*/
						
						//actualizar
						}
					//ImageTags it;
					for (int i = 1; i < n; i++) {
						it = images.get(i);
						iterator = it.getKeys().iterator();
						while (iterator.hasNext()) {
							elem = iterator.next();
							if (histoVariance.get(elem) != null) { // the tag is present in the hash
								if (i < weights.length) {
									//histoMean.put(elem, histoMean.get(elem) + (it.tags.get(elem) * weights[i] / sumWeights));
									histoVariance.put(elem,(float) (histoVariance.get(elem) + Math.pow(images.get(i).tags.get(elem)  - histoMean.get(elem), 2)* weights[i]/ sumWeights));
									histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								} else if (i > images.size() - weights.length) {
									//histoMean.put(elem,histoMean.get(elem) + (it.tags.get(elem) * weights[(n - 1) - i] / sumWeights));
									histoVariance.put(elem, (float) (histoVariance.get(elem) + Math.pow(images.get(i).tags.get(elem)  - histoMean.get(elem), 2)* weights[(n - 1) - i]/ sumWeights));
									histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								} else {
								//	histoMean.put(elem, histoMean.get(elem) + (it.tags.get(elem) / sumWeights));
									histoVariance.put(elem,	(float) (histoVariance.get(elem) + Math.pow(images.get(i).tags.get(elem)  - histoMean.get(elem), 2)/ sumWeights));
									histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								}
							} else { // The actual tag don't exits in the hash,so we add
								// Deberia hacer una comprobacion similar a la
								// de arriba para que tome en cuenta los
								// pesos?-------------------------------------------------
								if (i < weights.length) {
									//histoMean.put(elem, (it.tags.get(elem) * weights[i] / sumWeights));
									histoVariance.put(elem,	(float) (Math.pow(images.get(i).tags.get(elem)  - histoMean.get(elem), 2)* weights[i]/ sumWeights));
									histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								} else if (i > images.size() - weights.length) {
								//	histoMean.put(elem, (it.tags.get(elem) * weights[(n - 1) - i] / sumWeights));
									histoVariance.put(elem, (float) (Math.pow(images.get(i).tags.get(elem)  - histoMean.get(elem),2) * weights[(n - 1) - i]/ sumWeights));
									histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								} else {
									// original
								//	histoMean.put(elem, it.tags.get(elem)/ sumWeights);
									histoVariance.put(elem,(float) (Math.pow(images.get(i).tags.get(elem) - histoMean.get(elem), 2))/ sumWeights);
									histoStanDev.put(elem, (float) Math.sqrt(histoVariance.get(elem)));
								}
							}
						}
					}//end for calculo media
					
					for (int i = 0; i < n; i++) {
						xmedian[i] = images.get(i).xcoord;
						ymedian[i] = images.get(i).ycoord;
					}
				}

				Arrays.sort(xmedian);
				Arrays.sort(ymedian);
				representative.xcoord = xmedian[xmedian.length / 2];
				representative.ycoord = ymedian[ymedian.length / 2];
				minDist = Double.MAX_VALUE;
				auxx = -1;
				auxy = -1;
				for (ImageTags it : images) {
					auxDist = Math.hypot(it.xcoord - representative.xcoord, it.ycoord - representative.ycoord);
					if (auxDist < minDist) {
						minDist = auxDist;
						auxx = it.xcoord;
						auxy = it.ycoord;
					}
				}
				representative.xcoord = auxx;
				representative.ycoord = auxy;
			}
		} // end if of begin processes
		/*Iterator<Entry<String, Float>> varIter = histoVariance.entrySet().iterator();
		Entry<String, Float> variance;*/
		
		nodeVariance = 0;
		
		
		
		for (Entry<String, Float> e : histoVariance.entrySet()) {
			nodeVariance += e.getValue();
		}
		
	

	}

	public double distance(ImageTags t1, ImageTags t2) {
		float dist = 0.0f;
		String elem;
		Set<String> hs1;
		Object[] hs2;
		Iterator<String> iterator;

		hs2 = t2.getKeys().toArray();
		hs1 = t1.getKeys();
		iterator = hs1.iterator();
		while (iterator.hasNext()) {
			elem = iterator.next();
			dist += Math.pow(t1.getValue(elem) - t2.getValue(elem), 2.0);
			for (int i = 0; i < hs2.length; i++) { // this is for the situation
													// that in the second tags
													// there is a tag not
													// present in the first
				if (hs2[i] != null && ((String) hs2[i]).equals(elem)) {
					hs2[i] = null;
					break;
				}
			}
		}
		for (int i = 0; i < hs2.length; i++) {
			if (hs2[i] != null)
				dist += Math.pow(t2.getValue((String) hs2[i]), 2.0);
		}

		return (float) Math.sqrt(dist);
	}

	public double distance(HashMap<String, Float> histo, ImageTags t2) {
		float dist = 0.0f;
		String elem;
		Set<String> hs1;
		Iterator<String> iterator;
			
		HashMap<String, Float> w  =new HashMap<String,Float>();
		float wSum=0;
		for (Entry<String, Float> entry : histoStanDev.entrySet()) {
			w.put(entry.getKey(),1-2* entry.getValue());
			wSum += w.get(entry.getKey());
		}
		
		hs1 = histo.keySet();
		iterator = hs1.iterator();
		while (iterator.hasNext()) {
			elem = iterator.next();
			if (t2.exists(elem)) {
				//if(histoStanDev.get(elem)== 0)			
			//	dist += Math.pow(histo.get(elem) - t2.getValue(elem), 2.0);
			//	else
					//dist += Math.pow(histo.get(elem) - t2.getValue(elem), 2.0)/histoStanDev.get(elem);
				    dist += Math.pow(histo.get(elem) - t2.getValue(elem), 2.0)*w.get(elem);
			}
		}
		return (float) Math.sqrt(dist/wSum);
	}
	
		
	public double x2(HashMap<String, Float> histo, ImageTags t2) {
		float dist = 0.0f;
		String elem;
		Set<String> hs1;
		Iterator<String> iterator;

		hs1 = histo.keySet();
		iterator = hs1.iterator();
		while (iterator.hasNext()) {
			elem = iterator.next();
			if (t2.exists(elem)) {
				dist += Math.pow(histo.get(elem) - t2.getValue(elem), 2.0)/(histo.get(elem) + t2.getValue(elem));
									
			}
		}
		return (float) dist;
	}
	
	
	
	public double kullback(HashMap<String, Float> histo, ImageTags t2) {
		float dist = 0.0f;
		String elem;
		Set<String> hs1;
		Iterator<String> iterator;

		hs1 = histo.keySet();
		iterator = hs1.iterator();
		while (iterator.hasNext()) {
			elem = iterator.next();
			if (t2.exists(elem)) {
				dist +=(histo.get(elem) - t2.getValue(elem))* Math.log(histo.get(elem)/ t2.getValue(elem));
									
			}
		}
		return (float) dist;
	}
	

	
	public double distance(ImageTags img) {
		if (!useHisto)
			return distance(img, representative);
		else {
			return distance(histoMean, img);
		}
	}

	public String getTextTags() {
		String text, elem, auxS;
		Iterator<String> iterator;
		HashMap<String, Float> auxMap = new HashMap<String, Float>();
		ArrayList<HistoOrdered> histo = new ArrayList<HistoOrdered>();
		//ArrayList<Histogram> histof = new ArrayList<Histogram>();

		// ESTO DEBERIA CALCULARSE CON EL HISTOGRAMA MEDIO
		// Se Calcula la media de los tags sin los pesos    //Revisar
		for (ImageTags img : images) {
			iterator = img.getKeys().iterator();
			while (iterator.hasNext()) {
				elem = iterator.next();
				if (auxMap.get(elem) != null) {
					auxMap.replace(elem, auxMap.get(elem) + img.getValue(elem) / images.size());
				} else
					auxMap.put(elem, img.getValue(elem) / images.size());
			}
		}
		//int ix =0;
		iterator = auxMap.keySet().iterator();
		while (iterator.hasNext()) {
			//System.out.println(ix++);
			auxS = iterator.next();
			histo.add(new HistoOrdered(auxS, auxMap.get(auxS)));  //auxMap -->histoMean
						//System.out.println(auxS+" --"+ auxMap.get(auxS));
			
		}
		//---------------------------------------------
		//Ordeno segun los mayores valores
		
		Set<Entry<String, Float>> entriesAux = auxMap.entrySet();
		Comparator<Entry<String, Float>> valueComparator = new Comparator<Entry<String, Float>>() {
			@Override
			public int compare(Entry<String, Float> e1, Entry<String, Float> e2) {
				Float v1 = e1.getValue();
				Float v2 = e2.getValue();
				return v2.compareTo(v1);
			}
		};

		// Sort method needs a List, so let's first convert Set to List in Java
		List<Entry<String, Float>> listOfEntriesAux = new ArrayList<Entry<String, Float>>(entriesAux);

		// sorting HashMap by values using comparator
		Collections.sort(listOfEntriesAux, valueComparator);

		LinkedHashMap<String, Float> sortedByValueAux = new LinkedHashMap<String, Float>(listOfEntriesAux.size());

		
		
		int h =0; //Solo los 100 mayores
		// copying entries from List to Map
		for (Entry<String, Float> entryAux : listOfEntriesAux) {
			if(++h== 100)
				break;
			sortedByValueAux.put(entryAux.getKey(), entryAux.getValue());
		}
				
//--------------------------------------------------------------
		/*Collections.sort(histo); // ----------------
		for (int i = 0; i < 100; i++) {
			if (histo.size() - 1 <= i)
				break;
			histof.add(new Histogram(histo.get(i).name, histo.get(i).value));
			
		}
		Collections.sort(histof);*/
		text = "<html>\n";
		
		//-------Ordenos los Tags-----------------------------
				
		Set<Entry<String, Float>> entriesAuxString = sortedByValueAux.entrySet();
		Comparator<Entry<String, Float>> valueComparatorString = new Comparator<Entry<String, Float>>() {
			@Override
			public int compare(Entry<String, Float> e1, Entry<String, Float> e2) {
				String v1 = e1.getKey();
				String v2 = e2.getKey();
				return v1.compareTo(v2);
			}
		};

		// Sort method needs a List, so let's first convert Set to List in Java
		List<Entry<String, Float>> listOfEntriesAuxString = new ArrayList<Entry<String, Float>>(entriesAuxString);

		// sorting HashMap by values using comparator
		Collections.sort(listOfEntriesAuxString, valueComparatorString);

		LinkedHashMap<String, Float> sortedByValueAuxString = new LinkedHashMap<String, Float>(listOfEntriesAuxString.size());

		// copying entries from List to Map
		for (Entry<String, Float> entryAuxString : listOfEntriesAuxString) {
			sortedByValueAuxString.put(entryAuxString.getKey(), entryAuxString.getValue());
		}
		
		//for (Histogram h : histof) {
					
		for(Entry<String, Float> e : sortedByValueAuxString.entrySet()){
			
				text += "<span title=\"" + String.valueOf(e.getValue()) + "\"><font size=\"";
			text += (int) (e.getValue() * 70);
			text += "\">" + e.getKey() + "</font> </span>";
		}
		text += "\n</html>";
		//System.out.println(text);
		return text;
	}
	
	public String countCategory(){
		HashMap<String, Integer> datAux = new HashMap<String,Integer>();
		int h = 0;
		for (int i = 0; i < images.size(); ++i) {
			ImageTags imagesAux = images.get(i);
			
			if(datAux.get(imagesAux.category)!=null){
				h = datAux.get(imagesAux.category);
				datAux.put(imagesAux.category, h+1);
			}else
				datAux.put(imagesAux.category, 1);
						
			}
		
		
		String text ="<br>Category &nbsp;&nbsp; Amount<br>\n";
		for(Entry<String, Integer> e : datAux.entrySet()){
			text+=e.getKey() +" &nbsp;&nbsp; "+ String.valueOf(e.getValue())+" &nbsp;&nbsp; "+((e.getValue()/(float)images.size())*100) +"%<br>\n";
		}
		return text;
	}
	
	public int getCategoryAmount(){
		HashMap<String, Integer> datAux = new HashMap<String,Integer>();
		int h = 0;
		for (int i = 0; i < images.size(); ++i) {
			ImageTags imagesAux = images.get(i);
			
			if(datAux.get(imagesAux.category)!=null){
				h = datAux.get(imagesAux.category);
				datAux.put(imagesAux.category, h+1);
			}else
				datAux.put(imagesAux.category, 1);
						
			}
		
		
		return datAux.size();
	}
		
	public String getTop10(){
		String text = "<html>\n";
		//order the histoMean
		ArrayList<String> mean10 = new ArrayList<String>();
		LinkedHashMap<String, Float> sortedByValue = orderHashMap(histoMean, true);
		HashMap<String,Float> presenceData = new HashMap<String,Float>();
		
		int h = 0;
		for(Entry<String, Float> e : sortedByValue.entrySet()){
			mean10.add(e.getKey());
			if(++h ==10) 
				break;
			
		}
		
		ImageTags imagesAux;
		LinkedHashMap<String, Float> orderImageTags;
		// int imgSize = images.size();
		for (int i = 0; i < images.size(); ++i) {
			imagesAux = images.get(i);
			HashMap<String, Float> datAux = new HashMap<String, Float>();
			datAux.putAll(imagesAux.tags);
			orderImageTags = orderHashMap(datAux, true);
			
			int j = 0;
			ArrayList<String> mean10Image = new ArrayList<String>();
			for(Entry<String, Float> e : orderImageTags.entrySet()){
				mean10Image.add(e.getKey());
				if(++j ==10) 
					break;
				
			}
			presenceData.put(imagesAux.imageName, getTagPresence(mean10, mean10Image));
					
		}
		int p = 10;
	for (int i = 0; i< mean10.size(); i++) {
		text += "<span><font size=\"";
		text += (int) (--p);
		text += "\">" + mean10.get(i) + "</font> </span><br>";
	}	
		text+="<br><br><br>";
		
		
		text += "<table border=\"1\">";
		text += "<tr> <th>#</th><th>ImageName</th><th>Top10 (%)</th></tr>";
				
		int g =1;
		int acum = 0;
			
		for(Entry<String, Float> e : presenceData.entrySet()){
			text += "<tr> <td>"+String.valueOf(g) +"</td><td>" 
		                        + e.getKey() + "</td><td>"; 
		                        if(e.getValue() > 6)
		                        	text += "<b>" +String.valueOf((e.getValue()*10)) + "</b></td></tr>";
		                        	else
		                        		text += String.valueOf((e.getValue()*10)) + "</td></tr>";
		                   
			++g;
			acum += e.getValue();
		}
		
		float top = acum/images.size() *10;
				text += "<tr> <td colspan = \"2\"> Node Top10</td><td><b>" + String.valueOf(top)+ "</b></td></tr>";
		text += "</table>";
		text += "\n</html>";
		return text;
				
	}
	
	public ArrayList<String> getTop10Nodes(){
		//order the histoMean
		ArrayList<String> mean10 = new ArrayList<String>();
		LinkedHashMap<String, Float> sortedByValue = orderHashMap(histoMean, true);
	
		
		int h = 0;
		for(Entry<String, Float> e : sortedByValue.entrySet()){
			mean10.add(e.getKey());
			if(++h ==10) 
				break;
			}
		return mean10;
		}
	
	private float getTagPresence(ArrayList<String> pattern, ArrayList<String> surface ){
		int found = 0;
		
		for (Iterator<String> iterator = pattern.iterator(); iterator.hasNext();) {
			String data = iterator.next();
			if (surface.contains(data)) ++found;
			}
		return (float)found;
	}
		
	private LinkedHashMap<String, Float> orderHashMap(HashMap<String,Float> hashData, boolean descendant){
		Set<Entry<String, Float>> entries = hashData.entrySet();
		Comparator<Entry<String, Float>> valueComparator = new Comparator<Entry<String, Float>>() {
			@Override
			public int compare(Entry<String, Float> e1, Entry<String, Float> e2) {
				Float v1 = e1.getValue();
				Float v2 = e2.getValue();
				if(descendant)
				return v2.compareTo(v1);
				else
					return v1.compareTo(v2);
							
			}
		};

		// Sort method needs a List, so let's first convert Set to List in Java
		List<Entry<String, Float>> listOfEntries = new ArrayList<Entry<String, Float>>(entries);

		// sorting HashMap by values using comparator
		Collections.sort(listOfEntries, valueComparator);

		LinkedHashMap<String, Float> sortedByValue = new LinkedHashMap<String, Float>(listOfEntries.size());

		// copying entries from List to Map
		for (Entry<String, Float> entry : listOfEntries) {
			sortedByValue.put(entry.getKey(), entry.getValue());
		}
		return sortedByValue;
				
	}
	
	public String getNodeInfo(ArrayList<Edge> edges, ArrayList<Node> nodes) {
		String text = "<html>\n";
		String[] split;

		text += "Total images=" + images.size() + "<br>";
		text += "Edges from this node<br>";
		for (Edge e : edges) {
			if (e.a == this)
				text += "Node " + nodes.indexOf(e.b) + "<br>";
			else if (e.b == this)
				text += "Node " + nodes.indexOf(e.a) + "<br>";
		}
		for (ImageTags img : images) {
			split = img.imageName.split("/");
			text += split[split.length - 1] + " " + img.xcoord + " " + img.ycoord + ""+img.category+ "<br>";
		}

		// imprimir desviacion standard y varianza

		Set<Entry<String, Float>> entries = histoStanDev.entrySet();
		Comparator<Entry<String, Float>> valueComparator = new Comparator<Entry<String, Float>>() {
			@Override
			public int compare(Entry<String, Float> e1, Entry<String, Float> e2) {
				Float v1 = e1.getValue();
				Float v2 = e2.getValue();
				return v2.compareTo(v1);
			}
		};

		// Sort method needs a List, so let's first convert Set to List in Java
		List<Entry<String, Float>> listOfEntries = new ArrayList<Entry<String, Float>>(entries);

		// sorting HashMap by values using comparator
		Collections.sort(listOfEntries, valueComparator);

		LinkedHashMap<String, Float> sortedByValue = new LinkedHashMap<String, Float>(listOfEntries.size());

		// copying entries from List to Map
		for (Entry<String, Float> entry : listOfEntries) {
			sortedByValue.put(entry.getKey(), entry.getValue());
		}

		text += "<table border=\"1\">";
		text += "<tr> <th>#</th><th>Tag</th><th>Mean</th><th>Variance</th><th>Standard Deviation</th> <th>CV</th>  </tr>";

		// sort by the stand deviation
		int g = 1;
		float sumCV = 0;
		for (Entry<String, Float> e : sortedByValue.entrySet()) { // sort by the StandDeviation
			
			float CV  = e.getValue()/ histoMean.get(e.getKey());
			sumCV += CV;
			
			text += "<tr> <td>"+String.valueOf(g) +"</td><td>" + e.getKey() + "</td><td>" + histoMean.get(e.getKey()) + "</td><td>"
					+ histoVariance.get(e.getKey()).toString() + "</td><td>" + e.getValue().toString() + "</td><td> "+ CV  +"   </td> </tr>";
			++g;
		}
		
		

		text += "<tr> <td colspan = \"3\"> Node Variance</td><td>" + String.valueOf(nodeVariance)+ "</td><td>Node CV</td><td>" + sumCV+  " </td></tr>";
		text += "</table>";
		text += "\n</html>";
	
		return text;

	}
	/*
	public CategoryDataset getDataset(){
		String elem;
		//String text,  auxS;
		Iterator<String> iterator;
		HashMap<String, Float> auxMap = new HashMap<String, Float>();
		//ArrayList<HistoOrdered> histo = new ArrayList<HistoOrdered>();
		
		
		   // create the dataset...
	       final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	       // column keys...
	       final String category = "Tags";
		
		for (ImageTags img : images) {
			iterator = img.getKeys().iterator();
			while (iterator.hasNext()) {
				elem = iterator.next();
				if (auxMap.get(elem) != null) {
					auxMap.replace(elem, auxMap.get(elem) + img.getValue(elem) / images.size());
				} else
					auxMap.put(elem, img.getValue(elem) / images.size());
			}
		}
		
		
		 Set<Entry<String, Float>> entries = auxMap.entrySet();
	        Comparator<Entry<String, Float>> valueComparator = new Comparator<Entry<String, Float>>() {
	            @Override
	            public int compare(Entry<String, Float> e1, Entry<String, Float> e2) {
	                Float v1 = e1.getValue();
	                Float v2 = e2.getValue();
	                return v2.compareTo(v1);
	            }
	        };

	        // Sort method needs a List, so let's first convert Set to List in Java
	        List<Entry<String, Float>> listOfEntries = new ArrayList<Entry<String, Float>>(entries);

	        // sorting HashMap by values using comparator
	        Collections.sort(listOfEntries, valueComparator);

	        LinkedHashMap<String, Float> sortedByValue = new LinkedHashMap<String, Float>(listOfEntries.size());

	        // copying entries from List to Map
	        for (Entry<String, Float> entry : listOfEntries) {
	            sortedByValue.put(entry.getKey(), entry.getValue());
	        }
	       
	       
	        int i =0;
	        for (Entry<String, Float> e :sortedByValue.entrySet()) {
	             dataset.addValue(e.getValue()*1,e.getKey() , category);
	             i++;
	             if(i >50)
	                 break;
	        }
		
		/*Collections.sort(histo); // ----------------
		for (int i = 0; i < histo.size(); i++) {
			
			  dataset.addValue(histo.get(i).value*1000,histo.get(i).name , category);
			//}
		
       return dataset;
	}*/
				
	public String getNodesContent() {
		String nodeInfo = "";
		//Verificar lo que imprime
		ImageTags imagesAux2;
		imagesAux2 = images.get(0);
		Iterator<String> keyAux2 = imagesAux2.getKeys().iterator();
		while (keyAux2.hasNext()) {
			String key = keyAux2.next();
			nodeInfo += key + "\t";
			for (int i = 0; i < images.size(); ++i) {
				imagesAux2 = images.get(i);
				if (imagesAux2.tags.get(key) != null) { // the actual tag is present in the hashMap
					nodeInfo += "\t" + imagesAux2.getValue(key);
				} else // the tag isn't in the hashMap
					nodeInfo += "\t?";
			}
			nodeInfo += "\n";
		}

		return nodeInfo;
	}
		
	//A
	public float getQCat_A(){
		
		float A  =0;
		int categoriesCant= getCategoryAmount();
		if (categoriesCant == 1){
			A = 0;
		}else{
		HashMap<String, Float> datAux = new HashMap<String,Float>();
		float h = 0;
		for (int i = 0; i < images.size(); ++i) {
			ImageTags imagesAux = images.get(i);
			
			if(datAux.get(imagesAux.category)!=null){
				h = datAux.get(imagesAux.category);
				datAux.put(imagesAux.category, h+1);
			}else
				datAux.put(imagesAux.category, (float) 1.0);
						
			}
		
		HashMap<String, Float> hm = orderHashMap(datAux, true);
		float mayor=0, acum=0;
		int y = 0;
		for(Entry<String, Float> e : hm.entrySet()){
			if(y ==0)
				mayor = e.getValue();
			    acum +=e.getValue()/mayor;
				++y;
		}
		acum--;
		A = acum/(categoriesCant-1);
		
		//A = (acum)/(float)images.size();
		
		}
		
		//A = ((float)categoriesCant)/images.size();
		return A;
	}
	
	//B
	public float getQConnAvg_B(ArrayList<Edge> edges){
		float B = 0;
		int edgesCant=0;
		for (Edge e : edges) {
			if (e.a == this){
				++edgesCant;
			}
			else if (e.b == this){
				++edgesCant;
			}
		}
		B= edgesCant;
		return B;
		
	}
	
	//D
	public float getQImgDev_D(){
		float D = 0;
		
		
		ImageTags imagesAux2;
		imagesAux2 = images.get(0);
		
		
		float xAcum=0, yAcum=0, xMean, yMean, xDev, yDev;
		for (int i = 0; i < images.size(); ++i) {
			imagesAux2 = images.get(i);
			xAcum+=imagesAux2.xcoord;
			yAcum+=imagesAux2.ycoord;
			
		}
			
		xMean = xAcum/images.size();
		yMean = yAcum/images.size();
		
		xAcum =0;
		yAcum = 0;
		
		for (int i = 0; i < images.size(); ++i) {
			xAcum +=Math.pow(imagesAux2.xcoord-xMean, 2)/images.size();
			yAcum +=Math.pow(imagesAux2.ycoord-yMean, 2)/images.size();
		}
		
		xDev= (float) Math.sqrt(xAcum);
		yDev= (float) Math.sqrt(yAcum);
		D =  (float) Math.sqrt(Math.pow(xDev, 2)+ Math.pow(yDev, 2));
		
		
		/*//Variancia de la media de la distancia de cada imagen al nodo
		float acumDist2=0;
		float distMean=0;
		for (int i = 0; i < images.size(); ++i) {
			imagesAux2 = images.get(i);
			acumDist2 += Math.sqrt(Math.pow((this.representative.xcoord-imagesAux2.xcoord),2) +Math.pow((this.representative.ycoord-imagesAux2.ycoord),2));
		}
		distMean = acumDist2 /images.size();
		
		acumDist2 =0;
		for (int i = 0; i < images.size(); ++i) {
			imagesAux2 = images.get(i);
			acumDist2 += Math.pow(Math.sqrt(Math.pow((this.representative.xcoord-imagesAux2.xcoord),2) +Math.pow((this.representative.ycoord-imagesAux2.ycoord),2))-distMean,2)/images.size();
		}
		
		D= (float) Math.sqrt(acumDist2);	*/	
		
		return D;
	}
			
	public String[] getNodeMetric(ArrayList<Edge> edges, ArrayList<Node> nodes, int nClass,float height, float width){
		int categoriesCant= getCategoryAmount();
		float A  =0;
		float B = 0;
		float C = 0;
		float D = 0;
		float E = 0;
		int edgesCant=0;
		//A
		A = ((float)categoriesCant)/images.size();
		
		//B y C   C->Afuera
		float acumDist=0;
		
		for (Edge e : edges) {
			if (e.a == this){
				++edgesCant;
				acumDist += Math.sqrt(Math.pow((this.representative.xcoord-e.b.representative.xcoord),2) +Math.pow((this.representative.ycoord-e.b.representative.ycoord),2));
				//nodes.get(nodes.indexOf(e.a)).representative.xcoord;
			}
			else if (e.b == this){
				++edgesCant;
				acumDist += Math.sqrt(Math.pow((this.representative.xcoord-e.a.representative.xcoord),2) +Math.pow((this.representative.ycoord-e.a.representative.ycoord),2));
			}
		}
		B = edgesCant;
		C = acumDist/B;
		
		
		
		//D
		ImageTags imagesAux2;
		imagesAux2 = images.get(0);
		
		float acumDist2=0;
		float distMean=0;
		for (int i = 0; i < images.size(); ++i) {
			imagesAux2 = images.get(i);
			acumDist2 += Math.sqrt(Math.pow((this.representative.xcoord-imagesAux2.xcoord),2) +Math.pow((this.representative.ycoord-imagesAux2.ycoord),2));
		}
		distMean = acumDist2 /images.size();
		
		acumDist2 =0;
		for (int i = 0; i < images.size(); ++i) {
			imagesAux2 = images.get(i);
			acumDist2 += Math.pow(Math.sqrt(Math.pow((this.representative.xcoord-imagesAux2.xcoord),2) +Math.pow((this.representative.ycoord-imagesAux2.ycoord),2))-distMean,2)/images.size();
		}
		
		D= (float) Math.sqrt(acumDist2);		
		
		//Afuera
		//E		
		//float acumDist3=0;
		float xAcum=0, yAcum=0, xMean, yMean, xDev, yDev;
		for (Node node : nodes) {
			//acumDist3 += Math.sqrt(Math.pow((this.representative.xcoord-node.representative.xcoord),2) +Math.pow((this.representative.ycoord-node.representative.ycoord),2));
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
		
		float dmax = (float) Math.sqrt(Math.pow(width, 2)+ Math.pow(height, 2));
		
		
		//System.out.println("Dmax= "+dmax);
		//System.out.println("Width= "+width);
		//System.out.println("Height= "+height);
		
		float wA=1, wB=1, wC=1, wD=1, wE =1;
		
		wA=20;
		wB=20;
		wC=20;
		wD=20;
		wE=20;
		
		//float metrica = wA*(nClass-A) +wB*(nodes.size()-1-B)+wC*(dmax-C)+wD*(dmax/2 -D) + wE*(E/dmax/2);
		float metrica = wA*(1-A)/(float)1 +wB*(float)(nodes.size()-1-B)/(nodes.size()-2)+wC*(dmax-C)/dmax+wD*(dmax/2 -D)/(dmax/2) + wE*(E)/(dmax/2);
		
		String texto = "<tr><td>X</td><td>"+A+"</td><td>"+B+"</td><td>"+C+"</td><td>"+D+"</td><td><"+xDev+"----"+yDev+"</td><td>"+metrica+"</td></tr>";
		
		String valores[]= new String[2];
		valores[0]=texto;
		valores[1]= String.valueOf(metrica);
		
		return valores;
		
		
	}
	
	public String printNodeMet(int n, ArrayList<Edge> edges){
		String text="";
		
		float a,b,d;
		a = getQCat_A();
		b = getQConnAvg_B(edges);
		d= getQImgDev_D();
		text = "<td>"+n+"</td><td>"+a+"</td><td>"+b+"</td><td>"+d+"</td>";
		
		return text;
	}

	class HistoOrdered implements Comparable<HistoOrdered> {
		String name;
		float value;

		public HistoOrdered(String iName, float ivalue) {
			value = ivalue;
			name = iName;
		}

		public int compareTo(HistoOrdered h) {
			return (int) (100000 * (h.value - value));
		}
	}

	class Histogram implements Comparable<Histogram> {
		String name;
		float value;

		public Histogram(String iName, float ivalue) {
			value = ivalue;
			name = iName;
		}

		public int compareTo(Histogram h) {
			return name.compareToIgnoreCase(h.name);
		}
	}
}