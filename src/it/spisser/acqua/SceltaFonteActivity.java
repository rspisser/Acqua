package it.spisser.acqua;

import it.spisser.acqua.R;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class SceltaFonteActivity extends AcquaBaseActivity {

	private TextView elencoFonti;
	private DataHelper dh;
	private AutoCompleteTextView fontiView;
	private AutoCompleteTextView comuniView;

	private Button calcolaDistanzaButton;
	private Button rilevaPosizioneButton;
	private Button visualizzaSuMappaButton;
	private static final String TAG = "Acqua";
	private static final String current_activity = "SceltaFonte";

	private void viewData(Fonte fonte, Comune comune) {
		Double latitudine = fonte.getLat_gradi_dec();
		Double longitudine = fonte.getLong_gradi_dec();
		TextView distanza = (TextView) findViewById(R.id.SceltaFonteDistanzaDistanza);
		TextView testo = (TextView) findViewById(R.id.SceltaFonteDistanzaTesto);

		try {
			Double dist = calcolaDistanza(fonte.getLat_gradi_dec(),
					fonte.getLong_gradi_dec(), comune.getLatitudine(),
					comune.getLongitudine());
			distanza.setText(Math.round(dist) + "km");
			String t = "Distanza tra fonte: " + fonte.getNomeCommerciale()
					+ " - " + fonte.getComuneFonte() + " ( "
					+ fonte.getProvinciaFonte() + ") e\n "
					+ comune.getNomecomune() + " (" + comune.getProvincia()
					+ "):";
			testo.setText(t);
			Acqua gd = (Acqua) getApplication();
			gd.setComuneCorrente(comune);

			List<Fonte> listaFonti = new ArrayList<Fonte>();

			fonte.setDistanza(dist);
			listaFonti.add(fonte);
			gd.setListaFonti(listaFonti);

			if (dist < 50) {
				distanza.setTextColor(Color.GREEN);

			} else if (dist < 100) {
				distanza.setTextColor(Color.YELLOW);

			} else {
				distanza.setTextColor(Color.RED);
			}
		} catch (Exception e) {
			Builder builder = new AlertDialog.Builder(SceltaFonteActivity.this);
			builder.setTitle("LocationManager");

			builder.setMessage("Nessuna fonte o Comune selezionato");
			builder.setPositiveButton("ok", null);
			builder.show();

		}

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sceltafonte);

		Button MappaButton = (Button) findViewById(R.id.SceltaFonteVisualizzaMappaButton);
		MappaButton.setVisibility(View.GONE);

		SharedPreferences sharedPrefs = PreferenceManager
		.getDefaultSharedPreferences(this);

		// Create the adView
		AdView adView = new AdView(this, AdSize.BANNER, "a14db7ec5158eb6");

		// Lookup your LinearLayout assuming it’s been given
		// the attribute android:id="@+id/mainLayout"
		LinearLayout layout = (LinearLayout) this
				.findViewById(R.id.adviewSceltaFonteLinearLayout);
		// Add the adView to it
		layout.addView(adView);
		// Initiate a generic request to load it with an ad
		adView.loadAd(new AdRequest());

		this.dh = new DataHelper(this);

		List<Fonte> fontiList = this.dh.elencoFontiBean();
		ArrayAdapter<Fonte> adapter = new ArrayAdapter<Fonte>(this,
				android.R.layout.simple_dropdown_item_1line, fontiList);
		fontiView = (AutoCompleteTextView) findViewById(R.id.SceltaFonteScegliFonte);

		fontiView.setThreshold(1);
		fontiView.setAdapter(adapter);

		List<String> comuniList = this.dh.elencoComuni();
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, comuniList);
		comuniView = (AutoCompleteTextView) findViewById(R.id.SceltaFonteScegliComune);
		comuniView.setThreshold(1);
		comuniView.setAdapter(adapter2);
		comuniView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					public void onItemClick(AdapterView av, View v, int pos,
							long a) {
						av.getItemAtPosition(pos);

						// Log.w(TAG, "" + av.getSelectedItemId());
						// Log.w(TAG, "posizione" + pos);
						// Log.w(TAG, "a: " + a);
						//
						// Log.w(TAG, "boh" + av.getChildAt(pos));

					}

				});
		String comuneDefault = sharedPrefs.getString(
				"preference_comune_default", "");
		comuniView.setText(comuneDefault.trim());
		
		fontiView
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onNothingSelected(AdapterView av) {

					}

					public void onItemSelected(AdapterView av, View v, int pos,
							long a) {
						// Log.w(TAG, "posizione" + pos);
						// Log.w(TAG, "a" + a);
						// Log.w(TAG, "boh" + av.getChildAt(pos));
					}
				});

		calcolaDistanzaButton = (Button) findViewById(R.id.SceltaFontiCalcolaDistanzaButton);

		calcolaDistanzaButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				SharedPreferences sharedPrefs = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());

				Boolean useGoogleMaps = sharedPrefs.getBoolean(
						"preference_usa_google_maps", true);

				String fonteNome = fontiView.getText().toString();
				String comuneNome = comuniView.getText().toString();
				Fonte fonteSelezionata = dh.getFonteByName(fontiView.getText()
						.toString());
				Log.d(TAG, "fonteSelezionata" + fonteSelezionata.toString());
				Comune comune = dh.getComuneByName(comuniView.getText()
						.toString());
				if (useGoogleMaps == true) {
					Button MappaButton = (Button) findViewById(R.id.SceltaFonteVisualizzaMappaButton);
					MappaButton.setVisibility(View.VISIBLE);
					Acqua gd = (Acqua) getApplication();
					gd.setComuneCorrente(comune);
					Log.d(TAG, "fonteSelezionata" + fonteSelezionata.toString());

				}
				viewData(fonteSelezionata, comune);
			}
		});

		rilevaPosizioneButton = (Button) findViewById(R.id.SceltaFontiRilevaPosizioneButton);

		rilevaPosizioneButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				comuniView.setText("");
				
				Acqua gd = (Acqua) getApplication();
				Double latitudine;
				Double longitudine;
				Comune comune = getComuneFromLocation(gd.getCurrent_latitude(),
						gd.getCurrent_longitude());
				String t;
				if (comune == null) {
					Builder builder = new AlertDialog.Builder(
							SceltaFonteActivity.this);
					builder.setTitle("LocationManager");

					builder.setMessage("Sto aspettando il fix del gps");
					builder.setPositiveButton("ok", null);
					builder.show();

				} else {
					Fonte fonteSelezionata = dh.getFonteByName(fontiView
							.getText().toString());
					SharedPreferences sharedPrefs = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());

					Boolean useGoogleMaps = sharedPrefs.getBoolean(
							"preference_usa_google_maps", true);
					if (useGoogleMaps == true) {
						Button MappaButton = (Button) findViewById(R.id.SceltaFonteVisualizzaMappaButton);
						MappaButton.setVisibility(View.VISIBLE);
						
						gd.setComuneCorrente(comune);
						Log.d(TAG,
								"fonteSelezionata"
										+ fonteSelezionata.toString());

					}

					viewData(fonteSelezionata, comune);
				}
			}
		});

		visualizzaSuMappaButton = (Button) findViewById(R.id.SceltaFonteVisualizzaMappaButton);

		visualizzaSuMappaButton
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View arg0) {

						startActivity(new Intent(SceltaFonteActivity.this,
								MappaActivity.class));

					}
				});

	}

}
