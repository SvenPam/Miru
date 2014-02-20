package main;

import instruments.Instrument;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import properties.Route;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.miru.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * The main activity for the Miru which intiliazes the app. 
 * 
 * Final Year Project
 * 
 * @author Stephen Pammenter 
 * E: spammenter@live.com 
 * W: www.ste-pam.com 
 * 
 * Teesside University 
 * Uni ID: K0025970 
 * 
 * Created: 10-DEC-2013
 * */
public class MainActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
		OnMyLocationButtonClickListener {
	/**Stores the map, map settings and markers*/
	private GoogleMap mMap;
	/**Used for location.*/
	private LocationClient mLocationClient;
	/**Defines the map overlay.*/
	private UiSettings mUISettings;
	/**HashMap of key value pairs, key: marker and value: an instrument. Used between fragments.*/
	private static Map<Marker, Instrument> mapMarkers;
	/**Used to prevent the map re-zooming to user location after initial startup.*/
	private boolean blnIsReady;
	/**Stored the ID of the last selected marker.*/
	public static Integer intSelectedMarker;

	/**All filler, no thriller. DELETE after data implementation*/
	private SampleData sd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sd = new SampleData();
	}

	/**
	 * Populates the action bar.
	 * */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.filter, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
		addMarkersToMap();
		onMyLocationButtonClick(); // Automatically zoom to users location.
		// Assign a listener to each marker.
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			public void onInfoWindowClick(Marker marker) {
				intSelectedMarker = mapMarkers.get(marker).GetID();
				Intent intent = new Intent(MainActivity.this,
						InstrumentListActivity.class);
				startActivity(intent);

			}

		});
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (!blnIsReady) {
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					latLng, 17);
			mMap.animateCamera(cameraUpdate);
			blnIsReady = true;
		}

	}

	/**
	 * Callback called when connected to GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	/**
	 * Callback called when disconnected from GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onDisconnected() {
		// Do nothing
	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the wSupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true); // Show user location.
				mMap.setOnMyLocationButtonClickListener(this); // Adds the
																// "zoom to me"
																// button.
				mMap.setMapType(4); // Hybrid map.
				mUISettings = mMap.getUiSettings();
				mUISettings.setCompassEnabled(true);

			}
		}
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	@Override
	public boolean onMyLocationButtonClick() {
		return false;
	}

	private void addMarkersToMap() {

		// First iterate through available instruments, and assign an individual
		// marker to each instrument.
		mapMarkers = new HashMap<Marker, Instrument>();
		Marker marker;

		for (Iterator<Instrument> i = sd.GetInstruments().iterator(); i
				.hasNext();) {
			Instrument inst = i.next();

			if (inst instanceof Route) {

				((Route) inst).setRouteID(mMap.addPolyline(
						new PolylineOptions().addAll(((Route) inst).getRoute())
								.width(3).color(Color.BLUE)).getId());

			}
			marker = mMap
					.addMarker(new MarkerOptions()
							.position(inst.GetLatLng())
							.title(inst.getName())
							.snippet(
									"We need to decide what we want in this Info box.")
							.icon(BitmapDescriptorFactory.fromResource(inst
									.GetIconID())));
			mapMarkers.put(marker, inst); // Note: Markers are used as a key,
											// and the instrument as a value.
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		filterMapMarkers("class instruments." + item.getTitle());
		return true;
	}

	/**
	 * Toggles visibility of specified marker type.
	 * */
	private void filterMapMarkers(String Type) {
		for (Entry<Marker, Instrument> i : mapMarkers.entrySet()) {
			if (i.getValue().getClass().toString().equals(Type)) {
				if (i.getKey().isVisible()) {
					i.getKey().setVisible(false);
					if (i instanceof Route) {

					}
				} else {
					i.getKey().setVisible(true);
				}

			}
		}
	}

	public static Map<Marker, Instrument> GetInstruments() {
		return MainActivity.mapMarkers;
	}
}