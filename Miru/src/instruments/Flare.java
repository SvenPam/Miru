package instruments;

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

	public Flare(String Name, int ID, double Lat, double Long) {
		this.strName = Name;
		this.intID = ID;
		this.dblLat = Lat;
		this.dblLong = Long;
	}

}
