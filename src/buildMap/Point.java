package buildMap;

// This Class manages points in any dimension
class Point {
    int dim;
    double []data;
    double xcoord, ycoord;

    public Point (int dimi) {
    	if (dimi>0) {
    		dim=dimi;
    		data=new double[dim];  //
    	}
    }

    public Point(int dimi, double []datai, double x, double y) {  //recibe el entryset y el hashmap, tiene ke cargar los valores en el arreglo data
        if (dimi>0) {
            dim=dimi;
            xcoord = x;
            ycoord = y;
            data=new double[dim];
            for (int i=0; i<dim; i++)
                data[i]=datai[i];
        }
    }

    public void copy(Point p) {
    	for (int d=0; d<dim; d++)
    		data[d] = p.data[d];
    	xcoord = p.xcoord;
    	ycoord = p.ycoord;
    }

    public void add (Point p) {
    	for (int d=0; d<dim; d++)
    		data[d] += p.data[d];
    	xcoord += p.xcoord;
    	ycoord += p.ycoord;
    }

    public void init () {
    	for (int d=0; d<dim; d++)
    		data[d] =0.00;
    	xcoord = 0;
    	ycoord = 0;
    }

    public void div (int i) {
    	for (int d=0; d<dim; d++)
    		data[d] /= (double)i;
    	xcoord /= (double)i;;
    	ycoord /= (double)i;;
    }

    public double distance (Point p) {
    	double dist=0;
    	for (int d=0; d<dim; d++)
    		dist += (data[d]-p.data[d])*(data[d]-p.data[d]);
    	return Math.sqrt(dist);
    }

    public double distance (double []p) {
    	double dist=0;
    	for (int d=0; d<dim; d++)
    		dist += (data[d]-p[d])*(data[d]-p[d]);
    	return Math.sqrt(dist);
    }

    public double get (int ind) {
        return data[ind];
    }

    public double [] getData () {
        return data;
    }

    public String toString () {
    	String str="";
    	for (int i=0; i<6; i++)
    		str += data[i]+" ";
    	return str;
    }
    public double getX(){
    	return xcoord;
    	
    }
    
    public double getY(){
    	return ycoord;
    	
    }

public static String printReal(double num) {
        int cont=0;
        int maxDec=6;
        int digito;
        String s="";

        if(Double.isNaN(num))
        return "NaN";
        if(Double.isInfinite(num))
        return "Infinity";

        if(num<0) {
        s+="-";
        num=Math.abs(num);
        }
        if((int)Math.floor(num) == 0)
        s+="0";
        while( (int)Math.floor(num) != 0) {
        num/=10;
        cont++;
        }
        for(;cont>0;cont--) {
        num*=10;
        digito=(int)Math.floor(num);
        num-=digito;
        s+=digito;
        }
        s+=".";
        for(cont=0;cont<maxDec;cont++) {
        num*=10;
        digito=(int)Math.floor(num);
        num-=digito;
        s+=digito;
    }
    return s;
 }


    public void set (int ind, double d) {
        data[ind]=d;
    }
    
    
}