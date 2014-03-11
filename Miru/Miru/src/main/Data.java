package main;


import java.io.BufferedReader;
import java.io.FileInputStream;
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

import android.content.Context;
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

	private Gson gson = new Gson();

	static SampleData sd = new SampleData();
	private static ArrayList<Asset> alInstruments = sd.GetInstruments();

	/**
	 * A map of instruments, by ID. Used by master/details activity.
	 */
	private static Map<String, Asset> InstrumentMap = new HashMap<String, Asset>();

	/**
	 * Used in the detail master view, returns Map of instruments.
	 * 
	 * @return String Instrument pair.
	 * */
	public static Map<String, Asset> GetInstrumentMap() {

		for (Iterator<Asset> i = alInstruments.iterator(); i.hasNext();) {
			addItem(i.next());

		}

		return InstrumentMap;
	}

	/**
	 * Used by GetInstrumentMap().
	 * 
	 * @param Asset
	 *            The instrument to be added.
	 **/
	private static void addItem(Asset item) {
		InstrumentMap.put(item.getID().toString(), item);
	}

	/**
	 * Main function for retrieving assets.
	 * 
	 * @return The most up to date objects.
	 */
	public static ArrayList<Asset> GetAssets() {
		SampleData sd = new SampleData();
		return alInstruments;
	}

	/**
	 * Add instruments to the main Instrument list.
	 * 
	 * @param The
	 *            instrument to be added.
	 * */
	public static void AddAsset(Asset inst) {
		if (inst != null) {
			alInstruments.add(inst);
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
		SampleData sd = new SampleData();

		strJSON = gson.toJson(sd.GetInstruments(), Asset.class);

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

				Asset event = gson.fromJson(bufferedReader, Asset.class);

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
	public static void simpleReadObjectsFromFile() throws IOException {
		ObjectInputStream oos = null;

		try {
			FileInputStream fout = new FileInputStream("Data.mir");
			oos = new ObjectInputStream(fout);
			alInstruments = (ArrayList<Asset>) oos.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			oos.close();
		}
	}

	/**
	 * Write main collection of instruments into local data file.
	 */
	public static void simpleWriteObjectsToFile() throws IOException {
		ObjectOutputStream oos = null;

		try {
			FileOutputStream fout = new FileOutputStream("Data.mir");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(GetAssets());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			oos.close();
		}
	}
}
