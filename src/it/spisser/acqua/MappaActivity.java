package it.spisser.acqua;

import it.spisser.acqua.R;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MappaActivity extends MapActivity {

	private Double latitudine;
	private Double longitudine;

	private GeoPoint geopoint;
	private MapView mapView;
	private MapController mapController;
	private static final String TAG = "Acqua";
	private Double current_latitude;
	private Double current_longitude;
	private int numero_sorgenti;
	List<Overlay> mapOverlays;
	Drawable drawable;
	AcquaItemizedOverlay itemizedOverlay;

	public Double getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(Double latitudine) {
		this.latitudine = latitudine;
	}

	public Double getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(Double longitudine) {
		this.longitudine = longitudine;
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onNewIntent(Intent intent) {

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mappa);
		Log.d(TAG,"entrato in onCreate");
		try {
			mapView = (MapView) findViewById(R.id.mappaMapView);

			mapView.setBuiltInZoomControls(true);

			mapController = mapView.getController();
			mapController.setZoom(10); // Zoom 1 is world-wide
			GlobalData gd = (GlobalData) getApplication();
			Comune cc = gd.getComuneCorrente();

			List<Overlay> overlays = mapView.getOverlays();

			drawable = this.getResources().getDrawable(R.drawable.marker_blue);
			itemizedOverlay = new AcquaItemizedOverlay(drawable, this);

			int geolat = (int) (cc.getLatitudine() * 1E6);
			int geolong = (int) (cc.getLongitudine() * 1E6);
			geopoint = new GeoPoint(geolat, geolong);
			mapController.animateTo(geopoint);
			current_latitude = cc.getLatitudine();
			current_longitude = cc.getLongitudine();
			numero_sorgenti = 0;
			String titlestring = gd.getComuneCorrente().getNomecomune();
			String snippetstring = "";
			String current_colour="";
			int fontesize = gd.getListaFonti().size();
			for (Fonte fonte : gd.getListaFonti()) {
				Log.d(TAG,
						"fonte: " + fonte.getLat_gradi_dec() + " "
								+ fonte.getLong_gradi_dec());
				if (fontesize==1){
					/* regolamento automatico dello zoom in funzione
					 * della distanza tra la fonte e la locazione
					 * scelta.
					 * funziona bene nord-sud, meno bene est-ovest
					 */
					if (fonte.getDistanza()>550)mapController.setZoom(7);
					else if (fonte.getDistanza()>250)mapController.setZoom(8);
					else if (fonte.getDistanza()>125)mapController.setZoom(9);
					else if (fonte.getDistanza()>75)mapController.setZoom(10);
					else if (fonte.getDistanza()>35)mapController.setZoom(11);
					else if (fonte.getDistanza()>15)mapController.setZoom(12);
					else if (fonte.getDistanza()>8)mapController.setZoom(13);
					else if (fonte.getDistanza()>4)mapController.setZoom(14);
					else mapController.setZoom(15);
					
					/* calcoliamo il punto che sta esattamente a metà tra i due punti 
					 * e centriamo la mappa in quel punto
					 */
					Double animPointLatitudine = current_latitude+(fonte.getLat_gradi_dec()-current_latitude)/2;
					Double animPointLongitudine = current_longitude+(fonte.getLong_gradi_dec()-current_longitude)/2;
					int geolat2 = (int) (animPointLatitudine * 1E6);
					int geolong2 = (int) (animPointLongitudine * 1E6);
					GeoPoint geopoint2 = new GeoPoint(geolat2, geolong2);
					mapController.animateTo(geopoint2);
				}
				if ((fonte.getLat_gradi_dec().equals(current_latitude))
						&& (fonte.getLong_gradi_dec().equals(current_longitude))) {
					numero_sorgenti++;
					titlestring = fonte.getComuneFonte()+ " (" + fonte.getProvinciaFonte()+ ") - "+ Math.round(fonte.getDistanza())+"km";
					snippetstring +=  fonte.getNomeCommerciale() +"<br>";
					

				} else {
					/* per prima cosa disegnamo l'overlay */
					snippetstring="numero sorgenti: "+numero_sorgenti+"<br>"+snippetstring+"";
					OverlayItem overlayitem = new OverlayItem(geopoint,
							titlestring, snippetstring);
					itemizedOverlay.addOverlay(overlayitem);
					overlays.add(itemizedOverlay);
					/* reimpostiamo i valori */
					titlestring = "";
					snippetstring = "";
					Log.d(TAG,"snippetstring "+snippetstring);
					numero_sorgenti = 1;
					current_latitude = fonte.getLat_gradi_dec();
					current_longitude = fonte.getLong_gradi_dec();
					
					if (fonte.getDistanza() < 50) {

						drawable = this.getResources().getDrawable(
								R.drawable.marker_green);
						current_colour="green";
					} else if (fonte.getDistanza() < 100) {
						drawable = this.getResources().getDrawable(
								R.drawable.marker_yellow);
						current_colour="yellow";
					} else {
						drawable = this.getResources().getDrawable(
								R.drawable.marker_red);
						current_colour="red";
					}
					itemizedOverlay = new AcquaItemizedOverlay(drawable, this);

					Double flat = fonte.getLat_gradi_dec() * 1E6;
					Double flong = fonte.getLong_gradi_dec() * 1E6;
					geopoint = new GeoPoint(flat.intValue(), flong.intValue());
					titlestring = fonte.getComuneFonte()+ " (" + fonte.getProvinciaFonte()+ ") - "+ Math.round(fonte.getDistanza())+"km";
					snippetstring += fonte.getNomeCommerciale()+"<br>";
				}
			}
			if (gd.getListaFonti().size()>0){
				
			OverlayItem overlayitem = new OverlayItem(geopoint,
					titlestring, snippetstring);
			itemizedOverlay.addOverlay(overlayitem);
			overlays.add(itemizedOverlay);
			

			}

			mapView.setClickable(true);
			mapView.setSatellite(false);
			mapView.postInvalidate();

		} catch (Exception e) {
			Log.d(TAG, "errore nella visualizzazione della mappa ",e);
		}

	}

}
