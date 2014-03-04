package main;

import instruments.Flare;
import instruments.Instrument;
import instruments.Pipe;
import instruments.Tank;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import properties.Route;

import android.content.Context;
import android.graphics.Color;
import android.renderscript.Type;
import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Helper class for providing content for user interfaces.
 * 
 * Final Year Project
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
	
	private Gson gson = new Gson();
	
	static SampleData sd = new SampleData();
	private static ArrayList<Instrument> alInstruments = sd.GetInstruments();
	
	/**
	 * A map of instruments, by ID. Used by master/details activity.
	 */
	private static Map<String, Instrument> InstrumentMap = new HashMap<String, Instrument>();


	public static Map<String, Instrument> GetInstrumentMap() {

		for (Iterator<Instrument> i = alInstruments.iterator(); i
				.hasNext();) {
			addItem(i.next());
			
		}
		
		return InstrumentMap;
	}

	private static void addItem(Instrument item) {
		InstrumentMap.put(item.GetID().toString(), item);
	}
	
	public static ArrayList<Instrument> GetAssets()
	{	
		SampleData sd = new SampleData();		
		return alInstruments;
	}
	
	public static void AddAsset(Instrument inst)
	{
		if (inst != null)
		{
			alInstruments.add(inst);
		}
	}
	
	public void writeAssetsToJSON(Context context)
	{
		String strJSON = "";
		SampleData sd = new SampleData();
		
		strJSON = gson.toJson(sd.GetInstruments(), Instrument.class);
	
		FileOutputStream outputStream;

		try {
		  outputStream = context.openFileOutput("Data.json", Context.MODE_PRIVATE);
		  outputStream.write(strJSON.getBytes());
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}

	}
	
	public void readJSONToAssets(Context context) {
		
		StringBuilder strbJSON = new StringBuilder();

	    try {
	    	
	        InputStream inputStream = context.openFileInput("Data.json");

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            
	             
	             //JsonParser parser = new JsonParser();
	            // JsonArray array = parser.parse(bufferedReader).getAsJsonArray();

	             Instrument event = gson.fromJson(bufferedReader, Instrument.class);
	             
	             System.out.print(event.getDescription());

	            inputStream.close();
	            	            
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }
		    
	}
	
	@SuppressWarnings("unchecked")
	public static void simpleReadObjectsFromFile() throws IOException
	{
		ObjectInputStream oos = null;
		
		try
		{
		    FileInputStream fout = new FileInputStream("Data.mir");
	        oos = new ObjectInputStream(fout);
	        alInstruments =  (ArrayList<Instrument>) oos.readObject();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			oos.close();
		}
	}
	
	public static void simpleWriteObjectsToFile() throws IOException
	{
		ObjectOutputStream oos = null;
		
		try
		{
		    FileOutputStream fout = new FileOutputStream("Data.mir");
	        oos = new ObjectOutputStream(fout);
	        oos.writeObject(GetAssets());	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			oos.close();
		}
	}
	
}
