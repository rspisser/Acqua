package it.spisser.android.acqua;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AcquaBaseActivity extends Activity {

	protected LocationManager locationManager;
	protected LocationListener listenerCoarse;
	protected LocationListener listenerFine;

	protected Context ctx;
	private Activity acqua;
	private String versionName;
	// Holds the most up to date location.
	protected Location currentLocation;

	// Set to false when location services are

	// unavailable.
	private boolean locationAvailable = true;

	private static final String TAG = "Acqua";

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu, menu);
		return true;

	}

	public void onCreate(Bundle savedInstanceState) {
		// super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		super.onCreate(savedInstanceState);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		// Log.w(TAG,"premuto Informazioni" + item.getItemId());
		Builder builder = new AlertDialog.Builder(this);

		String informazioniFormat;
		switch (item.getItemId()) {
		case R.id.menuInfo:
			// Log.w(TAG,"premuto Informazioni");

			String versione;
			PackageInfo packageInfo;
			try {
				packageInfo = getPackageManager().getPackageInfo(
						getPackageName(), 0);
				versione = packageInfo.versionName;
				// + "Version Code: " + String.valueOf(packageInfo.versionCode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				versione = "Cannot load Version!";
			}

			String titolo = String.format(getString(R.string.InfoTitle,
					versione));

			builder.setTitle(titolo);
			// AlertDialog.Builder ab = null;
			// ab = new AlertDialog.Builder( AlertDialogTest.this );
			// ab.setMessage(
			// Html.fromHtml("<b><font color=#ff00ff> HTML View</font></b><br>Android.com"));
			// ab.setPositiveButton(android.R.string.ok, null);
			// ab.setTitle( "Basic Alert Dialog" );
			// ab.show();
			//

			Log.d(TAG, versione);
			builder.setMessage(getResources().getString(R.string.informazioni)
					.concat((getString(R.string.ChangeLog))));
			// builder.setMessage(
			// Html.fromHtml("<b><font color=#ff00ff> HTML View</font></b><br>Android.com"));
			// builder.setMessage(Html.fromHtml(getResources().getString(R.string.ChangeLog)).toString());

			builder.setPositiveButton(R.string.chiudiInfoButton, null);
			builder.show();
			return true;
		case R.id.menuSettings:
			// Log.w(TAG, "premuto Impostazioni");

			startActivity(new Intent(this, PrefsActivity.class));
			return true;

		case R.id.menuElencoFonti:
			Log.w(TAG, "premuto Elenco Fonti");
			startActivity(new Intent(this, AcquaActivity.class));
			return true;

		case R.id.menuSceltaFonte:
			Log.d(TAG, "premuto Scegli Fonte");
			startActivity(new Intent(this, SceltaFonteActivity.class));
			return true;

		case R.id.menuSuggerisiciFonte:
			Log.d(TAG, "premuto Suggerisci fonte");
			startActivity(new Intent(this, SuggerisciFonteActivity.class));
			return true;

		case R.id.menuContact:

			final Intent emailIntent = new Intent(
					android.content.Intent.ACTION_SEND);

			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { "reinhard@spisser.it" });
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"[acqua] Feedback");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			return true;

			// case R.id.menuSalvaAttuale:
			// SharedPreferences sharedPrefs = PreferenceManager
			// .getDefaultSharedPreferences(this);
			// builder.setMessage(getResources().getString(R.string.informazioni));
			// builder.setPositiveButton(R.string.chiudiInfoButton, null);
			// builder.show();
			//
			// SharedPreferences.Editor editor = sharedPrefs.edit();
			// AutoCompleteTextView c = (AutoCompleteTextView)
			// findViewById(R.id.sceltaComune);
			// editor.putString("preference_comune_default_category",
			// c.getText().toString());
			// editor.commit();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void registerLocationListeners() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// Initialize criteria for location providers
		Criteria fine = new Criteria();
		fine.setAccuracy(Criteria.ACCURACY_FINE);
		Criteria coarse = new Criteria();
		coarse.setAccuracy(Criteria.ACCURACY_COARSE);

		// Get at least something from the device,
		// could be very inaccurate though
		currentLocation = locationManager.getLastKnownLocation(locationManager
				.getBestProvider(fine, true));

		if (listenerFine == null || listenerCoarse == null)
			createLocationListeners();
		// Log.w(TAG,"registerLocationListeners");
		// Will keep updating about every 500 ms until
		// accuracy is about 1000 meters to get quick fix.
		locationManager.requestLocationUpdates(
				locationManager.getBestProvider(coarse, true), 500, 1000,
				listenerCoarse);
		// Will keep updating about every 500 ms until
		// accuracy is about 50 meters to get accurate fix.
		locationManager.requestLocationUpdates(
				locationManager.getBestProvider(fine, true), 500, 50,
				listenerFine);
	}

	/**
	 * Creates LocationListeners
	 */
	private void createLocationListeners() {
		listenerCoarse = new LocationListener() {
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				switch (status) {
				case LocationProvider.OUT_OF_SERVICE:
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					locationAvailable = false;
					break;
				case LocationProvider.AVAILABLE:
					locationAvailable = true;
				}
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}

			public void onLocationChanged(Location location) {
				// Log.w(TAG,"locationchanged");
				currentLocation = location;
				if (location.getAccuracy() > 1000 && location.hasAccuracy())
					locationManager.removeUpdates(this);
			}
		};
		listenerFine = new LocationListener() {
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				switch (status) {
				case LocationProvider.OUT_OF_SERVICE:
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					locationAvailable = false;
					break;
				case LocationProvider.AVAILABLE:
					locationAvailable = true;
				}
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}

			public void onLocationChanged(Location location) {
				currentLocation = location;
				GlobalData gd = (GlobalData) getApplication();
				gd.setCurrent_latitude(location.getLatitude());
				gd.setCurrent_longitude(location.getLongitude());
				if (location.getAccuracy() > 1000 && location.hasAccuracy())
					locationManager.removeUpdates(this);
			}
		};
	}

	public static Double calcolaDistanza(Double lat1, Double lng1, Double lat2,
			Double lng2) {
		double earthRadius = 6371;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}

	/*
	 * metodo che restituisce un istanze di Comune valorizzato con i dati
	 * recuperati tramite il reverse geocoding utilizzando GeoCoder
	 */

	public Comune getComuneFromLocation(Double latitudine, Double longitudine) {

		Comune result = new Comune();

		Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
		try {
			List<Address> addresses = gc.getFromLocation(latitudine,
					longitudine, 1);
			if (addresses.size() > 0) {
				Address address = addresses.get(0);

				result.setNomecomune(address.getLocality());
				result.setProvincia(address.getSubAdminArea());
			}
		} catch (Exception e) {
			/* in qualche modo non è stato possibile fare il reverse geocoding, 
			 * ad esempio perché manca la connettivita' internet
			 */
				result.setNomecomune("");
				result.setProvincia("");
				
		}

		Double cl = Math.rint(latitudine * 10000);

		Double clong = Math.rint(longitudine * 10000);

		result.setLatitudine(latitudine);
		result.setLongitudine(longitudine);
		result.setLatitudine_display(cl / 10000);
		result.setLongitudine_display(clong / 10000);

		return result;
	}
}
