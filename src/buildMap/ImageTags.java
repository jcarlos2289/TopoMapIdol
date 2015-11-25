package buildMap;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ImageTags {
	String imageName;
	ConcurrentHashMap<String,Float> tags;
	double xcoord, ycoord;
	float threshold;
	String category;
	
	public ImageTags (String in) {
		imageName=in;
		tags=new ConcurrentHashMap<String,Float>();
	}
	
	public void setThreshold (float th) {
		threshold=th;
	}
	
	public void setCoords (double x, double y) {
		xcoord=x;
		ycoord=y;
	}
	
	public void setCategory(String cat){
		category=cat;
	}
	
	public Set<String> getKeys () {
		return tags.keySet();
	}
	
	public boolean exists (String s) {
		return (tags.get(s)!=null);
	}
	
	public float getValue (String s) {
		if (tags.get(s)==null) 
			return 0.0f;
		else
			return tags.get(s);
	}
	
	public void addTag (String line) {
		float value;
		String[] split=line.split("-");
		value=Float.valueOf(split[1]);
		if (value>threshold)
			tags.put(split[0], value);
		
	}

}
