package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import properties.Route;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.Toast;
import assets.Asset;

import com.example.miru.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * The main activity for the Miru which intiliazes the app.
 * 
 * Final Year Project
 * 
 * @author Stephen Pammenter
 * 
 *         E: spammenter@live.com W: www.ste-pam.com
 * 
 *         Teesside University Uni ID: K0025970
 * 
 *         Created: 10-DEC-2013
 * */
public class MainActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
		OnMyLocationButtonClickListener, OnMapLongClickListener,
		SensorEventListener {
	/** Stores the map, map settings and markers */
	private static GoogleMap sMap;
	/** Used for location. */
	private static LocationClient sLocationClient;
	/** Defines the map overlay. */
	private UiSettings mUISettings;
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	/**
	 * HashMap of key value pairs, key: marker and value: an instrument. Used
	 * between fragments.
	 */
	private static Map<Marker, Asset> sMapMarkers;
	/**
	 * Used to prevent the map re-zooming to user location after initial
	 * startup.
	 */
	private boolean mIsReady;
	/** Stored the ID of the last selected marker. */
	public static Integer sSelectedMarker;
	/**
	 * Used to store the map details, when orientation changes prevents us
	 * losing settings.
	 */
	private CameraPosition mCameraPosition;
	private SensorManager mSensorManager;
	private float[] mAccelerometerValues = new float[3];
	public static boolean sliftDetected = false;
	/**
	 * Used to retain state of camera view after ARActivity is paused/destroyed.
	 */
	public static SurfaceView sCCView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			Data.simpleWriteObjectsToFile(getApplicationContext());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (sLocationClient != null) {
			sLocationClient.disconnect();
		}
		mCameraPosition = sMap.getCameraPosition();
		sMap = null;
	}

	@Override
	protected void onResume() {
		super.onResume();

		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		sLocationClient.connect();

		onMyLocationButtonClick(); // Automatically zoom to users location.
		if (mCameraPosition != null) {
			sMap.moveCamera(CameraUpdateFactory
					.newCameraPosition(mCameraPosition));
			mCameraPosition = null;
		}

		try {
			Data.simpleReadObjectsFromFile(getApplicationContext());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (Data.getAssets() != null) {
			// Assign a listener to each marker.
			sMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				public void onInfoWindowClick(Marker marker) {

					try {
						Data.simpleWriteObjectsToFile(getApplicationContext());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if (sMapMarkers != null && marker != null) {
						try {
							Asset i = sMapMarkers.get(marker);
							sSelectedMarker = i.getID();
						} catch (Exception e) {
							// Seriously have no idea why this occasionally happens.
						}
					}

					try {
						startDetailActivity();
					} catch (NullPointerException e) {

					}
				}

			});
			addMarkersToMap();

		} else {
			Toast.makeText(getApplication(), "Data could not be found.",
					Toast.LENGTH_LONG).show();

		}
		sMap.setOnMapLongClickListener(this);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				1000000000);
	}

	/**
	 * Populates the action bar.
	 * */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.filter, menu);

		menu.add(1, 1, 1, "Go to AR");
		menu.add(1, 2, 2, "Go to Detail View");
		menu.add(1, 3, 3, "Clear Cache");
		menu.add(1, 4, 4, "Settings");

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		DialogFragment clearCacheDialogue;
		switch (item.getItemId()) {
		case 1:
			startARActivity();
			break;
		case 2:
			startDetailActivity();
			break;
		case 3:
			clearCacheDialogue = new ClearCacheDialogue();
			clearCacheDialogue.show(getFragmentManager(), "GeoTag");
			break;
		case 4:
			startSettingsActivity();
			break;
		default:
			filterMapMarkers("class assets." + item.getTitle());
			item.setChecked(!item.isChecked());
			break;
		}

		return true;
	}

	/**
	 * Callback called when connected to GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		sLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	/**
	 * Callback called when disconnected from GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onDisconnected() {
		try {
			Data.simpleWriteObjectsToFile(getApplicationContext());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (!mIsReady) {
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					latLng, 17);
			sMap.animateCamera(cameraUpdate);
			mIsReady = true;
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			Data.simpleWriteObjectsToFile(getApplicationContext());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

	private void setUpMapIfNeeded() {
		//Used to reintilize the map after an event which triggers the destruction of the current fragment.
		try {
			MapsInitializer.initialize(getApplication());
		} catch (GooglePlayServicesNotAvailableException e) {
		}
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (sMap == null) {
			// Try to obtain the map from the wSupportMapFragment.
			sMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (sMap != null) {
				sMap.setMyLocationEnabled(true); // Show user location.
				sMap.setOnMyLocationButtonClickListener(this); // Adds the
																// "zoom to me"
																// button.
				sMap.setMapType(4); // Hybrid map.
				mUISettings = sMap.getUiSettings();
				mUISettings.setCompassEnabled(true);
			}
		}
	}

	@Override
	public boolean onMyLocationButtonClick() {
		return false;
	}

	@Override
	public void onMapLongClick(LatLng Ltlng) {

		LatLng currentPosi;

		double lat;
		double lng;

		DialogFragment geoTagDialogue;

		if (sLocationClient.getLastLocation() != null) {
			lat = sLocationClient.getLastLocation().getLatitude();
			lng = sLocationClient.getLastLocation().getLongitude();
			currentPosi = new LatLng(lat, lng);
		} else {
			currentPosi = null;
		}

		geoTagDialogue = new GeoTagDialogue(currentPosi, Ltlng, null, null,
				null);

		geoTagDialogue.show(getFragmentManager(), "GeoTag");

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mAccelerometerValues = event.values;
			//Device has been lifted up.
			if ((mAccelerometerValues[0] < -6 || mAccelerometerValues[0] > 6)
					&& mAccelerometerValues[2] < 7 && !sliftDetected) {
				startARActivity();
			}
		}

	}

	private void setUpLocationClientIfNeeded() {
		if (sLocationClient == null) {
			sLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	/**
	 * Removes existing markers from map, builds a fresh set and adds them
	 * again.
	 */
	public static void addMarkersToMap() {

		// First iterate through available instruments, and assign an individual
		// marker to each instrument.
		sMapMarkers = null;
		Marker marker;
		sMap.clear();
		sMapMarkers = new HashMap<Marker, Asset>();
		for (Iterator<Asset> i = Data.getAssets().iterator(); i.hasNext();) {
			Asset inst = i.next();

			if (inst instanceof Route) {

				((Route) inst).setRouteID(sMap.addPolyline(
						new PolylineOptions().addAll(((Route) inst).getRoute())
								.width(3).color(Color.BLUE)).getId());

			}
			marker = sMap
					.addMarker(new MarkerOptions()
							.position(inst.getLatLng())
							.title(inst.getName())
							.snippet(
									"We need to decide what we want in this Info box.")
							.icon(BitmapDescriptorFactory.fromResource(inst
									.getIconID())));
			sMapMarkers.put(marker, inst); // Note: Markers are used as a key,
											// and the instrument as a value.
		}
	}

	/**
	 * Toggles visibility of specified marker type.
	 * */
	private void filterMapMarkers(String Type) {
		try {
			for (Entry<Marker, Asset> i : sMapMarkers.entrySet()) {
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
		} catch (NullPointerException e) {
			Toast.makeText(getApplicationContext(),
					"Data has not been correctly loaded, restart app.",
					Toast.LENGTH_LONG).show();
		}
	}

	public static Location getLastKnownLocation() {
		return sLocationClient.getLastLocation();
	}

	private void startARActivity() {

		Intent intent = new Intent(MainActivity.this, main.ARActivity.class);
		this.startActivity(intent);
		if (sCCView == null) {
			sCCView = new CustomCameraView(this);
		}
		sliftDetected = true;

	}

	private void startDetailActivity() {
		Intent intent = new Intent(MainActivity.this,
				InstrumentListActivity.class);
		startActivity(intent);

	}

	private void startSettingsActivity() {
		Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
		startActivity(intent);

	}
}