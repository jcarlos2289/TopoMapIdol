/*************************************************************
 * Author: Miguel Cazorla
 * Date: 14-07-2003
 * Class Kmeans: apply the K means algorithm to a set of
 * points in a file. You can provide these points by a method.
 *************************************************************/
package buildMap;

import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Kmeans {
	// k indicates number of means
	int k;
	// dim is the dimension of the points
	int dim;
	// means to return
	Point[] means;
	// Number of points in each mean
	int[] nPointsMean;
	ArrayList<Point> obtained;
	// near tells us which is the nearest mean to this Point
	ArrayList<Integer> near;
	// this tell us at what distance is the Point from its mean
	ArrayList<Double> dist;

	// Constructor
	public Kmeans(int ki, int dimi, ArrayList<ImageTags> ImageList) { // recibiria
																		// arraylist
																		// de
																		// imagetags
		if (ki > 0 && dimi > 0) { // saco el keyset con el orden
			k = ki;
			dim = dimi;
			means = new Point[k];
			nPointsMean = new int[k];
			Set<String> hs1;
			hs1 = ImageList.get(0).tags.keySet();

			obtained = new ArrayList<Point>();

			near = new ArrayList<Integer>();
			dist = new ArrayList<Double>();

			double dataTags[] = new double[hs1.size()];
			for (Iterator<ImageTags> iterator = ImageList.iterator(); iterator
					.hasNext();) {
				ImageTags auxImage = (ImageTags) iterator.next();
				int h = 0;
				for (Iterator<String> itKey = hs1.iterator(); itKey
						.hasNext();) {
					String key = (String) itKey.next();
					dataTags[h] = auxImage.getValue(key);
					++h;
				}
				obtained.add(new Point(dim, dataTags, auxImage.xcoord,
						auxImage.ycoord));
				near.add(0);
				dist.add(0.0);

			}

			for (int i = 0; i < k; i++)
				means[i] = new Point(dim); // enviar el entryset + los
											// datos[hashmap]
		} // almacenar todo el arraylist<ImageTag> como arrarylist>point>
			// obtanied

	}

	public void setK(int kaux) {
		k = kaux;
		means = new Point[k];
		nPointsMean = new int[k];
		for (int i = 0; i < k; i++)
			means[i] = new Point(dim);
	}

	// Random initialize
	private void inicializeMeans() {
		for (int i = 0; i < k; i++) {
			means[i].copy(
					obtained.get((int) (Math.random() * obtained.size())));
			nPointsMean[i] = 0;
		}
	}

	private boolean change(Point[] means, Point[] meansAux) {
		double desv = 0.0;
		for (int p = 0; p < k; p++)
			for (int i = 0; i < dim; i++)
				desv += (means[p].get(i) - meansAux[p].get(i))
						* (means[p].get(i) - meansAux[p].get(i));
		return (Math.sqrt(desv) > 0.00);
	}

	private void nearMean() {
		double d, dAux;
		int ind;
		// For each Point
		for (int p = 0; p < obtained.size(); p++) {
			// We have to calculate the near mean to this Point
			d = obtained.get(p).distance(means[0]);
			ind = 0;
			for (int m = 1; m < k; m++) {
				dAux = obtained.get(p).distance(means[m]);
				if (dAux < d) {
					d = dAux;
					ind = m;
				}
			}

			// if(!near.isEmpty()){
			near.set(p, ind);
			dist.set(p, d);
			nPointsMean[ind]++;
			// }
			// else{
			// near.add(ind);
			// dist.add(d);
			// nPointsMean[ind]++;
			// }
		}
	}

	// In this method we have to ensure that every cluster has at least one
	// Point
	private void ensureNotEmpty() {
		int acum[] = new int[k];
		double d;
		int ind;

		for (int m = 0; m < k; m++)
			acum[m] = 0;
		// First we count the number of points in each cluster
		for (int p = 0; p < obtained.size(); p++)
			acum[near.get(p)]++;
		// Now, for each cluster, we have to check if it is empty
		for (int m = 0; m < k; m++)
			if (acum[m] == 0) {
				d = dist.get(0);
				ind = 0;
				// The cluster is empty. We put a Point which has the big
				// distance to its mean
				for (int p = 1; p < obtained.size(); p++)
					if (dist.get(p) > d) {
						d = dist.get(p);
						ind = p;
					}
				near.set(ind, m);
				// We put its distance to 0, in order to not be selected again
				dist.set(ind, 0.0);
				nPointsMean[m] = 1;
			}
	}

	private void replaceMeans() {
		for (int m = 0; m < k; m++) {
			means[m].init();
			nPointsMean[m] = 0;
		}
		for (int p = 0; p < obtained.size(); p++) {
			means[near.get(p)].add(obtained.get(p));
			nPointsMean[near.get(p)]++;
		}
		for (int m = 0; m < k; m++)
			means[m].div(nPointsMean[m]);
	}

	public void findMeans() {
		Point meansAux[] = new Point[k];

		for (int i = 0; i < k; i++)
			meansAux[i] = new Point(dim);
		inicializeMeans();
		do {
			for (int m = 0; m < k; m++)
				meansAux[m].copy(means[m]);

			nearMean();
			ensureNotEmpty();
			replaceMeans();
		} while (change(means, meansAux));
	}

	public float findMeansCoef() {
		float coef = 0;
		Point meansAux[] = new Point[k];
		float meansAcum[] = new float[k];

		for (int i = 0; i < k; i++)
			meansAux[i] = new Point(dim);
		inicializeMeans();
		do {
			for (int m = 0; m < k; m++)
				meansAux[m].copy(means[m]);

			nearMean();
			ensureNotEmpty();
			replaceMeans();
		} while (change(means, meansAux));

		
		for (int i = 0; i < meansAcum.length; i++) {
			meansAcum[i] = 0;
			for (int j = 0; j < near.size(); ++j) {
				int data = near.get(j);
				if (data == i)
					meansAcum[i] += Math.pow(dist.get(j), 2) / nPointsMean[i];
			}
			//meansAcum[i] ;
			coef += meansAcum[i]/ k;
		}
		
		return coef;
	}

	public static void nothing(String args[]) {
		
		//  Kmeans km;
		 
		  //ArrayList<ImageTags> h = new ArrayList<ImageTags>();
		//   km = new Kmeans(1,6, h);
		  // int k=1; 
		   //do { km.setK(k);
		   //km.findMeans(); }
		    //while(k<300);
		 
	}
}
