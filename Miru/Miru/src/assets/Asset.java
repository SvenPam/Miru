package assets;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * A simple asset.
 * 
 * @author Stephen Pammenter
 * 
 *         E: spammenter@live.com
 * 
 *         W: www.ste-pam.com
 * 
 *         Teesside University
 * 
 *         Uni ID: K0025970
 * 
 *         Created: 06-DEC-2013
 * */
public abstract class Asset {

	/** For use in JSON */
	public String name;
	public Integer id;
	public LatLng latLng;
	public int iconID;
	public String strDescription;
	/**
	 * Most likely a temp value to store SAP ref so we can store a bank of
	 * geodata then automate the detail creation process.
	 */
	public String strRef;

	public String getName() {
		return name;
	}

	public void setName(String strName) {
		this.name = strName;
	}

	public Integer getID() {
		return id;
	}

	public void setID(int intID) {
		this.id = intID;
	}

	public LatLng getLatLng() {
		return this.latLng;
	}

	/**
	 * Converts Google LatLong into Android Location.
	 * 
	 * @return Location of current asset.
	 */
	public Location getLocation() {
		Location loc = new Location(this.latLng.toString());
		loc.setLatitude(this.latLng.latitude);
		loc.setLongitude(this.latLng.longitude);
		return loc;
	}

	public int getIconID() {
		return this.iconID;
	}

	public String getDescription() {
		return this.strDescription;
	}

	public String getJSON() {
		String strJSON;

		strJSON = "{";
		strJSON += "\"ID\": \"" + this.id.toString() + "\",";
		strJSON += "\"Name\": \"" + this.name + "\",";
		strJSON += "\"Description\": \"" + this.strDescription + "\",";
		strJSON += "\"Lat\": \"" + this.latLng.latitude + "\",";
		strJSON += "\"Long\": \"" + this.latLng.longitude + "\"";
		strJSON += "}";
		return strJSON;

	}

	public String getStrRef() {
		return strRef;
	}

	public void setStrRef(String strRef) {
		this.strRef = strRef;
	}

	public String toString() {
		return this.name;
	}

}
