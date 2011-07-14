package it.spisser.acqua;

import it.spisser.acqua.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;

public class PrefsActivity extends PreferenceActivity{
	
	private CheckBox cb;
	private static final String TAG = "Acqua";
	private AutoCompleteTextView comuniView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

//		comuniView = (AutoCompleteTextView) findViewById(R.id.sceltaComune);
//			
//		
//		SharedPreferences sharedPrefs = PreferenceManager
//		.getDefaultSharedPreferences(this);
//
//		AcquaPreference comuneDefault = (AcquaPreference) findPreference("preference_comune_default");
//		
//		Preference comuneCheck = (Preference) findPreference("preference_comune_default_usa_corrente");
//		
//		comuneCheck.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
//			public boolean onPreferenceChange(Preference preference, Object object){
//				AcquaPreference comuneDefault = (AcquaPreference) findPreference("preference_comune_default");
//					Log.w(TAG,"comuniview"+comuniView.getText().toString());
//					
//					comuneDefault.savePreference(comuniView.getText().toString());		
//				return true;
//				
//			}
//			
//		});

//		Preference ccomuneCheck = (Preference) findPreference("usacorrente");
//		
//		ccomuneCheck.setOnPreferenceClickListener(new OnPreferenceClickListener(){
//			public boolean onPreferenceClick(Preference preference){
//				Log.w(TAG,"cliccato usacorrente");
//				return true;
//				
//			}
//			
//		});

	}
}
