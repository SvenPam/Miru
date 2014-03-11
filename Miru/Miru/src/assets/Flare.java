package assets;

import com.example.miru.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Represents a flare.
 * 
 * @author Stephen Pammenter E: spammenter@live.com W: www.ste-pam.com
 * 
 *         Teesside University Uni ID: K0025970
 * 
 *         Created: 06-DEC-2013
 * */
public class Flare extends Asset {

	public Flare(String Name, int ID, LatLng LatLong, String Description,
			String Ref) {
		this.name = Name;
		this.id = ID;
		this.latLng = LatLong;
		this.iconID = R.drawable.marker_pin_flare;
		this.strDescription = Description;
		if (Ref != null) {
			this.strRef = Ref;
		} else {
			this.strRef = "";
		}

	}

	public String getJSON() {
		return "\"Flare\": " + super.getJSON();
	}

}
