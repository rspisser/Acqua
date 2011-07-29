package it.spisser.acqua;

import java.util.List;

import android.app.Application;



public class Acqua extends Application{

	private Double listener_latitude;
	private Double listener_longitudine;
	private Double current_latitude;
	private Double current_longitude;
	private String current_activity;
	private Comune comuneCorrente;
	private List<Fonte> listaFonti;


	
	public Double getListener_latitude() {
		return listener_latitude;
	}
	public void setListener_latitude(Double listener_latitude) {
		this.listener_latitude = listener_latitude;
	}
	public Double getListener_longitudine() {
		return listener_longitudine;
	}
	public void setListener_longitudine(Double listener_longitudine) {
		this.listener_longitudine = listener_longitudine;
	}
	public Comune getComuneCorrente() {
		return comuneCorrente;
	}
	public void setComuneCorrente(Comune comuneCorrente) {
		this.comuneCorrente = comuneCorrente;
	}

	public Double getCurrent_latitude() {
		return current_latitude;
	}
	public void setCurrent_latitude(Double current_latitude) {
		this.current_latitude = current_latitude;
	}
	public Double getCurrent_longitude() {
		return current_longitude;
	}
	public void setCurrent_longitude(Double current_longitude) {
		this.current_longitude = current_longitude;
	}
	public String getCurrent_activity() {
		return current_activity;
	}
	public void setCurrent_activity(String current_activity) {
		this.current_activity = current_activity;
	}
	public List<Fonte> getListaFonti() {
		return listaFonti;
	}
	public void setListaFonti(List<Fonte> listaFonti) {
		this.listaFonti = listaFonti;
	}
	
	
	
	
}
