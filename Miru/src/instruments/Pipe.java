package instruments;

import java.util.List;

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
public class Pipe extends Instrument implements properties.Flow,
		properties.Route {

	private List<LatLng> ltLngRoute;
	private String strRouteID;

	public Pipe(String Name, int ID, LatLng LatLong, String Description,
			List<LatLng> Route) {
		this.strName = Name;
		this.intID = ID;
		this.latLng = LatLong;
		this.intIconID = R.drawable.marker_pin_pipe;
		this.strDescription = Description;
		this.ltLngRoute = Route;
		this.strRouteID = "-1";
	}

	@Override
	public List<LatLng> getRoute() {
		return ltLngRoute;
	}

	@Override
	public void setRoute(List<LatLng> Route) {
		this.ltLngRoute = Route;
	}

	@Override
	public void addPoint(LatLng latlng) {
		ltLngRoute.add(latlng);

	}

	@Override
	public void removePoint(LatLng latlng) {
		ltLngRoute.remove(latlng);
	}

	public void setRouteID(String ID) {
		this.strRouteID = ID;
	}

	public String getRouteID() {
		return this.strRouteID;
	}

}
