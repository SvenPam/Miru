package main;


import java.util.ArrayList;
import java.util.List;

import assets.Asset;
import assets.Flare;
import assets.Tank;

import com.google.android.gms.maps.model.LatLng;

/**
 * Sample data to used prior to creation of a data source.
 * 
 * @author Stephen Pammenter E: spammenter@live.com W: www.ste-pam.com
 * 
 *         Teesside University Uni ID: K0025970
 * 
 *         Created: 10-DEC-2013
 * */
public class SampleData {

	private ArrayList<Asset> alInstruments;

	public SampleData() {

		List<LatLng> ltlngList = new ArrayList<LatLng>();

		alInstruments = new ArrayList<Asset>();

		alInstruments.add(new Flare("Behind", 1,
				new LatLng(54.56354, -1.163902), "Flare for Olfeins hot end.",
				null));
		alInstruments.add(new Tank("Olefins Tank 1", 2, new LatLng(54.563938,
				-1.164621), "Random tank.", null));
		alInstruments.add(new Tank("Olefins Tank 2", 3, new LatLng(54.563998,
				-1.163849), "Random tank.", null));
		alInstruments.add(new Tank("Olefins Tank 3", 3, new LatLng(54.563718,
				-1.164965), "Random tank.", null));
		alInstruments.add(new Tank("LDPE Silo BN58601", 4, new LatLng(
				54.563478, -1.16446), "LDPE storage silo.", null));
		alInstruments.add(new Tank("In front", 5, new LatLng(54.563581,
				-1.164283), "LDPE storage silo.", null));

		/**
		 * alInstruments.add(new Flare("Olefins Flare", 1, new LatLng(54.580811,
		 * -1.108327), "Flare for Olfeins hot end.", null));
		 * alInstruments.add(new Tank("Olefins Tank 1", 2, new
		 * LatLng(54.563683,-1.164299), "Random tank.", null));
		 * alInstruments.add(new Tank("Olefins Tank 2", 3, new LatLng(54.580194,
		 * -1.105017), "Random tank.", null)); alInstruments.add(new
		 * Tank("Olefins Tank 3", 3, new LatLng(54.580137, -1.104974),
		 * "Random tank.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58601", 4, new LatLng( 54.591382, -1.105481),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58602", 5, new LatLng( 54.591463, -1.105532),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58603", 6, new LatLng( 54.591542, -1.105564),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58604", 7, new LatLng( 54.591604, -1.105626),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58605", 8, new LatLng( 54.591676, -1.105687),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58606", 9, new LatLng( 54.591427, -1.105349),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58607", 10, new LatLng( 54.591505, -1.105398),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58608", 11, new LatLng( 54.591567, -1.105454),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58609", 12, new LatLng( 54.591637, -1.105502),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58610", 13, new LatLng( 54.591708, -1.105548),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58611", 14, new LatLng( 54.59146, -1.105248),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58612", 15, new LatLng( 54.59153, -1.10528),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58613", 16, new LatLng( 54.591592, -1.105352),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58614", 17, new LatLng( 54.591662, -1.105387),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58615", 18, new LatLng( 54.591732, -1.105457),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58616", 19, new LatLng( 54.591483, -1.105124),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58617", 20, new LatLng( 54.591553, -1.105175),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58618", 21, new LatLng( 54.59169, -1.105272),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58619", 22, new LatLng( 54.591769, -1.105352),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58620", 23, new LatLng( 54.591492, -1.105003),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58621", 24, new LatLng( 54.591593, -1.105065),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58622", 25, new LatLng( 54.591652, -1.105111),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58623", 26, new LatLng( 54.591716, -1.105164),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58624", 27, new LatLng( 54.591783, -1.105191),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58625", 28, new LatLng( 54.591558, -1.104894),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58626", 29, new LatLng( 54.591615, -1.104958),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58627", 30, new LatLng( 54.591688, -1.104995),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58628", 31, new LatLng( 54.591747, -1.105036),
		 * "LDPE storage silo.", null)); alInstruments.add(new
		 * Tank("LDPE Silo BN58629", 32, new LatLng( 54.591812, -1.105081),
		 * "LDPE storage silo.", null));
		 * 
		 * ltlngList.add(new LatLng(54.58383, -1.101638)); ltlngList.add(new
		 * LatLng(54.580074, -1.098811)); ltlngList.add(new LatLng(54.579117,
		 * -1.102534)); ltlngList.add(new LatLng(54.577158, -1.100661));
		 * 
		 * Pipe pipe = new Pipe("Test Pipe 1", 33, new LatLng(54.58383,
		 * -1.101638), "This is a test route.", null); pipe.setRoute(ltlngList);
		 * alInstruments.add(pipe);
		 */

	}

	public ArrayList<Asset> GetInstruments() {

		return this.alInstruments;
	}

}
