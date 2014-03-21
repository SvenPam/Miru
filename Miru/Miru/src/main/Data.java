package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import assets.Asset;

import com.google.gson.Gson;

/**
 * Helper class for providing content for user interfaces.
 * 
 * Final Year Project
 * 
 * @author Stephen Pammenter
 * 
 *         E: spammenter@live.com W: www.ste-pam.com
 * 
 *         Teesside University Uni ID: K0025970
 * 
 *         Created: 15-JAN-2013
 */
public class Data {

	private Gson mGSON = new Gson();
	/** The main collection of assets used through out application. */
	private static ArrayList<Asset> sAssets = new ArrayList<Asset>();
	/** A map of instruments, by ID. Used by master/details activity. */
	private static Map<String, Asset> sInstrumentMap = new HashMap<String, Asset>();

	/**
	 * Returns assets with distance from passed location.
	 * 
	 * @param Location
	 *            to determine distance from
	 * @returns TreeMap of key value pairs, where the key is the distance.
	 */
	public static Map<Float, Asset> getAssets(Location loc) {
		Map<Float, Asset> assetsWithDistance = new TreeMap<Float, Asset>();
		for (Iterator<Asset> i = sAssets.iterator(); i.hasNext();) {
			Asset inst = i.next();
			assetsWithDistance.put(loc.distanceTo(inst.getLocation()), inst);
		}

		return assetsWithDistance;
	}

	/**
	 * Used in the detail master view, returns Map of instruments.
	 * 
	 * @return String Instrument pair.
	 * */
	public static Map<String, Asset> getAssetMap() {

		for (Iterator<Asset> i = sAssets.iterator(); i.hasNext();) {
			addItem(i.next());

		}
		return sInstrumentMap;
	}

	/** Returns a single asset for passed ID. */
	public static Asset getAsset(int ID) {
		Asset asset = null;
		try {
			for (Iterator<Asset> i = sAssets.iterator(); i.hasNext();) {
				asset = i.next();
				if (asset.getID() == ID) {
					break;
				}

			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}

		return asset;
	}

	/** Returns a single asset for passed ID. */
	public static Asset getAsset(String ID) {
		Asset asset = null;
		try {
			asset = getAsset(Integer.parseInt(ID));

		} catch (NumberFormatException e) {
			asset = null;
		}
		return asset;
	}

	/**
	 * Used by GetInstrumentMap().
	 * 
	 * @param Asset
	 *            The instrument to be added.
	 **/
	private static void addItem(Asset item) {
		sInstrumentMap.put(item.getID().toString(), item);
	}

	/**
	 * Main function for retrieving assets.
	 * 
	 * @return The most up to date objects.
	 */
	public static ArrayList<Asset> getAssets() {
		return sAssets;
	}

	/**
	 * Add instruments to the main Instrument list.
	 * 
	 * @param The
	 *            instrument to be added.
	 * */
	public static void addAsset(Asset inst) {
		if (inst != null) {
			sAssets.add(inst);
		}
	}

	/**
	 * Converts an the main list of Instruments into JSON.
	 * 
	 * @param The
	 *            context of the calling class.
	 */
	public void writeAssetsToJSON(Context context) {
		String strJSON = "";

		strJSON = mGSON.toJson(sAssets, Asset.class);

		FileOutputStream outputStream;

		try {
			outputStream = context.openFileOutput("Data.json",
					Context.MODE_PRIVATE);
			outputStream.write(strJSON.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Read JSON string into list of objects.
	 * 
	 * @param The
	 *            context of the calling class.
	 */
	public void readJSONToAssets(Context context) {

		StringBuilder strbJSON = new StringBuilder();

		try {

			InputStream inputStream = context.openFileInput("Data.json");

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);

				// JsonParser parser = new JsonParser();
				// JsonArray array =
				// parser.parse(bufferedReader).getAsJsonArray();

				Asset event = mGSON.fromJson(bufferedReader, Asset.class);

				System.out.print(event.getDescription());

				inputStream.close();

			}
		} catch (FileNotFoundException e) {
			Log.e("login activity", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("login activity", "Can not read file: " + e.toString());
		}

	}

	@SuppressWarnings("unchecked")
	/**
	 * Read objects from local file into main collection of instruments.
	 */
	public static void simpleReadObjectsFromFile(Context context)
			throws IOException {
		ObjectInputStream oos = null;

		try {
			InputStream inputStream = context.openFileInput("Data");
			oos = new ObjectInputStream(inputStream);
			sAssets = (ArrayList<Asset>) oos.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				oos.close();
			}

		}
	}

	/**
	 * Write main collection of instruments into local data file.
	 */
	public static void simpleWriteObjectsToFile(Context context)
			throws IOException {
		ObjectOutputStream oos = null;
		FileOutputStream fos;
		try {
			fos = context.openFileOutput("Data", Context.MODE_PRIVATE);

			oos = new ObjectOutputStream(fos);
			oos.writeObject(sAssets);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clears all data.
	 * 
	 * @param context
	 * @param save
	 *            If true, will also overwrite local file.
	 */
	public static void clearLocalData(Context context, boolean save) {
		sAssets.clear();
		if (save) {
			try {
				simpleWriteObjectsToFile(context);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		MainActivity.addMarkersToMap();
	}

}
