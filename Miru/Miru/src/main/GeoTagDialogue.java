package main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import assets.Asset;
import assets.Flare;
import assets.Pump;
import assets.Tank;

import com.example.miru.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Provides functions to geotag new instruments.
 * 
 * @author Stephen Pammenter E: spammenter@live.com W: www.ste-pam.com
 * 
 *         Teesside University Uni ID: K0025970
 * 
 *         Created: 24-FEB-2013
 */
@SuppressLint("ValidFragment")
public class GeoTagDialogue extends DialogFragment {

	private LatLng deviceLoc;
	private LatLng pressedLoc;
	private String name;
	private String description;
	private String ref;
	private int id;

	public GeoTagDialogue(LatLng DevicePosi, LatLng PressedPosi, String Name,
			String Description, String Ref) {
		this.deviceLoc = DevicePosi;
		this.pressedLoc = PressedPosi;
		this.name = Name;
		this.description = Description;
		this.ref = Ref;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View view = (View) inflater.inflate(R.layout.geotag_dialogue,
				null);

		EditText txt;

		//Lock switch if device location is not present.
		if (deviceLoc == null) {

			Switch swt = (Switch) view.findViewById(R.id.switchLocation);
			swt.setChecked(false);
			swt.setEnabled(false);
		}

		if (name != null) {
			txt = (EditText) view.findViewById(R.id.txtName);
			txt.setText(name);
		}
		if (description != null) {
			txt = (EditText) view.findViewById(R.id.txtName);
			txt.setText(name);
		}
		if (ref != null) {
			txt = (EditText) view.findViewById(R.id.txtName);
			txt.setText(name);
		}

		builder.setView(view);
		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean blnUseCurrentLocation;
				Switch swt = (Switch) view.findViewById(R.id.switchLocation);
				Spinner spn = (Spinner) view.findViewById(R.id.spinnerType);
				EditText txt;
				String strType, strName, strDesc, strRef;
				LatLng latlng;

				blnUseCurrentLocation = swt.isChecked();

				strType = spn.getSelectedItem().toString();

				txt = (EditText) view.findViewById(R.id.txtName);
				strName = txt.getText().toString();
				txt = (EditText) view.findViewById(R.id.txtDesc);
				strDesc = txt.getText().toString();
				txt = (EditText) view.findViewById(R.id.txtRef);
				strRef = txt.getText().toString();
				if (blnUseCurrentLocation && deviceLoc != null) {
					latlng = deviceLoc;
				} else {
					latlng = pressedLoc;
				}

				try {
					id = Data.getAssets().get(Data.getAssets().size()).getID() + 1;
				} catch (Exception e) {
					id = 1;
				}

				if (Data.getAssetMap().get(id) != null) {

				}

				Asset inst = null;
				//Because Java 1.6 does not support case statements for strings, we have to use a bunch of if's...
				if (strType.equals("Pump")) {
					inst = new Pump(strName, id, latlng.latitude,
							latlng.longitude, strDesc, strRef);
				} else if (strType.equals("Tank")) {
					inst = new Tank(strName, id, latlng.latitude,
							latlng.longitude, strDesc, strRef);
				} else if (strType.equals("Flare")) {
					inst = new Flare(strName, id, latlng.latitude,
							latlng.longitude, strDesc, strRef);
				}

				Data.addAsset(inst);
				MainActivity.addMarkersToMap();
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// User cancelled dialogue.

			}
		});

		return builder.create();
	}
}