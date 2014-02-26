package instruments;

import com.google.android.gms.maps.model.LatLng;

/**
 * A simple instrument.
 * 
 * @author Stephen Pammenter 
 * E: spammenter@live.com 
 * W: www.ste-pam.com 
 * 
 * Teesside University 
 * Uni ID: K0025970 
 * 
 * Created: 06-DEC-2013
 * */
public abstract class Instrument {

	/**For use in JSON*/
	public String strThisClass;
	public String strName;
	public Integer intID;
	public LatLng latLng;
	public int intIconID;
	public String strDescription;

	
	
	public String getThisClass() {
		return strThisClass;
	}

	public void setThisClass(String strThisClass) {
		this.strThisClass = strThisClass;
	}

	public String getName() {
		return strName;
	}

	public void SetName(String strName) {
		this.strName = strName;
	}

	public Integer GetID() {
		return intID;
	}

	public void SetID(int intID) {
		this.intID = intID;
	}

	public LatLng GetLatLng() {
		return this.latLng;
	}

	public int GetIconID() {
		return this.intIconID;
	}

	public String getDescription() {
		return this.strDescription;
	}
	
	public String getJSON()
	{
	String strJSON;
		
		strJSON = "{";
		strJSON += "\"ID\": \"" + this.intID.toString() +  "\",";
		strJSON += "\"Name\": \"" + this.strName +  "\",";
		strJSON += "\"Description\": \"" + this.strDescription +  "\",";
		strJSON += "\"Lat\": \"" + this.latLng.latitude +  "\",";
		strJSON += "\"Long\": \"" + this.latLng.longitude +  "\"";
		strJSON += "}";
		return strJSON;
		
	}
	
	public String toString() {
		return this.strName;
	}

}
