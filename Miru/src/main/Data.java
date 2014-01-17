package main;

import instruments.Instrument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.android.gms.maps.model.Marker;

/**
 * Helper class for providing content for user interfaces.
 *
 * * Final Year Project
 * 
 * @author Stephen Pammenter 
 * E: spammenter@live.com 
 * W: www.ste-pam.com 
 * 
 * Teesside University 
 * Uni ID: K0025970 
 * 
 * Created: 15-JAN-2013
 */
public class Data {
	/**
	 * An array of instruments.
	 */
	public static List<Instrument> InstrumentList = new ArrayList<Instrument>();
	/**
	 * A map of instruments, by ID.
	 */
	public static Map<String, Instrument> InstrumentMap = new HashMap<String, Instrument>();

	public Data() {

		for (Entry<Marker, Instrument> i : MainActivity.GetInstruments()
				.entrySet()) {
			addItem(i.getValue());
		}
	}

	private static void addItem(Instrument item) {
		InstrumentList.add(item);
		InstrumentMap.put(item.GetID().toString(), item);
	}
}
