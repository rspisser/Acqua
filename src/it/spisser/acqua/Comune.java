package it.spisser.acqua;

public class Comune {

	int id;

	String nomecomune;

	String provincia;
	Double latitudine;
	Double longitudine;
	Double latitudine_display;
	Double longitudine_display;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomecomune() {
		return nomecomune;
	}

	public void setNomecomune(String nomecomune) {
		this.nomecomune = nomecomune;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

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

	public Double getLatitudine_display() {
		return latitudine_display;
	}

	public void setLatitudine_display(Double latitudine_display) {
		this.latitudine_display = latitudine_display;
	}

	public Double getLongitudine_display() {
		return longitudine_display;
	}

	public void setLongitudine_display(Double longitudine_display) {
		this.longitudine_display = longitudine_display;
	}



}