package assets;

import com.example.miru.R;

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

	public Flare(String Name, int ID, double Lat, double Lng,
			String Description, String Ref) {
		this.name = Name;
		this.id = ID;
		this.lat = Lat;
		this.lng = Lng;
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
