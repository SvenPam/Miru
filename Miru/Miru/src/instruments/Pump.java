package instruments;

import com.example.miru.R;
import com.google.android.gms.maps.model.LatLng;
/**
 * Represents one pump.
 * 
 * @author Stephen Pammenter 
 * E: spammenter@live.com 
 * W: www.ste-pam.com 
 * 
 * Teesside University 
 * Uni ID: K0025970 
 * 
 * Created: 26-FEB-2013
 */
public class Pump extends Instrument {

	public Pump(String Name, int ID, LatLng LatLong, String Description, String Ref) {
			this.strName = Name;
			this.intID = ID;
			this.latLng = LatLong;
			this.intIconID = R.drawable.marker_pin_pump;
			this.strDescription = Description;
			if (Ref != null)
			{
				this.strRef = Ref;
			}
			else
			{
				this.strRef = "";
			}
		}
		public String getJSON()
		{
			return "\"Pump\": " + super.getJSON();
		}
	}