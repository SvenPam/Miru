package properties;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public interface Route {

	public List<LatLng> getRoute();

	public void setRoute(List<LatLng> Route);

	public void addPoint(LatLng latlng);

	public void removePoint(LatLng latlng);

}
