package instruments;

import com.google.android.gms.maps.model.LatLng;

/**
 * Represents one Tank.
 * 
 * @author Stephen Pammenter 
 * E: spammenter@live.com 
 * W: www.ste-pam.com 
 * 
 * Teesside University 
 * Uni ID: K0025970 
 * 
 * Created: 06-DEC-2013
 */
public class Tank extends Instrument {

	/**
	 * 
	 */
	public Tank(String Name, int ID, LatLng LatLong) {
		this.strName = Name;
		this.intID = ID;
		this.latLng = LatLong;
	}
}
