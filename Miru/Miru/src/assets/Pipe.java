package assets;

import java.util.List;

import com.example.miru.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Represents a simple pipe.
 * 
 * @author Stephen Pammenter E: spammenter@live.com W: www.ste-pam.com
 * 
 *         Teesside University Uni ID: K0025970
 * 
 *         Created: 06-DEC-2013
 */
public class Pipe extends Asset implements properties.Route {

	private List<LatLng> ltLngRoute;
	private String strRouteID;

	public Pipe(String Name, int ID, double Lat, double Lng,
			String Description, String Ref) {
		this.name = Name;
		this.id = ID;
		this.lat = Lat;
		this.lng = Lng;
		this.iconID = R.drawable.marker_pin_pipe;
		this.strDescription = Description;
		if (Ref != null) {
			this.strRef = Ref;
		} else {
			this.strRef = "";
		}
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

	public String getJSON() {
		return "\"Pipe\": " + super.getJSON();
	}

}
