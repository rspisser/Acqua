package it.spisser.acqua;

import it.spisser.acqua.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class AcquaActivity extends AcquaBaseActivity {

	// Set to false when location services are

	// unavailable.

	private TextView comune;
	private TextView comuneProvincia;
	private TextView lat_gradi_dec;
	private TextView long_gradi_dec;
	private int currentSelectedItem = 0;

	private List<Fonte> fontiList;
	private ListView menuList;
	private Double current_latitude;
	private Double current_longitude;
	private Button cercaButton;
	private AutoCompleteTextView comuniView;
	// private MultiAutoCompleteTextView comuniView;
	private TextView elencoFonti;
	private DataHelper dh;
	private static final String TAG = "Acqua";
	private static String currentSelectedComune;

	static final int REQUEST_CODE = 0;

	public void handleFonti(Double actual_latitude, Double actual_longitude) {
		Double lat_min = actual_latitude - 1;
		Double lat_max = actual_latitude + 1;
		Double long_min = actual_longitude - 1;
		Double long_max = actual_longitude + 1;

		List<Fonte> fonti = new ArrayList<Fonte>();

		fonti = null;
		fonti = dh.getFonti(lat_min, lat_max, long_min, long_max);
		fonti = calcolaDistanze(fonti, actual_latitude, actual_longitude);
		Collections.sort(fonti);
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		Boolean useGoogleMaps = sharedPrefs.getBoolean(
				"preference_usa_google_maps", true);

		if ((fonti.size() > 0) && (useGoogleMaps == true)) {
			Button MappaButton = (Button) findViewById(R.id.ElencoFontiMappaButton);
			MappaButton.setVisibility(View.VISIBLE);
			Acqua gd = (Acqua) this.getApplication();
			gd.setListaFonti(fonti);
			// this.fontiList=fonti;
		}
		menuList = (ListView) findViewById(R.id.lista);

		menuList.setTextFilterEnabled(true);
		ListAdapter adapter = new FonteListAdapter(fonti, this);
		menuList.setAdapter(adapter);

		menuList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				LinearLayout layout = (LinearLayout) view;

				Fonte fonte = (Fonte) menuList.getItemAtPosition(position);
				// viewFontiDetails(fonte);

				LinearLayout sotto = (LinearLayout) layout
						.findViewById(R.id.vista_dettaglio);
				sotto.setVisibility(View.VISIBLE);

				((TextView) sotto.findViewById(R.id.vistadettaglionomefonte))
						.setText(fonte.getNomeCommerciale());
				((TextView) sotto.findViewById(R.id.vistadettagliocomunefonte))
						.setText(fonte.getComuneFonte());

				((TextView) sotto
						.findViewById(R.id.vistadettaglioprovinciafonte))
						.setText("(" + fonte.getProvinciaFonte() + ")");

				((TextView) sotto
						.findViewById(R.id.vistadettagliodistanzafonte))
						.setText(Math.round(fonte.getDistanza()) + "km");
				((TextView) sotto.findViewById(R.id.vistadettagliolatitudine))
						.setText(fonte.getLat_gradi_dec().toString());
				((TextView) sotto.findViewById(R.id.vistadettagliolongitudine))
						.setText(fonte.getLong_gradi_dec().toString());

				currentSelectedItem = position;
			}
		});

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		super.onCreate(savedInstanceState);

		// Create the adView
		AdView adView = new AdView(this, AdSize.BANNER, "a14db7ec5158eb6");

		// Lookup your LinearLayout assuming it’s been given
		// the attribute android:id="@+id/mainLayout"
		LinearLayout layout = (LinearLayout) this
				.findViewById(R.id.adviewLinearLayout);
		// Add the adView to it
		layout.addView(adView);
		// Initiate a generic request to load it with an ad
		adView.loadAd(new AdRequest());

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if ((locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
				|| (locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
			registerLocationListeners();

		}

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		this.dh = new DataHelper(this);

		this.comune = (TextView) this.findViewById(R.id.DettaglioNomeComune);
		this.comuneProvincia = (TextView) this
				.findViewById(R.id.DettaglioProvinciaComune);
		this.lat_gradi_dec = (TextView) this.findViewById(R.id.latitudine);
		this.long_gradi_dec = (TextView) this.findViewById(R.id.longitudine);

		List<String> comuniList = this.dh.elencoComuni();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, comuniList);
		// comuniView = (AutoCompleteTextView) findViewById(R.id.sceltaComune);
		comuniView = (AutoCompleteTextView) findViewById(R.id.sceltaComune);
		comuniView.setThreshold(1);
		comuniView.setAdapter(adapter);
		String comuneDefault = sharedPrefs.getString(
				"preference_comune_default", "");
		comuniView.setText(comuneDefault.trim());

		/* pulsante cerca */

		cercaButton = ((Button) findViewById(R.id.cercaButton));

		cercaButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				Comune comuneDett = new Comune();
				comuneDett = dh
						.getComuneByName(comuniView.getText().toString());
				Acqua gd = (Acqua) getApplication();

				gd.setComuneCorrente(comuneDett);
				try {

					String nomeComune = comuneDett.getNomecomune();

					if (nomeComune.length() > 40) {
						nomeComune = nomeComune.substring(0, 40);
						nomeComune = nomeComune + "...";
					}
					comune.setText(nomeComune);
					comuneProvincia.setText(" (" + comuneDett.getProvincia()
							+ ")");
					lat_gradi_dec.setText(comuneDett.getLatitudine_display()
							.toString());

					long_gradi_dec.setText(comuneDett.getLatitudine_display()
							.toString());
					lat_gradi_dec.setTextColor(Color.CYAN);
					long_gradi_dec.setTextColor(Color.CYAN);
					handleFonti(comuneDett.getLatitudine(),
							comuneDett.getLongitudine());

				} catch (Exception e) {
					comune.setText("Comune non trovato, riprova!");
				}

			}
		});

		Button mappaButton = ((Button) findViewById(R.id.ElencoFontiMappaButton));
		mappaButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				startActivity(new Intent(AcquaActivity.this,
						MappaActivity.class));

			}
		});

		Button rilevaPosizioneButton = ((Button) findViewById(R.id.rileva_posizione));

		rilevaPosizioneButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				comune.setText("");
				comuniView.setText("");
				comuneProvincia.setText("");
				if (locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
					// Log.w(TAG, "abilitato networkprovider");

				}
				if (locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					// Log.w(TAG, "abilitato gpsworkprovider");
				}

				if ((!locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
						&& (!locationManager
								.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
					Builder builder = new AlertDialog.Builder(
							AcquaActivity.this);

					builder.setTitle(R.string.ElencoFontiNoLocationManagerTitle);
					;

					builder.setMessage(R.string.ElencoFontiNoLocationManagerMessage);
					builder.setPositiveButton(
							R.string.ElencoFontiNoLocationManagerOk,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									Intent intent = new Intent(
											Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivityForResult(intent, REQUEST_CODE);

								}
							});
					builder.setNegativeButton(
							R.string.ElencoFontiNoLocationManagerAnnulla, null);

					builder.show();

				}

				// preso da
				// http://stackoverflow.com/questions/1608632/android-locationmanager-getlastknownlocation-returns-null
				Acqua gd = (Acqua) getApplication();

				if (currentLocation == null) {
					Toast.makeText(getApplicationContext(),
							R.string.ElencoFontiStoAspettandoWifiText,
							Toast.LENGTH_SHORT).show();

					return;
				}

				current_latitude = currentLocation.getLatitude();
				current_longitude = currentLocation.getLongitude();
				Comune c = getComuneFromLocation(current_latitude,
						current_longitude);

				Acqua gdata = (Acqua) getApplication();

				Log.d(TAG, "latitudine display" + c.getLatitudine_display());
				lat_gradi_dec.setText(Double.toString(c.getLatitudine_display()));

				long_gradi_dec.setText(Double.toString(c
						.getLongitudine_display()));
				lat_gradi_dec.setTextColor(Color.CYAN);
				long_gradi_dec.setTextColor(Color.CYAN);
				comune.setText(c.getNomecomune());

				comuneProvincia.setText(" (" + c.getProvincia() + ") ");

				gd.setComuneCorrente(c);
				handleFonti(current_latitude, current_longitude);
			}

		});

	}

	/** {@inheritDoc} */
	@Override
	protected void onResume() {
		// Make sure that when the activity has been
		// suspended to background,
		// the device starts getting locations again
		// temporeaneamente disabilitato
		// registerLocationListeners();

		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		comuniView = (AutoCompleteTextView) findViewById(R.id.sceltaComune);

		if (comuniView.getText().toString().equals("")) {
			String comuneDefault = sharedPrefs.getString(
					"preference_comune_default", "");
			// Log.w(TAG, "comuneDefault" + comuneDefault);
			comuniView.setText(comuneDefault);

		}

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// Make sure that when the activity goes to
		// background, the device stops getting locations
		// to save battery life.
		// temporeaneamente disabilitato
		// Log.w(TAG, "remove location listeners");
		// locationManager.removeUpdates(listenerFine);
		// locationManager.removeUpdates(listenerCoarse);
		super.onPause();
	}

	private static List<Fonte> calcolaDistanze(List<Fonte> elencoFonti,
			Double latitudine, Double longitudine) {
		List<Fonte> result = new ArrayList<Fonte>();
		Double distanza;
		for (Fonte fonte : elencoFonti) {
			distanza = calcolaDistanza(latitudine, longitudine,
					fonte.getLat_gradi_dec(), fonte.getLong_gradi_dec());
			fonte.setDistanza(distanza);
			result.add(fonte);
		}

		return result;

	}

	/* metodo per fare il cleanup dell'interfaccia */
	private void cleanUpInterface() {

		elencoFonti.setText("");
		comune.setText("");
		comuneProvincia.setText("");

	}

	/*
	 * 
	 * preso da:
	 * http://www.sunearthtools.com/dp/tools/distance.php?lang=it#txtDist_1
	 * Calcolo del punto di destinazione Per determinare il punto di
	 * destinazione, conoscendo il punto iniziale la direzione DIR e la distanza
	 * d, viene utilizzata la seguente formula:
	 * 
	 * latB = asin( sin( latA) * cos( d / R ) + cos( latA ) * sin( d / R ) *
	 * cos( DIR)) lonB = lonA +
	 * atan2(sin(direzione)*sin(DIREZIONE/Raggio)*cos(latA
	 * ),cos(direzione/Raggio) - sin(latA)*sin(LatB))
	 */

}
