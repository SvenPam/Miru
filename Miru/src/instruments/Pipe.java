package instruments;

import com.example.miru.R;
import com.google.android.gms.maps.model.LatLng;

/**
* Represents a simple pipe.
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
public class Pipe extends Instrument {

	public Pipe(String Name, int ID, LatLng LatLong) {
		this.strName = Name;
		this.intID = ID;
		this.latLng = LatLong;
		this.intIconID = R.drawable.marker_pin_pipe;
	}
}
