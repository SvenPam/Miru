package instruments;

import com.example.miru.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Represents a flare.
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
public class Flare extends Instrument {

	public Flare(String Name, int ID, LatLng LatLong) {
		this.strName = Name;
		this.intID = ID;
		this.latLng = LatLong;
		this.intIconID = R.drawable.marker_pin_flare;
	}

}
