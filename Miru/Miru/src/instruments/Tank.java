package instruments;

import com.example.miru.R;
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
	public Tank(String Name, int ID, LatLng LatLong, String Description) {
		this.strName = Name;
		this.intID = ID;
		this.latLng = LatLong;
		this.intIconID = R.drawable.marker_pin_tank;
		this.strDescription = Description;
	}
	public String getJSON()
	{
		return "\"Tank\": " + super.getJSON();
	}
}