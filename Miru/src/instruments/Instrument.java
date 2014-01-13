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

	public String strName;
	public int intID;
	public LatLng latLng;

	// public Image imgInstrumentIcon

	public String GetName() {
		return strName;
	}

	public void SetName(String strName) {
		this.strName = strName;
	}

	public int GetID() {
		return intID;
	}

	public void SetID(int intID) {
		this.intID = intID;
	}

	public LatLng GetLatLng() {
		return this.latLng;
	}

}
