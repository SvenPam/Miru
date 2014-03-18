package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
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
	private float[] mMagneticValues;
	/** Sensor.TYPE_ACCELEROMETER values. */
	private float[] mAccelerometerValues = new float[3];
	private float mdeviceHeading;
	/* Stores x y cords of canvas artifacts. ID | left | top | right | bottom* */
	private ArrayList<int[]> mArtefactLocations;
	/** If camera API cannot provide us with FOV, we use this. */
	private double mDefaultFOV = 120;
	/** Field of View - what the 'camera' can see. */
	private double mFieldOfView = mDefaultFOV;

	private final SensorEventListener mListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				mAccelerometerValues = event.values;
				//Device has been lifted up.
				if ((mAccelerometerValues[0] > -3 && mAccelerometerValues[0] < 3)
						&& mAccelerometerValues[2] > 10) {
					finish();
				}
			}
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				mMagneticValues = event.values;
			}
			if (mView != null) {
				//Sets the FOV if we haven't already.
				if (mFieldOfView == mDefaultFOV || mFieldOfView != mFieldOfView) {
					try {
						mFieldOfView = CustomCameraView.getFOV();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
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
		MainActivity.sliftDetected = false;
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
			mdeviceHeading = values[0];
		}
	}

	private float[] calculateOrientation() {
		float[] values = new float[3];
		float[] R = new float[9];
		float[] outR = new float[9];

		try {
			SensorManager.getRotationMatrix(R, null, mAccelerometerValues,
					mMagneticValues);
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
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(mListener, magField,
				SensorManager.SENSOR_DELAY_UI);
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
			int left = 0, top = 0, right = 0, bottom = 0;
			int[] coords = new int[5];

			mArtefactLocations = new ArrayList<int[]>();
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
			multiplier = w / mFieldOfView;
			lowerLimit = mdeviceHeading - (mFieldOfView / 2);
			upperLimit = mdeviceHeading + (mFieldOfView / 2);

			if (deviceLoc != null && count <= maxArtefacts) {
				TreeMap tm = (TreeMap) Data.getAssets(deviceLoc);
				Iterator i = tm.entrySet().iterator();
				while (i.hasNext()) {
					Entry entry = (Entry) i.next();
					Asset asset = (Asset) entry.getValue();

					// Get asset location.
					assetLocation = asset.getLocation();

					// Set headings.
					assetHeading = deviceLoc.bearingTo(assetLocation);

					// Set up canvas.
					paint.setAntiAlias(true);
					paint.setColor(Color.rgb(41, 169, 225));
					paint.setStyle(Paint.Style.FILL);

					// If the asset is visible, it goes in our bounds.
					if (assetHeading > lowerLimit && assetHeading < upperLimit) {
						fltPoint = (float) (multiplier * ((mFieldOfView / 2) + (assetHeading - mdeviceHeading)));

						left = Math.round(fltPoint) - 60;
						top = yPosi;
						right = Math.round(fltPoint) + 60;
						bottom = yPosi + 30;

						canvas.drawRect(left, top, right, bottom, paint);
						paint.setColor(Color.WHITE);
						canvas.drawText(asset.getName(), fltPoint - 50,
								yPosi + 10, paint);
						canvas.drawText(deviceLoc.distanceTo(assetLocation)
								+ "m", fltPoint - 5, yPosi + 25, paint);

						//Store position of artifact for listener use.
						coords[0] = asset.getID();
						coords[1] = left;
						coords[2] = top;
						coords[3] = right;
						coords[4] = bottom;
						mArtefactLocations.add(coords);

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

			canvas.drawText("x: " + mAccelerometerValues[0] + " | y:"
					+ mAccelerometerValues[1] + " | z:"
					+ mAccelerometerValues[2], 10, 600, paint);
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
		}

		@Override
		protected void onDetachedFromWindow() {
			super.onDetachedFromWindow();
		}

		@Override
		public boolean onTouchEvent(final MotionEvent ev) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				int posX = Math.round(ev.getX());
				int posY = Math.round(ev.getY());
				int id = 0;
				int left = 0, top = 0, right = 0, bottom = 0;

				for (int[] coords : mArtefactLocations) {
					id = coords[0];
					left = coords[1];
					top = coords[2];
					right = coords[3];
					bottom = coords[1];
					Rect r = new Rect(left, top, right, bottom);
					if (r.contains(posX, posY)) {
						MainActivity.sSelectedMarker = id;
						Intent intent = new Intent(ARActivity.this,
								InstrumentListActivity.class);
						startActivity(intent);
						break;
					}
				}
				break;
			}
			}
			return true;
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
