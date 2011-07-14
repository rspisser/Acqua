package it.spisser.acqua;

import it.spisser.acqua.R;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class SuggerisciFonteActivity extends AcquaBaseActivity {

	private DataHelper dh;
	private EditText nomeFonte;
	private AutoCompleteTextView comuniView;

	private Comune currentComune;
	private Button rilevaPosizioneButton;
	private Button suggerisciButton;
	private static final String TAG = "Acqua";
	private static final String current_activity = "SuggerisciFonte";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggeriscifonte);

		SharedPreferences sharedPrefs = PreferenceManager
		.getDefaultSharedPreferences(this);

		nomeFonte = (EditText) findViewById(R.id.SuggerisciFonteInserisciFonteEditText);
		// Create the adView
		AdView adView = new AdView(this, AdSize.BANNER, "a14db7ec5158eb6");

		// Lookup your LinearLayout assuming it’s been given
		// the attribute android:id="@+id/mainLayout"
		LinearLayout layout = (LinearLayout) this
				.findViewById(R.id.adviewSuggerisciFonteLinearLayout);
		// Add the adView to it
		layout.addView(adView);
		// Initiate a generic request to load it with an ad
		adView.loadAd(new AdRequest());
		this.dh = new DataHelper(this);

		List<String> comuniList = this.dh.elencoComuni();
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, comuniList);
		comuniView = (AutoCompleteTextView) findViewById(R.id.SuggerisciFonteScegliComune);
		comuniView.setThreshold(1);
		comuniView.setAdapter(adapter2);
		
		String comuneDefault = sharedPrefs.getString(
				"preference_comune_default", "");
		comuniView.setText(comuneDefault.trim());
	

		rilevaPosizioneButton = (Button) findViewById(R.id.SuggerisciFonteRilevaPosizioneButton);

		rilevaPosizioneButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				GlobalData gd = (GlobalData) getApplication();
				nomeFonte.setText("");
				currentComune = getComuneFromLocation(gd.getCurrent_latitude(),
						gd.getCurrent_longitude());

			}
		});

		suggerisciButton = (Button) findViewById(R.id.SuggerisciFonteButton);

		suggerisciButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				try {
					String comuneScelto = comuniView.getText().toString();

					String nf = nomeFonte.getText().toString();

					if (comuneScelto.equals("")) {
						/* è stato abilitato il rileva posizione */
					} else {
						if (!(comuneScelto.equals(""))) {
							currentComune = dh.getComuneByName(comuneScelto);

						}
						/* e' stato scelto un nome comune */
					}
					/*
					 * contenuto del suggerimento da inviare, inizialmente solo
					 * una mail con: nome del comune scelto con coordinate e il
					 * nome della fonte
					 */
					final Intent emailIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					String messaggio = String.format(
							getString(R.string.SuggerisciEmailContentString),
							currentComune.getNomecomune(), currentComune
									.getLatitudine().toString(), currentComune
									.getLongitudine().toString(), nf);
					emailIntent.setType("plain/text");
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
							new String[] { "reinhard@spisser.it" });
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							getString(R.string.SuggerisciEmailSubject));
					Log.d(TAG,getString(R.string.SuggerisciEmailSubject));
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							messaggio);
					startActivity(Intent.createChooser(emailIntent, "Invia..."));

				} catch (Exception e) {
					Builder builder = new AlertDialog.Builder(
							SuggerisciFonteActivity.this);

					builder.setTitle(R.string.SuggerisciEmailAlertTitle);

					builder.setMessage(R.string.SuggerisciEmailAlertMessage);
					builder.setPositiveButton(R.string.SuggerisciEmailOkButton, null);

					builder.show();
					Log.d(TAG, e.toString());
				}
			}
		});

	}
}
