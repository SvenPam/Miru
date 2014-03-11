package main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;



import com.example.miru.R;
import com.example.miru.R.layout;
import com.google.android.gms.maps.model.LatLng;

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
import assets.*;
/**
 * Provides functions to geotag new instruments.
 * 
 * @author Stephen Pammenter 
 * E: spammenter@live.com 
 * W: www.ste-pam.com 
 * 
 * Teesside University 
 * Uni ID: K0025970 
 * 
 * Created: 24-FEB-2013
 */
@SuppressLint("ValidFragment")
public class GeoTagDialogue  extends DialogFragment {
	
	private LatLng ltlngDevice;
	private LatLng ltlngPressed;
	private String strName;
	private String strDescription;
	private String strRef;
	
	

	public GeoTagDialogue(LatLng DevicePosi, LatLng PressedPosi, String Name, String Description, String Ref) {
		this.ltlngDevice = DevicePosi;
		this.ltlngPressed = PressedPosi;
		this.strName = Name;
		this.strDescription = Description;
		this.strRef = Ref;
	}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	   	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = (View) inflater.inflate(R.layout.geotag_dialogue, null);
        
        EditText txt;
        
        
        //Lock switch if device location is not present.
        if (ltlngDevice == null)
        {
        	
        	Switch swt = (Switch) view.findViewById(R.id.switchLocation);
        	swt.setChecked(false);
        	swt.setEnabled(false);
        }
        
        if(strName != null)
        {
        	txt = (EditText) view.findViewById(R.id.txtName);
        	txt.setText(strName);
        }
        if(strDescription != null)
        {
        	txt = (EditText) view.findViewById(R.id.txtName);
        	txt.setText(strName);
        }
        if(strRef != null)
        {
        	txt = (EditText) view.findViewById(R.id.txtName);
        	txt.setText(strName);
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
				if(blnUseCurrentLocation && ltlngDevice != null)
				{
					latlng = ltlngDevice;
				}
				else
				{
					latlng = ltlngPressed;
				}
				
				Asset inst  = null;
				
				//Because Java 1.6 does not support case statements for strings, we have to use a bunch of if's...
				if (strType.equals("Pump"))
				{
					inst = new Pump(strName, -1, latlng, strDesc, strRef);
				}
				else if (strType.equals("Tank"))
				{
					inst = new Tank(strName, -1, latlng, strDesc, strRef);
				}
				else if (strType.equals("Flare"))
				{
					inst = new Flare(strName, -1, latlng, strDesc, strRef);
				}
				
				Data.AddAsset(inst);
				MainActivity.addMarkersToMap();
				
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// User cancelled dialogue.
				
			}
		});
        
        
        
        return builder.create();
    }
    
   
    
}