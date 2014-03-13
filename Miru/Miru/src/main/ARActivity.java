package main;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import assets.Asset;

/**
 * Sample data to used prior to creation of a data source.
 * 
 * @author Stephen Pammenter
 * 
 *         E: spammenter@live.com W: www.ste-pam.com
 * 
 *         Teesside University Uni ID: K0025970
 * 
 *         Created: 07-MAR-2014
 * */
public class ARActivity extends Activity {
	private SensorManager mSensorManager;
	private AREngine mView;
	/** Sensor.TYPE_MAGNETIC_FIELD values */
	private float[] mValues;
	/** Sensor.TYPE_ACCELEROMETER values. */
	private float[] aValues = new float[3];
	private float deviceHeading;

	private final SensorEventListener mListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
				aValues = event.values;
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
				mValues = event.values;
			if (mView != null) {
				mView.invalidate();
			}
			updateOrientation(calculateOrientation());
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		updateOrientation(new float[] { 0, 0, 0 });
		mView = new AREngine(this);
		SurfaceView mCCView = new CustomCameraView(this);

		this.addContentView(mCCView, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));
		this.addContentView(mView, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));
	}

	private void updateOrientation(float[] values) {
		if (values != null) {
			deviceHeading = values[0];
		}
	}

	private float[] calculateOrientation() {
		float[] values = new float[3];
		float[] R = new float[9];
		float[] outR = new float[9];

		try {
			SensorManager.getRotationMatrix(R, null, aValues, mValues);
			SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X,
					SensorManager.AXIS_Z, outR);

			SensorManager.getOrientation(outR, values);

			// Convert from Radians to Degrees.
			values[0] = (float) Math.toDegrees(values[0]);
			values[1] = (float) Math.toDegrees(values[1]);
			values[2] = (float) Math.toDegrees(values[2]);
		} catch (Exception e) {
			//Happens when sensors have not yet initialised.
			e.printStackTrace();
			values[0] = 0;
		}
		return values;
	}

	@Override
	protected void onResume() {
		super.onResume();

		Sensor accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor magField = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		mSensorManager.registerListener(mListener, accelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(mListener, magField,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(mListener);
		super.onStop();
	}

	/**
	 * An inner class which performs the calcs on placing assets on a canvas...
	 * then places them.
	 */
	private class AREngine extends View {
		private Paint mPaint = new Paint();

		public AREngine(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			Paint paint = mPaint;
			Location assetLocation;
			/** Field of View - what the 'camera' can see. */
			double fieldOfView = 120;
			/** Multiplier is used scale heading into the screen width. */
			double multiplier;
			/** Determines the limits of our FOV in degrees. */
			double lowerLimit, upperLimit;
			double assetHeading;
			/** Used to position our asset on screen. */
			float fltPoint = 0;
			int count = 0;
			/** Used to determine the max amount of assets shown on Screen. */
			int maxArtefacts = 30;
			int yPosi = 30;

			canvas.drawColor(Color.TRANSPARENT);

			// Get device location.
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			LocationListener locationListener = new MyLocationListener();
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
			Location deviceLoc = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			// Get dimensions of canvas - will be full screen.
			int w = canvas.getWidth();

			// Set our constants.
			multiplier = w / fieldOfView;
			lowerLimit = deviceHeading - (fieldOfView / 2);
			upperLimit = deviceHeading + (fieldOfView / 2);

			if (deviceLoc != null && count <= maxArtefacts) {
				TreeMap tm = (TreeMap) Data.GetAssets(deviceLoc);
				Iterator i = tm.entrySet().iterator();
				while (i.hasNext()) {
					Entry entry = (Entry) i.next();
					Asset asset = (Asset) entry.getValue();

					// Get asset location.
					assetLocation = asset.getLocation();

					// Set headings.
					assetHeading = 0;
					assetHeading = deviceLoc.bearingTo(assetLocation);

					// Set up canvas.
					paint.setAntiAlias(true);
					paint.setColor(Color.rgb(41, 169, 225));
					paint.setStyle(Paint.Style.FILL);

					// If the asset is visible, it goes in our bounds.
					if (assetHeading > lowerLimit && assetHeading < upperLimit) {
						fltPoint = (float) (multiplier * ((fieldOfView / 2) + (assetHeading - deviceHeading)));
						canvas.drawRect(fltPoint - 60, yPosi, fltPoint + 60,
								yPosi + 30, paint);
						paint.setColor(Color.WHITE);
						canvas.drawText(asset.getName(), fltPoint - 50,
								yPosi + 10, paint);
						canvas.drawText(deviceLoc.distanceTo(assetLocation)
								+ "m", fltPoint - 5, yPosi + 25, paint);

						//If an image wants adding:
						//Bitmap largeIcon = BitmapFactory.decodeResource(
						//		getResources(), asset.getIconID());
						//canvas.drawBitmap(largeIcon, 40, 50, paint);
					}
					// To our left.
					else if (assetHeading < lowerLimit) {
						canvas.drawRect(0, yPosi, 30, yPosi + 30, paint);
					}
					// To our right.
					else if (assetHeading > lowerLimit) {
						canvas.drawRect(w - 30, yPosi, w, yPosi + 30, paint);

					}

					yPosi = yPosi + 40;
					count++;
				}
			} else {
				canvas.drawText("Cannot find Location.", 100, 200, paint);
			}

			canvas.drawText(String.valueOf(deviceHeading), 10, 600, paint);
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
		}

		@Override
		protected void onDetachedFromWindow() {
			super.onDetachedFromWindow();
		}
	}
}

/**
 * Helper class to access device location.
 */
class MyLocationListener implements LocationListener {

	private Location currentLoc;

	@Override
	public void onLocationChanged(Location loc) {
		this.currentLoc = loc;
	}

	public Location getCurrentLoc() {
		return this.currentLoc;
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
