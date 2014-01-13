package com.example.miru;

import instruments.Flare;
import instruments.Instrument;
import instruments.Pipe;
import instruments.Tank;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

/**
 * Sample data to used prior to creation of a data source.
 * 
 * @author Stephen Pammenter 
 * E: spammenter@live.com 
 * W: www.ste-pam.com 
 * 
 * Teesside University 
 * Uni ID: K0025970 
 * 
 * Created: 10-DEC-2013
 * */
public class SampleData {

	private ArrayList<Instrument> alInstruments;

	public SampleData() {
		alInstruments = new ArrayList<Instrument>();

		alInstruments.add(new Pipe("Pipe 1", 1,
				new LatLng(54.578778, -1.108764)));
		alInstruments.add(new Pipe("Pipe 2", 2,
				new LatLng(54.577485, -1.107884)));
		alInstruments.add(new Tank("Tank 1", 3,
				new LatLng(54.578305, -1.105095)));
		alInstruments.add(new Tank("Tank 2", 4,
				new LatLng(54.578529, -1.104129)));
		alInstruments.add(new Tank("Tank 3", 5,
				new LatLng(54.579039, -1.105932)));
		alInstruments
				.add(new Tank("Tank 4", 6, new LatLng(54.579325, -1.10473)));
		alInstruments.add(new Flare("Flare", 6,
				new LatLng(54.577559, -1.103722)));
	}

	public ArrayList<Instrument> GetInstruments() {
		return this.alInstruments;
	}
}
