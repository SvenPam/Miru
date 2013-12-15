package instruments;

public/**
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
*/class Pipe extends Instrument {

	public Pipe(String Name, int ID, double Lat, double Long) {
		this.strName = Name;
		this.intID = ID;
		this.dblLat = Lat;
		this.dblLong = Long;
	}
}
