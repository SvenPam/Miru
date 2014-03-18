package main;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * A custom view used to set up and display the camera.
 * 
 * Final Year Project
 * 
 * @author Stephen Pammenter E: spammenter@live.com W: www.ste-pam.com
 * 
 *         Teesside University Uni ID: K0025970
 * 
 *         Created: 28-FEB-2014
 * */
public class CustomCameraView extends SurfaceView {

	private static Camera camera;
	private SurfaceHolder previewHolder;

	public CustomCameraView(Context context) {
		super(context);

		previewHolder = this.getHolder();
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		previewHolder.addCallback(surfaceHolderListener);

	}

	public static float getFOV() {
		return camera.getParameters().getVerticalViewAngle();
	}

	SurfaceHolder.Callback surfaceHolderListener = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {

			try {
				camera = Camera.open(0);
				camera.setPreviewDisplay(previewHolder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			Camera.Parameters parameters = camera.getParameters();

			List<Size> sizes = parameters.getSupportedPreviewSizes();
			Size optimalSize = getOptimalPreviewSize(sizes, w, h);
			parameters.setPreviewSize(optimalSize.width, optimalSize.height);

			Display display = ((WindowManager) getContext().getSystemService(
					Context.WINDOW_SERVICE)).getDefaultDisplay();

			if (display.getRotation() == Surface.ROTATION_0) {
				parameters.setPreviewSize(h, w);
				camera.setDisplayOrientation(180);
			}

			if (display.getRotation() == Surface.ROTATION_90) {
				camera.setDisplayOrientation(180);
			}

			if (display.getRotation() == Surface.ROTATION_180) {
				camera.setDisplayOrientation(180);
			}

			if (display.getRotation() == Surface.ROTATION_270) {
				camera.setDisplayOrientation(180);
			}

			camera.setParameters(parameters);
			camera.startPreview();
		}

		private Size getOptimalPreviewSize(List<Size> sizes, int Width,
				int Height) {
			final double ASPECT_TOLERANCE = 0.05;
			double targetRatio = (double) Width / Height;
			double ratio;
			Size optimalSize = null;
			double minDiff = Double.MAX_VALUE;
			int targetHeight = Height;

			// Attempt to find a size matching ratio and size.
			for (Size size : sizes) {
				ratio = (double) size.width / size.height;
				if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
					continue;
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
			if (optimalSize == null) {
				minDiff = Double.MAX_VALUE;
				for (Size size : sizes) {
					if (Math.abs(size.height - targetHeight) < minDiff) {
						optimalSize = size;
						minDiff = Math.abs(size.height - targetHeight);
					}
				}
			}
			return optimalSize;
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub

		}

	};

}
